package com.example.cloudraya

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.cloudraya.API.ApiService
import com.example.cloudraya.Model.UserRequest
import com.example.cloudraya.Model.UserResponse
import com.example.cloudraya.Register.RegisterActivity
import com.example.cloudraya.SiteList.SiteListActivity
import com.example.cloudraya.VmListActivity.VmListActivity
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
        binding.btnNext.setOnClickListener {
            intent = Intent(this, SiteListActivity::class.java)
            startActivity(intent)
        }
    }
}