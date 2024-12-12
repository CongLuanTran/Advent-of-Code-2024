import kotlin.time.measureTime

fun main() {

    class Plot(val x: Int, val y: Int) {
        fun isAdjacent(other: Plot) =
            this.x == other.x - 1 && this.y == other.y || this.x == other.x + 1 && this.y == other.y || this.x == other.x && this.y == other.y - 1 || this.x == other.x && this.y == other.y + 1

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Plot

            if (x != other.x) return false
            if (y != other.y) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            return result
        }

        override fun toString(): String {
            return "Plot($x, $y)"
        }
    }

    fun parse(input: List<String>): MutableMap<Char, MutableList<Plot>> {
        val map = mutableMapOf<Char, MutableList<Plot>>()
        for (i in input.indices) {
            for (j in input[0].indices) {
                val list = map.getOrPut(input[i][j]) { mutableListOf() }
                list.add(Plot(i, j))
            }
        }
        return map
    }

    fun findRegions(plotMap: List<Plot>): MutableList<MutableList<Plot>> {
        var regions = mutableListOf<MutableList<Plot>>()
        var plots = plotMap.toMutableList()
        while (!plots.isEmpty()) {
            val list = mutableListOf(plots.first())
            do {
                plots -= list
                var connected = plots.filter { plot -> list.any { it.isAdjacent(plot) } }
                list += connected
            } while (!connected.isEmpty())
            regions.add(list)
        }
        return regions
    }

    fun findPerimeter(region: MutableList<Plot>): Int {
        return region.sumOf { plot -> 4 - region.count { it.isAdjacent(plot) } }
    }

    fun findLines(region: MutableList<Plot>): Int {
        /*
        Line are simply regions that are connected only vertically or horizontally
        So we split a region into list of plots that lie on the same line with groupBy
        Then we convert them into regions (to split the plot that not connected)
        The list of point are not guaranteed to be sorted, so we have to sort them along the axis
         */
        var horizontalLines = region.groupBy { it.x }.flatMap { (_, plots) -> findRegions(plots.toMutableList()) }
            .map { line -> line.sortedBy { it.y } }

        var verticalLines = region.groupBy { it.y }.flatMap { (_, plots) -> findRegions(plots.toMutableList()) }
            .map { line -> line.sortedBy { it.x } }

        /*
        Then for each line, we check if they can be combined into another longer neighbour line.
        Because we consider horizontal and vertical line separately, each line now only have 2 sides
        If they can merge one of their line is no longer counted. However, merging can also split the
        longer line when we merge into the middle of the line. Ex:
                E
                EE
                E
        When the middle E merge into the longer vertical line, it creates a new line! So when we merge into the middle
        of a long line, we also split it
        The final formula would be 2 - number of merge + number of split
         */
        var lineCount = 0
        lineCount += horizontalLines.sumOf { line ->
            val merge = horizontalLines.count { other ->
                other.first().x - line.first().x in listOf(-1, 1)
                    && other.first().y <= line.first().y && other.last().y >= line.last().y
            }
            val split = horizontalLines.count { other ->
                other.first().x - line.first().x in listOf(-1, 1)
                    && other.first().y < line.first().y && other.last().y > line.last().y
            }
            2 - merge + split
        }
        lineCount += verticalLines.sumOf { line ->
            val merge = verticalLines.count { other ->
                other.first().y - line.first().y in listOf(-1, 1)
                    && other.first().x <= line.first().x && other.last().x >= line.last().x
            }
            val split = verticalLines.count { other ->
                other.first().y - line.first().y in listOf(-1, 1)
                    && other.first().x > line.first().x && other.last().x < line.last().x
            }
            2 - merge + split
        }

        return lineCount
    }

    fun part1(input: List<String>): Int {
        return parse(input).flatMap { (_, plots) -> findRegions(plots) }.sumOf { it.size * findPerimeter(it) }
    }

    fun part2(input: List<String>): Int {
        return parse(input).flatMap { (_, plots) -> findRegions(plots) }.sumOf { it.size * findLines(it) }
    }

    val input = readInput("Day12")
    measureTime {
        part1(input).println()
    }.println()
    measureTime {
        part2(input).println()
    }.println()
}