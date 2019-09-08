package gsonpath.adapter.standard.model

import gsonpath.model.FieldInfo
import gsonpath.util.FieldNamingPolicyMapper

class FieldPathFetcher(
        private val serializedNameFetcher: SerializedNameFetcher,
        private val fieldNamingPolicyMapper: FieldNamingPolicyMapper
): BahFieldPathFetcher<FieldInfo> {

    override fun getJsonFieldPath(bah: FieldInfo, metadata: GsonObjectMetadata): FieldPath {
        val serializedName = serializedNameFetcher.getSerializedName(bah, metadata.flattenDelimiter)
        val path = if (serializedName != null && serializedName.isNotBlank()) {
            if (metadata.pathSubstitutions.isNotEmpty()) {

                // Check if the serialized name needs any values to be substituted
                metadata.pathSubstitutions.fold(serializedName) { fieldPath, substitution ->
                    fieldPath.replace("{${substitution.original}}", substitution.replacement)
                }

            } else {
                serializedName
            }

        } else {
            // Since the serialized annotation wasn't specified, we need to apply the naming policy instead.
            fieldNamingPolicyMapper.applyFieldNamingPolicy(metadata.gsonFieldNamingPolicy, bah.fieldName)
        }

        return if (path.contains(metadata.flattenDelimiter)) {
            FieldPath.Nested(
                    if (path.last() == metadata.flattenDelimiter) {
                        path + bah.fieldName
                    } else {
                        path
                    }
            )
        } else {
            FieldPath.Standard(path)
        }
    }
}