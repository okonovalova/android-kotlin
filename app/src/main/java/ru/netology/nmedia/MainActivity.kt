package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.post_text_textview)
        textView.setStyledSpan(
            getString(R.string.post_text_link_part),
            getColor(R.color.teal_200)
        )
    }

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
}