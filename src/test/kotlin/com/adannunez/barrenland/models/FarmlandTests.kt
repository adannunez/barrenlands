package com.adannunez.barrenland.models

import com.adannunez.barrenland.extensions.at
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.awt.Point
import java.util.stream.Stream

class FarmlandTests {

    @Test
    fun `grid is constructed`() {
        val bottomLeft = FarmlandCell(Point(0, 0))
        val topRight = FarmlandCell(Point(2, 2))
        val farmland = Farmland(bottomLeft, topRight)

        farmland.grid.size shouldBeEqualTo 3
        farmland.grid[0].size shouldBeEqualTo 3

        for (x in bottomLeft.point.x..topRight.point.x) {
            for (y in bottomLeft.point.y..topRight.point.y) {
                val cell = farmland.grid.at(x, y)
                cell.isBarren shouldBe false
                cell.wasVisited shouldBe false
            }
        }
    }

    @Test
    fun `barren lands are marked`() {
        val bottomLeft = FarmlandCell(Point(0, 0))
        val topRight = FarmlandCell(Point(2, 2))
        val farmland = Farmland(bottomLeft, topRight, listOf(Pair(Point(0, 2), Point(1, 2))))

        for (row in farmland.grid) {
            for (cell in row) {
                if (cell.point in listOf(Point(0,2), Point(1,2))) {
                    cell.isBarren shouldBe true
                    cell.isBarren shouldBe true
                }
                else cell.isBarren shouldBe false
            }
        }
    }

    @ParameterizedTest
    @MethodSource("neighborTests")
    fun `valid neighbors are calculated correctly`(cell: FarmlandCell, expectedNeighbors: List<FarmlandCell>) {
        val bottomLeft = FarmlandCell(Point(0, 0))
        val topRight = FarmlandCell(Point(2, 2))
        val farmland = Farmland(bottomLeft, topRight, listOf(Pair(Point(0, 0), Point(0, 0)), Pair(Point(1, 1), Point(1, 1))))

        val neighborsOfCenter = farmland.validNeighborsOf(cell)
        neighborsOfCenter shouldContainSame expectedNeighbors
    }
    companion object {
        @JvmStatic
        fun neighborTests(): Stream<Arguments> =
                Stream.of(
                        Arguments.of(FarmlandCell(Point(1, 1)), listOf(
                                FarmlandCell(Point(0, 1)),
                                FarmlandCell(Point(1, 2)),
                                FarmlandCell(Point(2, 1)),
                                FarmlandCell(Point(1, 0)))),
                        Arguments.of(FarmlandCell(Point(1, 0)), listOf(
                                FarmlandCell(Point(2, 0)))),
                        Arguments.of(FarmlandCell(Point(2, 2)), listOf(
                                FarmlandCell(Point(1, 2)),
                                FarmlandCell(Point(2, 1))))
                )
    }
}