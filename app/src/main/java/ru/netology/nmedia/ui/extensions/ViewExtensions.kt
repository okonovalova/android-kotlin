package ru.netology.nmedia.ui.extensions

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView

fun TextView.setStyledSpan(
    styledTextFragment: String,
    textColor: Int? = null
) {
    text.indexOf(styledTextFragment)
        .takeIf { it >= 0 }
        ?.let { startIndex ->
            val endIndex = startIndex + styledTextFragment.length
            text = SpannableString(text).apply {
                textColor?.let {
                    setSpan(
                        ForegroundColorSpan(it),
                        startIndex,
                        endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
        }
}