package den.harbut.randomizer.utils

import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

fun generateRandomNumbers(
    minNumber: String,
    maxNumber: String,
    numbersToGenerate: String,
    avoidDuplicates: Boolean
): List<Long> {
    val min = minNumber.toLong()
    val max = maxNumber.toLong()
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

fun realRange(min: Long, max: Long): LongRange{
    val realMin = min(min, max)
    val realMax = max(min, max)
    return realMin..realMax
}

private fun generateNumbers(range: LongRange, count: Int): List<Long>{
    return List(count){Random.nextLong(range.first, range.last + 1)}
}

private fun generateUniqueNumbers(range: LongRange, count: Int): List<Long>{
    val numbers = mutableListOf<Long>()
    while (numbers.size < count){
        val number = Random.nextLong(range.first, range.last + 1)
        if (!numbers.contains(number)){
            numbers.add(number)
        }
    }
    return numbers
}