fun main() {
    val data = readFile(3)

    println("Part 1: ${part1(data)}")
}


//Part 1
private fun part1(data: List<String>): Int {
    return data.sumOf { line ->
        var max = 0
        for (i in 0..<line.length) {
            for (j in (i+1)..<line.length) {
                val new = (line[i] + "" + line[j]).toInt()

                if (max < new) max = new
            }
        }

        max
    }
}
