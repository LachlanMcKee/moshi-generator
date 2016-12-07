package gsonpath.generator.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.squareup.javapoet.*;

import java.util.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

import gsonpath.generator.Generator;
import gsonpath.generator.HandleResult;
import gsonpath.internal.TypeAdapterLoader;

public class TypeAdapterLoaderGenerator extends Generator {
    private static final String PACKAGE_PRIVATE_TYPE_ADAPTER_LOADER_CLASS_NAME = "PackagePrivateTypeAdapterLoader";

    public TypeAdapterLoaderGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    public boolean generate(List<HandleResult> generatedGsonAdapters) {
        if (generatedGsonAdapters.size() == 0) {
            return false;
        }

        Map<String, List<HandleResult>> packageLocalHandleResults = new HashMap<>();
        for (HandleResult generatedGsonAdapter : generatedGsonAdapters) {
            String packageName = generatedGsonAdapter.generatedClassName.packageName();

            List<HandleResult> localResults = packageLocalHandleResults.get(packageName);
            if (localResults == null) {
                localResults = new ArrayList<>();
                packageLocalHandleResults.put(packageName, localResults);
            }

            localResults.add(generatedGsonAdapter);
        }

        for (String packageName : packageLocalHandleResults.keySet()) {
            if (!createPackageLocalTypeAdapterLoaders(packageName, packageLocalHandleResults.get(packageName))) {
                // If any of the package local adapters fail to generate, we must fail the entire process.
                return false;
            }
        }

        return createRootTypeAdapterLoader(packageLocalHandleResults);
    }

    /**
     * Create the GsonPathLoader which is used by the GsonPathTypeAdapterFactory class.
     */
    private boolean createRootTypeAdapterLoader(Map<String, List<HandleResult>> packageLocalHandleResults) {
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder("GeneratedTypeAdapterLoader")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(TypeAdapterLoader.class);

        typeBuilder.addField(FieldSpec.builder(ArrayTypeName.of(TypeAdapterLoader.class), "mPackagePrivateLoaders")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build());

        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);

        CodeBlock.Builder constructorCodeBlock = CodeBlock.builder();
        constructorCodeBlock.addStatement("mPackagePrivateLoaders = new $T[$L]", TypeAdapterLoader.class, packageLocalHandleResults.size());

        // Add the package local type adapter loaders to the hash map.
        int index = 0;
        for (String packageName : packageLocalHandleResults.keySet()) {
            constructorCodeBlock.addStatement("mPackagePrivateLoaders[$L] = new $L.$L()", index++, packageName, PACKAGE_PRIVATE_TYPE_ADAPTER_LOADER_CLASS_NAME);
        }

        constructorBuilder.addCode(constructorCodeBlock.build());
        typeBuilder.addMethod(constructorBuilder.build());

        //
        // <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type);
        //
        MethodSpec.Builder createMethod = MethodSpec.methodBuilder("create")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeAdapter.class)
                .addParameter(Gson.class, "gson")
                .addParameter(TypeToken.class, "type");

        CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.beginControlFlow("for (int i = 0; i < mPackagePrivateLoaders.length; i++)");
        codeBlock.addStatement("TypeAdapter typeAdapter = mPackagePrivateLoaders[i].create(gson, type)");
        codeBlock.add("\n");

        codeBlock.beginControlFlow("if (typeAdapter != null)");
        codeBlock.addStatement("return typeAdapter");
        codeBlock.endControlFlow();

        codeBlock.endControlFlow();
        codeBlock.addStatement("return null");

        createMethod.addCode(codeBlock.build());
        typeBuilder.addMethod(createMethod.build());

        return writeFile("gsonpath", typeBuilder);
    }

    private boolean createPackageLocalTypeAdapterLoaders(String packageName, List<HandleResult> packageLocalGsonAdapters) {
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(ClassName.get(packageName, PACKAGE_PRIVATE_TYPE_ADAPTER_LOADER_CLASS_NAME))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(TypeAdapterLoader.class);

        //
        // <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type);
        //
        MethodSpec.Builder createMethod = MethodSpec.methodBuilder("create")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeAdapter.class)
                .addParameter(Gson.class, "gson")
                .addParameter(TypeToken.class, "type");

        CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.addStatement("Class rawType = type.getRawType()");

        int currentAdapterIndex = 0;
        for (HandleResult result : packageLocalGsonAdapters) {
            if (currentAdapterIndex == 0) {
                codeBlock.beginControlFlow("if (rawType.equals($T.class))", result.originalClassName);
            } else {
                codeBlock.add("\n"); // New line for easier readability.
                codeBlock.nextControlFlow("else if (rawType.equals($T.class))", result.originalClassName);
            }
            codeBlock.addStatement("return new $T(gson)", result.generatedClassName);

            currentAdapterIndex++;
        }
        codeBlock.endControlFlow();
        codeBlock.add("\n");
        codeBlock.addStatement("return null");

        createMethod.addCode(codeBlock.build());
        typeBuilder.addMethod(createMethod.build());

        return writeFile(packageName, typeBuilder);
    }

}
