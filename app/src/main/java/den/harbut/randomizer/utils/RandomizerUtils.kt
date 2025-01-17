package den.harbut.randomizer.utils

import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

fun generateRandomNumbers(
    minNumber: String,
    maxNumber: String,
    numbersToGenerate: String,
    avoidDuplicates: Boolean
): List<Int> {
    val min = minNumber.toInt()
    val max = maxNumber.toInt()
    val count = numbersToGenerate.toIntOrNull() ?: 1

    if(count <= 0) {
        throw IllegalArgumentException("Кількість чисел має бути більше 0")
    }

    val range = realRange(min, max)

    if(avoidDuplicates && range.count().toLong() < count) {
        throw IllegalArgumentException("Діапазон занадто малий для такої кількості унікальних чисел")
    }

    return if(avoidDuplicates) {
        generateUniqueNumbers(range, count)
    } else {
        generateNumbers(range, count)
    }
}

fun realRange(min: Int, max: Int): IntRange{
    val realMin = min(min, max)
    val realMax = max(min, max)
    return realMin..realMax
}

fun realRangeCount(min: Int, max: Int): Int{
    val realMin = min(min, max)
    val realMax = max(min, max)
    val range = realMin..realMax
    return range.count()
}

private fun generateNumbers(range: IntRange, count: Int): List<Int>{
    return List(count){Random.nextInt(range.first, range.last + 1)}
}

private fun generateUniqueNumbers(range: IntRange, count: Int): List<Int>{
    val numbers = mutableListOf<Int>()
    while (numbers.size < count){
        val number = Random.nextInt(range.first, range.last + 1)
        if (!numbers.contains(number)){
            numbers.add(number)
        }
    }
    return numbers
}