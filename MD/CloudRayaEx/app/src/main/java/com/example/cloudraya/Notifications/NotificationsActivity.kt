package com.example.cloudraya.Notifications

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cloudraya.DetailVm.DetailActivity
import com.example.cloudraya.Local.NotifData
import com.example.cloudraya.ModelApiTest.DataVm
import com.example.cloudraya.ModelApiTest.NotificationsData
import com.example.cloudraya.SiteList.SiteListAdapter
import com.example.cloudraya.Verify.VerifyActivity
import com.example.cloudraya.VmListActivity.VmListActivity
import com.example.cloudraya.VmListActivity.VmListAdapter
import com.example.cloudraya.databinding.ActivityNotificationsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class NotificationsActivity : AppCompatActivity() {

    lateinit var binding: ActivityNotificationsBinding
    private val viewModel by viewModels<NotificationsViewModel>()
    private val adapter by lazy{
        NotificationsAdapter {
            Intent(this, VerifyActivity::class.java).apply {
                putExtra("notif", it)
                startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set recycler view
        binding.rvNotif.layoutManager = LinearLayoutManager(this)
        binding.rvNotif.adapter = adapter
        viewModel.getSite().observe(this){
            adapter.setNotifList(it)
        }
    }
}