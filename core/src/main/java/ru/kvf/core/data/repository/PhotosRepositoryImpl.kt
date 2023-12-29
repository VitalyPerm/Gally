package ru.kvf.core.data.repository

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.repository.PhotosRepository

class PhotosRepositoryImpl(
    private val context: Context,
) : PhotosRepository {

    private val projection = arrayOf(
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DATE_TAKEN,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
    )

    override val photosFlow: MutableStateFlow<List<Photo>> = MutableStateFlow(emptyList())

    override suspend fun loadPhotos(): Unit = withContext(Dispatchers.IO) {
        photosFlow.value = emptyList()
        val photosAccumulator = mutableListOf<Photo>()
        val query = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        query?.use { cursor ->
            val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
            val dateColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)
            val bucketColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val date = cursor.getLong(dateColumn)
                val uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                val folder = cursor.getString(bucketColumn)
                photosAccumulator.add(
                    Photo(
                        id = id,
                        name = name,
                        timeStamp = date,
                        uri = uri,
                        folder = folder
                    )
                )
            }
            photosFlow.value = photosAccumulator.sortedByDescending { it.timeStamp }
        }
    }
}
