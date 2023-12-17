import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("testResources/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun Iterable<String>.transpose(): List<String> = buildList {
 val strings = this@transpose.filterTo(mutableListOf()) { it.isNotEmpty() }
 var i = 0
 while (strings.isNotEmpty()) {
  add(buildString(strings.size) { for (string in strings) append(string[i]) })
  i++
  strings.removeAll { it.length == i }
 }
}

fun readInputAndChunkByBlankLines(name: String) = sequence {
    val lines = readInput(name)
    var start = 0
    for (i in 1..lines.size) {
        if (lines.getOrNull(i).isNullOrEmpty()) {
            yield(lines.slice(start until i))
            start = i.inc()
        }
    }
}
