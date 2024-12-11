import kotlin.time.measureTime

fun main() {
    fun List<String>.parse(): List<MutableList<Pair<Pair<Int, Int>, MutableSet<Pair<Int, Int>>>>> {
        val dp = List(10) { mutableListOf<Pair<Pair<Int, Int>, MutableSet<Pair<Int, Int>>>>() }
        var idy = 0
        for (line in this) {
            var idx = 0
            for (char in line) {
                dp[char.digitToInt()].add((idy to idx++) to mutableSetOf<Pair<Int, Int>>())
            }
            idy++
        }
        return dp
    }

    fun countPath(list: List<MutableList<Pair<Pair<Int, Int>, MutableSet<Pair<Int, Int>>>>>): Int {
        var record = list
        for (entry in record.indices.reversed().drop(1)) {
            for (point in record[entry]) {
                var connected = record[entry + 1].filter { pair ->
                    pair.first in listOf(
                            point.first.first + 1 to point.first.second,
                            point.first.first - 1 to point.first.second,
                            point.first.first to point.first.second + 1,
                            point.first.first to point.first.second - 1,
                    )
                }
                if (entry == 8) {
                    connected.forEach { t ->
                        point.second += t.first
                    }
                } else {
                    connected.forEach { t ->
                        point.second += t.second
                    }
                }

            }
        }
        return record[0].sumOf { it.second.size }
    }

    fun countDistinctPath(list: List<MutableList<Pair<Pair<Int, Int>, MutableSet<Pair<Int, Int>>>>>): Int {
        var record = list.map { points ->
            points.map{ point ->
                point.first to point.second.size
            }.toMutableList()
        }
        for (entry in record.indices.reversed().drop(1)) {
            for (idx in record[entry].indices) {
                var point = record[entry][idx].first
                var connected = record[entry + 1].filter { pair ->
                    pair.first in listOf(
                            point.first + 1 to point.second,
                            point.first - 1 to point.second,
                            point.first to point.second + 1,
                            point.first to point.second - 1,
                    )
                }
                if (entry == 8) {
                    record[entry][idx] = point to connected.size
                } else {
                    connected.forEach { t ->
                        record[entry][idx] = record[entry][idx].first to record[entry][idx].second + t.second
                    }
                }

            }
        }
        return record[0].sumOf { it.second }
    }

    val input = readInput("Day10")
    measureTime {
        println(countPath(input.parse()))
    }.println()
    measureTime {
        println(countDistinctPath(input.parse()))
    }.println()


}