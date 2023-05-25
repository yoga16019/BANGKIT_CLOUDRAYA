package com.example.cloudraya.DetailVm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.cloudraya.Local.SiteRegister
import com.example.cloudraya.Model.ResponseVmList
import com.example.cloudraya.Model.ResquestActionVm
import com.example.cloudraya.Model.UserResponse
import com.example.cloudraya.R
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

        //token
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                // Tangani kegagalan mendapatkan token
                return@addOnCompleteListener
            }

            // Token perangkat yang berhasil diperoleh
            val key = task.result
            Log.e("key: ", key)
            // Lakukan tindakan sesuai kebutuhan aplikasi Anda dengan token
        }
        val tokens = intent.getStringExtra("token")
        val token = intent.getParcelableExtra<SiteRegister>("siteList")
        val item = intent.getParcelableExtra<ResponseVmList.DataVmList>("vmList")

        binding.buttonBack.setOnClickListener {
            onBackPressed()
        }

        swipeRefresh = binding.refresh
        swipeRefresh.setOnRefreshListener {
            viewModel.getVmDetail(token!!.api_url, tokens.toString(), item!!.localId!!, swipeRefresh)
            viewModel.responseDetailVm.observe(this){
                binding.apply {
                    statusValue.text = it.data?.state
                    vmName.text = it.data?.hostname
                    tvIp.text = it.data?.publicIp
                    tvLocation.text = it.data?.location

                }
                if (it.data?.state == "Running" || it.data?.state == "Stopped"){
                    swipeRefresh.isRefreshing = false
                }
            }
        }

        viewModel.getVmDetail(token!!.api_url,tokens.toString(), item!!.localId!!, swipeRefresh)
        viewModel.responseDetailVm.observe(this){
            binding.apply {
                statusValue.text = it.data?.state
                vmName.text = it.data?.hostname
                tvIp.text = it.data?.publicIp
                tvLocation.text = it.data?.location
            }
        }

        binding.btnDeleteVM.setOnClickListener {

            val requestDelete = ResquestActionVm()
            requestDelete.vmId = item?.localId.toString()
            requestDelete.request = "destroy"
            requestDelete.releaseIp = true

            val alertBuilder = AlertDialog.Builder(this)

            alertBuilder.setMessage("Are you sure to Delete this Vm?")
            alertBuilder.setTitle("Confirm Delete Vm")
            alertBuilder.setCancelable(false)
            alertBuilder.setPositiveButton("Yes"){_,_ ->
                viewModel.deleteVm(token!!.api_url,tokens.toString(),requestDelete)
                Toast.makeText(this, "Please wait processing for Delete VM", Toast.LENGTH_SHORT).show()
            }
            alertBuilder.setNegativeButton("No"){_,_->
                Toast.makeText(this, "Cancel to Delete VM", Toast.LENGTH_SHORT).show()

            }
            alertBuilder.setNeutralButton("Cancel"){_,_->
            }
            val alertDeleteVm = alertBuilder.create()
            alertDeleteVm.show()
        }

        binding.btnStartVM.setOnClickListener {
            val requestStart = ResquestActionVm()
            requestStart.vmId = item?.localId.toString()
            requestStart.request = "start"
            requestStart.releaseIp = false
            viewModel.startVm(token!!.api_url,tokens.toString(),requestStart)
            binding.statusValue.text = viewModel.responseDetailVm.value?.data?.state
            Toast.makeText(this, "Starting VM ", Toast.LENGTH_SHORT).show()

        }

        binding.btnStopVM.setOnClickListener {
            val requestStop = ResquestActionVm()
            requestStop.vmId = item?.localId.toString()
            requestStop.request = "stop"
            requestStop.releaseIp = false

            val alertBuilder = AlertDialog.Builder(this)
            alertBuilder.setMessage("Are you sure to Stop this Vm?")
            alertBuilder.setTitle("Confirm Delete Vm")
            alertBuilder.setCancelable(false)
            alertBuilder.setPositiveButton("Yes"){_,_ ->
                viewModel.stopVm(token!!.api_url,tokens.toString(),requestStop)
                binding.statusValue.text = viewModel.responseDetailVm.value?.data?.state
                Toast.makeText(this, "Stopping VM ", Toast.LENGTH_SHORT).show()
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
}