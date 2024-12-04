fun main() {
    val mulReg = Regex("""mul\((\d{1,3}),(\d{1,3})\)""")

    fun mul(input: MatchResult): Long {
        val (first, second) = input.destructured
        return first.toLong() * second.toLong()
    }

    fun part1(input: List<String>): Long {
        return input.flatMap { mulReg.findAll(it) }.sumOf { mul(it) }
    }

    /*
    This is what I would consider the more elegant way to deal with Part 2:
    First we join the lines and then split the joined string by "do()".
    The resulted splits always have a part of them not affected by "don't()"
    Then we strip the part after the first "don't()" in each split, those are
    the disabled parts.
    Then for the rest, we just use regex to filter the correct "mul" and sum up
    their multiplication
     */
    fun part2(input: List<String>): Long {
        return input.joinToString("")
            .split("""do\(\)""".toRegex())
            .map { it.substringBefore("don't()") }
            .flatMap { mulReg.findAll(it) }
            .sumOf { mul(it) }
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
