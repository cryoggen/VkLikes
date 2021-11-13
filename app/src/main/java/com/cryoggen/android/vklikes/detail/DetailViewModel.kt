package com.cryoggen.android.vklikes.detail

import android.app.Application
import androidx.lifecycle.*
import com.cryoggen.android.vklikes.R
import com.cryoggen.android.vklikes.network.ItemPhoto
import com.cryoggen.android.vklikes.network.Size
import java.text.SimpleDateFormat
import java.util.*

//The [ViewModel] that is associated with the [DetailFragment].

class DetailViewModel(
    itemPhoto: ItemPhoto,
    app: Application
) : AndroidViewModel(app) {

    //A list of links to the same photos of different sizes
    private val _selectedPhotoDifferentSizes = MutableLiveData<List<Size>>()
    val selectedPhotoDifferentSizes: LiveData<List<Size>>
        get() = _selectedPhotoDifferentSizes

    //Selected photo
    private val _selectedItemPhoto = MutableLiveData<ItemPhoto>()
    val selectedItemPhoto: LiveData<ItemPhoto>
        get() = _selectedItemPhoto

    init {
        _selectedPhotoDifferentSizes.value = itemPhoto.sizes
        _selectedItemPhoto.value = itemPhoto
    }

    //format the date and display
    val displayDate = Transformations.map(selectedItemPhoto) {
        val sdf = SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.ENGLISH)
        sdf.timeZone = TimeZone.getTimeZone("Europe/Moscow")
        val netDate = Date(selectedItemPhoto.value?.date?.toLong()!! * 1000L)
        val date = sdf.format(netDate)
        app.applicationContext.getString(
            R.string.display_date, date.toString()
        )
    }

    //Displaying the amount of likes
    val displayLikes = Transformations.map(selectedItemPhoto) {
        app.applicationContext.getString(
            R.string.display_likes, selectedItemPhoto.value?.likes?.count.toString()
        )
    }

}

class DetailViewModelFactory(
    private val itemPhoto: ItemPhoto,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(itemPhoto, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}