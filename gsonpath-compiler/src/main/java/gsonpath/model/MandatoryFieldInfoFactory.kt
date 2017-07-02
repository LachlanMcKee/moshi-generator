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
                        is GsonField -> {
                            //
                            // For all required fields we add an index field so we can easily check whether the
                            // value has been assigned after the json has been parsed.
                            //
                            if (gsonModel.isRequired) {
                                val fieldName = gsonModel.fieldInfo.fieldName
                                val mandatoryFieldIndexName = "MANDATORY_INDEX_" + fieldName.toUpperCase()

                                // Keep track of the information for later use. Since this is a linked list, we keep track of insert order.
                                return@fold map.plus(Pair(fieldName, MandatoryFieldInfo(mandatoryFieldIndexName, gsonModel)))
                            }
                            return@fold map
                        }

                        is GsonObject ->
                            // Recursive call, navigating further down the tree.
                            return@fold map.plus(createMandatoryFieldsFromGsonObject(gsonModel))
                    }
                }
    }

}
