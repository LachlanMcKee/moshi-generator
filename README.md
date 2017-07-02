# Gson Path

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.lachlanmckee/gsonpath/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.lachlanmckee/gsonpath) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-gsonpath-green.svg?style=true)](https://android-arsenal.com/details/1/4191)

An annotation processor library which generates gson type adapters at compile time which also use basic JsonPath functionality.

The benefits of this library are as follows:
- Statically generated Gson Type Adapters can remove the majority of reflection used by the Gson library.
- JsonPath syntax can reduce the number of POJOs required to parse a JSON file. An example of this is shown in the next section.
   - This allows for easier integration with other libraries which rely on a flat class structure (such as DBFlow).
- Add optional client side validation to your json using `@Nullable` and `NonNull` annotations to add mandatory field constraints.
- Generates immutable POJOs based off annotated interfaces
   - Similar to AutoValue, however you do not need to reference the concrete implementation as the Type Adapter creates it on your behalf.
   - See the [interfaces guide](guides/interfaces.md) for further details.
- Reduce the amount of repetition when creating POJOs using Path Substitutions. 
   - A more powerful version of the Gson `SerializedName` alternate key by using string replacement functionality within the `AutoGsonAdapter` annotation.
   - See the [path substitution guide](guides/path_substitution.md) for further details.

## Example
Given the following JSON:

```json
{
   "person": {
      "names": {
         "first": "Lachlan",
         "last": "McKee"
      }
   }
}
```

We can deserialize the content with a single class by using Gson Path. The following class demonstrates the annotations required to create a type adapter which can correctly read the content.

```java
@AutoGsonAdapter(rootField = "person.names")
public class PersonModel {
   @SerializedName("first")
   String firstName;
   
   @SerializedName("last")
   String lastName;
}
```

We could also write it as follows (to reduce the number of annotations required):

```java
@AutoGsonAdapter(rootField = "person.names")
public class PersonModel {
   String first;
   String last;
}
```

## Setup
The following steps are required to use the generated `TypeAdapters` within your project.

### AutoGsonAdapterFactory
Create a type adapter factory by annotating an interface as follows:

```java
package com.example;
 
@AutoGsonAdapterFactory
public interface ExampleGsonTypeFactory extends TypeAdapterFactory {
}
```

Gson Path can be used across multiple modules by defining a factory within each. 

*Note: Only one `@AutoGsonAdapterFactory` annotation may be used per module/project. If you do this accidentally, the annotation processor will raise a helpful error.*

### AutoGsonAdapter
Create any number of type adapters by annotating a class or interface as follows:

```java
package com.example;
 
@AutoGsonAdapter
public class ExampleModel {
    String value;
}
```

or

```java
package com.example;
 
@AutoGsonAdapter
public interface ExampleModel {
    String getValue();
}
```

### Gson Integration
For each type adapter factory interface, register it with your Gson builder as follows:

```
return new GsonBuilder()
                .registerTypeAdapterFactory(GsonPath.createTypeAdapterFactory(ExampleGsonTypeFactory.class))
                .create();
```

## Proguard
To use proguard within your project, you must add the generated type adapter factory. Using the example above, this would be:

```
-keep public class com.example.ExampleGsonTypeFactoryImpl
```

## Download
This library is available on Maven, you can add it to your project using the following gradle dependencies:

```gradle
compile 'net.lachlanmckee:gsonpath:2.2.0'
apt 'net.lachlanmckee:gsonpath-compiler:2.2.0'
```
