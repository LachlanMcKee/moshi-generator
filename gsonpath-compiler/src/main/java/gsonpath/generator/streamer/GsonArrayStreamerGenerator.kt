package gsonpath.generator.streamer

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.JsonReader
import com.squareup.javapoet.*
import gsonpath.*
import gsonpath.generator.AdapterGeneratorDelegate
import gsonpath.generator.Generator
import gsonpath.model.GsonObject
import gsonpath.generator.HandleResult
import gsonpath.model.GsonObjectTreeFactory

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import java.io.IOException

class GsonArrayStreamerGenerator(processingEnv: ProcessingEnvironment) : Generator(processingEnv) {
    private val adapterGeneratorDelegate: AdapterGeneratorDelegate

    init {
        adapterGeneratorDelegate = AdapterGeneratorDelegate()
    }

    @Throws(ProcessingException::class)
    fun handle(streamerElement: TypeElement): HandleResult {
        // The class must implement the GsonArrayStreamer interface!
        val streamerClassName = ClassName.get(streamerElement)
        val outputClassName = ClassName.get(streamerClassName.packageName(),
                adapterGeneratorDelegate.generateClassName(streamerClassName, "GsonArrayStreamer"))

        val gsonArrayStreamerElement = streamerElement.interfaces
                .filter {
                    val interfaceElement = processingEnv.typeUtils.asElement(it) as TypeElement

                    (ClassName.get(interfaceElement) == ClassName.get(GsonArrayStreamer::class.java))
                }
                .firstOrNull() ?: throw ProcessingException("Class must extend " + GsonArrayStreamer::class.java.name)

        // Get the actual argument used for json parsing from the interface generics.
        val parameterizedTypeName = TypeName.get(gsonArrayStreamerElement) as ParameterizedTypeName

        val gsonModelClassName = parameterizedTypeName.typeArguments[0]
        val originalAdapterInterface = ClassName.get(streamerElement)

        val streamerTypeBuilder = TypeSpec.classBuilder(outputClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(ParameterizedTypeName.get(ClassName.get(AbstractGsonArrayStreamer::class.java), gsonModelClassName))
                .addSuperinterface(originalAdapterInterface)

        val autoGsonArrayAnnotation = streamerElement.getAnnotation(AutoGsonArrayStreamer::class.java)
        val rootField = autoGsonArrayAnnotation.rootField
        val flattenDelimiter = autoGsonArrayAnnotation.flattenDelimiter

        val isRootFieldSpecified = rootField.isNotEmpty()

        // This flag is only valid if the rootField value is populated, since it only affects the behaviour of rootField.
        val consumeReaderFully = autoGsonArrayAnnotation.consumeReaderFully && isRootFieldSpecified

        val rootElements = GsonObject()
        if (isRootFieldSpecified) {
            GsonObjectTreeFactory().createGsonObjectFromRootField(rootElements, rootField, flattenDelimiter)
        }

        // getArray
        val elementArrayType = ArrayTypeName.of(gsonModelClassName)
        val getArrayJsonReader = createBasicBuilder("getArray", elementArrayType)

        val getArrayBlock = CodeBlock.builder()

        // If we are reading the whole object, we need to a variable declared outside the try/catch
        if (consumeReaderFully) {
            getArrayBlock.addStatement("\$T result = null", elementArrayType)
        }

        if (rootElements.size() > 0) {
            // The code must navigate to the correct root field.
            addToSimpleCodeBlock(getArrayBlock, rootElements, object : AdapterGeneratorDelegate.ObjectParserCallback {
                override fun onInitialObjectNull() {
                    getArrayBlock.addStatement("return null")
                }

                override fun onInitialise() {

                }

                override fun onFieldAssigned(fieldName: String) {

                }

                override fun onNodeEmpty() {
                    if (consumeReaderFully) {
                        // Since we read the json entirely, we cannot return here.
                        getArrayBlock.addStatement("result = gson.fromJson(in, \$T[].class)", gsonModelClassName)
                        getArrayBlock.addStatement("break")

                    } else {
                        getArrayBlock.addStatement("return gson.fromJson(in, \$T[].class)", gsonModelClassName)
                    }
                }
            })

            // Since we may not read the entire object, we won't return a result at the end of the code block.
            if (consumeReaderFully) {
                getArrayBlock.addStatement("return result")
            } else {
                getArrayBlock.addStatement("return null")
            }

        } else {
            // There is no custom root field specified, therefore this is the root field.
            if (consumeReaderFully) {
                // Since we read the json entirely, we cannot return here.
                getArrayBlock.addStatement("result = gson.fromJson(in, \$T[].class)", gsonModelClassName)

            } else {
                getArrayBlock.addStatement("return gson.fromJson(in, \$T[].class)", gsonModelClassName)
            }
        }
        getArrayJsonReader.addCode(getArrayBlock.build())
        streamerTypeBuilder.addMethod(getArrayJsonReader.build())

        // getList
        val elementListType = ParameterizedTypeName.get(ClassName.get(List::class.java), gsonModelClassName)
        val getListJsonReader = createBasicBuilder("getList", elementListType)

        val getListBlock = CodeBlock.builder()

        // If we are reading the whole object, we need to a variable declared outside the try/catch
        if (consumeReaderFully) {
            getListBlock.addStatement("\$T result = null", elementListType)
        }

        if (rootElements.size() > 0) {
            // The code must navigate to the correct root field.
            addToSimpleCodeBlock(getListBlock, rootElements, object : AdapterGeneratorDelegate.ObjectParserCallback {
                override fun onInitialObjectNull() {
                    getListBlock.addStatement("return null")
                }

                override fun onInitialise() {

                }

                override fun onFieldAssigned(fieldName: String) {

                }

                override fun onNodeEmpty() {
                    if (consumeReaderFully) {
                        // Sine we read the json entirely, we cannot return here.
                        getListBlock.addStatement("result = gson.fromJson(in, new com.google.gson.reflect.TypeToken<List<\$T>>() {}.getType())", gsonModelClassName)
                        getListBlock.addStatement("break")

                    } else {
                        getListBlock.addStatement("return gson.fromJson(in, new com.google.gson.reflect.TypeToken<List<\$T>>() {}.getType())", gsonModelClassName)
                    }
                }
            })

            // Since we may not read the entire object, we won't return a result at the end of the code block.
            if (consumeReaderFully) {
                getListBlock.addStatement("return result")
            } else {
                getListBlock.addStatement("return null")
            }

        } else {
            // There is no custom root field specified, therefore this is the root field.
            if (consumeReaderFully) {
                // Sine we read the json entirely, we cannot return here.
                getListBlock.addStatement("result = gson.fromJson(in, new com.google.gson.reflect.TypeToken<List<\$T>>() {}.getType())", gsonModelClassName)

            } else {
                getListBlock.addStatement("return gson.fromJson(in, new com.google.gson.reflect.TypeToken<List<\$T>>() {}.getType())", gsonModelClassName)
            }
        }
        getListJsonReader.addCode(getListBlock.build())
        streamerTypeBuilder.addMethod(getListJsonReader.build())

        // Stream results, multiple - json reader.
        val streamMultipleJsonReader = createBasicBuilder("streamArraySegmented", null)
        streamMultipleJsonReader.addParameter(TypeName.INT, "streamSize")
        streamMultipleJsonReader.addParameter(ParameterizedTypeName.get(ClassName.get(GsonArrayStreamer.StreamCallback::class.java), elementArrayType), "callback")

        val streamCodeBlock = CodeBlock.builder()
        streamCodeBlock.addStatement("\$T[] results", gsonModelClassName)
        streamCodeBlock.addStatement("StreamCallback.StreamHandler callbackResponse")
        streamCodeBlock.addStatement("int resultIndex")
        streamCodeBlock.add("\n")

        if (rootElements.size() > 0) {
            // The code must navigate to the correct root field.
            addToSimpleCodeBlock(streamCodeBlock, rootElements, object : AdapterGeneratorDelegate.ObjectParserCallback {
                override fun onInitialObjectNull() {
                    streamCodeBlock.addStatement("return")
                }

                override fun onInitialise() {
                    addStreamInitializerToCodeBlock(streamCodeBlock, gsonModelClassName)
                }

                override fun onFieldAssigned(fieldName: String) {

                }

                override fun onNodeEmpty() {
                    addStreamCodeBlock(streamCodeBlock, gsonModelClassName)
                }
            })
        } else {
            // There is no custom root field specified, therefore this is the root field.
            streamCodeBlock.beginControlFlow("try")

            // Ensure that the array actually exists before attempting to read it.
            streamCodeBlock.add("// Ensure the array is not null.\n")
            streamCodeBlock.beginControlFlow("if (!isValidValue(in))")
            streamCodeBlock.addStatement("return")
            streamCodeBlock.endControlFlow()
            streamCodeBlock.add("\n")

            addStreamInitializerToCodeBlock(streamCodeBlock, gsonModelClassName)
            addStreamCodeBlock(streamCodeBlock, gsonModelClassName)

            streamCodeBlock.nextControlFlow("catch (\$T e)", ClassName.get(IOException::class.java))
            streamCodeBlock.addStatement("throw new \$T(e)", ClassName.get(JsonSyntaxException::class.java))
            streamCodeBlock.endControlFlow()
        }

        streamCodeBlock.add("\n")
        streamCodeBlock.add("// We have left over results to send back.\n")
        streamCodeBlock.beginControlFlow("if (resultIndex >= 0)")
        streamCodeBlock.add("// To avoid creating a new array, we will simply remove the invalid results at the end.\n")
        streamCodeBlock.beginControlFlow("for (int i = resultIndex + 1; i < streamSize; i++)")
        streamCodeBlock.addStatement("results[i] = null")
        streamCodeBlock.endControlFlow()
        streamCodeBlock.add("\n")
        streamCodeBlock.addStatement("callback.onValueParsed(results, resultIndex + 1, callbackResponse)")
        streamCodeBlock.endControlFlow()

        streamMultipleJsonReader.addCode(streamCodeBlock.build())
        streamerTypeBuilder.addMethod(streamMultipleJsonReader.build())

        if (writeFile(outputClassName.packageName(), streamerTypeBuilder)) {
            return HandleResult(originalAdapterInterface, outputClassName)
        }
        throw ProcessingException("Failed to write generated file: " + outputClassName.simpleName())
    }

    private fun addStreamCodeBlock(builder: CodeBlock.Builder, elementClassName: TypeName) {
        builder.addStatement("in.beginArray()")
        builder.beginControlFlow("while (in.hasNext())")
        builder.addStatement("results[++resultIndex] = gson.fromJson(in, \$T.class)", elementClassName)
        builder.add("\n")
        builder.add("// Once we reach the requested stream size, we should return the results.\n")
        builder.beginControlFlow("if (resultIndex == streamSize - 1)")
        builder.addStatement("callback.onValueParsed(results, streamSize, callbackResponse)")
        builder.add("\n")
        builder.beginControlFlow("if (callbackResponse.isStreamStopped())")
        builder.add("// Since we stop the stream mid-way, we cannot call end-array safely.\n")
        builder.addStatement("return")
        builder.endControlFlow()
        builder.add("\n")
        builder.addStatement("resultIndex = -1")
        builder.endControlFlow()
        builder.endControlFlow()
        builder.addStatement("in.endArray()")
    }

    @Throws(ProcessingException::class)
    private fun addToSimpleCodeBlock(builder: CodeBlock.Builder, rootElements: GsonObject, callback: AdapterGeneratorDelegate.ObjectParserCallback) {
        builder.beginControlFlow("try")
        if (rootElements.size() > 0) {
            adapterGeneratorDelegate.addGsonAdapterReadCode(builder, rootElements, true, null, callback)
        }
        builder.nextControlFlow("catch (\$T e)", ClassName.get(IOException::class.java))
        builder.addStatement("throw new \$T(e)", ClassName.get(JsonSyntaxException::class.java))
        builder.endControlFlow()
    }

    private fun addStreamInitializerToCodeBlock(builder: CodeBlock.Builder, elementClassName: TypeName) {
        builder.addStatement("results = new \$T[streamSize]", elementClassName)
        builder.addStatement("callbackResponse = new StreamCallback.StreamHandler()")
        builder.addStatement("resultIndex = -1")
    }

    private fun createBasicBuilder(name: String, returnTypeName: TypeName?): MethodSpec.Builder {
        val methodBuilder = MethodSpec.methodBuilder(name)
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Gson::class.java, "gson")
                .addParameter(JsonReader::class.java, "in")
                .addException(JsonSyntaxException::class.java)

        if (returnTypeName != null) {
            methodBuilder.returns(returnTypeName)
        }

        return methodBuilder
    }

    public override fun onJavaFileBuilt(builder: JavaFile.Builder) {
        builder.addStaticImport(GsonUtil::class.java, "*")
    }
}
