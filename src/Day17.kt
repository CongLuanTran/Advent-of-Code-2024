import kotlin.math.pow

fun main() {

    class Device(info: Input){
        var A = 0
        var B = 0
        var C = 0
        var instructions = listOf<Int>()
        var pointer = 0
        var output = ""

        init {
            A = info[0].split(": ")[1].toInt()
            B = info[1].split(": ")[1].toInt()
            C = info[2].split(": ")[1].toInt()
            instructions = info[4].split(": ")[1].split(",").map { it.toInt() }
        }

        fun combo(operand: Int) =
            when (operand) {
                4 -> A
                5 -> B
                6 -> C
                else -> operand
            }

        fun adv(operand: Int) {
            val value = combo(operand)
            A /= 2.0.pow(value).toInt()
        }

        fun bxl(operand: Int) {
            B = B xor operand
        }

        fun bst(operand: Int) {
            val value = combo(operand)
            B = value % 8
        }

        fun jnz(operand: Int) {
            if (A != 0) {
                pointer = operand - 2
            }
        }

        fun bxc(operand: Int) {
            B = B xor C
        }

        fun out(operand: Int) {
            val value = combo(operand)
            output = "${if (output.isEmpty()) "" else "$output,"}${value % 8}"
        }

        fun bdv(operand: Int) {
            val value = combo(operand)
            B = A/2.0.pow(value).toInt()
        }

        fun cdv(operand: Int) {
            val value = combo(operand)
            C = A/2.0.pow(value).toInt()
        }

        fun run(): String {
            val operations = arrayOf(::adv, ::bxl, ::bst, ::jnz, ::bxc, ::out, ::bdv, ::cdv)
            while (pointer < instructions.size) {
                val execute = operations[instructions[pointer]]
                execute(instructions[pointer+1])
                pointer += 2
            }
            return output
        }
    }

    fun part1(input: Input): String {
        return Device(input).run()
    }

    val input = readInput("Day17")
    part1(input).println()
}