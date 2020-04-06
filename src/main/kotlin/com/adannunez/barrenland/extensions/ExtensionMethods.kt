package com.adannunez.barrenland.extensions

import com.adannunez.barrenland.models.FarmlandCell

fun Array<Array<FarmlandCell>>.at(x: Int, y: Int) : FarmlandCell {
    return this[x][y]
}
