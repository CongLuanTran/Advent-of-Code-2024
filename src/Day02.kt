fun main() {
    fun isLineSafe(input: List<Int>): Boolean {
        val diff = input.zipWithNext { a, b -> a - b }
        return diff.all { it in -3..3 } &&
                (diff.all { it > 0 } || diff.all { it < 0 })
    }

    fun part1(input: List<String>): Int {
        return input.map { line ->
            line.split("\\s+".toRegex()).map { it.toInt() }
        }.count(::isLineSafe)
    }

    fun part2(input: List<String>): Int {
        return input.map { line ->
            line.split("\\s+".toRegex()).map { it.toInt() }
        }.count { list ->
            /*
            Here we check if the safe condition can be met by removing any one
            element from the list. For the already safe cases, this check pass
            at the first index (because if the entire list meet the condition,
            then the list minus the first index also meet the condition).
             */
            list.indices.any {
                val dampened = list.toMutableList().apply { removeAt(it) }
                isLineSafe(dampened)
            }
        }
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
