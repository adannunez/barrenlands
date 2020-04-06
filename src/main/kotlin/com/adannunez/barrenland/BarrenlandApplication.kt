package com.adannunez.barrenland

import com.adannunez.barrenland.services.BarrenLandAnalysis
import com.google.gson.Gson
import java.awt.Point
import java.io.InputStream
import java.io.PrintStream
import java.util.*

/**
 * The class that starts it all. Mainly worries about the I/O as specified by the exercise.
 */
class BarrenlandApplication {
    companion object {
        /**
         * Waits for user input then interprets it.
         */
        fun handleInput(inputStream: InputStream, outputStream: PrintStream) {
            val inputScanner = Scanner(inputStream)

            // Keep the app alive and accepting more input of new barren lands
            while (inputScanner.hasNextLine()) {
                // check for terminating command
                val input = inputScanner.nextLine()
                if (input == "exit") {
                    outputStream.println("Exiting application. Adios!")
                    return
                }

                // sanitize and map the input to objects and data structures we can use
                // assuming the input will actually contain the curly brackets
                val barrenCoordinatesString = input.replace("{", "[").replace("}", "]")
                val barrenCoordinatesList = Gson().fromJson(barrenCoordinatesString, Array<String>::class.java)!!
                val barrenAreas = barrenCoordinatesList.map { it.toPointPair() }

                // This could become parameterized somehow...
                val width = 400
                val depth = 600
                val maxBarrenAreas = 1000

                // Adding this restriction to control runtime
                if (barrenAreas.size > maxBarrenAreas) {
                    outputStream.println("Cannot have more 1000 barren areas defined. Try Again!")
                    return
                }

                // Now we're actually doing stuff...
                val land = BarrenLandAnalysis.initializeLand(width, depth, barrenAreas)
                val fertileSurfaceAreas = BarrenLandAnalysis.getFertileSurfaceAreas(land)

                // produce the output as defined in the exercise
                outputStream.println(fertileSurfaceAreas.joinToString(separator = " ", prefix = "", postfix = ""))
            }
        }
    }
}

fun String.toPointPair() : Pair<Point, Point> {
    val coords = this.split(" ").map { it.toInt() }
    val firstPoint = Point(coords[0], coords[1])
    val secondPoint = Point(coords[2], coords[3])
    return Pair(firstPoint, secondPoint)
}

fun main() {
    println("Ready for input! Please enter barren lands.")
    BarrenlandApplication.handleInput(System.`in`, System.out)
}

/** example inputs
 * {"0 292 399 307"} -> 116800  116800
 * {"48 192 351 207", "48 392 351 407", "120 52 135 547", "260 52 275 547"} -> 22816 192608
 */
