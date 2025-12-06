fun main() {
    val (ranges, ids) = readFile(5).joinToString("\r\n").split("\r\n\r\n").take(2).map { it.split("\r\n") }

    println("Part 1: ${part1(ranges, ids)}")
}


//Part 1
private fun part1(ranges: List<String>, ids: List<String>): Int {
    val rRanges = ranges.map {
        val row = it.split("-")
        val start = row[0].toLong()
        val end = row[1].toLong()

        start..end
    }

    var count = 0
    ids.forEach {
        val id = it.toLong()
        if (rRanges.firstOrNull { range -> id in range } != null) count++
    }

    return count
}
