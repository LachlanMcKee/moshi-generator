package gsonpath.compiler

import gsonpath.model.AdapterFieldInfo

/**
 * Metadata about the field being read by the parent processor
 */
data class ExtensionFieldMetadata(
        val fieldInfo: AdapterFieldInfo,
        val variableName: String,
        val jsonPath: String,
        val isRequired: Boolean)