package gsonpath.compiler;

import com.squareup.javapoet.CodeBlock;
import gsonpath.model.FieldInfo;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * An extension to the Gsonpath annotation processor.
 * <p>
 * It allows external libraries to add extra code to the standard generated TypeAdapter.
 * <p>
 * Extensions are discovered at compile time using the {@link java.util.ServiceLoader} APIs.
 * <p>
 * To ensure that the extension is discovered the extension must do the following:
 * <ul>
 * <li>Implement this interface</li>
 * <li>Ensure that a public no-arg constructor is available</li>
 * <li>Annotate the class with the '@AutoService' annotation provided by 'com.google.auto.service' library</li>
 * </ul>
 */
public interface GsonPathExtension {
    /**
     * The name of the extension that will be written in the generated TypeAdapter.
     *
     * @return a non-null name that represents the extension's responsibilities.
     */
    String getExtensionName();

    /**
     * Creates a code block that executes any field validation that the implementing extension wishes. This block of
     * code will be inserted after a variable has been assigned within the 'read' method of the TypeAdapter.
     * <p>
     * Typically each extension should be used to handle a single annotation, however this is at the discression of
     * the implementing library.
     * <p>
     * Note: The parent processor will wrap this code block with a null-safety check if it is not a primitive value,
     * therefore the code block being generated does not need to implement this behaviour.
     *
     * @param processingEnvironment exposes facilities provided by the annotation processor framework
     * @param fieldInfo             all the available information about the field being read by the parent processor
     * @param variableName          the name of the variable within the TypeAdapter
     * @return a code block if applicable, can be null.
     */
    CodeBlock createFieldReadCodeBlock(ProcessingEnvironment processingEnvironment, FieldInfo fieldInfo, String variableName);
}
