import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val a = mutableListOf<Int>()
        val b = mutableListOf<Int>()

        input.map {
            val arr = it.split("\\s+".toRegex())
            a.add(arr[0].toInt())
            b.add(arr[1].toInt())
        }

        a.sort()
        b.sort()
        var total = 0
        for(i in input.indices) {
           total += abs(a[i] - b[i])
        }
        return total
    }

    fun part2(input: List<String>): Int {
        val a = mutableListOf<Int>()
        val b = mutableMapOf<Int, Int>()

        input.map {
            val arr = it.split("\\s+".toRegex())
            a.add(arr[0].toInt())
            b[arr[1].toInt()] = b.getOrDefault(arr[1].toInt(), 0) + 1
        }
        return a.sumOf { it * b.getOrDefault(it, 0) }
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
