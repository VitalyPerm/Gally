package ru.kvf.core.data.repository

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.database.MergeCursor
import android.os.Bundle
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MimeType
import ru.kvf.core.domain.repository.MediaRepository
import ru.kvf.core.utils.L

class MediaRepositoryImpl(
    private val context: Context,
) : MediaRepository {

    private companion object {
        const val TRASHED_VALUE = 1
    }

    private val projection = arrayOf(
        MediaStore.MediaColumns._ID,
        MediaStore.MediaColumns.DISPLAY_NAME,
        MediaStore.MediaColumns.DATE_TAKEN,
        MediaStore.MediaColumns.DURATION,
        MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
        MediaStore.MediaColumns.MIME_TYPE,
        MediaStore.MediaColumns.IS_TRASHED,
        MediaStore.MediaColumns.DATE_EXPIRES
    )

    override val mediaFlow: MutableStateFlow<List<Media>> = MutableStateFlow(emptyList())

    override suspend fun loadMedia(): Unit = withContext(Dispatchers.IO) {
        val bundle = Bundle().apply {
            putInt(MediaStore.QUERY_ARG_MATCH_TRASHED, MediaStore.MATCH_INCLUDE)
        }
        val media = MergeCursor(
            arrayOf(
                context.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    bundle,
                    null
                ),
                context.contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    bundle,
                    null
                )
            )
        ).let(::getMedia)

        mediaFlow.update { media.sortedByDescending { it.timeStamp } }
    }

    private fun getMedia(cursor: Cursor?) = mutableListOf<Media>().apply {
        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            val dateColumn = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_TAKEN)
            val bucketColumn = it.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME)
            val trashColumn = it.getColumnIndexOrThrow(MediaStore.MediaColumns.IS_TRASHED)
            val mimeColumn = it.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)
            val durationColumn = it.getColumnIndex(MediaStore.MediaColumns.DURATION)
            val dateExpiresColumn = it.getColumnIndex(MediaStore.MediaColumns.DATE_EXPIRES)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val name = it.getString(nameColumn)
                val timeStamp = it.getLong(dateColumn)
                val folder = it.getString(bucketColumn)
                val isTrashed = it.getInt(trashColumn) == TRASHED_VALUE
                val mime = MimeType.fromString(it.getString(mimeColumn))
                val duration =
                    if (mime == MimeType.Video) getDurationString(it.getLong(durationColumn)) else null
                val contentUri = if (mime == MimeType.Image) {
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
                val expiresTimeStamp = if (isTrashed) it.getLong(dateExpiresColumn) else null
                val media = Media(
                    id = id,
                    name = name,
                    timeStamp = timeStamp,
                    uri = contentUri,
                    folder = folder,
                    mimeType = mime,
                    isTrashed = isTrashed,
                    duration = duration,
                    expiresTimeStamp = expiresTimeStamp
                )
                L.d("added $id = $isTrashed")
                add(media)
            }
        }
    }
}

private fun getDurationString(duration: Long): String? {
    try {
        val durationInSeconds = duration.div(1000)
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
