package gsonpath.generator.streamer

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class GsonArrayStreamerGeneratorTest : BaseGeneratorTest() {
    @Test
    fun testNoRoot() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("streamer/no_root")
                .addRelativeSource("TestNoRoot.java")
                .addRelativeGenerated("TestNoRoot_GsonArrayStreamer.java"))
    }

    @Test
    fun testRootReadingObjectEntirely() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("streamer/read_object_entirely")
                .addRelativeSource("TestReadObjectEntirely.java")
                .addRelativeGenerated("TestReadObjectEntirely_GsonArrayStreamer.java"))
    }

    @Test
    fun testRootPartialStreamReading() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("streamer/partial_stream_reading")
                .addRelativeSource("TestStreamReading.java")
                .addRelativeGenerated("TestStreamReading_GsonArrayStreamer.java"))
    }
}
