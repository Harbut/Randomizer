package den.harbut.randomizer.utils

import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

fun generateRandomNumbers(
    minNumber: String,
    maxNumber: String,
    numbersToGenerate: String = "1",
    avoidDuplicates: Boolean = false
): List<Int> {
    val min = minNumber.toIntOrNull() ?: 0 // Обробка помилок парсингу
    val max = maxNumber.toIntOrNull() ?: 10 // Значення за замовчуванням
    val count = numbersToGenerate.toIntOrNull() ?: 1

    if (min > max) {
        throw IllegalArgumentException("Мінімальне число має бути менше або дорівнювати максимальному")
    }

    if (count <= 0) {
        throw IllegalArgumentException("Кількість чисел має бути більше 0")
    }

    val range = realRange(min, max)

    if (avoidDuplicates && (range.last - range.first) < count) { // count() повертає Int, тому порівнюємо з Int
        throw IllegalArgumentException("Діапазон занадто малий для такої кількості унікальних чисел")
    }

    return if (avoidDuplicates) {
        generateUniqueNumbersUsingSet(range, count)
    } else {
        generateNumbers(range, count)
    }
}

fun realRange(min: Int, max: Int): IntRange {
    return min(min, max)..max(min, max) // Коротший запис
}

private fun generateNumbers(range: IntRange, count: Int): List<Int> {
    return List(count) { range.random() } // Використовуємо range.random()
}

private fun generateUniqueNumbersUsingSet(range: IntRange, count: Int): List<Int> {
    return List(count) { range.random() }
}