package com.cryoggen.android.vklikes.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.cryoggen.android.vklikes.R

class LoginFragment : Fragment() {

    val viewModel by lazy {
        val application = requireNotNull(this.activity).application
        val viewModelFactory = LoginViewModelFactory(application)
        ViewModelProvider(
            this, viewModelFactory
        )[LoginViewModel::class.java]
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //If the fragment is launched from the OverviewFragment fragment, then we launch the authorization window,
        // if not, then we work with the saved token
        if (LoginFragmentArgs.fromBundle(requireArguments()).newLogin) {
            viewModel.refreshToken("*")
        } else {
            viewModel.getToken()
        }

        viewModel.accessToken.observe(viewLifecycleOwner, {
            //If the first authorization or you need to re-authorize, then we launch the activity with authorization.
            if (it == "*") {
                println("11111 $it")
                val intent = Intent(requireActivity(), VkActivity::class.java)
                startActivityForResult(intent, 1)
            } else {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToOverviewFragment(it)
                )
            }
        })

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        viewModel.refreshToken(data?.getStringExtra("token").toString())

    }

}

