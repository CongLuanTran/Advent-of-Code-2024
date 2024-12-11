import java.util.*

class Point2D(val x: Int, val y: Int) {

    fun inGird(grid: List<String>): Boolean {
        return x in grid[0].indices && y in grid.indices
    }

    override fun toString(): String {
        return "($x, $y)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point2D

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(x, y)
    }

}

fun mapPoints(input: List<String>): Map<Char, List<Point2D>> {
    return input.flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, c ->
            if (c == '.') null else c to Point2D(x, y)
        }
    }.groupBy({ it.first }) { it.second }
}

fun findAntinodes(a: Point2D, b: Point2D, grid: List<String>): List<Point2D> {
    val xDiff = b.x - a.x
    val yDiff = b.y - a.y
    return listOf(
        Point2D(b.x + xDiff, b.y + yDiff),
        Point2D(a.x - xDiff, a.y - yDiff)
    )
        .filter { it.inGird(grid) }
}

fun findAntinodes2(a: Point2D, b: Point2D, grid: List<String>): List<Point2D> {
    val xDiff = b.x - a.x
    val yDiff = b.y - a.y
    return buildList {
        add(a)
        add(b)
        while (true) {
            val p = Point2D(this.last().x + xDiff, this.last().y + yDiff)
            if (!p.inGird(grid)) break
            add(p)
        }
        while (true) {
            val p = Point2D(this.last().x - xDiff, this.last().y - yDiff)
            if (!p.inGird(grid)) break
            add(p)
        }
    }
}

fun part1(input: List<String>): Int {
    val dict = mapPoints(input)
    val antinodes = mutableSetOf<Point2D>()
    dict.values.forEach {
        if (it.size == 1) antinodes += it.first()
        else {
            for (i in 0..it.size - 2) {
                for (j in i + 1..<it.size) {
                    antinodes += findAntinodes(it[i], it[j], input)
                }
            }
        }
    }
    return antinodes.size
}

fun part2 (input: List<String>): Int {
    val dict = mapPoints(input)
    val antinodes = mutableSetOf<Point2D>()
    dict.values.forEach {
        if (it.size == 1) antinodes += it.first()
        else {
            for (i in 0..it.size - 2) {
                for (j in i + 1..<it.size) {
                    antinodes += findAntinodes2(it[i], it[j], input)
                }
            }
        }
    }
    return antinodes.size
}

fun main() {
    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}