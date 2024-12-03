import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val (left, right) = input.map {
            val first = it.substringBefore(" ").toInt()
            val second = it.substringAfterLast(" ").toInt()
            first to second
        }.unzip()

        return left.sorted().zip(right.sorted()).sumOf { (first, second) ->
            abs(first - second)
        }
    }

    fun part2(input: List<String>): Int {
        val (left, right) = input.map {
            val first = it.substringBefore(" ").toInt()
            val second = it.substringAfterLast(" ").toInt()
            first to second
        }.unzip()

        val frequencies = right.groupingBy { it }.eachCount()
        return left.fold(0) { acc, num ->
            acc + num * frequencies.getOrDefault(num, 0)
        }
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
