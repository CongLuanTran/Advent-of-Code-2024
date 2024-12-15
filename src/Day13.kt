import kotlin.time.measureTime

typealias PointL = Pair<Long, Long>

fun main() {

    fun parseLine(line: String): PointL {
        val (x, y) = line.split(":")[1].trim().split(",").map { it.split("""[+=]""".toRegex())[1] }
        return x.toLong() to y.toLong()
    }

    fun getMatrixParam(machine: List<String>): List<PointL> {
        return machine.map { parseLine(it) }
    }

    fun solveLinearEquation(a: PointL, b: PointL, c: PointL): Long {
        val det = (a.first * b.second - a.second * b.first)
        val detA = c.first * b.second - c.second * b.first
        val detB = a.first * c.second - a.second * c.first
        if (detA % det == 0L && detB % det == 0L)
            return 3L*detA/det + detB/det
        return 0L
    }

    fun part1(input: List<String>): Long {
        val machines = input.filterNot { it.isEmpty() }.chunked(3)
        return machines.sumOf {
            var (a, b, c) = getMatrixParam(it)
            solveLinearEquation(a, b, c)
        }
    }

    fun part2(input: List<String>): Long {
        val add = 10000000000000
        val machines = input.filterNot { it.isEmpty() }.chunked(3)
        return machines.sumOf {
            var (a, b, c) = getMatrixParam(it)
            c = c.first + add to c.second + add
            solveLinearEquation(a, b, c)
        }
    }
    val input = readInput("Day13")
    measureTime {
        part1(input).println()
    }.println()
    measureTime {
        part2(input).println()
    }.println()
}