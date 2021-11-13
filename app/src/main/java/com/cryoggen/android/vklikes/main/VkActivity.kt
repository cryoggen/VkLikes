package com.cryoggen.android.vklikes.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import com.vk.api.sdk.exceptions.VKAuthException

class VkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //We send a request for permissions for VKontakte and receive a token
        VK.login(this, arrayListOf(VKScope.WALL, VKScope.PHOTOS, VKScope.OFFLINE))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                val intent = Intent()
                intent.putExtra("token", token.accessToken)
                setResult(RESULT_OK, intent)
                finish()
            }

            override fun onLoginFailed(authException: VKAuthException) {

            }

        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)

        }
    }
}