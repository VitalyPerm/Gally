package ru.kvf.core.domain.entities

enum class MimeType(val value: String) {
    Video("video/*"),
    Image("image/*");

    companion object {
        fun get(isPhoto: Boolean) = if (isPhoto) Image else Video
    }
}
