package den.harbut.randomizer

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("randomizer_settings", Context.MODE_PRIVATE)

    companion object {
        // NumberGenerator
        const val MIN_NUMBER = "min_number"
        const val MAX_NUMBER = "max_number"
        const val NUMBERS_TO_GENERATE = "numbers_to_generate"
        const val ANIMATION_DURATION = "animation_duration"
        const val AVOID_DUPLICATES = "avoid_duplicates"
        const val SHOW_SUM = "show_sum"

        // DiceRoller
        const val DICE_COUNT = "dice_count"
        const val DICE_ANIMATION_DURATION = "dice_animation_duration"
        const val DICE_SHOW_SUM = "dice_show_sum"

        // CoinFlipper
        const val COIN_ANIMATION_DURATION = "coin_animation_duration"
        const val SHOW_DESCRIPTOR = "show_descriptor"
    }

    // NumberGenerator
    fun getMinNumber(): String = sharedPreferences.getString(MIN_NUMBER, "0") ?: "0"
    fun setMinNumber(minNumber: String) = sharedPreferences.edit().putString(MIN_NUMBER, minNumber).apply()

    fun getNumberToGenerate(): String = sharedPreferences.getString(NUMBERS_TO_GENERATE, "1") ?: "1"
    fun setNumberToGenerate(numberToGenerate: String) = sharedPreferences.edit().putString(NUMBERS_TO_GENERATE, numberToGenerate).apply()

    fun getMaxNumber(): String = sharedPreferences.getString(MAX_NUMBER, "10") ?: "10"
    fun setMaxNumber(maxNumber: String) = sharedPreferences.edit().putString(MAX_NUMBER, maxNumber).apply()

    fun getAnimationDuration(): String = sharedPreferences.getString(ANIMATION_DURATION, "1000") ?: "1000"
    fun setAnimationDuration(animationDuration: String) = sharedPreferences.edit().putString(ANIMATION_DURATION, animationDuration).apply()

    fun getAvoidDuplicates(): Boolean = sharedPreferences.getBoolean(AVOID_DUPLICATES, false)
    fun setAvoidDuplicates(avoidDuplicates: Boolean) = sharedPreferences.edit().putBoolean(AVOID_DUPLICATES, avoidDuplicates).apply()

    fun getShowSum(): Boolean = sharedPreferences.getBoolean(SHOW_SUM, false)
    fun setShowSum(showSum: Boolean) = sharedPreferences.edit().putBoolean(SHOW_SUM, showSum).apply()

    // DiceRoller
    fun getDiceCount(): String = sharedPreferences.getString(DICE_COUNT, "1") ?: "1"
    fun setDiceCount(diceCount: String) = sharedPreferences.edit().putString(DICE_COUNT, diceCount).apply()

    fun getDiceAnimationDuration(): String = sharedPreferences.getString(DICE_ANIMATION_DURATION, "1000") ?: "1000"
    fun setDiceAnimationDuration(diceCount: String) = sharedPreferences.edit().putString(DICE_ANIMATION_DURATION, diceCount).apply()

    fun getDiceShowSum(): Boolean = sharedPreferences.getBoolean(DICE_SHOW_SUM, false)
    fun setDiceShowSum(showSum: Boolean) = sharedPreferences.edit().putBoolean(DICE_SHOW_SUM, showSum).apply()

    // CoinFlipper
    fun getCoinAnimationDuration(): String = sharedPreferences.getString(COIN_ANIMATION_DURATION, "1000") ?: "1000"
    fun setCoinAnimationDuration(animationDuration: String) = sharedPreferences.edit().putString(COIN_ANIMATION_DURATION, animationDuration).apply()

    fun getCoinShowDescriptor(): Boolean = sharedPreferences.getBoolean(SHOW_DESCRIPTOR, false)
    fun setCoinShowDescriptor(showDescriptor: Boolean) = sharedPreferences.edit().putBoolean(SHOW_DESCRIPTOR, showDescriptor).apply()
}