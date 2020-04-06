package com.adannunez.barrenland.models

import java.awt.Point

/**
 * A basic 1 meter X 1 meter cell of land that will be in the [Farmland.grid] at coordinates [point].
 */
data class FarmlandCell(val point: Point, var isBarren: Boolean = false, var wasVisited: Boolean = false)
