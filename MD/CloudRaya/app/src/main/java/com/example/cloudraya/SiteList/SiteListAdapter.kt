package com.example.cloudraya.SiteList


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cloudraya.Local.SiteRegister
import com.example.cloudraya.databinding.SiteItemBinding

class SiteListAdapter(private var siteList: MutableList<SiteRegister> = mutableListOf(), private val listener:(SiteRegister) -> Unit) : RecyclerView.Adapter<SiteListAdapter.SiteListViewHolder>()  {

    fun setSiteList(siteList: MutableList<SiteRegister>){
        this.siteList.clear()
        this.siteList.addAll(siteList)
        notifyDataSetChanged()
    }

    inner class SiteListViewHolder(private val binding: SiteItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(siteList: SiteRegister) {
            binding.tvSiteTitle.text = siteList.site_name
            binding.tvUrl.text = siteList.api_url
        }
    }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiteListViewHolder  {
            val itemView = SiteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SiteListViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return siteList.size
        }

        override fun onBindViewHolder(holder: SiteListViewHolder, position: Int) {
            holder.bind(siteList[position])
            holder.itemView.setOnClickListener {
                listener(siteList[position])
            }
        }
}