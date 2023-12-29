package ru.kvf.core.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GridCellsCountChangeUseCase {
    fun get(screen: Screen): Flow<Int>
    suspend fun set(value: Int, screen: Screen)

    sealed class Screen(val key: String) {
        data object PhotosList : Screen("grid_cells_photos_list")
        data object FoldersList : Screen("grid_cells_folders_list")
        data object Favorite : Screen("grid_cells_favorite_list")
    }
}
