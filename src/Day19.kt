fun main() {

    fun Input.parse(): Pair<List<String>, List<String>> {
        val patterns = this[0].split(", ").map { it.trim() }
        val designs = this.subList(2, this.size)

        return patterns to designs
    }

    fun part1(input: Input) : Int {
        val (patterns, designs) = input.parse()
        val regex = Regex("(${patterns.joinToString("|")})*")
        return designs.count {
            regex.matchEntire(it) != null
        }
    }

    val input = readInput("Day19")
    part1(input).println()
}