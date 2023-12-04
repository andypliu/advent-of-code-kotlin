/**
--- Day 3: Gear Ratios ---
You and the Elf eventually reach a gondola lift station; he says the gondola lift will take you up to the water source, but this is as far as he can bring you. You go inside.

It doesn't take long to find the gondolas, but there seems to be a problem: they're not moving.

"Aaah!"

You turn around to see a slightly-greasy Elf with a wrench and a look of surprise. "Sorry, I wasn't expecting anyone! The gondola lift isn't working right now; it'll still be a while before I can fix it." You offer to help.

The engineer explains that an engine part seems to be missing from the engine, but nobody can figure out which one. If you can add up all the part numbers in the engine schematic, it should be easy to work out which part is missing.

The engine schematic (your puzzle input) consists of a visual representation of the engine. There are lots of numbers and symbols you don't really understand, but apparently any number adjacent to a symbol, even diagonally, is a "part number" and should be included in your sum. (Periods (.) do not count as a symbol.)

Here is an example engine schematic:

467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..
In this schematic, two numbers are not part numbers because they are not adjacent to a symbol: 114 (top right) and 58 (middle right1). Every other number is adjacent to a symbol and so is a part number; their sum is 4361.

Of course, the actual engine schematic is much larger. What is the sum of all of the part numbers in the engine schematic?
*/

fun main() {
    val input = readInput("Day03")
    Day03().part1(input).println()
    Day03().part2(input).println()
}

class Day03 {
    fun part1(input: List<String>) : Long {
        val matrix = input.toTypedArray()
        val number = StringBuilder()
        var isPartNumber = false
        var sum = 0L

        for (i in matrix.indices) {
            for (j in matrix[0].indices) {
                val c = matrix[i][j]
                if (c.isDigit()) {
                    if (number.isEmpty()) {
                        isPartNumber = checkFront(matrix, i, j)
                    } else {
                        if (!isPartNumber) {
                            isPartNumber = checkMiddle(matrix, i, j)
                        }
                    }
                    number.append(c)
                } else {
                    if (number.isNotEmpty()) {
                        if (!isPartNumber) {
                            if (checkEnd(matrix, i, j)) {
                                sum += number.toString().toLong()
                            }
                        } else {
                            sum += number.toString().toLong()
                        }
                        number.clear()
                        isPartNumber = false
                    }
                }
            }

            if (number.isNotEmpty() && isPartNumber) {
                sum += number.toString().toLong()
                number.clear()
                isPartNumber = false
            }
        }

        return sum
    }
    
    private fun checkFront(matrix: Array<String>, i: Int, j: Int): Boolean {
        if (i - 1 >= 0) {                   // upper row
            var c = matrix[i - 1][j]  // above
            if (c.isSymbol()) {
                return true
            }

            if (j - 1 >= 0) {
                c = matrix[i - 1][j - 1]      // upper left
                if (c.isSymbol()) {
                    return true
                }
            }
        }

        if (i + 1 < matrix.size) {            // below row
            val c = matrix[i + 1][j]   // below
            if (c.isSymbol()) {
                return true
            }

            if (j - 1 >= 0) {                 
                val s = matrix[i + 1][j - 1]   // lower right
                if (s.isSymbol()) {
                    return true
                }
            }
        }

        if (j - 1 >= 0) {                     
            val c = matrix[i][j - 1]    // front
            if (c.isSymbol()) {
                return true
            }
        }

        return false
    }

    private fun checkMiddle(matrix: Array<String>, i: Int, j: Int): Boolean {
        if (i - 1 >= 0) {                     
            val c = matrix[i - 1][j]  // above
            if (c.isSymbol()) {
                return true
            }
        }

        if (i + 1 < matrix.size) { 
            val c = matrix[i + 1][j]  // below
            if (c.isSymbol()) {
                return true
            }
        }
        return false
    }

    private fun checkEnd(matrix: Array<String>, i: Int, j: Int): Boolean {
        return if (checkMiddle(matrix, i, j)) true else matrix[i][j].isSymbol()
    }

