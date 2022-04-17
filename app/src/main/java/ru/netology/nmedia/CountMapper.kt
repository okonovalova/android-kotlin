package ru.netology.nmedia

object CountMapper {
    fun mapCountToTitle(count: Int): String {
        return when {
            count >= 1_000_000 && (count.toDouble() % 1_000_000) > 99_999 -> {
                ((count - count.toDouble() % 100_000) / 1_000_000).toString() + "M"
            }
            count >= 1_000_000 -> {
                (count / 1_000_000).toString() + "M"
            }
            count >= 1_000 && (count.toDouble() % 1_000) > 99 -> {
                ((count - count.toDouble() % 100) / 1_000).toString() + "K"
            }
            count >= 1_000 -> {
                (count / 1_000).toString() + "K"
            }
            else -> count.toString()
        }
    }
}