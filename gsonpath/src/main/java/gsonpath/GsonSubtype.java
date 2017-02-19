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

    String fieldName();

    boolean failOnMissingKey() default false;

    StringKey[] stringKeys();

    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @interface StringKey {
        String key();

        Class subtype();
    }

}
