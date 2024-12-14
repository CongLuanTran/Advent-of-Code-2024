fun main() {

    fun Long.mod(other: Long) = (this % other + other) % other

    operator fun Point.plus(other: Point) =
        this.first + other.first to this.second + other.second

    operator fun Point.times(num: Long) =
        this.first * num to this.second * num

    operator fun Point.rem(other: Point) =
        this.first.mod(other.first) to this.second.mod(other.second)

    fun getTiles(p: Point, v: Point, size: Point, times: Long) =
        (p + v * times) % size


    fun parseLine(line: String): Pair<Point, Point> {
        val regex = List(4) { """(-?\d+)""" }.joinToString(".*?").toRegex()
        val (px, py, vx, vy) = regex.find(line)!!.destructured
        return (px.toLong() to py.toLong()) to (vx.toLong() to vy.toLong())
    }

    fun moves(robots: List<Pair<Point, Point>>, size: Point, times: Long) =
        robots.map { getTiles(it.first,  it.second, size, times) }


    fun safetyScore(robots: List<Pair<Point, Point>>, size: Point, times: Long): Long {
        val positions = moves(robots, size, times)
            .filterNot { it.first == size.first/2 || it.second == size.second/2 }

        val (q12, q34) = positions.partition { it.first in 0 until size.first / 2 }
        val (q1, q2) = q12.partition { it.second in 0 until size.second / 2 }
        val (q3, q4) = q34.partition { it.second in 0 until size.second / 2 }

        return q1.size.toLong() * q2.size.toLong() * q3.size.toLong() * q4.size.toLong()
    }

    fun part1(input: List<String>): Long {
        val size = 101L to 103L
        val robots = input.map { parseLine(it) }
        return safetyScore(robots, size, 100)
    }

    fun part2(input: List<String>) {
        val size = 101L to 103L
        val robots = input.map { parseLine(it) }
        var times = 1L

        val list = buildList {
            repeat((size.first *size.second).toInt()) {
                add(times to safetyScore(robots, size, times++))
            }
        }.toMutableList()

        list.sortBy { it.second }
        for (record in list.take(10)) {
            println("\n${record.first}\n")
            val positions = moves(robots, size, record.first)
            for (i in 0 until size.second) {
                for (j in 0 until size.first) {
                    if (j to i in positions) print('*')
                    else print(' ')
                }
                println()
            }
        }
    }

    val input = readInput("Day14")
    part1(input).println()
    part2(input)
}