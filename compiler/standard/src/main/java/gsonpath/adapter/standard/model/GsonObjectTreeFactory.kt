package gsonpath.adapter.standard.model

import gsonpath.ProcessingException
import gsonpath.model.FieldInfo
import java.util.regex.Pattern

data class GsonTreeResult<T>(
        val rootObject: GsonObject<T>,
        val flattenedFields: List<GsonField<T>>
)

class GsonObjectTreeFactory<T>(private val gsonObjectFactory: GsonObjectFactory<T>) {
    @Throws(ProcessingException::class)
    fun createGsonObject(
            fieldInfoList: List<FieldInfo>,
            rootField: String,
            metadata: GsonObjectMetadata): GsonTreeResult<T> {

        // Obtain the correct mapping structure beforehand.
        val absoluteRootObject = MutableGsonObject<T>()

        val gsonPathObject =
                if (rootField.isNotEmpty()) {
                    createGsonObjectFromRootField(absoluteRootObject, rootField, metadata.flattenDelimiter)

                } else {
                    absoluteRootObject
                }

        for (fieldInfoIndex in fieldInfoList.indices) {
            gsonObjectFactory.addGsonType(gsonPathObject, fieldInfoList[fieldInfoIndex], fieldInfoIndex, metadata)
        }
        val immutableAbsoluteGsonObject = absoluteRootObject.toImmutable()
        val flattenedFields = getFlattenedFields(immutableAbsoluteGsonObject).sortedBy { it.fieldIndex }
        return GsonTreeResult(immutableAbsoluteGsonObject, flattenedFields)
    }

    private fun createGsonObjectFromRootField(
            rootObject: MutableGsonObject<T>,
            rootField: String,
            flattenDelimiter: Char): MutableGsonObject<T> {

        if (rootField.isEmpty()) {
            return rootObject
        }

        val regexSafeDelimiter = Pattern.quote(flattenDelimiter.toString())
        val split = rootField.split(regexSafeDelimiter.toRegex()).dropLastWhile(String::isEmpty)

        if (split.isNotEmpty()) {
            // Keep adding branches to the tree and switching our root to the new branch.
            return split.fold(rootObject) { currentRoot, field ->
                val currentObject = MutableGsonObject<T>()
                currentRoot.addObject(field, currentObject)
                return@fold currentObject
            }

        } else {
            // Add a single branch to the tree and return the new branch.
            val mapWithRoot = MutableGsonObject<T>()
            rootObject.addObject(rootField, mapWithRoot)
            return mapWithRoot
        }
    }

    private fun getFlattenedFields(currentGsonObject: GsonObject<T>): List<GsonField<T>> {
        return currentGsonObject.entries()
                .flatMap { (_, gsonType) ->
                    when (gsonType) {
                        is GsonField -> listOf(gsonType)
                        is GsonObject -> getFlattenedFields(gsonType)
                        is GsonArray -> {
                            gsonType.entries()
                                    .flatMap { (_, arrayGsonType) ->
                                        when (arrayGsonType) {
                                            is GsonField -> listOf(arrayGsonType)
                                            is GsonObject -> getFlattenedFields(arrayGsonType)
                                        }
                                    }
                        }
                    }
                }
    }
}
