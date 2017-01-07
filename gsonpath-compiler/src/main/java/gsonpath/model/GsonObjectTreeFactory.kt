package gsonpath.model

import com.google.gson.FieldNamingPolicy
import gsonpath.GsonFieldValidationType
import gsonpath.PathSubstitution
import gsonpath.ProcessingException

import java.util.ArrayList
import java.util.regex.Pattern

class GsonObjectTreeFactory {
    @Throws(ProcessingException::class)
    fun createGsonObject(fieldInfoList: List<FieldInfo>,
                         rootField: String,
                         flattenDelimiter: Char,
                         gsonFieldNamingPolicy: FieldNamingPolicy,
                         gsonFieldValidationType: GsonFieldValidationType,
                         pathSubstitutions: Array<PathSubstitution>): GsonObject {

        // Obtain the correct mapping structure beforehand.
        val absoluteRootObject = GsonObject()
        var gsonPathObject = absoluteRootObject

        val gsonObjectFactory = GsonObjectFactory()

        if (rootField.isNotEmpty()) {
            gsonPathObject = createGsonObjectFromRootField(gsonPathObject, rootField, flattenDelimiter)

        } else {
            gsonPathObject = absoluteRootObject
        }

        for (fieldInfoIndex in fieldInfoList.indices) {
            gsonObjectFactory.addGsonType(gsonPathObject, fieldInfoList[fieldInfoIndex], fieldInfoIndex,
                    flattenDelimiter, gsonFieldNamingPolicy, gsonFieldValidationType, pathSubstitutions)
        }
        return absoluteRootObject
    }

    fun createGsonObjectFromRootField(rootObject: GsonObject, rootField: String, flattenDelimiter: Char): GsonObject {
        if (rootField.isEmpty()) {
            return rootObject
        }

        var currentRootObject = rootObject
        val regexSafeDelimiter = Pattern.quote(flattenDelimiter.toString())
        val split = rootField.split(regexSafeDelimiter.toRegex()).dropLastWhile(String::isEmpty).toTypedArray()

        if (split.isNotEmpty()) {
            // Keep adding branches to the tree and switching our root to the new branch.
            for (field in split) {
                val currentObject = GsonObject()
                currentRootObject.addObject(field, currentObject)
                currentRootObject = currentObject
            }

            return currentRootObject

        } else {
            // Add a single branch to the tree and return the new branch.
            val mapWithRoot = GsonObject()
            currentRootObject.addObject(rootField, mapWithRoot)
            return mapWithRoot
        }
    }

    fun getFlattenedFieldsFromGsonObject(gsonObject: GsonObject): List<GsonField> {
        val flattenedFields = ArrayList<GsonField>()
        getFlattenedFields(gsonObject, flattenedFields)

        flattenedFields.sort { o1, o2 -> Integer.compare(o1.fieldIndex, o2.fieldIndex) }

        return flattenedFields
    }

    private fun getFlattenedFields(currentGsonObject: GsonObject, flattenedFields: MutableList<GsonField>) {
        currentGsonObject.keySet()
                .map { currentGsonObject[it]!! }
                .forEach {
                    if (it is GsonField) {
                        flattenedFields.add(it)

                    } else {
                        val nextLevelMap = it as GsonObject
                        if (nextLevelMap.size() > 0) {
                            getFlattenedFields(nextLevelMap, flattenedFields)
                        }
                    }
                }
    }
}
