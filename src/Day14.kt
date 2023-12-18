/**
 * --- Day 14: Parabolic Reflector Dish ---
You reach the place where all of the mirrors were pointing: a massive parabolic reflector dish attached to the side of another large mountain.

The dish is made up of many small mirrors, but while the mirrors themselves are roughly in the shape of a parabolic reflector dish, each individual mirror seems to be pointing in slightly the wrong direction. If the dish is meant to focus light, all it's doing right now is sending it in a vague direction.

This system must be what provides the energy for the lava! If you focus the reflector dish, maybe you can go where it's pointing and use the light to fix the lava production.

Upon closer inspection, the individual mirrors each appear to be connected via an elaborate system of ropes and pulleys to a large metal platform below the dish. The platform is covered in large rocks of various shapes. Depending on their position, the weight of the rocks deforms the platform, and the shape of the platform controls which ropes move and ultimately the focus of the dish.

In short: if you move the rocks, you can focus the dish. The platform even has a control panel on the side that lets you tilt it in one of four directions! The rounded rocks (O) will roll when the platform is tilted, while the cube-shaped rocks (#) will stay in place. You note the positions of all of the empty spaces (.) and rocks (your puzzle input). For example:

O....#....
O.OO#....#
.....##...
OO.#O....O
.O.....O#.
O.#..O.#.#
..O..#O..O
.......O..
#....###..
#OO..#....
Start by tilting the lever so all of the rocks will slide north as far as they will go:

OOOO.#.O..
OO..#....#
OO..O##..O
O..#.OO...
........#.
..#....#.#
..O..#.O.O
..O.......
#....###..
#....#....
You notice that the support beams along the north side of the platform are damaged; to ensure the platform doesn't collapse, you should calculate the total load on the north support beams.

The amount of load caused by a single rounded rock (O) is equal to the number of rows from the rock to the south edge of the platform, including the row the rock is on. (Cube-shaped rocks (#) don't contribute to load.) So, the amount of load caused by each rock in each row is as follows:

OOOO.#.O.. 10
OO..#....#  9
OO..O##..O  8
O..#.OO...  7
........#.  6
..#....#.#  5
..O..#.O.O  4
..O.......  3
#....###..  2
#....#....  1
The total load is the sum of the load caused by all of the rounded rocks. In this example, the total load is 136.

Tilt the platform so that the rounded rocks all roll north. Afterward, what is the total load on the north support beams?

Your puzzle answer was 108935.

--- Part Two ---
The parabolic reflector dish deforms, but not in a way that focuses the beam. To do that, you'll need to move the rocks to the edges of the platform. Fortunately, a button on the side of the control panel labeled "spin cycle" attempts to do just that!

Each cycle tilts the platform four times so that the rounded rocks roll north, then west, then south, then east. After each tilt, the rounded rocks roll as far as they can before the platform tilts in the next direction. After one cycle, the platform will have finished rolling the rounded rocks in those four directions in that order.

Here's what happens in the example above after each of the first few cycles:

After 1 cycle:
.....#....
....#...O#
...OO##...
.OO#......
.....OOO#.
.O#...O#.#
....O#....
......OOOO
#...O###..
#..OO#....

After 2 cycles:
.....#....
....#...O#
.....##...
..O#......
.....OOO#.
.O#...O#.#
....O#...O
.......OOO
#..OO###..
#.OOO#...O

After 3 cycles:
.....#....
....#...O#
.....##...
..O#......
.....OOO#.
.O#...O#.#
....O#...O
.......OOO
#...O###.O
#.OOO#...O
This process should work if you leave it running long enough, but you're still worried about the north support beams. To make sure they'll survive for a while, you need to calculate the total load on the north support beams after 1000000000 cycles.

In the above example, after 1000000000 cycles, the total load on the north support beams is 64.

Run the spin cycle for 1000000000 cycles. Afterward, what is the total load on the north support beams?

Your puzzle answer was 100876.
 */

fun main() {
    val input = readInput("Day14")
    val grid = Grid(Array(input.size) { y ->
        Array(input[y].length) { x -> input[y][x].toTileContents() }
    })
    //part1
    grid.copy().apply { tiltNorth() }.totalNorthLoad().println()

    //part2
    val turtle = grid.copy()
    val hare = grid.copy()
    do {
        turtle.executeNextStep()
        repeat(2){hare.executeNextStep()}
    }while (!turtle.myEquals(hare))
    val stepsRemaining = 4_000_000_000 % turtle.stepsExecuted
    repeat(stepsRemaining.toInt()){turtle.executeNextStep()}
    turtle.totalNorthLoad().println()
}

private class Grid(private val grid: Array<Array<TileContents>>) {
    var stepsExecuted = 0
    private val stepIterator =
        sequence { while (true) yieldAll(listOf(::tiltNorth, ::tiltWest, ::tiltSouth, ::tiltEast)) }.iterator()
    fun executeNextStep(){
        stepIterator.next()()
        stepsExecuted++
    }
    fun myEquals(otherGrid: Grid) =
        grid.contentDeepEquals(otherGrid.grid) && stepsExecuted % 4 == otherGrid.stepsExecuted % 4

    fun totalNorthLoad() = grid
        .mapIndexed { y, row -> row.count { it == TileContents.ROUND } * grid.size.minus(y) }
        .sum()

    fun tiltNorth() {
        for (y in 1..grid.lastIndex) {
            for (x in grid[y].indices) {
                if (grid[y][x] == TileContents.ROUND) {
                    val newY = y.dec().downTo(0).firstOrNull { grid[it][x] != TileContents.EMPTY }?.inc() ?: 0
                    roll(y to x, newY to x)
                }
            }
        }
    }

    fun tiltWest() {
        for (x in 1..grid.first().lastIndex) {
            for (y in grid.indices) {
                if (grid[y][x] == TileContents.ROUND) {
                    val newX = x.dec().downTo(0).firstOrNull { grid[y][it] != TileContents.EMPTY }?.inc() ?: 0
                    roll(y to x, y to newX)
                }
            }
        }
    }

    fun tiltSouth() {
        for (y in grid.lastIndex.dec() downTo 0) {
            for (x in grid[y].indices) {
                if (grid[y][x] == TileContents.ROUND) {
                    val newY = y.inc().rangeTo(grid.lastIndex).firstOrNull { grid[it][x] != TileContents.EMPTY }?.dec()
                            ?: grid.lastIndex
                    roll(y to x, newY to x)
                }
            }
        }
    }

    fun tiltEast() {
        for (x in grid.first().lastIndex.dec() downTo 0) {
            for (y in grid.indices) {
                if (grid[y][x] == TileContents.ROUND) {
                    val newX =
                        x.inc().rangeTo(grid.first().lastIndex).firstOrNull { grid[y][it] != TileContents.EMPTY }?.dec()
                                ?: grid.first().lastIndex
                    roll(y to x, y to newX)
                }
            }
        }
    }

    private fun Grid.roll(from: Pair<Int, Int>, to: Pair<Int, Int>) {
        if (from == to) return
        grid[from.first][from.second] = TileContents.EMPTY
        grid[to.first][to.second] = TileContents.ROUND
    }

    fun copy() = Grid(Array(grid.size) { y -> grid[y].copyOf() })
}

private fun Char.toTileContents() = when (this) {
    '#' -> TileContents.SQUARE
    'O' -> TileContents.ROUND
    '.' -> TileContents.EMPTY
    else -> throw IllegalArgumentException()
}


private enum class TileContents {
                                EMPTY, ROUND, SQUARE
}