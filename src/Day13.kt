/**
 * --- Day 13: Point of Incidence ---
With your help, the hot springs team locates an appropriate spring which launches you neatly and precisely up to the edge of Lava Island.

There's just one problem: you don't see any lava.

You do see a lot of ash and igneous rock; there are even what look like gray mountains scattered around. After a while, you make your way to a nearby cluster of mountains only to discover that the valley between them is completely full of large mirrors. Most of the mirrors seem to be aligned in a consistent way; perhaps you should head in that direction?

As you move through the valley of mirrors, you find that several of them have fallen from the large metal frames keeping them in place. The mirrors are extremely flat and shiny, and many of the fallen mirrors have lodged into the ash at strange angles. Because the terrain is all one color, it's hard to tell where it's safe to walk or where you're about to run into a mirror.

You note down the patterns of ash (.) and rocks (#) that you see as you walk (your puzzle input); perhaps by carefully analyzing these patterns, you can figure out where the mirrors are!

For example:

#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#
To find the reflection in each pattern, you need to find a perfect reflection across either a horizontal line between two rows or across a vertical line between two columns.

In the first pattern, the reflection is across a vertical line between two columns; arrows on each of the two columns point at the line between the columns:

123456789
    ><
#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.
    ><
123456789
In this pattern, the line of reflection is the vertical line between columns 5 and 6. Because the vertical line is not perfectly in the middle of the pattern, part of the pattern (column 1) has nowhere to reflect onto and can be ignored; every other column has a reflected column within the pattern and must match exactly: column 2 matches column 9, column 3 matches 8, 4 matches 7, and 5 matches 6.

The second pattern reflects across a horizontal line instead:

1 #...##..# 1
2 #....#..# 2
3 ..##..### 3
4v#####.##.v4
5^#####.##.^5
6 ..##..### 6
7 #....#..# 7
This pattern reflects across the horizontal line between rows 4 and 5. Row 1 would reflect with a hypothetical row 8, but since that's not in the pattern, row 1 doesn't need to match anything. The remaining rows match: row 2 matches row 7, row 3 matches row 6, and row 4 matches row 5.

To summarize your pattern notes, add up the number of columns to the left of each vertical line of reflection; to that, also add 100 multiplied by the number of rows above each horizontal line of reflection. In the above example, the first pattern's vertical line has 5 columns to its left and the second pattern's horizontal line has 4 rows above it, a total of 405.

Find the line of reflection in each of the patterns in your notes. What number do you get after summarizing all of your notes?

Your puzzle answer was 34993.

--- Part Two ---
You resume walking through the valley of mirrors and - SMACK! - run directly into one. Hopefully nobody was watching, because that must have been pretty embarrassing.

Upon closer inspection, you discover that every mirror has exactly one smudge: exactly one . or # should be the opposite type.

In each pattern, you'll need to locate and fix the smudge that causes a different reflection line to be valid. (The old reflection line won't necessarily continue being valid after the smudge is fixed.)

Here's the above example again:

#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#
The first pattern's smudge is in the top-left corner. If the top-left # were instead ., it would have a different, horizontal line of reflection:

1 ..##..##. 1
2 ..#.##.#. 2
3v##......#v3
4^##......#^4
5 ..#.##.#. 5
6 ..##..##. 6
7 #.#.##.#. 7
With the smudge in the top-left corner repaired, a new horizontal line of reflection between rows 3 and 4 now exists. Row 7 has no corresponding reflected row and can be ignored, but every other row matches exactly: row 1 matches row 6, row 2 matches row 5, and row 3 matches row 4.

In the second pattern, the smudge can be fixed by changing the fifth symbol on row 2 from . to #:

1v#...##..#v1
2^#...##..#^2
3 ..##..### 3
4 #####.##. 4
5 #####.##. 5
6 ..##..### 6
7 #....#..# 7
Now, the pattern has a different horizontal line of reflection between rows 1 and 2.

Summarize your notes as before, but instead use the new different reflection lines. In this example, the first pattern's new horizontal line has 3 rows above it and the second pattern's new horizontal line has 1 row above it, summarizing to the value 400.

In each pattern, fix the smudge and find the different line of reflection. What number do you get after summarizing the new reflection line in each pattern in your notes?

Your puzzle answer was 29341.
 */
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