package gsonpath.adapter

import com.squareup.javapoet.TypeName
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import gsonpath.util.MethodSpecExt
import java.io.IOException

object AdapterMethodBuilder {
    fun createReadMethodBuilder(returnTypeName: TypeName) = MethodSpecExt.overrideMethodBuilder("readImpl").apply {
        returns(returnTypeName)
        addParameter(JsonReader::class.java, Constants.READER)
        addException(IOException::class.java)
    }

    fun createWriteMethodBuilder(writtenValueTypeName: TypeName) = MethodSpecExt.overrideMethodBuilder("writeImpl").apply {
        addParameter(JsonWriter::class.java, Constants.WRITER)
        addParameter(writtenValueTypeName, Constants.VALUE)
        addException(IOException::class.java)
    }
}
