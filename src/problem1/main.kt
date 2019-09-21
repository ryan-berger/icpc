package problem1

import java.io.File


fun main() {
    val rawInput =
        File("/home/ryan/Downloads/icpc2019data/A-azulejos/secret-30.in")
            .readLines()
            .map { line -> line.split(" ") }
            .map { splitLine -> splitLine.map { it.toInt() } }

    val joaoTiles = rawInput[1]
        .mapIndexed { i, price ->
            Tile(height = rawInput[2][i], price = price, index = i + 1)
        }
        .sortedWith(compareBy(Tile::price, Tile::height))

    val mariaTiles = rawInput[3]
        .mapIndexed { i, price ->
            Tile(height = rawInput[4][i], price = price, index = i + 1)
        }
        .sortedByDescending { t -> t.height }
        .groupByTo(LinkedHashMap(), {it.price})

    val solution: MutableList<Pair<Tile, Tile>> = mutableListOf()

    var invalidPairing = false

    joaoTiles.forEach { tile ->
        val min = mariaTiles.keys.min()!!
        val heightList = mariaTiles.getValue(min)
        val pairIndex = heightList.indexOfFirst { mTile -> mTile.height < tile.height }
        if (pairIndex == -1) {
            invalidPairing = true
            return@forEach
        }

        solution.add(Pair(tile, heightList[pairIndex]))
        heightList.removeAt(pairIndex)

        if (heightList.isEmpty()) {
            mariaTiles.remove(min)
        }
    }

    if (invalidPairing) {
        println("impossible")
        return
    }

    println(solution.map { it.first.index }.joinToString(" "))
    println(solution.map { it.second.index }.joinToString(" "))
}