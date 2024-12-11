import kotlin.time.measureTime

class Arrangement(stones: String) {
    var arrangement = stones.split(" ")
    var dp = mutableMapOf<Pair<String, Int>, Long>()

    fun transform(times: Int): Long {
        fun blink(stone: String, times: Int): Long {
            if (times == 0) return 1L
            dp[stone to times]?.let { return it }

            return when {
                stone == "0" -> blink("1", times - 1)
                stone.length % 2 == 0 -> {
                    var (left, right) = stone.chunked(stone.length / 2)
                        .map { "${it.toLong()}" }
                    blink(left, times - 1) + blink(right, times - 1)
                }

                else -> blink("${stone.toLong() * 2024}", times - 1)
            }.also { dp[stone to times] = it }
        }

        fun blinkUp(stone: String, times: Int, length: Long, end: Int): {
            if (times == end) return length
            dp[stone to times]?.let{ return it }

            return
        }

        return arrangement.sumOf { blink(it, times) }
    }
}

fun main() {
    val input = readInput("Day11").first()
    measureTime() {
        Arrangement(input).transform(25).println()
    }.println()

    measureTime() {
        Arrangement(input).transform(75).println()
    }.println()
}