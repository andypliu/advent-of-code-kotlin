import java.util.regex.Pattern

/**
 * --- Day 12: Hot Springs ---
You finally reach the hot springs! You can see steam rising from secluded areas attached to the primary, ornate building.

As you turn to enter, the researcher stops you. "Wait - I thought you were looking for the hot springs, weren't you?" You indicate that this definitely looks like hot springs to you.

"Oh, sorry, common mistake! This is actually the onsen! The hot springs are next door."

You look in the direction the researcher is pointing and suddenly notice the massive metal helixes towering overhead. "This way!"

It only takes you a few more steps to reach the main gate of the massive fenced-off area containing the springs. You go through the gate and into a small administrative building.

"Hello! What brings you to the hot springs today? Sorry they're not very hot right now; we're having a lava shortage at the moment." You ask about the missing machine parts for Desert Island.

"Oh, all of Gear Island is currently offline! Nothing is being manufactured at the moment, not until we get more lava to heat our forges. And our springs. The springs aren't very springy unless they're hot!"

"Say, could you go up and see why the lava stopped flowing? The springs are too cold for normal operation, but we should be able to find one springy enough to launch you up there!"

There's just one problem - many of the springs have fallen into disrepair, so they're not actually sure which springs would even be safe to use! Worse yet, their condition records of which springs are damaged (your puzzle input) are also damaged! You'll need to help them repair the damaged records.

In the giant field just outside, the springs are arranged into rows. For each row, the condition records show every spring and whether it is operational (.) or damaged (#). This is the part of the condition records that is itself damaged; for some springs, it is simply unknown (?) whether the spring is operational or damaged.

However, the engineer that produced the condition records also duplicated some of this information in a different format! After the list of springs for a given row, the size of each contiguous group of damaged springs is listed in the order those groups appear in the row. This list always accounts for every damaged spring, and each number is the entire size of its contiguous group (that is, groups are always separated by at least one operational spring: #### would always be 4, never 2,2).

So, condition records with no unknown spring conditions might look like this:

#.#.### 1,1,3
.#...#....###. 1,1,3
.#.###.#.###### 1,3,1,6
####.#...#... 4,1,1
#....######..#####. 1,6,5
.###.##....# 3,2,1
However, the condition records are partially damaged; some of the springs' conditions are actually unknown (?). For example:

???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1
Equipped with this information, it is your job to figure out how many different arrangements of operational and broken springs fit the given criteria in each row.

In the first line (???.### 1,1,3), there is exactly one way separate groups of one, one, and three broken springs (in that order) can appear in that row: the first three unknown springs must be broken, then operational, then broken (#.#), making the whole row #.#.###.

The second line is more interesting: .??..??...?##. 1,1,3 could be a total of four different arrangements. The last ? must always be broken (to satisfy the final contiguous group of three broken springs), and each ?? must hide exactly one of the two broken springs. (Neither ?? could be both broken springs or they would form a single contiguous group of two; if that were true, the numbers afterward would have been 2,3 instead.) Since each ?? can either be #. or .#, there are four possible arrangements of springs.

The last line is actually consistent with ten different arrangements! Because the first number is 3, the first and second ? must both be . (if either were #, the first number would have to be 4 or higher). However, the remaining run of unknown spring conditions have many different ways they could hold groups of two and one broken springs:

?###???????? 3,2,1
.###.##.#...
.###.##..#..
.###.##...#.
.###.##....#
.###..##.#..
.###..##..#.
.###..##...#
.###...##.#.
.###...##..#
.###....##.#
In this example, the number of possible arrangements for each row is:

???.### 1,1,3 - 1 arrangement
.??..??...?##. 1,1,3 - 4 arrangements
?#?#?#?#?#?#?#? 1,3,1,6 - 1 arrangement
????.#...#... 4,1,1 - 1 arrangement
????.######..#####. 1,6,5 - 4 arrangements
?###???????? 3,2,1 - 10 arrangements
Adding all of the possible arrangement counts together produces a total of 21 arrangements.

For each row, count all of the different arrangements of operational and broken springs that meet the given criteria. What is the sum of those counts?

Your puzzle answer was 8193.

--- Part Two ---
As you look out at the field of springs, you feel like there are way more springs than the condition records list. When you examine the records, you discover that they were actually folded up this whole time!

To unfold the records, on each row, replace the list of spring conditions with five copies of itself (separated by ?) and replace the list of contiguous groups of damaged springs with five copies of itself (separated by ,).

So, this row:

.# 1
Would become:

.#?.#?.#?.#?.# 1,1,1,1,1
The first line of the above example would become:

???.###????.###????.###????.###????.### 1,1,3,1,1,3,1,1,3,1,1,3,1,1,3
In the above example, after unfolding, the number of possible arrangements for some rows is now much larger:

???.### 1,1,3 - 1 arrangement
.??..??...?##. 1,1,3 - 16384 arrangements
?#?#?#?#?#?#?#? 1,3,1,6 - 1 arrangement
????.#...#... 4,1,1 - 16 arrangements
????.######..#####. 1,6,5 - 2500 arrangements
?###???????? 3,2,1 - 506250 arrangements
After unfolding, adding all of the possible arrangement counts together produces 525152.

Unfold your condition records; what is the new sum of possible arrangement counts?

Your puzzle answer was 45322533163795.
 */

