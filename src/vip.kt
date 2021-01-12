import java.lang.Exception

const val PART_ONE_CORRECT : String   = "[[1, 9], [1, 9], [4, 6], [4, 6], [5, 5], [5, 5], [5, 5], [5, 5], [5, 5], [5, 5], [6, 4], [6, 4], [9, 1], [9, 1]]"
const val PART_TWO_CORRECT : String   = "[[1, 9], [9, 1], [4, 6], [6, 4], [5, 5]]"
const val PART_THREE_CORRECT : String = "[[1, 9], [4, 6], [5, 5]]"

fun main() {

    val defaultList = listOf(1, 1, 2, 4, 4, 5, 5, 5, 6, 7, 9)

    // Try to parse list from input, otherwise use default
    val inputList = parseInput() ?: defaultList

    val partOneSolution = allPairs(inputList)
    val partTwoSolution = uniquePairs(inputList)
    val partThreeSolution = sameComboPairs(inputList)

    /* This section was made to test the requirements based on the given arrays. The test values were hard coded and
        formatted the way Kotlin Lists print. If a custom list was used, the solutions are printed on their own. */

    if (inputList == defaultList) {

        if (partOneSolution.toString() == PART_ONE_CORRECT) println("Part One Correct.") else println("Part One Failed")
        println("Expected: $PART_ONE_CORRECT \n  Actual: $partOneSolution\n")

        if (partTwoSolution.toString() == PART_TWO_CORRECT) println("Part Two Correct.") else println("Part Two Failed")
        println("Expected: $PART_TWO_CORRECT \n  Actual: $partTwoSolution\n")

        if (partThreeSolution.toString() == PART_THREE_CORRECT) println("Part Three Correct.") else println("Part Three Failed")
        println("Expected: $PART_THREE_CORRECT \n  Actual: $partThreeSolution\n")

    } else {
        println("Part One   : $partOneSolution\n" +
                "Part Two   : $partTwoSolution\n" +
                "Part Three : $partThreeSolution")
    }

}

// Solution 1
fun allPairs(inputList: List<Int>): List<List<Int>> {

    return inputList.mapIndexed { i, value ->
        inputList.mapIndexedNotNull { j, pair ->
            if (j != i && (value + pair) == 10) listOf(value, pair) else null
        }
    }.flatten()
}

// Wrapper function for solution 2
fun uniquePairs(inputList: List<Int>): List<List<Int>> {
    return pairOnlyOnce(inputList, sameCombo = false)
}

// Wrapper function for solution 3
fun sameComboPairs(inputList: List<Int>): List<List<Int>> {
    return pairOnlyOnce(inputList, sameCombo = true)
}

/* Solution 2 & 3 shared code. List is sorted, and then attempts pairs from the high end
*   and low end simultaneously that sum up to 10. This saves time over searching for each
*   pair through brute force.
*/
fun pairOnlyOnce(inputList: List<Int>, sameCombo: Boolean): List<List<Int>> {
    val sortedList = inputList.sorted()
    val solution: MutableSet<List<Int>> = mutableSetOf()

    var lowIndex = 0
    var highIndex = sortedList.size - 1

    while (lowIndex < highIndex) {
        val lowValue = sortedList[lowIndex]
        val highValue = sortedList[highIndex]

        /* When the sum == 10, add it to the list.
         *  Otherwise, decrement highIndex if the sum was too high
         *  or increment the low index if the sum was too low
         */
        when (lowValue + highValue) {
            10 -> {
                // Add paris going both ways if looking for solution #2
                solution.apply {
                    add(listOf(lowValue, highValue))
                    if (!sameCombo) add(listOf(highValue, lowValue))
                }
                lowIndex++
            }
            in 10..Int.MAX_VALUE -> highIndex--
            else -> lowIndex++
        }


    }

    return solution.toList()
}

/* Parses user input and returns lists they can create.
 * If no list is provided, this returns null.
 */
fun parseInput() : List<Int>? {

    println("Enter list to pair, or press enter to use default list\n" +
            "Format is numbers with spaces - 1 2 3")

    return try {
        readLine()?.split(' ')?.map { it.toInt() }
    } catch (_ : Exception) { null }
}