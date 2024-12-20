fun main() {
    fun searchXMAS(grid: List<String>, row: Int, col: Int): Int {
        val m = grid.size
        val n = grid[0].length
        var count = 0

        if (grid[row][col] != 'X') return 0
        val x = intArrayOf(-1, -1, -1, 0, 0, 1, 1, 1)
        val y = intArrayOf(-1, 0, 1, -1, 1, -1, 0, 1)

        for (dir in y.indices) {
            var currY = row + y[dir]
            var currX = col + x[dir]
            var k = 1
            while (k < 4) {
                if (currX >= n || currX < 0 || currY >= m || currY < 0) break
                if (grid[currY][currX] != "XMAS"[k]) break
                currY += y[dir]
                currX += x[dir]
                k++
            }

            if (k == 4) count++
        }
        return count
    }

    fun searchXofMAS(grid: List<String>, row: Int, col: Int): Boolean {
        val mas = Regex("MAS|SAM")
        if (grid[row][col] != 'A') return false
        val diag1 = "" + grid[row - 1][col - 1] + grid[row][col] + grid[row + 1][col + 1]
        val diag2 = "" + grid[row - 1][col + 1] + grid[row][col] + grid[row + 1][col - 1]
        return mas.matches(diag1) && mas.matches(diag2)
    }

    fun part1(input: List<String>): Int {
        var count = 0
        for (i in input.indices) {
            for (j in input[0].indices) {
                count += searchXMAS(input, i, j)
            }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        var count = 0
        for (i in 1..<input.size - 1) {
            for (j in 1..<input[0].length - 1) {
                if (searchXofMAS(input, i, j)) count++
            }
        }
        return count
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
