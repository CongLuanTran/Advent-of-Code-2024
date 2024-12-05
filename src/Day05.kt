fun main() {
    fun part1(input: List<String>): Long {
        // Regex to get the rules and the updates
        val ruleReg = Regex("""\d+[|]\d+""")
        val updatereg = Regex("""\d+(,\d+)*""")

        /*
        Get the rules and turn them into a map. The key are the numbers that
        must appear in front of some other numbers and the values of each key
        are the numbers that must appear after it.
         */
        val rules = input.filter { ruleReg.matches(it) }
            .map { it.split("|") }
            .groupBy(keySelector = { it.first() }, valueTransform = { it.last() })
        /*
        For the update, simply turn them into list
         */
        val updates = input.filter { updatereg.matches(it) }
            .map { it.split(",") }

        /*
        For each update we will check its safety by checking the sublist of
        preceding numbers for each element in the update. For each element
        in the update, if some numbers that should appear after it appear in its
        preceding sublist, the whole update is not safe. For the cases that the
        element doesn't appear in the rules, they can appear after any number,
        so we just pass them.
         */
        return updates.filter {
            it.withIndex().all { (i, s) ->
                rules[s] == null || it.subList(0, i).all { num -> !rules[s]!!.contains(num) }
            }
        }.sumOf {
            it[it.size/2].toLong()
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
