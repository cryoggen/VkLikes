package com.cryoggen.android.vklikes.overview

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.cryoggen.android.vklikes.adapters.PhotoGridAdapter
import com.cryoggen.android.vklikes.databinding.FragmentOverviewBinding

//Fragment to display a list of images
class OverviewFragment : Fragment() {

    var token = ""

    private val viewModel: OverviewViewModel by lazy {
        val viewModelFactory = OverviewViewModelFactory(token)
        ViewModelProvider(
            this, viewModelFactory
        )[OverviewViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        token = OverviewFragmentArgs.fromBundle(requireArguments()).token

        val binding = FragmentOverviewBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.photosGrid.adapter = PhotoGridAdapter(PhotoGridAdapter.OnClickListener {
            viewModel.displayImageDetails(it)
        })

        viewModel.navigateToSelectedPhoto.observe(viewLifecycleOwner, {
            if (null != it) {
                findNavController().navigate(
                    OverviewFragmentDirections.actionShowDetail(
                        it
                    )
                )
                viewModel.displayPropertyDetailsComplete()
            }
        })

        viewModel.chkErrorToken.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(
                    OverviewFragmentDirections.actionOverviewFragmentToLoginFragment(
                        it
                    )
                )
                viewModel.resetChekErrorToken()

            }
        })

        binding.viewModel = viewModel

        return binding.root
    }
}
