package com.example.cloudraya

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.cloudraya.API.ApiService
import com.example.cloudraya.Model.UserRequest
import com.example.cloudraya.Model.UserResponse
import com.example.cloudraya.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAction()
    }
    fun initAction(){
        binding.login.setOnClickListener {
            login()
        }
    }

    fun login(){
        val request = UserRequest()
        request.app_key = binding.appKey.text.toString().trim()
        request.secret_key = binding.secretKey.text.toString().trim()

        ApiService.instanceRetrofit.login(request).enqueue(object : Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val response = response.body()
                    if (response?.code == 404 ){
                        Toast.makeText(this@MainActivity, "gagal", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@MainActivity, response?.message, Toast.LENGTH_SHORT).show()
                    }
                }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("error", t.message!!)
            }

        })
    }

}