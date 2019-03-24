package gsonpath.generator.standard.extension.flatten;

import gsonpath.AutoGsonAdapter;
import gsonpath.extension.annotation.FlattenJson;

@AutoGsonAdapter
interface FlattenModel {
    @FlattenJson
    String getValue();
}
