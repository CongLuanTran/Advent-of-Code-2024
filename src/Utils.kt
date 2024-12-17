import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

typealias Map2D = List<MutableList<Char>>
typealias Point = Pair<Int,Int>
typealias Input = List<String>

enum class Direction(val x: Int, val y: Int) {
    NORTH(0, -1),
    SOUTH(0, 1),
    EAST(1, 0),
    WEST(-1, 0);

    fun turnRight(): Direction =
        when(this) {
            NORTH -> EAST
            EAST -> SOUTH
            SOUTH -> WEST
            WEST -> NORTH
        }

    fun turnLeft(): Direction =
        when(this) {
            NORTH -> WEST
            WEST -> SOUTH
            SOUTH -> EAST
            EAST -> NORTH
        }

    fun asLetter(): Char =
        when(this) {
            NORTH -> 'N'
            SOUTH -> 'S'
            EAST -> 'E'
            WEST -> 'W'
        }
}

fun Point.move(direction: Direction) =
    this.first + direction.x to this.second + direction.y

operator fun Map2D.get(point: Point) = this[point.second][point.first]

operator fun Map2D.set(point: Point, char: Char) {
    this[point.second][point.first] = char
}