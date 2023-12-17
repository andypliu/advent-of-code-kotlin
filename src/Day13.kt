fun main() {
    val input = readInputAndChunkByBlankLines("Day13")

    input.sumOf {
        patternSummary(it) ?: throw IllegalArgumentException()
    }.println()

    input.sumOf {
        smudgeSummary(it)
    }.println()
}

private fun smudgeSummary(pattern: List<String>): Int {
    val current = patternSummary(pattern) ?: throw IllegalArgumentException()
    return pattern.indices.asSequence()
        .flatMap { y -> pattern[y].indices.asSequence().map { x -> y to x } }
        .firstNotNullOf { (y, x) ->
            smudgeSummary(pattern, y, x, current)
        }
}

private fun smudgeSummary(pattern: List<String>, y: Int, x: Int, current: Int) = patternSummary(pattern.flip(y, x), current)

private fun List<String>.flip(y: Int, x: Int) = mapIndexed { i, s ->
    if (i != y) s else s.replaceRange(x..x, "${if (s[x] == '#') '.' else '#'}")
}

private fun patternSummary(pattern: List<String>, current: Int? = null): Int? {
    val yExclude = current?.takeIf { it % 100 == 0 }?.let { it / 100 }
    val xExclude = current?.takeIf { it % 100 != 0 }
    return 1.rangeTo(pattern.lastIndex)
        .firstOrNull { yExclude != it && pattern.isReflectedAlongHorizontal(it) }
        ?.let { it * 100 }
        ?: 1.rangeTo(pattern.first().lastIndex)
            .firstOrNull {xExclude != it && pattern.isReflectedAlongVertical(it) }
}

private fun List<String>.isReflectedAlongHorizontal(y: Int) = first()
    .indices
    .map { x -> this.map { it[x] } }
    .all { it.isReflectedAlong(y) }

private fun List<String>.isReflectedAlongVertical(x: Int) = all {
    it.toList().isReflectedAlong(x)
}

private fun List<Char>.isReflectedAlong(i: Int): Boolean {
    val stack = ArrayDeque(
        slice((i - (size - i)).coerceAtLeast(0) until i)
    )

    @Suppress("NAME_SHADOWING") var i = i
    while (i < size && stack.isNotEmpty()) {
        if (stack.removeLast() != get(i)) return false
        i++
    }
    return true
}


/*
fun main() {
    val input = readInput("Day13Test")
    val day13 = Day13()
    day13.part1(input).println()
    day13.part2(input).println()
}

class Day13 {
    override fun part1(lines: List<String>) {
        println(
            groupLines(lines, "").map {
                MatrixString.build(splitLines(it))
            }.flatMap {
                listOf(
                    (findMirrorInLists(it.getColumns()) + 1) * 100,
                    findMirrorInLists(it.getRows()) + 1
                )
            }.sum()
        )
    }

    override fun part2(lines: List<String>) {
        println(
            groupLines(lines, "").asSequence()
                .map {
                    MatrixString.build(splitLines(it))
                }.map {
                    // Find the 'default' mirror row or column
                    Triple(it, findMirrorInLists(it.getColumns()), findMirrorInLists(it.getRows()))
                }.flatMap { triple ->
                    val (grid, row, column) = triple

                    // Create all grid options where 1 location is changed to its opposite value
                    grid.allPoints().map { point ->
                        val copy = grid.copy()
                        copy.set(point, opposite(grid.get(point)))
                        Triple(copy, row, column)
                    }
                }.flatMap { triple ->
                    val (grid, row, column) = triple
                    // Find the mirror row or column while ignoring the 'default' mirror row or column
                    listOf(
                        (findMirrorInLists(grid.getColumns(), row) + 1) * 100,
                        findMirrorInLists(grid.getRows(), column) + 1
                    )
                }.sum() / 2
        )
    }

    private fun findMirrorInLists(lists: List<List<String>>, exclude: Int = -1): Int {
        val mirrorOptions = lists.map {
            findMirrorsInList(it) - exclude
        }.reduce { a, b ->
            a.intersect(b)
        }
        return if (mirrorOptions.size != 1) -1 else mirrorOptions.first()
    }

    private fun findMirrorsInList(list: List<String>): Set<Int> {
        return (0 until list.size - 1).mapNotNull {
            // Compare equal lists and reduce to the minimum size
            val size = minOf(it + 1, list.size - it - 1)

            // Compare the (reversed) left and right side
            val left = list.subList(0, it + 1).asReversed().subList(0, size)
            val right = list.subList(it + 1, list.size).subList(0, size)
            if (left == right) it else null
        }.toSet()
    }

    private fun opposite(value: String): String {
        return if (value == "#") "." else "#"
    }
}
*/

/*
class Day13(input: String) {
    private val groups: List<List<String>> = buildList {
        input.lineSequence().plus("").fold(mutableListOf<String>()) { group, line ->
            if (line.isNotEmpty()) return@fold group.apply { add(line) } else if (group.isNotEmpty()) add(group)
            mutableListOf()
        }
    }

    fun part1(): Int = groups.sumOf { group ->
        100 * group.findReflection(::zipEquals) + group.transpose().findReflection(::zipEquals)
    }

    fun part2(): Int = groups.sumOf { group ->
        100 * group.findReflection(::zipAlmostEqual) + group.transpose().findReflection(::zipAlmostEqual)
    }

    companion object {
        private fun List<String>.findReflection(eq: List<String>.(List<String>) -> Boolean): Int {
            for (i in 1 until lastIndex) {
                if (subList(0, i).asReversed().eq(subList(i, size))) return i
            }
            return 0
        }

        private fun zipEquals(first: List<String>, second: List<String>): Boolean {
            val n = minOf(first.size, second.size)
            return first.subList(0, n) == second.subList(0, n)
        }

        @Suppress("ReturnCount")
        private fun zipAlmostEqual(first: List<String>, second: List<String>): Boolean {
            var almostEqual = false
            for (i in 0..minOf(first.lastIndex, second.lastIndex)) {
                val a = first[i]
                val b = second[i]
                val delta = (0..maxOf(a.lastIndex, b.lastIndex)).count { a.getOrNull(it) != b.getOrNull(it) }
                if (delta > 1) return false
                if (delta == 1) if (almostEqual) return false else almostEqual = true
            }
            return almostEqual
        }
    }
}
*/

/*
class Day13(input: List<String>) {
    val inputs = input

    fun part1() {
        var start: Int
        var end: Int
        var rowRelection: Int = 0
        var colRelection: Int = 0
        var sum = 0
        var grandSum = 0
        start = 0
        var matrixNum = 1
        matrix@ for (i in 0..<inputs.size) {
            println(inputs[i])
            
            if (matrixNum % 2 == 0) {
                if (i + 1 < inputs.size) {
                    if (inputs[i] == inputs[i + 1]) {
                        println("Found reflection")
                        rowRelection = i - start + 1
                    }
                }
            }
            
            if (inputs[i].isEmpty()) {  // find column reflection
                if (matrixNum % 2 == 1) {
                    end = i
                    outer@ for (j in 0..<inputs[start].length - 1) {
                        var k = start
                        while (k < end) {
                            if (inputs[k][j] != inputs[k][j + 1]) {
                                continue@outer
                            }
                            k++
                        }
                        // found reflection
                        println("Found column reflection")
                        colRelection = j + 1
                        start = i + 1
                        matrixNum++
                        continue@matrix
                    }
                } else {
                    sum = rowRelection * 100 + colRelection
                    grandSum += sum
                }
            }
        }
        
        println("Sum: $sum")
    }

    fun part2() {
        println("2")
    }
}
*/