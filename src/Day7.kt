fun calculate(data: List<String>): Pair<Int, Long> {
    var beams = arrayListOf<Pair<Point, Long>>()

    val split = mutableSetOf<Point>()

    val start = data[0].indexOf('S')
    beams.add(Point(start, 0) to 1L)

    for (i in 1 until data.size) {
        val newBeans = arrayListOf<Pair<Point, Long>>()

        beams.forEach { (point, count)  ->
            if (data[point.y+1][point.x] == '.') {
                newBeans.add(point.down() to count)
            } else {
                split.add(point.down())
                newBeans.add(point.down().left() to count)
                newBeans.add(point.down().right() to count)
            }
        }

        beams = newBeans.fold(arrayListOf()) { acc, pair ->
            val (point, count) = pair

            val index = acc.indexOfFirst { it.first == point }
            if (index != -1 && i != index) {
                acc[index] = point to acc[index].second + count
            } else {
                acc.add(point to count)
            }

            return@fold acc
        }
    }

    return split.count() to beams.sumOf { it.second }
}

fun main() {
    val data = readFile(7)

    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}


//Part 1
private fun part1(data: List<String>): Int {
    return calculate(data).first
}


//Part 2
private fun part2(data: List<String>): Long {
    return calculate(data).second
}
