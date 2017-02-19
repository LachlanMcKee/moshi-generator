package gsonpath;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows a developer to mark a gson field as heterogenous, and exposes the ability to map specific types to a key value
 * that is found within the field name defined within the model being deserialized.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface GsonSubtype {

    /**
     * The name of the field contained within the json object that is used to determine what the type should be
     * instantiated.
     *
     * @return a non-null field name that belongs to either a String, Integer or Boolean value.
     */
    String fieldName();

    /**
     * Whether the TypeAdapter should throw an exception when an unexpected type is found.
     *
     * @return true if the TypeAdapter should throw an exception.
     */
    boolean failOnMissingKey() default false;

    /**
     * An array of string keys and their related subtype class, this may be empty, however one of the 'keys' arrays
     * must be assigned, otherwise the processor will fail.
     *
     * @return the array of string keys and their related subtype class.
     */
    StringKey[] stringKeys() default {};

    /**
     * An array of integer keys and their related subtype class, this may be empty, however one of the 'keys' arrays
     * must be assigned, otherwise the processor will fail.
     *
     * @return the array of integer keys and their related subtype class.
     */
    IntegerKey[] integerKeys() default {};

    /**
     * An array of boolean keys and their related subtype class, this may be empty, however one of the 'keys' arrays
     * must be assigned, otherwise the processor will fail.
     *
     * @return the array of boolean keys and their related subtype class.
     */
    BooleanKey[] booleanKeys() default {};

    /**
     * Represents a string key and its related subtype class.
     * Whenever the key value is found, the associated subtype will be instatiated.
     */
    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @interface StringKey {
        /**
         * The key that maps to the subtype.
         *
         * @return a non-null key.
         */
        String key();

        /**
         * The subtype that is used when the appropriate key is found.
         * This class MUST be a subtype of either:
         * 1. The annotated field
         * 2. The method return type (for interfaces)
         * 3. The List type for interfaces that extend List
         *
         * @return a non-null class.
         */
        Class subtype();
    }

    /**
     * Represents a integer key and its related subtype class.
     * Whenever the key value is found, the associated subtype will be instatiated.
     */
    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @interface IntegerKey {
        /**
         * The key that maps to the subtype.
         *
         * @return a non-null key.
         */
        int key();

        /**
         * The subtype that is used when the appropriate key is found.
         * This class MUST be a subtype of either:
         * 1. The annotated field
         * 2. The method return type (for interfaces)
         * 3. The List type for interfaces that extend List
         *
         * @return a non-null class.
         */
        Class subtype();
    }

    /**
     * Represents a boolean key and its related subtype class.
     * Whenever the key value is found, the associated subtype will be instatiated.
     */
    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @interface BooleanKey {
        /**
         * The key that maps to the subtype.
         *
         * @return a non-null key.
         */
        boolean key();

        /**
         * The subtype that is used when the appropriate key is found.
         * This class MUST be a subtype of either:
         * 1. The annotated field
         * 2. The method return type (for interfaces)
         * 3. The List type for interfaces that extend List
         *
         * @return a non-null class.
         */
        Class subtype();
    }
}
