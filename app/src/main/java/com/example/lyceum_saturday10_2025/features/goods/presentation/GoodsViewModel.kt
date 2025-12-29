package com.example.lyceum_saturday10_2025.features.goods.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lyceum_saturday10_2025.App
import com.example.lyceum_saturday10_2025.db.Good
import com.example.lyceum_saturday10_2025.features.goods.presentation.contract.GoodsUiState
import com.example.lyceum_saturday10_2025.features.goods.presentation.model.GoodsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GoodsViewModel : ViewModel() {

    val db = App.getDatabase()

    private val _state = MutableStateFlow(GoodsUiState())
    val state: StateFlow<GoodsUiState>
        get() = _state

    init {
        loadGoods()
    }

    private fun loadGoods() {
        val goodsFromDb = db
            ?.goodsDao()
            ?.getAllGoods()
            ?.map { good ->
                GoodsItem(
                    id = good.id,
                    name = good.name,
                    rating = good.rating,
                    description = good.description,
                    imageURL = good.imageURL
                )
            } ?: emptyList()

        viewModelScope.launch {
            _state.value = GoodsUiState(
                mockList + goodsFromDb
            )
        }
    }

    fun addGood(name: String, description: String, imageUrl: String) {
        val newGood = Good(
            name = name,
            description = description,
            rating = 5,
            imageURL = imageUrl
        )
        db?.goodsDao()?.insert(newGood)
        
        // Reload from DB to get the ID and updated list
        loadGoods()
    }

    fun deleteGood(item: GoodsItem) {
        // Only delete from DB if it's not a mock item (assuming mock items have id 0 or we just can't delete them from DB if they are not in DB)
        // However, looking at GoodsItem definition, id defaults to 0. 
        // Mock items in companion object don't have IDs specified, so they are 0.
        // DB items will have non-zero IDs (auto-generated).
        
        if (item.id != 0) {
            val goodToDelete = Good(
                id = item.id,
                name = item.name,
                description = item.description,
                rating = item.rating,
                imageURL = item.imageURL
            )
            db?.goodsDao()?.delete(goodToDelete)
            loadGoods()
        } else {
             // For mock items, we just remove them from the current state list if we want to support deleting them locally
             // But the prompt says "delete from database".
             // If we want to support deleting mock items from the list, we can filter them out.
             val currentList = _state.value.items.toMutableList()
             currentList.remove(item)
             _state.value = _state.value.copy(items = currentList)
        }
    }


    companion object {

        val mockList = listOf(
            GoodsItem(
                name = "Курс по Kotlin",
                rating = 4,
                description = "test description",
                imageURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSibbxABu10t0qxQWHjH-QQFSWaCgd68RbztA&s"
            ),
            GoodsItem(
                name = "Курс по Java",
                rating = 5,
                description = "test description2",
                imageURL = "https://1000logos.net/wp-content/uploads/2020/09/Java-Logo.jpg"
            ),
            GoodsItem(
                name = "Курс по Python",
                rating = 2,
                description = "test description3",
                imageURL = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/Python_logo_and_wordmark.svg/2560px-Python_logo_and_wordmark.svg.png"
            )
        )
    }
}
