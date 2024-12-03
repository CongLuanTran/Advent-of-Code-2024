fun main() {
    fun part1(input: List<String>): Long {
        val regex = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")
        return input.sumOf { line ->
            regex.findAll(line).sumOf {
                it.groupValues[1].toLong() * it.groupValues[2].toLong()
            }
        }
    }

    fun part2(input: List<String>): Long {
        val regex = Regex("(mul\\((\\d{1,3}),(\\d{1,3})\\))|((don't|do)\\(\\))")
        var sum = 0L
        var b = true
        input.forEach { line ->
            regex.findAll(line).forEach {
                val arg = it.value
                when (arg) {
                    "do()" -> b = true
                    "don't()" -> b = false
                    else -> if (b) sum += it.groupValues[2].toLong() * it.groupValues[3].toLong()
                }
            }
        }
        return sum
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
