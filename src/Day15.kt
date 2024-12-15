typealias Map2D = List<MutableList<Char>>
typealias Point = Pair<Int, Int>

fun main() {

    val ex = """
        ##########
        #..O..O.O#
        #......O.#
        #.OO..O.O#
        #..O@..O.#
        #O#..O...#
        #O..O..O.#
        #.OO.O.OO#
        #....O...#
        ##########

        <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
        vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
        ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
        <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
        ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
        ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
        >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
        <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
        ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
        v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
    """.trimIndent()

    fun List<String>.parseMap() = this.filter { !it.isEmpty() && it.first() == '#' }.map { it.toMutableList() }

    fun List<String>.parseMove() = this.filter { !it.isEmpty() && it.first() != '#' }.joinToString("")

    fun Map2D.findRobot(): Point {
        return this.flatMapIndexed { y, line ->
            line.mapIndexed { x, ch ->
                if (ch == '@') x to y else null
            }
        }.filterNotNull().first()
    }

    fun Map2D.state() {
        this.forEach {
            println(it.joinToString(""))
        }
        println("")
    }

    fun Map2D.mutate(): Map2D {
        val map = this
        return buildList {
            for (line in map) {
                add(buildList {
                    for (char in line) {
                        when (char) {
                            '#' -> addAll(listOf('#', '#'))
                            'O' -> addAll(listOf('[', ']'))
                            '@' -> addAll(listOf('@', '.'))
                            '.' -> addAll(listOf('.', '.'))
                        }
                    }
                }.toMutableList())
            }
        }
    }

    fun Point.move(direction: Direction) =
        this.first + direction.x to this.second + direction.y

    fun Char.toDirection() =
        when (this) {
            '>' -> Direction.EAST
            '<' -> Direction.WEST
            '^' -> Direction.NORTH
            'v' -> Direction.SOUTH
            else -> error("Invalid move")
        }

    operator fun Map2D.get(point: Point) = this[point.second][point.first]

    operator fun Map2D.set(point: Point, char: Char) {
        this[point.second][point.first] = char
    }

    fun Map2D.canPush(robot: Point, direction: Direction): Boolean {
        if (this[robot] == '.') return true
        if (this[robot] == '#') return false
        val next = robot.move(direction)
        return if (direction == Direction.EAST || direction == Direction.WEST || this[robot] == 'O') {
            canPush(next, direction)
        } else {
            val otherHalf = if (this[robot] == '[') Direction.EAST else Direction.WEST
            canPush(next, direction) && canPush(next.move(otherHalf), direction)
        }
    }

    fun Map2D.push(robot: Point, direction: Direction) {
        if (this[robot] == '.') return
        val next = robot.move(direction)
        if (direction == Direction.EAST || direction == Direction.WEST || this[robot] == 'O') {
            push(next, direction)
            this[next] = this[robot]
            this[robot] = '.'
        } else {
            val otherHalf = if (this[robot] == '[') Direction.EAST else Direction.WEST
            push(next, direction)
            push(next.move(otherHalf), direction)
            this[next] = this[robot]
            this[next.move(otherHalf)] = this[robot.move(otherHalf)]
            this[robot] = '.'
            this[robot.move(otherHalf)] = '.'
        }

    }

    fun Map2D.gps() =
        this.flatMapIndexed { y, line ->
            line.mapIndexed { x, ch ->
                if (ch == 'O' || ch == '[') x to y else null
            }
        }.filterNotNull()
            .sumOf { it.second * 100 + it.first }

    fun part1(input: List<String>): Int {
        val map = input.parseMap()
        val moves = input.parseMove()
        println("Initial State:")
        map.state()
        var robot = map.findRobot()
        moves.forEach {ch ->
                val next = robot.move(ch.toDirection())
                if (map.canPush(next, ch.toDirection())){
                    map.push(next, ch.toDirection())
                    map[next] = map[robot]
                    map[robot] = '.'
                    robot = next
                }
//                println("Move $ch")
//                map.state()
        }
        return map.gps()
    }

    fun part2(input: List<String>): Int {
        val map = input.parseMap().mutate()
        val moves = input.parseMove()
        println("Initial State:")
        map.state()
        var robot = map.findRobot()
        moves.forEach {ch ->
            val next = robot.move(ch.toDirection())
            if (map.canPush(next, ch.toDirection())){
                map.push(next, ch.toDirection())
                map[next] = map[robot]
                map[robot] = '.'
                robot = next
            }
//                println("Move $ch")
//                map.state()
        }
        return map.gps()
    }

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}