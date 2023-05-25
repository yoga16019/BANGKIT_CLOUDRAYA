package com.example.cloudraya.Register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.cloudraya.API.ApiService
import com.example.cloudraya.Local.SiteRegister
import com.example.cloudraya.Model.UserRequest
import com.example.cloudraya.Model.UserResponse
import com.example.cloudraya.SiteList.SiteListActivity
import com.example.cloudraya.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnRegisterSite.setOnClickListener {
                val siteName = siteName.text.toString()
                val apiUrl = apiUrl.text.toString()
                val appKey = appKey.text.toString()
                val secretKey = secretKey.text.toString()
                val request = UserRequest()
                request.app_key = appKey
                request.secret_key = secretKey
                intent = Intent(this@RegisterActivity, SiteListActivity::class.java)
                startActivity(intent)
                ApiService(apiUrl).instanceRetrofit.login(request).enqueue(object: Callback<UserResponse>{
                    override fun onResponse(
                        call: Call<UserResponse>,
                        response: Response<UserResponse>,
                    ) {
                        val respon = response.body()
                        if (respon?.code == 200 ){
                            Toast.makeText(this@RegisterActivity, respon.message, Toast.LENGTH_SHORT).show()
                            val token = "Bearer ${respon.data?.bearer_token.toString()}"
                            val userSite = SiteRegister(site_name = siteName, api_url = apiUrl, app_key = appKey, secret_key = secretKey, bearer_token = token)
                            viewModel.addSite(userSite)
                        }else{
                            Toast.makeText(this@RegisterActivity, "your credential not valid", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        Log.e("error", t.message!!)
                    }
                })

            }
        }
    }

}