package com.adannunez.barrenland.services

import com.adannunez.barrenland.models.Farmland
import com.adannunez.barrenland.models.FarmlandCell
import java.awt.Point
import java.util.*

/**
 * A public class that provides some static methods to initialize and analyse land and its fertile/barren areas.
 */
class BarrenLandAnalysis {
    companion object {

        /**
         * Initialize a [Farmland] with the given width, depth, and barren areas.
         * @param sizeX the width of the farmland
         * @param sizeY the depth of the farmland
         * @param barrenAreas a collection of barren rectangles, each defined by a pair of its bottom left cell point and top right cell point
         * @return the initialized farmland with no cells visited and the appropriate cells marked as barren
         */
        fun initializeLand(sizeX: Int, sizeY: Int, barrenAreas: Collection<Pair<Point, Point>>): Farmland {
            val bottomLeftCell = FarmlandCell(Point(0, 0))
            val topRightCell = FarmlandCell(Point(sizeX - 1, sizeY - 1))

            return Farmland(bottomLeftCell, topRightCell, barrenAreas)
        }

        /**
         * Performs a basic graph traversal to search for all groups of adjacent fertile land
         * @param farmland to get fertile square meters within
         * @return a list of the square meters of fertile areas sorted from smallest to largest
         */
        fun getFertileSurfaceAreas(farmland: Farmland): List<Int> {
            val fertileSurfaceAreas: MutableList<Int> = mutableListOf()

            for (column in farmland.grid) {
                for (cell in column) {
                    if (cell.wasVisited || cell.isBarren) continue

                    val newStack = Stack<FarmlandCell>()
                    newStack.push(cell)
                    val sqft = doDfs(farmland, newStack, 0)
                    if (sqft > 0) fertileSurfaceAreas.add(sqft)
                }
            }

            return fertileSurfaceAreas.sorted()
        }

        /**
         * A tail recursive implementation of DFS. Making it tail recursive allows us to lean on the compiler
         * optimization by giving it the tailrec modifier.
         * https://kotlinlang.org/docs/reference/functions.html#tail-recursive-functions
         */
        private tailrec fun doDfs(farmland: Farmland, stack: Stack<FarmlandCell>, currentSqft: Int = 0) : Int {
            // Base case, we end recursion
            if (stack.isEmpty()) return currentSqft

            var currentCell = stack.pop()
            // Look for the next valid cell in the stack.
            // A cell might have been visited via another path after it was added to the stack, so we need to check again.
            while(currentCell.isBarren || currentCell.wasVisited) {
                // If there's nothing left in the stack, we've traversed as far as we can down this path.
                if (stack.isEmpty()) return currentSqft

                else currentCell = stack.pop()
            }

            // We have a valid, fertile cell, mark it and continue traversal
            currentCell.wasVisited = true

            // update the stack to include the new neighbors
            farmland.validNeighborsOf(currentCell).forEach { stack.push(it) }

            // make the recursion call with the updated stack and current count
            return doDfs(farmland, stack, currentSqft + 1)
        }
    }
}
