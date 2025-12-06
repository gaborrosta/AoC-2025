fun main() {
    val data = readFile(6)

    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}


//Part 1
private fun part1(data: List<String>): Long {
    return data.map { it.trim().replace("      ", " ").replace("     ", " ").replace("    ", " ").replace("   ", " ").replace("  ", " ").split(" ") }
        .rotate().sumOf {
        val operation = it[0]
        val numbers = it.drop(1).map { n -> n.toLong() }

        return@sumOf when (operation) {
            "+" -> numbers.fold(0L) { a, b -> a + b }
            "*" -> numbers.fold(1L) { a, b -> a * b }
            else -> 0L
        }
    }
}


//Part 2
private fun part2(data: List<String>): Long {
    val operations =
        data[4].trim().replace("      ", " ").replace("     ", " ").replace("    ", " ").replace("   ", " ").replace("  ", " ").split(" ")
    val numbers = data.dropLast(1).rotateStrings().map { it.trim().reversed() }.groupConsecutiveBy { _, s -> s == "" }

    return operations.withIndex().sumOf { operation ->
        val numbers = numbers[operation.index].mapNotNull { n -> n.toLongOrNull() }

        return@sumOf when (operation.value) {
            "+" -> numbers.fold(0L) { a, b -> a + b }
            "*" -> numbers.fold(1L) { a, b -> a * b }
            else -> 0L
        }
    }
}
