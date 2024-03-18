package ru.kvf.core.domain.entities

enum class MimeType() {
    Video,
    Photo;

    companion object {
        private const val IMAGE = "image"
        private const val VIDEO = "video"

        fun fromString(value: String) = if (value.contains(IMAGE)) Photo else Video
    }
}
