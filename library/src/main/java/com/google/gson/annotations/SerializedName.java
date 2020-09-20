package com.google.gson.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface SerializedName {

  /**
   * @return the desired name of the field when it is serialized or deserialized
   */
  String value();
  /**
   * @return the alternative names of the field when it is deserialized
   */
  String[] alternate() default {};
}
