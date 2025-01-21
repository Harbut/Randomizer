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

    if (avoidDuplicates && range.count() < count) { // count() повертає Int, тому порівнюємо з Int
        throw IllegalArgumentException("Діапазон занадто малий для такої кількості унікальних чисел")
    }

    return if (avoidDuplicates) {
        generateUniqueNumbersReservoir(range, count)
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

private fun generateUniqueNumbers(range: IntRange, count: Int): List<Int> {
    return range.shuffled().take(count) // Оптимальний варіант
}

private fun generateUniqueNumbersUsingSet(range: IntRange, count: Int): List<Int> {
    if (range.count() < count) {
        throw IllegalArgumentException("Діапазон занадто малий")
    }

    val numbers = range.toSet().shuffled()
    return numbers.take(count)
}

private fun generateUniqueNumbersReservoir(range: IntRange, count: Int): List<Int> {
    if (range.count() < count) {
        throw IllegalArgumentException("Діапазон занадто малий")
    }

    val reservoir = mutableListOf<Int>()
    for (i in range) {
        if (reservoir.size < count) {
            reservoir.add(i)
        } else {
            val j = Random.nextInt(0, i + 1)
            if (j < count) {
                reservoir[j] = i
            }
        }
    }
    return reservoir
}