fun main() {
    val data = readFile(4)

    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}


//Part 1
private fun part1(data: List<String>): Int {
    val maxY = data.size - 1
    val maxX = data[0].length - 1

    var count = 0

    data.forEachIndexed { y, line ->
        line.forEachIndexed { x, char ->
            if (char == '@') {
                val position = Point(x, y)

                if (position.neighbors().count { it.inBound(maxX, maxY) && data[it.y][it.x] == '@' } < 4) {
                    count++
                }
            }
        }
    }

    return count
}


//Part 2
private fun part2(data: List<String>): Int {
    val maxY = data.size - 1
    val maxX = data[0].length - 1

    var removed = 0
    var canRemove = true
    val d = ArrayList(data)

    while (canRemove) {
        val toRemove = arrayListOf<Point>()

        d.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char == '@') {
                    val position = Point(x, y)

                    if (position.neighbors().count { it.inBound(maxX, maxY) && d[it.y][it.x] == '@' } < 4) {
                        toRemove.add(position)
                    }
                }
            }
        }

        if (toRemove.isEmpty()) {
            canRemove = false
        } else {
            removed += toRemove.size
            toRemove.forEach { p ->
                d[p.y] = d[p.y].replaceRange(p.x, p.x + 1, ".")
            }
        }
    }

    return removed
}
