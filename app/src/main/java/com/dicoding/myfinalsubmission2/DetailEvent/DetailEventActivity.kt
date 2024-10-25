package com.dicoding.myfinalsubmission2.DetailEvent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebStorage.QuotaUpdater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dicoding.myfinalsubmission2.EventApiConfig
import com.dicoding.myfinalsubmission2.EventDetail
import com.dicoding.myfinalsubmission2.R
import com.dicoding.myfinalsubmission2.databinding.ActivityDetailEventBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle = intent.extras
        if (bundle != null) {
            val eventId = bundle.getLong("event_id")
            fetchData(eventId)
        }

    }

    private fun fetchData(id: Long?) {
        if (id != null) {
            showLoading(true)
            val client = EventApiConfig.getApiService().getDetailEvent(id.toString())
            client.enqueue(object : Callback<EventDetail> {
                override fun onResponse(call: Call<EventDetail>, response: Response<EventDetail>) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        val res = response.body()
                        Log.e("SUCCESS", "Succes: ${res?.event}")
                        if (res != null) {
                            val eventDetail = res.event

                            val subtract: (Int?, Int?) -> Int? = { a, b ->
                                if (a != null && b != null) {
                                    a - b
                                } else {
                                    null 
                                }
                            }

                            val sisaQuota = subtract(eventDetail?.quota, eventDetail?.registrants)

                            Glide.with(binding.detailEventImage.context)
                                .load(eventDetail?.mediaCover).into(binding.detailEventImage)
                            binding.detailEventTitle.text = eventDetail?.name
                            binding.detailEventSubtitle.text = eventDetail?.summary
                            binding.detailEventSubtitle2.text =
                                "Penyelenggara: ${eventDetail?.ownerName}\nWaktu Acara: ${eventDetail?.beginTime}\nKuota: ${eventDetail?.quota}\nSisa Kuota: $sisaQuota\n"
                            binding.detailEventDescription.text = HtmlCompat.fromHtml(
                                eventDetail?.description.toString(),
                                HtmlCompat.FROM_HTML_MODE_LEGACY
                            )
                            binding.registerButton.setOnClickListener {
                               val url = eventDetail?.link
                                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                startActivity(webIntent)
                            }
                        }

                    } else {
                        Log.e("TAG", "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<EventDetail>, t: Throwable) {
                    showLoading(false)
                    Log.e("TAG", "onFailure: ${t.message}")
                }

            })
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}