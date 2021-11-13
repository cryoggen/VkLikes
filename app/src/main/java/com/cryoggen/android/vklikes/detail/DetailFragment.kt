package com.cryoggen.android.vklikes.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cryoggen.android.vklikes.databinding.FragmentDetailBinding

//Displays the selected photo from the list on the screen
class DetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val application = requireNotNull(activity).application
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val VkProperty = DetailFragmentArgs.fromBundle(
            requireArguments()
        ).selectedItemPhoto

        val viewModelFactory = DetailViewModelFactory(VkProperty, application)
        binding.viewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(DetailViewModel::class.java)

        return binding.root
    }
}