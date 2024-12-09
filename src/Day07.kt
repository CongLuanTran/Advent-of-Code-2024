import kotlin.time.measureTime

fun main() {
    fun parseInput(input: List<String>): List<Pair<Long, List<Long>>> {
        return input.map { line ->
            var (value, numbers) = line.split(":")
            value.toLong() to numbers.trim().split(" ").map { it.toLong() }
        }
    }

    /*
    We can only perform '+' or '*' on the numbers in left-to-right order.
    So to check if it is possible we can reverse the process and perform '-'
    or '/' with the expected value and the number from left to right. We can
    try to subtract the last number from the value and check if the remaining
    number can add up to the remaining value. When the value is divisible by
    the number, also try to divide it and continue checking.

    IMPORTANT: when the value is divisible by the last number, we need to check
    both '-' and '/' cases. This is because in such case the value is also
    subtractable. Failing to cover this case will lead to wrong answer.
    Don't be greedy, check all the possible cases.
     */
    fun canProduce(value: Long, numbers: List<Long>): Boolean {
        if (numbers.size == 1) return value == numbers[0]
        if (value < numbers.last()) return false

        return value % numbers.last() == 0L
                && canProduce(value / numbers.last(), numbers.dropLast(1))
                || canProduce(value - numbers.last(), numbers.dropLast(1))
    }


    /*
    This is a similar function with the above, just with the addition of the
    '||' operator. To reverse this operator, we check if the last number in the
    list is a proper suffix of the value (when they are both strings). Proper
    suffix means suffixes that doesn't equal the entire string. If it is proper
    suffix, we drop the suffix from the value and check with the rest.

    We have to check this case in addition to the other two cases. So even when
    the last number is a proper suffix, we still have to check the other two
    cases as well. Again failing to do so will result in missing some cases.
     */
    fun canProduceWithConcat(value: Long, numbers: List<Long>): Boolean {
        if (numbers.size == 1) return value == numbers[0]
        if (value < numbers.last()) return false

        fun Long.isProperSuffixOf(other: Long): Boolean {
            // Here we reverse so that is easier to check with indices
            val thisString = "$this"
            val otherString = "$other"

            if (thisString.length >= otherString.length) return false
            for (i in thisString.indices) {
                if (thisString[thisString.length - 1 - i] != otherString[otherString.length - 1 - i]) return false
            }
            return true
        }

        fun Long.drop(other: Long): Long {
            check(other.isProperSuffixOf(this))
            var dropped = other
            var dropper = this
            while (dropped > 0) {
                dropper /= 10
                dropped /= 10
            }
            return dropper
        }

        return numbers.last().isProperSuffixOf(value)
                && canProduceWithConcat(value.drop(numbers.last()), numbers.dropLast(1))
                || value % numbers.last() == 0L
                && canProduceWithConcat(value / numbers.last(), numbers.dropLast(1))
                || canProduceWithConcat(value - numbers.last(), numbers.dropLast(1))
    }

    fun part1(input: List<String>): Long {
        return parseInput(input).filter { pair ->
            canProduce(pair.first, pair.second)
        }.sumOf {
            it.first
        }
    }

    fun part2(input: List<String>): Long {
        return parseInput(input).filter { pair ->
                canProduceWithConcat(pair.first, pair.second)
        }.sumOf {
            it.first
        }
    }

    val input = readInput("Day07")
    measureTime {
        part1(input).println()
    }.println()
    measureTime {
        part2(input).println()
    }.println()
}