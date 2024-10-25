package com.dicoding.myfinalsubmission2.upcomingEvent

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myfinalsubmission2.Event
import com.dicoding.myfinalsubmission2.ListEventsItem
import com.dicoding.myfinalsubmission2.EventApiConfig
import com.dicoding.myfinalsubmission2.databinding.FragmentUpcomingEventBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingEventFragment : Fragment() {

    private var _binding: FragmentUpcomingEventBinding? = null
    private val binding get() = _binding!!

    private lateinit var upcomingEventAdapter: UpcomingEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingEventBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        binding.rvUpcomingEvents.layoutManager = layoutManager
        upcomingEventAdapter = UpcomingEventAdapter()
        binding.rvUpcomingEvents.adapter = upcomingEventAdapter
        fetchData()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun fetchData() {
        showLoading(true)
        val client = EventApiConfig.getApiService().getEvent("1")
        client.enqueue(object: Callback<Event>{
            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                showLoading(false)
                if (response.isSuccessful) {
                 val res= response.body()
                    if (res != null){
                       setReviewData(res.listEvents)
                    }

                }
                else{
                    Log.e("TAG", "onFailure: ${response.message()}")

                }
        }
            override fun onFailure(call: Call<Event>, t: Throwable) {
                showLoading(false)

                Log.e("TAG", "onFailure: ${t.message}")
            }

        })
    }

    private fun setReviewData(listUpcomingEvent: List<ListEventsItem?>?) {
        if (listUpcomingEvent != null) {
            upcomingEventAdapter.submitList(listUpcomingEvent) // Assuming you use ListAdapter or DiffUtil
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}