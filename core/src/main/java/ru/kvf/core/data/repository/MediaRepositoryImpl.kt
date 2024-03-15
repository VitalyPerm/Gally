package ru.kvf.core.data.repository

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MimeType
import ru.kvf.core.domain.repository.MediaRepository

class MediaRepositoryImpl(
    private val context: Context,
) : MediaRepository {

    private val photoProjection = arrayOf(
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DATE_TAKEN,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
    )
    private val videoProjection = arrayOf(
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DATE_TAKEN,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Video.Media.DURATION
    )

    override val mediaFlow: MutableStateFlow<List<Media>> = MutableStateFlow(emptyList())

    override suspend fun loadMedia(): Unit = withContext(Dispatchers.IO) {
        mediaFlow.value = emptyList()
        val imageQuery = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            photoProjection,
            null,
            null,
            null
        )
        val videoQuery = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            videoProjection,
            null,
            null,
            null
        )

        val media = getMedia(imageQuery, true) + getMedia(videoQuery, false)
        mediaFlow.value = media.sortedByDescending { it.timeStamp }
    }

    private fun getMedia(cursor: Cursor?, isPhotos: Boolean) = mutableListOf<Media>().apply {
        cursor?.use {
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val bucketColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val date = cursor.getLong(dateColumn)
                val contentUri = if (isPhotos) {
                    ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                } else {
                    ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                }
                val folder = cursor.getString(bucketColumn)

                val media = Media(
                    id = id,
                    name = name,
                    timeStamp = date,
                    uri = contentUri,
                    folder = folder,
                    mimeType = MimeType.get(isPhotos),
                    duration = getDuration(cursor, isPhotos)
                )
                add(media)
            }
        }
    }

    private fun getDuration(cursor: Cursor, photos: Boolean): String? {
        if (photos) return null
        try {
            val durationColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DURATION)
            val durationMillis = cursor.getLong(durationColumnIndex)
            val durationInSeconds = durationMillis.div(1000)
            val hours = durationInSeconds.div(3600)
            val hoursString = when {
                hours == 0L -> ""
                hours < 10L -> "0$hours:"
                else -> "$hours:"
            }
            val minutes = (durationInSeconds.rem(3600).div(60))
            val minutesString = if (minutes < 10L) "0$minutes:" else "$minutes:"
            val seconds = durationInSeconds.rem(60)
            val secondsString = if (seconds < 10L) "0$seconds" else "$seconds"
            return "$hoursString$minutesString$secondsString"
        } catch (e: Exception) {
            return null
        }
    }
}
