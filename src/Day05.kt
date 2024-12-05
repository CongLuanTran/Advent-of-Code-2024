fun main() {
    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day05")

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
    For each update we will check its safety comparing its supposed index with
    its real index. If the supposed index match the real index then it is safe,
    else it is unsafe. The supposed index of each element in the update is
    the number of element in the update that should appear after it, according
    to the rules.
    We actually need to reverse the update before we compare it with the index
    because array index is increasing while the index of the elements in the
    update is supposed to be decreasing.
    While we construct the supposed index list, we can also pair it with the
    value at that position so we don't have to find them again.
     */
    val (safe, unsafe) = updates.map { update ->
        update.map { number ->
            val r = rules[number]
            if (r == null) 0 else update.count { r.contains(it) }
        }.mapIndexed { index, i -> i to update[index] }
            .reversed()
    }.partition {
        it.withIndex().all { (index, num) -> index == num.first }
    }


    /*
    Simply sum up the middle elements of all safe update
     */
    fun part1(): Long {
        return safe.sumOf { it[it.size / 2].second.toLong() }
    }

    /*
    For the unsafe part, we actually don't need to reorder the update but only
    find the element that is supposed to be in the middle. The number supposed
    to appear in the middle should have supposed index of [size/2]. We turn the
    pairs into map for more convenient search.
     */
    fun part2(): Long {
        return unsafe.map { it.toMap() }.sumOf { it[it.size / 2]!!.toLong() }
    }


    part1().println()
    part2().println()
}
