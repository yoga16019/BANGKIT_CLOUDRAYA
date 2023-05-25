package com.example.cloudraya.SiteList


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cloudraya.Local.SiteRegister
import com.example.cloudraya.Register.RegisterActivity
import com.example.cloudraya.VmListActivity.VmListActivity
import com.example.cloudraya.databinding.ActivitySiteListBinding

class SiteListActivity : AppCompatActivity() {
    lateinit var binding: ActivitySiteListBinding
    lateinit var siteList: MutableList<SiteRegister>
    private val viewModel by viewModels<SiteListViewModel>()
    private val adapter by lazy{
        SiteListAdapter {
            Intent(this, VmListActivity::class.java).apply {
                putExtra("siteList", it)
                startActivity(this)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySiteListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            rvSiteList.layoutManager = LinearLayoutManager(this@SiteListActivity)
            rvSiteList.adapter = adapter
        }
            viewModel.getSite().observe(this){
                adapter.setSiteList(it)
            }
        binding.btnRegister.setOnClickListener {
            intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

}
