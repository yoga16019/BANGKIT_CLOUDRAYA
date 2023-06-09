package com.example.cloudraya.DetailVm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.cloudraya.Local.SiteRegister
import com.example.cloudraya.Model.ResponseVmList
import com.example.cloudraya.Model.ResquestActionVm
import com.example.cloudraya.Model.UserResponse
import com.example.cloudraya.ModelApiTest.DataVm
import com.example.cloudraya.ModelApiTest.ResponseActionVm
import com.example.cloudraya.ModelApiTest.ResponseDetailVm
import com.example.cloudraya.Notifications.NotificationsActivity
import com.example.cloudraya.R
import com.example.cloudraya.SiteList.SiteListActivity
import com.example.cloudraya.Util.NetworkResult
import com.example.cloudraya.databinding.ActivityDetailBinding
import com.google.firebase.messaging.FirebaseMessaging

class DetailActivity : AppCompatActivity() {
    private val viewModel by viewModels<DetailVmViewModel>()
    lateinit var binding: ActivityDetailBinding
    lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get fcm token
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }
            val key = task.result
            Log.e("key: ", key)
        }

        val tokens = intent.getStringExtra("token")
        val token = intent.getParcelableExtra<SiteRegister>("siteList")
        val item = intent.getParcelableExtra<DataVm>("vmList")

        //back button
        binding.buttonBack.setOnClickListener {
            onBackPressed()
        }

        // detail vm from api
        viewModel.responseDetailVm.observe(this@DetailActivity){
            when(it) {
                is NetworkResult.Success<*> -> {
                    val respon = it.data as ResponseDetailVm.Data
                    binding.apply {
                        statusValue.text = respon.isActive.toString()
                        vmName.text = respon.name
                        tvIp.text = respon.state.toString()
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(this@DetailActivity, it.exception.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                is NetworkResult.Loading -> {

                }
            }
        }
        viewModel.getVmDetail(token!!.api_url, item!!.id!!)

        //refresh
        swipeRefresh = binding.refresh
        swipeRefresh.setOnRefreshListener {
            viewModel.getVmDetail(token!!.api_url, item!!.id!!)
            viewModel.responseDetailVm.observe(this@DetailActivity){
                when(it) {
                    is NetworkResult.Success<*> -> {
                        val respon = it.data as ResponseDetailVm.Data
                        binding.apply {
                            statusValue.text = respon.isActive.toString()
                            vmName.text = respon.name
                            tvIp.text = respon.state.toString()
                        }
                    }
                    is NetworkResult.Error -> {
                        Toast.makeText(this@DetailActivity, it.exception.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                    is NetworkResult.Loading -> {

                    }
                }
            }
            swipeRefresh.isRefreshing =false
        }

        //start vm
        binding.btnStartVM.setOnClickListener {

            val start = "start"
            viewModel.actionVm(token.api_url, item!!.id!!, start)

            viewModel.responseActionVm.observe(this@DetailActivity){
                when(it) {
                    is NetworkResult.Success<*> -> {
                        val respon = it.data as ResponseActionVm
                        if (respon.code == 200){
                            Toast.makeText(this@DetailActivity, "succes", Toast.LENGTH_SHORT)
                                .show()
                        }else{
                            Toast.makeText(this@DetailActivity, "gagal", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    is NetworkResult.Error -> {
                        Toast.makeText(this@DetailActivity, it.exception.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                    is NetworkResult.Loading -> {

                    }
                }
            }

        }

        //stop vm
        binding.btnStopVM.setOnClickListener {

            //alert button
            val alertBuilder = AlertDialog.Builder(this)
            alertBuilder.setMessage("Are you sure to Stop this Vm?")
            alertBuilder.setTitle("Confirm Stop Vm")
            alertBuilder.setCancelable(false)
            alertBuilder.setPositiveButton("Yes"){_,_ ->
                val start = "stop"
                viewModel.actionVm(token.api_url, item!!.id!!, start)
                viewModel.responseActionVm.observe(this@DetailActivity){
                    when(it) {
                        is NetworkResult.Success<*> -> {
                            val respon = it.data as ResponseActionVm
                            if (respon.code == 200){
                                Toast.makeText(this@DetailActivity, "succes", Toast.LENGTH_SHORT)
                                    .show()
                            }else{
                                Toast.makeText(this@DetailActivity, "gagal", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        is NetworkResult.Error -> {
                            Toast.makeText(this@DetailActivity, it.exception.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                        is NetworkResult.Loading -> {

                        }
                    }
                }
            }
            alertBuilder.setNegativeButton("No"){_,_->
                Toast.makeText(this, "Cancel to Stop VM", Toast.LENGTH_SHORT).show()

            }
            alertBuilder.setNeutralButton("Cancel"){_,_->
            }
            val alertStopVM = alertBuilder.create()
            alertStopVM.show()
            
        }
    }

    //notificarions menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

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