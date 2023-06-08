package com.example.cloudraya.Notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cloudraya.Local.NotifData
import com.example.cloudraya.ModelApiTest.DataVm
import com.example.cloudraya.ModelApiTest.NotificationsData
import com.example.cloudraya.databinding.NotifItemBinding
import com.example.cloudraya.databinding.VmItemBinding

class NotificationsAdapter(private val dataNotif: MutableList<NotifData> = mutableListOf(), private val listener:(NotifData) -> Unit) : RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>(){

    fun setNotifList(dataNotif: MutableList<NotifData>){
        this.dataNotif.clear()
        this.dataNotif.addAll(0,dataNotif)
        notifyItemInserted(0)
        notifyDataSetChanged()

    }

    inner class NotificationViewHolder(private val binding: NotifItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(dataNotif: NotifData) {
            binding.NotifTitle.text = dataNotif.title
            binding.notifBody.text = dataNotif.body
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val itemView = NotifItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataNotif.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(dataNotif[position])
        holder.itemView.setOnClickListener {
            listener(dataNotif[position])
        }
    }
}