/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.cryoggen.android.vklikes.adapters

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cryoggen.android.vklikes.R
import com.cryoggen.android.vklikes.network.ItemPhoto
import com.cryoggen.android.vklikes.network.Size
import com.cryoggen.android.vklikes.overview.VkApiStatus

//rawing an image inside each list item
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, sizes: List<Size>?) {
    sizes?.let {
        val imgUri = sizes[sizes.size - 1].url.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    }
}

//Passing the list to the adapter
@BindingAdapter("listData")
fun bindRecyclerView(
    recyclerView: RecyclerView,
    data: List<ItemPhoto>?
) {
    val adapter = recyclerView.adapter as PhotoGridAdapter
    adapter.submitList(data)
}

//I add the number of ratings to each photo in the list
@SuppressLint("SetTextI18n")
@BindingAdapter("likesCount")
fun getLikes(
    textView: TextView,
    itemPhoto: ItemPhoto?
) {
    textView.text = "♥" + itemPhoto?.likes?.count.toString()
}

//I choose which image to choose when there is a download or loss of connection. If everything is ok, then I hide the image.
@BindingAdapter("vkApiStatus")
fun bindStatus(
    statusImageView: ImageView,
    status: VkApiStatus?
) {
    when (status) {
        VkApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        VkApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        VkApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}