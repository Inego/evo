package inego.evo.ui

import java.text.DecimalFormat

fun Double.format(fractionalDigits: Int): String {
    val df = DecimalFormat()
    df.maximumFractionDigits = fractionalDigits
    return df.format(this)
}
