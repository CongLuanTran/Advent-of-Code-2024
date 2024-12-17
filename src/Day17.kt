import kotlin.math.pow

fun main() {

    class Device(info: Input) {
        var A = 0L
        var B = 0L
        var C = 0L
        var instructions = listOf<Int>()
        var pointer = 0
        var output = mutableListOf<Int>()

        init {
            A = info[0].split(": ")[1].toLong()
            B = info[1].split(": ")[1].toLong()
            C = info[2].split(": ")[1].toLong()
            instructions = info[4].split(": ")[1].split(",").map { it.toInt() }
        }

        fun combo(operand: Int) =
            when (operand) {
                4 -> A
                5 -> B
                6 -> C
                else -> operand.toLong()
            }

        fun adv(operand: Int) {
            val value = combo(operand)
            A /= 2.0.pow(value.toDouble()).toLong()
        }

        fun bxl(operand: Int) {
            B = B xor operand.toLong()
        }

        fun bst(operand: Int) {
            val value = combo(operand)
            B = value % 8
        }

        fun jnz(operand: Int) {
            if (A != 0L) {
                pointer = operand - 2
            }
        }

        fun bxc(operand: Int) {
            B = B xor C
        }

        fun out(operand: Int) {
            val value = combo(operand)
            output.add((value % 8).toInt())
        }

        fun bdv(operand: Int) {
            val value = combo(operand)
            B = A / 2.0.pow(value.toDouble()).toLong()
        }

        fun cdv(operand: Int) {
            val value = combo(operand)
            C = A / 2.0.pow(value.toDouble()).toLong()
        }

        fun run(): List<Int> {
            val operations = arrayOf(::adv, ::bxl, ::bst, ::jnz, ::bxc, ::out, ::bdv, ::cdv)
            while (pointer < instructions.size) {
                val execute = operations[instructions[pointer]]
                execute(instructions[pointer + 1])
                pointer += 2
            }
            return output
        }

        fun trial(num: String, take: Int): Long {
            if (take > instructions.size) return num.toLong(8)
            for (i in 0..7) {
                pointer = 0
                output.clear()
                A = "$num$i".toLong(8)
                run()
                if (output == instructions.takeLast(take)){
                    val res = trial("$num$i", take + 1)
                    if (res != 0L) return res
                }
            }
            return 0L
        }
    }

    fun part1(input: Input): String {
        return Device(input).run().joinToString(",")
    }

    fun part2(input: Input): Long {
        return Device(input).trial("", 1)
    }

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}