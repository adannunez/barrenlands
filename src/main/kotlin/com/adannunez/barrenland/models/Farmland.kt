package com.adannunez.barrenland.models

import com.adannunez.barrenland.extensions.at
import java.awt.Point

/**
 * Represents a farm and its land. Mostly maintains the grid of 1 meter X 1 meter [FarmlandCell]s, the size of the land,
 * and some basics to facilitate the graph traversal through the cells.
 */
class Farmland(private val bottomLeftCell: FarmlandCell, private val topRightCell: FarmlandCell) {

    val grid: Array<Array<FarmlandCell>> = Array(topRightCell.point.x + 1) { x ->
        Array(topRightCell.point.y + 1) { y ->
            FarmlandCell(Point(x, y))
        }
    }

    /**
     * Secondary constructor to initialize the farm with barren land.
     */
    constructor(_bottomLeftCell: FarmlandCell, _topRightCell: FarmlandCell, barrenAreas: Collection<Pair<Point, Point>>)
            : this(_bottomLeftCell, _topRightCell) {
        markBarrenLand(barrenAreas)
    }

    fun validNeighborsOf(farmlandCell: FarmlandCell) =
            farmlandCell.neighborPoints
                    .filter { pointIsWithinBounds(it) } // Only look at the points within the grid bounds
                    .map { grid.at(it.x, it.y) } // Once we have valid points, go the cells from the grid
                    .filter { !it.isBarren && !it.wasVisited }

    private fun pointIsWithinBounds(point: Point) : Boolean {
        return (point.x >= bottomLeftCell.point.x && point.x <= topRightCell.point.x
                && point.y >= bottomLeftCell.point.y && point.y <= topRightCell.point.y)
    }

    private fun markBarrenLand(barrenAreas: Collection<Pair<Point, Point>>) {
        // go to every FarmlandCell within the barren area ranges and mark as barren
        for (barrenArea in barrenAreas) {
            for (x in barrenArea.first.x..barrenArea.second.x) {
                for (y in barrenArea.first.y..barrenArea.second.y) {
                    grid.at(x, y).isBarren = true
                }
            }
        }
    }

    // Assuming we don't need to traverse diagonally to completely capture fertile land
    private val FarmlandCell.neighborPoints: List<Point>
    get() {
        return listOf(topNeighborPoint, rightNeighborPoint, bottomNeighborPoint, leftNeighborPoint)
    }

    private val FarmlandCell.topNeighborPoint: Point
        get() {
            val newPoint = Point(point)
            newPoint.translate(0, 1)
            return newPoint
        }

    private val FarmlandCell.rightNeighborPoint: Point
        get() {
            val newPoint = Point(point)
            newPoint.translate(1, 0)
            return newPoint
        }

    private val FarmlandCell.bottomNeighborPoint: Point
        get() {
            val newPoint = Point(point)
            newPoint.translate(0, -1)
            return newPoint
        }

    private val FarmlandCell.leftNeighborPoint: Point
        get() {
            val newPoint = Point(point)
            newPoint.translate(-1, 0)
            return newPoint
        }
}
