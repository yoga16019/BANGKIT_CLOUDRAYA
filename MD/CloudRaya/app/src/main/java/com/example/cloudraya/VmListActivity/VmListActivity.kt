package com.example.cloudraya.VmListActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.cloudraya.API.ApiService
import com.example.cloudraya.DetailVm.DetailActivity
import com.example.cloudraya.Local.SiteRegister
import com.example.cloudraya.Model.ResponseVmList
import com.example.cloudraya.Model.UserRequest
import com.example.cloudraya.Model.UserResponse
import com.example.cloudraya.databinding.ActivityVmListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VmListActivity : AppCompatActivity() {
    lateinit var vmList: MutableList<ResponseVmList.DataVmList>
    lateinit var swipeRefresh: SwipeRefreshLayout
    private val viewModel by viewModels<VmListViewModel>()
    lateinit var binding: ActivityVmListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVmListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var token: String? = null
        val item = intent.getParcelableExtra<SiteRegister>("siteList")
        val adapter = VmListAdapter{
            Intent(this, DetailActivity::class.java).apply {
             putExtra("vmList", it)
             putExtra("siteList", item)
             putExtra("token", token)
             startActivity(this)
         }
        }

        val request = UserRequest()
        request.app_key = item?.app_key
        request.secret_key = item?.secret_key
        swipeRefresh = binding.swipeRefresh
        swipeRefresh.setOnRefreshListener {
            ApiService(item!!.api_url).instanceRetrofit.login(request).enqueue(object: Callback<UserResponse>{
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>,
                ) {
                    val respon = response.body()
                    if (respon?.code == 200 ){
                        Toast.makeText(this@VmListActivity, respon.message, Toast.LENGTH_SHORT).show()
                         token = "Bearer ${respon.data?.bearer_token.toString()}"
                        getVmList(token!!,adapter, item!!.api_url)
                    }else{
                        Toast.makeText(this@VmListActivity, "your credential not valid", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.e("error", t.message!!)
                }
            })
        }

        ApiService(item!!.api_url).instanceRetrofit.login(request).enqueue(object: Callback<UserResponse>{
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>,
            ) {
                val respon = response.body()
                if (respon?.code == 200 ){
                    Toast.makeText(this@VmListActivity, respon.message, Toast.LENGTH_SHORT).show()
                     token = "Bearer ${respon.data?.bearer_token.toString()}"
                    getVmList(token!!,adapter, item!!.api_url)
                }else{
                    Toast.makeText(this@VmListActivity, "your credential not valid", Toast.LENGTH_SHORT).show()
                }

            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("error", t.message!!)
            }
        })
    }
    fun getVmList(token: String, adapter: VmListAdapter, url: String){
        ApiService(url).instanceRetrofit.getVmList(token).enqueue(object: Callback<ResponseVmList>{
            override fun onResponse(
                call: Call<ResponseVmList>,
                response: Response<ResponseVmList>,
            ) {
                if (swipeRefresh.isRefreshing){
                    swipeRefresh.isRefreshing=false
                }
                val respon = response.body()
                if (respon?.code == 200 ){
                    Toast.makeText(this@VmListActivity, "berhasil ${respon.message}", Toast.LENGTH_SHORT).show()
                    vmList = respon.data
                    binding.apply {
                        rvVmList.layoutManager = LinearLayoutManager(this@VmListActivity)
                        rvVmList.adapter = adapter
                        adapter.setVmList(vmList)
                    }
                }else {
                    Toast.makeText(this@VmListActivity, "error ${respon?.code}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseVmList>, t: Throwable) {
                Log.e("error", t.message!!)
            }
        })
    }
}