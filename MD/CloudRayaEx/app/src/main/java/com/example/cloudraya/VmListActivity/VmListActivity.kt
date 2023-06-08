package com.example.cloudraya.VmListActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.cloudraya.API.ApiService
import com.example.cloudraya.DetailVm.DetailActivity
import com.example.cloudraya.Local.SiteRegister
import com.example.cloudraya.Model.ResponseVmList
import com.example.cloudraya.Model.UserRequest
import com.example.cloudraya.Model.UserResponse
import com.example.cloudraya.ModelApiTest.DataVm
import com.example.cloudraya.Notifications.NotificationsActivity
import com.example.cloudraya.R
import com.example.cloudraya.Service.FirebaseMessagingService
import com.example.cloudraya.Util.NetworkResult
import com.example.cloudraya.databinding.ActivityVmListBinding
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VmListActivity : AppCompatActivity() {
    lateinit var vmList: List<DataVm>
    lateinit var firebaseMessagingService: FirebaseMessagingService
    lateinit var swipeRefresh: SwipeRefreshLayout
    private val viewModel by viewModels<VmListViewModel>()
    lateinit var binding: ActivityVmListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVmListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseMessagingService = FirebaseMessagingService()

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
        binding.buttonBackToSiteList.setOnClickListener {
            onBackPressed()
        }
//        val request = UserRequest()
//        request.app_key = item?.app_key
//        request.secret_key = item?.secret_key
        swipeRefresh = binding.swipeRefresh
        swipeRefresh.setOnRefreshListener {
            viewModel.getVm(item!!.api_url)
            viewModel.listVm.observe(this){
                when(it){
                    is NetworkResult.Success<*> ->{
                        binding.rvVmList.layoutManager = LinearLayoutManager(this@VmListActivity)
                        binding.rvVmList.adapter = adapter
                        adapter.setVmList(it.data as MutableList<DataVm>)
                    }
                    is NetworkResult.Error ->{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResult.Loading ->{

                    }
                }
            }

        }

        viewModel.getVm(item!!.api_url)
        viewModel.listVm.observe(this){
            when(it){
                is NetworkResult.Success<*> ->{
                    binding.rvVmList.layoutManager = LinearLayoutManager(this@VmListActivity)
                    binding.rvVmList.adapter = adapter
                    adapter.setVmList(it.data as MutableList<DataVm>)
                }
                is NetworkResult.Error ->{
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading ->{
                    swipeRefresh.isRefreshing = it.isLoading
                }
            }
        }
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                val fcmToken = it.result
                Log.e("token: ", fcmToken)
            }

        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    //menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.notifications -> {
                Intent(this, NotificationsActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}