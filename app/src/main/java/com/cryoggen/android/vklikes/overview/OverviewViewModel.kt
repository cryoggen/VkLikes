package com.cryoggen.android.vklikes.overview

import androidx.lifecycle.*
import com.cryoggen.android.vklikes.request.RequestVkApi
import com.cryoggen.android.vklikes.network.ItemPhoto
import com.cryoggen.android.vklikes.network.Response
import com.cryoggen.android.vklikes.network.VkApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class OverviewViewModel(token: String) : ViewModel() {

    val requestVkApi by lazy {
        RequestVkApi(
            token = token,
            version = "5.131",
            extended = "1",
            count = "200",
            offset = "0",
            userId = "41238657"
        )
    }

    //Responsible for displaying the status image
    private val _status = MutableLiveData<VkApiStatus>()
    val status: LiveData<VkApiStatus>
        get() = _status

    //Responsible for errors when sending a request
    private val _chekErrorToken = MutableLiveData<Boolean>()
    val chkErrorToken: LiveData<Boolean>
        get() = _chekErrorToken

    private var _listPhoto = MutableLiveData<List<ItemPhoto>>()
    val listPhoto: LiveData<List<ItemPhoto>>
        get() = _listPhoto

    // LiveData to handle navigation to the selected property
    private val _navigateToSelectedPhoto = MutableLiveData<ItemPhoto>()
    val navigateToSelectedPhoto: LiveData<ItemPhoto>
        get() = _navigateToSelectedPhoto

    init {
        getVkPhoto()
    }

    private fun getVkPhoto() {
        viewModelScope.launch {
            _status.value = VkApiStatus.LOADING
            try {
                val sumList: MutableList<ItemPhoto> = mutableListOf()
                val response = initList()

                val count: Int = response.count / 200
                sumList.addAll(response.items)

                //we fill in the list with images, for one request, the API allows you to get only 200 images,
                //so the request must be repeated several times
                for (i in 1..count) {
                    delay(400)

                    //change the image number from which we will receive the next 200 images
                    requestVkApi.offset = (requestVkApi.offset.toInt() + 200).toString()
                    val response = initList()
                    sumList.addAll(response.items)
                }

                //sorting all the images in the list by the number of likes
                sumList.sortByDescending { it.likes.count }
                _status.value = VkApiStatus.DONE
                _listPhoto.value = sumList

            } catch (e: Exception) {
                //if we get an error when sending a request,
                // then we run LoginFragment for a new authorization
                _chekErrorToken.value = true
                _status.value = VkApiStatus.ERROR
                _listPhoto.value = ArrayList()
            }
        }
    }

    fun resetChekErrorToken() {
        _chekErrorToken.value = false
    }

    //Sending a request to VK api
    private suspend fun initList(): Response {
        return VkApi.retrofitService.getProperties(
            requestVkApi.token,
            requestVkApi.version,
            requestVkApi.extended,
            requestVkApi.count,
            requestVkApi.offset
        ).response
    }

    fun displayImageDetails(itemPhoto: ItemPhoto) {
        _navigateToSelectedPhoto.value = itemPhoto
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedPhoto.value = null
    }

}

enum class VkApiStatus { LOADING, ERROR, DONE }

class OverviewViewModelFactory(
    private val token: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OverviewViewModel::class.java)) {
            return OverviewViewModel(token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}