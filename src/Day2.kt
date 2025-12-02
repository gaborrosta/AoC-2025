fun main() {
    val data = readFile(2)[0].split(",")

    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}


//Part 1
private fun part1(data: List<String>): Long {
    return data.sumOf {
        val (start, stop) = it.split("-").map { num -> num.toLong() }
        var sum = 0L

        for (i in start..stop) {
            val text = i.toString()
            val length = text.length
            if (length % 2 == 1) continue

            val s1 = text.take(length / 2)
            val s2 = text.substring(length / 2)

            if (s1 == s2) sum += i
        }

        sum
    }
}


//Part 2
private fun part2(data: List<String>): Long {
    return data.sumOf {
        val (start, stop) = it.split("-").map { num -> num.toLong() }
        var sum = 0L

        for (i in start..stop) {
            val text = i.toString()
            val length = text.length

            for (l in 1..length/2) {
                if (length % l != 0) continue

                val ss = text.windowed(l,l).toSet()

                if (ss.size == 1) {
                    sum += i
                    break
                }

            }
        }

        sum
    }
}