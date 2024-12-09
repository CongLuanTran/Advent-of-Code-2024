import kotlin.time.measureTime

fun <T> MutableList<T>.swapAt(a: Int, b: Int) {
    this[a] = this[b].also { this[b] = this[a] }
}

class FileSystem(diskMap: List<Int>) {
    var memory = mutableListOf<Int>()

    init {
        var fileID = 0
        var freeID = 0
        var isSpace = false
        for (i in diskMap) {
            val num = if (isSpace) --freeID else fileID++
            repeat(i) { memory.add(num) }
            isSpace = !isSpace
        }
    }

    fun blockCompact(): MutableList<Int> {
        var newMemory = memory.toMutableList()
        var free = 0
        var file = newMemory.size - 1
        while (file > free) {
            while (newMemory[free] >= 0) free++
            while (newMemory[file] < 0) file--
            if (file > free)
                newMemory.swapAt(file, free)
        }
        return newMemory
    }

    fun wholeFileCompact(): MutableList<Int> {
        // Copy the memory state to another variable
        var newMemory = memory.toMutableList()

        var freeBlocks = mutableListOf<MutableList<Int>>()
        var fileBlocks = mutableListOf<MutableList<Int>>()

        var blocks = fileBlocks
        var currID = 0
        blocks.add(mutableListOf())
        for (i in newMemory.indices) {
            if (newMemory[i] != currID) {
                blocks = when (newMemory[i] >= 0) {
                    true -> fileBlocks
                    false -> freeBlocks
                }
                blocks.add(mutableListOf())
                currID = newMemory[i]
            }
            blocks.last().add(i)
        }

        for (file in fileBlocks.reversed()) {
            try {
                val alloc = freeBlocks.first { blocks ->
                    blocks.size >= file.size && blocks.last() < file.first()
                }
                for (block in file) {
                    newMemory.swapAt(block, alloc.first())
                    alloc.removeFirst()
                }
            } catch (_: NoSuchElementException) {
                continue
            }
        }

        return newMemory
    }

}

fun checkSum(memory: MutableList<Int>): Long {
    return memory.foldIndexed(0) { index, acc, i ->
        if (memory[index] >= 0) acc + i * index else acc
    }
}

fun main() {

    fun part1(input: String): Long {
        val fileSystem = FileSystem(input.map { it.digitToInt() })
        return checkSum(fileSystem.blockCompact())
    }

    fun part2(input: String): Long {
        val fileSystem = FileSystem(input.map { it.digitToInt() })
        return checkSum(fileSystem.wholeFileCompact())
    }

    val input = readInput("Day09").first()
    measureTime {
        part1(input).println()
    }.println()
    measureTime {
        part2(input).println()
    }.println()
}