package com.dicoding.myfinalsubmission2.finishedEvent

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.myfinalsubmission2.DetailEvent.DetailEventActivity
import com.dicoding.myfinalsubmission2.ListEventsItem
import com.dicoding.myfinalsubmission2.R
import com.dicoding.myfinalsubmission2.databinding.ItemRowEventBinding
import com.dicoding.myfinalsubmission2.upcomingEvent.UpcomingEventAdapter

class FinishedEventAdapter :
    ListAdapter<ListEventsItem, FinishedEventAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemRowEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listEventsItem = getItem(position)
        holder.bind(listEventsItem)
        holder.itemView.setOnClickListener{ view ->
            val mBundle = Bundle()
            if(listEventsItem.id != null){
                mBundle.putLong(EVENT_ID, listEventsItem.id)
                val intent = Intent(view.context, DetailEventActivity::class.java)
                mBundle.putLong(EVENT_ID, listEventsItem.id)
                intent.putExtras(mBundle)
                view.context.startActivity(intent)

                Log.e("ID", "Id: ${listEventsItem.id}")
            }
        }
    }

    class MyViewHolder(private val binding: ItemRowEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(eventsItem: ListEventsItem){
            val mediaCoverUrl = eventsItem.mediaCover
            binding.tvItemName.text = eventsItem.name
            Glide.with(binding.imgItemPhoto.context).load(mediaCoverUrl).into(binding.imgItemPhoto)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
        const val EVENT_ID = "event_id"
    }



}