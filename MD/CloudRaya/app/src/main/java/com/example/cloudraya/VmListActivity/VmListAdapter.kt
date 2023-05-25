package com.example.cloudraya.VmListActivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cloudraya.Model.ResponseVmList
import com.example.cloudraya.databinding.VmItemBinding

class VmListAdapter(private val vmList: MutableList<ResponseVmList.DataVmList> = mutableListOf(), private val listener:(ResponseVmList.DataVmList) -> Unit) : RecyclerView.Adapter<VmListAdapter.VmListViewHolder>(){

    fun setVmList(vmList: MutableList<ResponseVmList.DataVmList>){
        this.vmList.clear()
        this.vmList.addAll(vmList)
        notifyDataSetChanged()
    }

    inner class VmListViewHolder(private val binding: VmItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(vmList: ResponseVmList.DataVmList) {
            binding.tvVmName.text = vmList.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VmListViewHolder {
        val itemView = VmItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VmListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return vmList.size
    }

    override fun onBindViewHolder(holder: VmListViewHolder, position: Int) {
        holder.bind(vmList[position])
        holder.itemView.setOnClickListener {
            listener(vmList[position])
        }
    }
}