package com.cryoggen.android.vklikes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cryoggen.android.vklikes.databinding.GridViewItemBinding
import com.cryoggen.android.vklikes.network.ItemPhoto

class PhotoGridAdapter(private val onClickListener: OnClickListener) : ListAdapter<ItemPhoto,
        PhotoGridAdapter.VkPhotoViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VkPhotoViewHolder {
        return VkPhotoViewHolder(
            GridViewItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: VkPhotoViewHolder, position: Int) {
        val itemPhoto = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(itemPhoto)
        }
        holder.bind(itemPhoto)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ItemPhoto>() {
        override fun areItemsTheSame(oldItem: ItemPhoto, newItem: ItemPhoto): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ItemPhoto, newItem: ItemPhoto): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class VkPhotoViewHolder(
        private var binding:
        GridViewItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemPhoto: ItemPhoto) {
            binding.property = itemPhoto
            binding.executePendingBindings()
        }
    }

    class OnClickListener(val clickListener: (item: ItemPhoto) -> Unit) {
        fun onClick(item: ItemPhoto) = clickListener(item)
    }
}

