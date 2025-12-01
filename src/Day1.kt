fun main() {
    val data = readFile(1)

    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}


//Part 1
private fun part1(data: List<String>): Int {
    var current = 50
    var count = 0

    data.forEach { line ->
        val direction = line[0]
        val value = line.substring(1).toInt()

        when (direction) {
            'R' -> current += value
            'L' -> current -= value
        }

        if (current % 100 == 0) count++
    }

    return count
}


//Part 2
private fun part2(data: List<String>): Int {
    var current = 50
    var count = 0

    data.forEach { line ->
        val oldCurrent = current
        val direction = line[0]
        val value = line.substring(1).toInt()

        when (direction) {
            'R' -> current += value
            'L' -> current -= value
        }

        val start = minOf(oldCurrent, current)
        val end = maxOf(oldCurrent, current)
        for (i in start..end) {
            if (i % 100 == 0 && i != oldCurrent) {
                count++
            }
        }
    }

    return count
}
