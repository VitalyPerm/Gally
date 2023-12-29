package ru.kvf.gally.feature.photos.data

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kvf.gally.feature.photos.domain.Folder
import ru.kvf.gally.feature.photos.domain.Photo
import ru.kvf.gally.feature.photos.domain.PhotosRepository
import java.text.SimpleDateFormat
import java.util.Locale

class PhotosRepositoryImpl(
    private val context: Context
) : PhotosRepository {

    private val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    private val projection = arrayOf(
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DATE_TAKEN,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
    )
    private var photos: List<Photo> = emptyList()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    init {
        scope.launch {
            loadPhotos()
        }
    }

    private suspend fun loadPhotos() = withContext(Dispatchers.IO) {
        val accumulator = mutableListOf<Photo>()
        val query = context.contentResolver.query(
            collection,
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
                accumulator.add(
                    Photo(
                        id = id,
                        name = name,
                        date = date.toString(),
                        uri = uri,
                        folder = folder
                    )
                )
            }
            photos = accumulator
        }
    }

    override suspend fun getFolders(): List<Folder> {
        if (photos.isEmpty()) {
            loadPhotos()
        }
        return photos.groupBy {
            it.folder
        }.map { (folder, photos) ->
            Folder(
                id = photos.firstOrNull()?.id ?: 0,
                name = folder,
                photos = photos
            )
        }
    }

    override suspend fun getPhotos(): List<Photo> {
        if (photos.isEmpty()) {
            loadPhotos()
        }
        return photos
    }
}
