import kotlin.time.measureTime

fun parseMemory(input: List<Int>): MutableMap<Int, MutableList<Int>> {
    return input.flatMapIndexed { index, i ->
        buildList {
            repeat(i) { add(if (index % 2 == 0) index / 2 else -1) }
        }
    }.mapIndexed { index, i -> i to index }
        .groupBy({ it.first }) { it.second }
        .mapValues { it.value.toMutableList() }
        .toMutableMap()
}

fun reallocate(input: MutableMap<Int, MutableList<Int>>): MutableMap<Int, MutableList<Int>> {
    var clone = input.toMutableMap()
    for (num in clone.keys.sortedDescending().dropLast(1)) {
        if (clone[num]!!.last() < clone[-1]!!.first()) break
        while (clone[num]!!.last() > clone[-1]!!.first()){
            clone[num]!!.removeLast()
            clone[num]!!.add(clone[-1]!!.first())
            clone[-1]!!.removeFirst()
            clone[-1]!!.sort()
            clone[num]!!.sort()
        }
    }
    clone.remove(-1)
    return clone
}

fun checkSum(input: MutableMap<Int, MutableList<Int>>): Long {
    return input.map { it.key.toLong() * it.value.sum() }.sum()
}

fun consecutiveRange(input: MutableList<Int>): MutableList<MutableList<Int>> {
    var newList = mutableListOf<MutableList<Int>>()
    newList.add(mutableListOf(input.first()))
    for (num in input.drop(1)) {
        if (num - newList.last().last() != 1) {
            newList.add(mutableListOf())
        }
        newList.last().add(num)
    }
    return newList
}

fun blockReallocate(input: MutableMap<Int, MutableList<Int>>): MutableMap<Int, MutableList<Int>> {
    var clone = input.toMutableMap()
    var free = consecutiveRange(clone[-1]!!)
    for (num in clone.keys.sortedDescending().dropLast(1)) {
        var blockSize = clone[num]!!.size
        var freeBlock = free.indexOfFirst { block ->
            block.size >= blockSize
        }
        if (freeBlock >= 0 && clone[num]!!.first() > free[freeBlock].last()){
            clone[num] = free[freeBlock].take(blockSize).toMutableList()
            free[freeBlock] = free[freeBlock].drop(blockSize).toMutableList()
        }
    }
    clone.remove(-1)
    return clone
}

fun part1(input: List<Int>): Long {
    return checkSum(reallocate(parseMemory(input)))
}

fun part2(input: List<Int>): Long {
    return checkSum(blockReallocate(parseMemory(input)))
}

fun main() {
    val input = readInput("Day09").first().map { it.digitToInt() }
    measureTime {
        part1(input).println()
    }.println()
    measureTime {
        part2(input).println()
    }.println()
}