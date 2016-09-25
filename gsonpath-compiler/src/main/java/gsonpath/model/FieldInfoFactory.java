package gsonpath.model;

import com.google.gson.annotations.SerializedName;
import com.squareup.javapoet.TypeName;
import gsonpath.ExcludeField;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FieldInfoFactory {
    private final ProcessingEnvironment processingEnv;

    public FieldInfoFactory(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    public List<FieldInfo> getModelFieldsFromElement(TypeElement modelElement, boolean fieldsRequireAnnotation) {
        List<FieldInfo> fieldInfoList = new ArrayList<>();

        // Obtain all possible elements contained within the annotated class, including inherited fields.
        for (final Element memberElement : processingEnv.getElementUtils().getAllMembers(modelElement)) {

            // Ignore modelElement that are not fields.
            if (memberElement.getKind() != ElementKind.FIELD) {
                continue;
            }

            // Ignore final, static and transient fields.
            Set<Modifier> modifiers = memberElement.getModifiers();
            if (modifiers.contains(Modifier.FINAL) || modifiers.contains(Modifier.STATIC) || modifiers.contains(Modifier.TRANSIENT)) {
                continue;
            }

            if (fieldsRequireAnnotation && (memberElement.getAnnotation(SerializedName.class) == null)) {
                continue;
            }

            // Ignore any excluded fields
            if (memberElement.getAnnotation(ExcludeField.class) != null) {
                continue;
            }

            fieldInfoList.add(new FieldInfo() {
                @Override
                public TypeName getTypeName() {
                    return TypeName.get(memberElement.asType());
                }

                @Override
                public String getParentClassName() {
                    return memberElement.getEnclosingElement().toString();
                }

                @Override
                public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
                    return memberElement.getAnnotation(annotationClass);
                }

                @Override
                public String getFieldName() {
                    return memberElement.getSimpleName().toString();
                }

                @Override
                public String[] getAnnotationNames() {
                    List<? extends AnnotationMirror> annotationMirrors = memberElement.getAnnotationMirrors();

                    String[] annotationNames = new String[annotationMirrors.size()];

                    for (int i = 0; i < annotationMirrors.size(); i++) {
                        AnnotationMirror annotationMirror = annotationMirrors.get(i);
                        Element annotationElement = annotationMirror.getAnnotationType().asElement();

                        annotationNames[i] = annotationElement.getSimpleName().toString();
                    }
                    return annotationNames;
                }

                @Override
                public Element getElement() {
                    return memberElement;
                }
            });
        }
        return fieldInfoList;
    }

    public List<FieldInfo> getModelFieldsFromInterface(final InterfaceInfo interfaceInfo) {
        List<FieldInfo> fieldInfoList = new ArrayList<>();

        for (final InterfaceFieldInfo fieldSpec : interfaceInfo.fieldInfo) {

            fieldInfoList.add(new FieldInfo() {
                @Override
                public TypeName getTypeName() {
                    return fieldSpec.typeName;
                }

                @Override
                public String getParentClassName() {
                    return interfaceInfo.parentClassName.toString();
                }

                @Override
                public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
                    return fieldSpec.methodElement.getAnnotation(annotationClass);
                }

                @Override
                public String getFieldName() {
                    return fieldSpec.fieldName;
                }

                @Override
                public String[] getAnnotationNames() {
                    List<? extends AnnotationMirror> annotationMirrors = fieldSpec.methodElement.getAnnotationMirrors();

                    String[] annotationNames = new String[annotationMirrors.size()];

                    for (int i = 0; i < annotationMirrors.size(); i++) {
                        AnnotationMirror annotationMirror = annotationMirrors.get(i);
                        Element annotationElement = annotationMirror.getAnnotationType().asElement();

                        annotationNames[i] = annotationElement.getSimpleName().toString();
                    }
                    return annotationNames;
                }

                @Override
                public Element getElement() {
                    return fieldSpec.methodElement;
                }
            });
        }
        return fieldInfoList;
    }

}
