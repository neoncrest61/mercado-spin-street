package com.Xulzuge0.Zumgun.core.ui

object Format {

    fun grouped(value: Int): String {
        val raw = value.toString()
        val builder = StringBuilder()
        raw.forEachIndexed { index, character ->
            if (index > 0 && (raw.length - index) % 3 == 0) builder.append(',')
            builder.append(character)
        }
        return builder.toString()
    }

    fun tenths(value: Int): String {
        val whole = value / 10
        val fraction = value % 10
        return if (fraction == 0) whole.toString() else "$whole.$fraction"
    }
}
