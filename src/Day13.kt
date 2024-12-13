import kotlin.math.min

fun main() {

    fun parseLine(line: String): Pair<Int, Int> {
        val (x, y) = line.split(":")[1].trim().split(",").map { it.split("""[+=]""".toRegex())[1] }
        return x.toInt() to y.toInt()
    }

    fun optimize(machine: List<String>): Int {

        val a = parseLine(machine[0])
        val b = parseLine(machine[1])
        val c = parseLine(machine[2])
        var dp = MutableList(c.first+1) { MutableList(c.second + 1) { (1e9+1).toInt() } }
        dp[0][0] = 0

        for (i in 1..c.first) {
            for (j in 1..c.second) {
                for (k in listOf(a, b)) {
                    if (i - k.first >= 0 && j - k.second >= 0) {
                        dp[i][j] = min(dp[i][j], dp[i - k.first][j - k.second] + (if (k == a) 3 else 1))
                    }
                }
            }
        }

        return if (dp[c.first][c.second] == (1e9+1).toInt() ) 0 else dp[c.first][c.second]
    }

    fun part1(input: List<String>): Int {
        val machines = input.filterNot { it.isEmpty() }.chunked(3)
        return machines.sumOf { optimize(it) }
    }
    val input = readInput("Day13")
    part1(input).println()
}