fun main() {
    val input = readInput("Day12")
    val day12 = Day12()
    day12.part1(input).println()
    day12.part2(input).println()
}

class Day12 {
    
    fun part1(lines: List<String>) : Long {
        var sum = 0L
        for (line in lines) {
            val (pattern, expected) = line.split( " ")
            val expectedSequence = expected.split(",").map { it.toInt() }

            val options = generateOptions(pattern)
            val matches = options.map {
                toSequence(it)
            }.count {
                it == expectedSequence
            }
            sum += matches
        }
        return sum
    }

    private fun generateOptions(pattern: String): List<String> {
        val options = mutableListOf<String>()
        if (pattern.contains("?")) {
            val a = pattern.replaceFirst("?", ".")
            val b = pattern.replaceFirst("?", "#")
            if (a.contains("?")) {
                options.addAll(generateOptions(a))
            } else {
                options.add(a)
            }
            if (b.contains("?")) {
                options.addAll(generateOptions(b))
            } else {
                options.add(b)
            }
        }
        return options
    }

    private fun toSequence(line: String): List<Int> {
        val split = splitLine(line, pattern = Pattern.compile("\\.+")).filter { it.isNotEmpty() }
        return split.map {
            it.length
        }
    }
    
    fun part2(lines: List<String>) : Long {
        var sum = 0L
        for (line in lines) {
            val (pattern, expected) = splitLine(line, " ")
            val newPattern = makeNewPattern(pattern)
            val expectedSequence = splitLine(expected, ",").map { it.toInt() }
            val newExpectedSequence = makeNewExpected(expectedSequence)
            sum += countArrangements(newPattern, newExpectedSequence)
        }
        return sum
    }

    private fun makeNewPattern(pattern: String): String {
        val new = mutableListOf<String>()
        for (i in 0 until 5) {
            new.add(pattern)
        }
        return new.joinToString("?")
    }

    private fun makeNewExpected(expectedSequence: List<Int>): List<Int> {
        val new = mutableListOf<Int>()
        for (i in 0 until 5) {
            new.addAll(expectedSequence)
        }
        return new
    }

    private val cache = mutableMapOf<String, Long>()

    private fun countArrangements(pattern: String, expectedSequence: List<Int>): Long {
        // Check cache for speed
        val key = "$pattern|$expectedSequence"
        if (cache.containsKey(key)) return cache.getValue(key)

        // Break if expected is empty
        if (expectedSequence.isEmpty()) {
            return if (!pattern.contains("#")) 1 else 0
        }

        val size = expectedSequence.first()
        var total = 0L
        for (i in pattern.indices) {
            val range = saveSubstring(pattern, i, i + size)
            if (
                i + size <= pattern.length &&
                range.all { it != '.' } &&
                (i == 0 || pattern[i - 1] != '#') &&
                (i + size == pattern.length || pattern[i + size] != '#')
                ) {
                total += countArrangements(
                    saveSubstring(pattern, i + size + 1),
                    expectedSequence.subList(1, expectedSequence.size)
                )
            }

            if (pattern[i] == '#') break
        }

        // Add to cache
        cache[key] = total
        return total
    }

    private fun saveSubstring(input: String, startIndex: Int): String {
        return if (startIndex >= input.length) "" else input.substring(startIndex)
    }

    private fun saveSubstring(input: String, startIndex: Int, endIndex: Int): String {
        return if (startIndex >= input.length) "" else input.substring(startIndex, minOf(endIndex, input.length))
    }

    private fun splitLine(line: String, delimiter: String): List<String> {
        return line.split(delimiter)
    }

    private fun splitLine(line: String, pattern: Pattern): List<String> {
        return line.split(pattern)
    }
}