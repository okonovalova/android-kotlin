package ru.netology.nmedia.ui.extensions

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
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

fun View.hideKeyboard() {
    val inputMethodManager =
        this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}