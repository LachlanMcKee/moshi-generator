package gsonpath.model

class MandatoryFieldInfoFactory {

    /**
     * Add any mandatory field indexes as constants. This is done for code readability.
     * We will obtain the values using a depth-first recursion.
     */
    fun createMandatoryFieldsFromGsonObject(gsonObject: GsonObject): Map<String, MandatoryFieldInfo> {
        return gsonObject.entries()
                .map { it.value }
                .fold(emptyMap()) { map, gsonModel ->
                    when (gsonModel) {
                        is GsonField -> createConstantForField(gsonModel, map)
                        is GsonArray ->
                            gsonModel.entries()
                                    .map { it.value }
                                    .fold(map) { arrayMap, arrayGsonModel ->
                                        when (arrayGsonModel) {
                                            is GsonField ->
                                                createConstantForField(arrayGsonModel, arrayMap)

                                            is GsonObject ->
                                                arrayMap.plus(createMandatoryFieldsFromGsonObject(arrayGsonModel))
                                        }
                                    }
                        is GsonObject ->
                            // Recursive call, navigating further down the tree.
                            map.plus(createMandatoryFieldsFromGsonObject(gsonModel))
                    }
                }
    }

    private fun createConstantForField(info: GsonField, currentMap: Map<String, MandatoryFieldInfo>): Map<String, MandatoryFieldInfo> {
        //
        // For all required fields we add an index field so we can easily check whether the
        // value has been assigned after the json has been parsed.
        //
        if (info.isRequired) {
            val fieldName = info.fieldInfo.fieldName
            val mandatoryFieldIndexName = "MANDATORY_INDEX_" + fieldName.toUpperCase()

            // Keep track of the information for later use. Since this is a linked list, we keep track of insert order.
            return currentMap.plus(Pair(fieldName, MandatoryFieldInfo(mandatoryFieldIndexName, info)))
        }
        return currentMap
    }

}
