package com.adannunez.barrenland

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.*
import java.util.stream.Stream

class ApplicationTests {

    @ParameterizedTest
    @MethodSource("ioTests")
    fun `input produces expected output`(input: String, output: String) {
        val inputStream = input.byteInputStream()
        val outputStream = ByteArrayOutputStream()

        BarrenlandApplication.handleInput(inputStream, PrintStream(outputStream))
        val result = outputStream.toString()

        result shouldBeEqualTo output
    }
    companion object {
        @JvmStatic
        fun ioTests(): Stream<Arguments> =
                Stream.of(
                        Arguments.of("{\"0 292 399 307\"}", "116800 116800\n"),
                        Arguments.of("{\"48 192 351 207\", \"48 392 351 407\", \"120 52 135 547\", \"260 52 275 547\"}", "22816 192608\n")
                )
    }

    @Test
    fun `over 1000 barren lands exits`() {
        val inputBuilder = StringBuilder()
        inputBuilder.append("{")
        for (areaCount in 1..1000) {
            inputBuilder.append("\"$areaCount $areaCount ${areaCount+1} ${areaCount+1}\", ")
        }
        inputBuilder.append("\"1001 1001 1001 1001\"")
        inputBuilder.append("}")
        val input = inputBuilder.toString()
        val inputStream = input.byteInputStream()
        val outputStream = ByteArrayOutputStream()

        BarrenlandApplication.handleInput(inputStream, PrintStream(outputStream))
        val result = outputStream.toString()

        result shouldBeEqualTo "Cannot have more 1000 barren areas defined. Try Again!\n"
    }
}