    /**
    --- Part Two ---
    The engineer finds the missing part and installs it in the engine! As the engine springs to life, you jump in the closest gondola, finally ready to ascend to the water source.

    You don't seem to be going very fast, though. Maybe something is still wrong? Fortunately, the gondola has a phone labeled "help", so you pick it up and the engineer answers.

    Before you can explain the situation, she suggests that you look out the window. There stands the engineer, holding a phone in one hand and waving with the other. You're going so slowly that you haven't even left the station. You exit the gondola.

    The missing part wasn't the only issue - one of the gears in the engine is wrong. A gear is any * symbol that is adjacent to exactly two part numbers. Its gear ratio is the result of multiplying those two numbers together.

    This time, you need to find the gear ratio of every gear and add them all up so that the engineer can figure out which gear needs to be replaced.

    Consider the same engine schematic again:

    467..114..
    ...*......
    ..35..633.
    ......#...
    617*......
    .....+.58.
    ..592.....
    ......755.
    ...$.*....
    .664.598..
    In this schematic, there are two gears. The first is in the top left; it has part numbers 467 and 35, so its gear ratio is 16345. The second gear is in the lower right; its gear ratio is 451490. (The * adjacent to 617 is not a gear because it is only adjacent to one part number.) Adding up all of the gear ratios produces 467835.

    What is the sum of all of the gear ratios in your engine schematic?
     */
    fun part2(input: List<String>): Long {
        val matrix  = input.map { it.toCharArray() }.toTypedArray()
        var sum = 0L
        val gears = mutableListOf<Int>()
        
        for (i in matrix.indices) {
            for (j in matrix[0].indices) {
                val c = matrix[i][j]
                if (c.isStar()) {
                    checkAbove(matrix, i, j, gears)
                    checkLeft(matrix, i, j, gears)
                    checkBelow(matrix, i, j, gears)
                    checkRight(matrix, i, j, gears)
                    if (gears.size == 2) {
                        sum += gears[0] * gears[1]
                    }
                    gears.clear()
                }
            }
        }
        return sum
    }

    private fun checkAbove(matrix: Array<CharArray>, i: Int, j: Int, gears: MutableList<Int>) {
        if (i - 1 >= 0) {                                  // upper row
            getGear(matrix, i - 1, j, gears)            // upper center

            if (j - 1 >= 0) {
                getGear(matrix, i - 1, j - 1, gears) // upper left
            }
            
            if (j + 1 < matrix[i].size) {
                getGear(matrix, i - 1, j + 1, gears)  // upper right
            }
        }
    }

    private fun getGear(matrix: Array<CharArray>, i: Int, j: Int, gears: MutableList<Int>) {
        if (matrix[i][j].isDigit()) {
            gears.add(getNumber(matrix, i, j))
        }
    }

    private fun getNumber(matrix: Array<CharArray>, i: Int, j: Int): Int {
        var front = j
        while (front - 1 >= 0 && matrix[i][front - 1].isDigit()) {   // search for the front index
            front -= 1
        }
        
        var end = j
        while (end + 1 < matrix[i].size && matrix[i][end + 1].isDigit()) {
            end +=1
        }
        
        val number = StringBuilder()
        for (k in front .. end) {
            number.append(matrix[i][k])
            matrix[i][k] = '.'      // set digit to . so that will not get the number again when check from different directions
        }
        return number.toString().toInt()
    }

    private fun checkLeft(matrix: Array<CharArray>, i: Int, j: Int, gears: MutableList<Int>) {
        if (j - 1 >= 0) {
            getGear(matrix, i, j - 1, gears)  // left
        }
    }
    
    private fun checkBelow(matrix: Array<CharArray>, i: Int, j: Int, gears: MutableList<Int>) {
        if (i + 1 < matrix.size) {                         // lower row
            getGear(matrix, i + 1, j, gears)            // lower center

            if (j - 1 >= 0) {
                getGear(matrix, i + 1, j - 1, gears)  // lower left
            }

            if (j + 1 < matrix[i].size) {
                getGear(matrix, i + 1, j + 1, gears)  // lower right
            }
        }
    }
    
    private fun checkRight(matrix: Array<CharArray>, i: Int, j: Int, gears: MutableList<Int>) {
        if (j + 1 < matrix[i].size) {
            getGear(matrix, i, j + 1, gears)  // right
        }
    }
}

fun Char.isSymbol(): Boolean {
    return !this.isDigit() && this != '.'
}

fun Char.isStar() : Boolean {
    return this == '*'
}