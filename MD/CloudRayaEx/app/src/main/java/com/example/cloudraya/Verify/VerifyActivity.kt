package com.example.cloudraya.Verify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.cloudraya.Local.NotifData
import com.example.cloudraya.ModelApiTest.ResponseActionVmVerify
import com.example.cloudraya.Util.NetworkResult
import com.example.cloudraya.VmListActivity.VmListActivity
import com.example.cloudraya.databinding.ActivityVerifyBinding

class VerifyActivity : AppCompatActivity() {
    lateinit var binding: ActivityVerifyBinding
    private val viewModel by viewModels<VerifyViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val action = intent.getStringExtra("action")

        val id = intent.getStringExtra("vmId")
        val ids = intent.getParcelableExtra<NotifData>("notif")


        binding.btnVerify.setOnClickListener {
            if(ids!=null){
                binding.textView12.text = ids!!.vm_id.toString()
                if(ids.action == "create"){
                    val vCode = binding. etVerify.text
                    viewModel.responVerifyNewVm.observe(this@VerifyActivity){
                        when(it){
                            is NetworkResult.Success<*> ->{
                                val respon = it.data
                                Log.e("respon", it.data.toString())
                                if (respon == "Wrong Code"){
                                    Toast.makeText(this@VerifyActivity, respon.toString(), Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(this@VerifyActivity, respon.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }
                            is NetworkResult.Error ->{
                                Toast.makeText(this@VerifyActivity, it.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                            is NetworkResult.Loading ->{

                            }
                        }
                    }
                    viewModel.verifyNewVm("https://harirayacloud.as.r.appspot.com/", ids.vm_id,vCode.toString())

                }else{
                    //action starts stop
                    val vCode = binding. etVerify.text
                    viewModel.responVerifyAction.observe(this@VerifyActivity){
                        when(it){
                            is NetworkResult.Success<*> ->{
                                val respon = it.data as ResponseActionVmVerify
                                Log.e("respon", respon.message!!)
                                if (respon.code == 200){
                                    Toast.makeText(this@VerifyActivity, respon.message, Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(this@VerifyActivity, "your code is wrong", Toast.LENGTH_SHORT).show()

                                }

                            }
                            is NetworkResult.Error ->{
                                Toast.makeText(this@VerifyActivity, it.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                            is NetworkResult.Loading ->{

                            }
                        }
                    }
                    viewModel.verifyAction("https://harirayacloud.as.r.appspot.com/", ids.vm_id,ids.action,vCode.toString())
                }
            }else if(id != null){
                binding.textView12.text = id.toString()
                if(action == "create"){
                    val vCode = binding. etVerify.text
                    viewModel.responVerifyNewVm.observe(this@VerifyActivity){
                        when(it){
                            is NetworkResult.Success<*> ->{
                                val respon = it.data
                                Log.e("respon", it.data.toString())
                                if (respon == "Wrong Code"){
                                    Toast.makeText(this@VerifyActivity, respon.toString(), Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(this@VerifyActivity, respon.toString(), Toast.LENGTH_SHORT).show()

                                }
                            }
                            is NetworkResult.Error ->{
                                Toast.makeText(this@VerifyActivity, it.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                            is NetworkResult.Loading ->{

                            }
                        }
                    }
                    viewModel.verifyNewVm("https://harirayacloud.as.r.appspot.com/", id.toString(),vCode.toString())
                }else{
                    //action starts stop
                    val vCode = binding. etVerify.text
                    viewModel.responVerifyAction.observe(this@VerifyActivity){
                        when(it){
                            is NetworkResult.Success<*> ->{
                                Log.e("respon", it.data.toString())
                                Toast.makeText(this@VerifyActivity, it.data.toString(), Toast.LENGTH_SHORT).show()

                            }
                            is NetworkResult.Error ->{
                                Toast.makeText(this@VerifyActivity, it.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                            is NetworkResult.Loading ->{
                            }
                        }
                    }
                    viewModel.verifyAction("https://harirayacloud.as.r.appspot.com/", id.toString(),action.toString(),vCode.toString())
                }
            }

        }

    }
}