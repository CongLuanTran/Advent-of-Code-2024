import kotlin.time.measureTime

fun <T> MutableList<T>.swapAt(a: Int, b: Int) {
    this[a] = this[b].also { this[b] = this[a] }
}

class FileSystem(diskMap: List<Int>) {
    var memory = mutableListOf<Int>()

    init {
        var fileID = 0
        var freeID = -1
        for (pair in diskMap.chunked(2)) {
            repeat(pair.first()) {memory.add(fileID)}
            repeat(pair.last()) {memory.add(freeID)}
            fileID++
            freeID--
        }
    }

    fun blockCompact(): MutableList<Int> {
        var newMemory = memory.toMutableList()
        var free = newMemory.indexOfFirst { id -> id < 0 }
        var file = newMemory.indexOfLast { id -> id >= 0 }
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

        // Create free blocks and file blocks
        var freeBlocks = mutableListOf<MutableList<Int>>()
        var fileBlocks = mutableListOf<MutableList<Int>>()

        // Create another pointer to point to the list we want to add memory block to
        var blocks = fileBlocks
        // Initialize the current file ID we consider to 0
        // Because the first block in memory is guaranteed to be 0
        var currID = 0
        // Add an initial empty list because I don't know how to do it in the loop
        blocks.add(mutableListOf())
        // loop through each index in the memory
        for (i in newMemory.indices) {
            // If the ID at the index is not the same as the current ID we stored
            if (newMemory[i] != currID) {
                // Check if it is a free block or file block and switch the block pointer accordingly
                blocks = when (newMemory[i] >= 0) {
                    true -> fileBlocks
                    false -> freeBlocks
                }
                // Add another list to hold the new blocks
                blocks.add(mutableListOf())
                // switch the current ID value
                currID = newMemory[i]
            }
            // Add the index to the current list we are considering
            blocks.last().add(i)
        }

        // For each file in reverse order
        for (file in fileBlocks.reversed()) {
            // Try to find the suitable chunk to allocate the file
            try {
                val alloc = freeBlocks.first { blocks ->
                    blocks.size >= file.size && blocks.last() < file.first()
                }
                // If it exists then we move the file to the left most free blocks of that chunk
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