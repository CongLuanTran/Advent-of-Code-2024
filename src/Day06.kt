import kotlin.time.measureTime

fun main() {


    val input = readInput("Day06")
    var pos = 0 to 0
    for (i in input.indices) {
        for (j in input.first().indices) {
            if (input[i][j] == '^') pos = i to j
        }
    }

    val width = input.first().length
    val height = input.size
    var grid = input.toMutableList().map { it.toCharArray() }
    var gridCopy = grid.map { it.copyOf() }
    val dir = listOf(-1 to 0, 0 to 1, 1 to 0, 0 to -1)

    fun hasRepetition(grid: List<CharArray>, start : Pair<Int, Int>): Boolean {
        var gridClone = grid.map { it.copyOf() }
        var pos  = start
        val points = mutableSetOf<Pair<Pair<Int, Int>, Int>>()
        gridClone[pos.first][pos.second] = 'X'
        var currDir = 0
        while (true) {
            val nextY = pos.first + dir[currDir].first
            val nextX = pos.second + dir[currDir].second
            if (nextY >= height || nextX >= width || nextY < 0 || nextX < 0) break
            if (gridClone[nextY][nextX] != '#') {
                pos = nextY to nextX
                gridClone[nextY][nextX] = 'X'
            } else {
                if (points.contains(pos to currDir)) {
                    return true
                } else {
                    points.add(pos to currDir)
                }
                currDir = (currDir + 1) % 4
            }
        }
        return false
    }

    fun part1(start : Pair<Int, Int>): Int {
        var start = start
        grid[start.first][start.second] = 'X'
        var currDir = 0
        var count = 1
        while (true) {
            val nextY = start.first + dir[currDir].first
            val nextX = start.second + dir[currDir].second
            if (nextY >= height || nextX >= width || nextY < 0 || nextX < 0) break
            if (grid[nextY][nextX] != '#') {
                start = nextY to nextX
                if (grid[nextY][nextX] == '.') {
                    count++
                    grid[nextY][nextX] = 'X'
                }
            } else {
                currDir = (currDir + 1) % 4
            }
        }
        return count
    }

    fun part2(start : Pair<Int, Int>): Int {
        var ways = 0
        for (i in grid.indices) {
            for (j in grid.first().indices) {
                if (grid[i][j] == 'X') {
                    gridCopy[i][j] = '#'
                    if (hasRepetition(gridCopy, start)) ways++
                    gridCopy[i][j] = '.'
                }
            }
        }
        return ways
    }

    measureTime {
        part1(pos).println()
    }.println()
    measureTime {
        part2(pos).println()
    }.println()
}
