fun main() {
    val mulReg = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")
    val condReg = Regex("(don't|do)\\(\\)")

    fun mul(input: MatchResult): Long {
        val (first, second) = input.destructured
        return first.toLong() * second.toLong()
    }

    fun part1(input: List<String>): Long {
        return input.flatMap { mulReg.findAll(it) }.sumOf { mul(it) }
    }

    fun part2(input: List<String>): Long {
        var enabled = true
        return input.flatMap { "$mulReg|$condReg".toRegex().findAll((it)) }.filter {
            when (it.value) {
                "do()" -> enabled = true
                "don't()" -> enabled = false
            }
            enabled && mulReg.matches(it.value)
        }.sumOf { mul(it) }
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
