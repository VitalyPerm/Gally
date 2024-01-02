package ru.kvf.core.data

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import kotlinx.coroutines.flow.MutableStateFlow
import ru.kvf.core.domain.Folder
import ru.kvf.core.domain.Photo
import ru.kvf.core.domain.PhotosRepository
import java.util.Calendar
import java.util.Date

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

    override val foldersFlow: MutableStateFlow<List<Folder>> = MutableStateFlow(emptyList())
    override val photosSortedByDateFlow: MutableStateFlow<Map<CustomDate, List<Photo>>> = MutableStateFlow(emptyMap())
    override val photosFlow: MutableStateFlow<List<Photo>> = MutableStateFlow(emptyList())

    override suspend fun fetch() {
        val photosAccumulator = mutableListOf<Photo>()
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
                val calendar = Calendar.getInstance().apply {
                    time = Date(date)
                }
                photosAccumulator.add(
                    Photo(
                        id = id,
                        name = name,
                        date = CustomDate(calendar),
                        uri = uri,
                        folder = folder
                    )
                )
            }
            photosFlow.value = photosAccumulator.reversed()
            val sortedPhotos = photosAccumulator.reversed()
                .groupBy(Photo::date).toSortedMap()
            photosSortedByDateFlow.value = sortedPhotos
            val folders = photosAccumulator.groupBy {
                it.folder
            }.map { (folder, photos) ->
                Folder(
                    id = photos.firstOrNull()?.id ?: 0,
                    name = folder,
                    photos = photos
                )
            }
            foldersFlow.value = folders

        }
    }
}
