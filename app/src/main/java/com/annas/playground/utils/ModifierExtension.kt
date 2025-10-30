package com.annas.playground.utils

import androidx.compose.ui.Modifier

/**
 * Give configuration on [Modifier] when the [condition] is true or false.
 * @param condition is the condition to validate the given configuration.
 * @param onTrue is the modifier scope function to when the [condition] is true.
 * @param onElse is the modifier scope function to when the [condition] is false.
 * */
fun Modifier.onCondition(
    condition: Boolean,
    onElse: (Modifier.() -> Modifier)? = null,
    onTrue: Modifier.() -> Modifier,
): Modifier {
    return if (condition) onTrue(this)
    else if (onElse != null) onElse()
    else this
}
