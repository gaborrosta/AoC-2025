import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.*

/**
 *   Read input file.
 */
fun readFile(day: Int): List<String> = Path("src/Day${day}.txt").readLines()


/**
 *   Rotates a matrix.
 */
fun <T> Collection<Collection<T>>.rotate(): List<List<T>> {
    return this.flatMap { it.withIndex() }.groupBy({ (i, _) -> i }, { (_, v) -> v }).map { (_, v) -> v.reversed() }
}


/**
 *   Increment value in a MutableMap.
 */
fun <T : Any> MutableMap<T, Long>.increment(key: T, long: Long = 1) {
    this.compute(key) { _: T, v: Long? -> if (v == null) long else v + long }
}


/**
 *   Point in 2D.
 */
data class Point(val x: Int, val y: Int) {

    operator fun plus(other: Point): Point = Point(x + other.x, y + other.y)

    operator fun minus(other: Point): Point = Point(x - other.x, y - other.y)

    fun manhattan(other: Point): Int = abs(x - other.x) + abs(y - other.y)

    fun inBound(minX: Int, maxX: Int, minY: Int, maxY: Int): Boolean = x in minX..maxX && y in minY..maxY
    fun inBound(maxX: Int, maxY: Int): Boolean = inBound(0, maxX, 0, maxY)

    fun rotate(angle: Double): Point {
        val s = sin(angle)
        val c = cos(angle)

        val nx = x * c - y * s
        val ny = x * s + y * c

        return Point(nx.roundToInt(), ny.roundToInt())
    }

    fun rotate(degrees: Int): Point = rotate(degrees * (Math.PI / 180.0))

    fun neighborsHv(): List<Point> = NEIGHBORS_HV.map { Point(it.x + this.x, it.y + this.y) }

    fun down(amount: Int = 1): Point = copy(y = y + amount)
    fun up(amount: Int = 1): Point = copy(y = y - amount)
    fun left(amount: Int = 1): Point = copy(x = x - amount)
    fun right(amount: Int = 1): Point = copy(x = x + amount)

    fun upLeft(amount: Int = 1): Point = copy(x = x - amount, y = y - amount)
    fun upRight(amount: Int = 1): Point = copy(x = x + amount, y = y - amount)
    fun downLeft(amount: Int = 1): Point = copy(x = x - amount, y = y + amount)
    fun downRight(amount: Int = 1): Point = copy(x = x + amount, y = y + amount)

    companion object {

        val NEIGHBORS_H: List<Point> = listOf(Point(-1, 0), Point(1, 0))
        val NEIGHBORS_V: List<Point> = listOf(Point(0, -1), Point(0, 1))
        val NEIGHBORS_HV: List<Point> = NEIGHBORS_H + NEIGHBORS_V

    }

}


/**
 *   Permutations.
 *   https://stackoverflow.com/questions/63433335/java-alternative-of-product-function-of-python-form-itertools
 */
fun <T> Collection<T>.product(r: Int): List<Collection<T>> {
    var result = Collections.nCopies<Collection<T>>(1, emptyList())
    for (pool in Collections.nCopies(r, LinkedHashSet(this))) {
        val temp: MutableList<Collection<T>> = ArrayList()
        for (x in result) {
            for (y in pool) {
                val z: MutableCollection<T> = ArrayList(x)
                z.add(y)
                temp.add(z)
            }
        }
        result = temp
    }
    return result
}

/**
 *   Combinations.
 */
fun <T> combinations(seed: Iterable<T>, count: Int): List<List<T>> {

    fun inner(acc: List<List<T>>, remaining: Int): List<List<T>> = when (remaining) {
        0 -> acc
        count -> inner(seed.map { s -> listOf(s) }, remaining - 1)
        else -> inner(seed.flatMap { s -> acc.map { list -> list + s } }, remaining - 1)
    }

    return inner(emptyList(), count)
}


/**
 *   Basic Graph.
 */
class Graph<T> {
    val adjacencyMap: HashMap<T, HashSet<T>> = HashMap()

    fun addEdge(sourceVertex: T, destinationVertex: T) {
        adjacencyMap.computeIfAbsent(sourceVertex) { HashSet() }.add(destinationVertex)
    }

    fun findCycles(maxLength: Int? = null): List<List<T>> {
        fun util(node: T, visited: HashMap<T, Boolean>, parent: T?, path: ArrayList<T>, cycles: HashSet<List<T>>, maxLength: Int?) {
            visited[node] = true
            path.add(node)

            if (maxLength == null || path.size < maxLength + 1) {
                (adjacencyMap[node] ?: hashSetOf()).forEach { neighbour ->
                    if (visited[neighbour] != true) {
                        util(neighbour, visited, parent, path, cycles, maxLength)
                    } else if (neighbour != parent) {
                        val cycleStartIndex = path.indexOf(neighbour)
                        cycles.add(path.drop(cycleStartIndex))
                    }
                }
            }

            path.removeLast()
            visited[node] = false
        }

        val visited = this.adjacencyMap.keys.associateWithTo(hashMapOf()) { false }
        val cycles = hashSetOf<List<T>>()

        this.adjacencyMap.keys.forEach { key ->
            if (visited[key] != true) {
                util(key, visited, null, arrayListOf(), cycles, maxLength)
            }
        }

        return cycles.toList()
    }

    /**
     *   Bron–Kerbosch algorithm
     */
    fun getAllCliques(
        currentClique: Set<T>,
        remainingNodes: MutableSet<T>,
        visitedNodes: MutableSet<T>
    ): List<Set<T>> {
        if (remainingNodes.isEmpty() && visitedNodes.isEmpty()) return listOf(currentClique)
        val results = mutableListOf<Set<T>>()

        remainingNodes.toList().forEach { v ->
            val neighbours = adjacencyMap[v]?.toSet() ?: emptySet()
            results.addAll(
                getAllCliques(
                    currentClique + v,
                    remainingNodes.intersect(neighbours).toMutableSet(),
                    visitedNodes.intersect(neighbours).toMutableSet()
                )
            )
            remainingNodes.remove(v)
            visitedNodes.add(v)
        }
        return results
    }

}


/**
 *   Convert binary number to decimal.
 */
fun binToDec(bin: String): Long {
    var num = bin
    var decimalNumber = 0L
    var i = 0
    var remainder: Long

    while (num.isNotEmpty()) {
        remainder = num.last().code.toLong() - 48
        num = num.dropLast(1)
        decimalNumber += (remainder * 2.0.pow(i.toDouble())).toLong()
        i++
    }

    return decimalNumber
}

/**
 *   Rotate a string matrix.
 */
fun Collection<String>.rotateStrings(): List<String> {
    return this.flatMap { it.withIndex() }.groupBy({ (i, _) -> i }, { (_, v) -> v }).map { (_, v) -> v.reversed().joinToString("") }
}
