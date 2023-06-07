package com.mjtech.pexelswallpaper.kotlin

/*

import android.os.Bundle
import android.widget.AbsListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class MainActivityKotlin : AppCompatActivity() {
    //private lateinit var binding :ActivityMainBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var edittext: AppCompatEditText
    private lateinit var searchOption: AppCompatImageButton
    lateinit var wallpaperAdapter: WallpaperAdapter
    lateinit var wallpaperModelList: MutableList<WallpaperModel>
    var pageNumber = 1
    var isScrolling = false
    var currentItems:Int = 0
    var totalItems :Int = 0
    var scrollOutItems: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

//        binding.webViewSite.loadUrl("file:///android_asset/index.html")
//        binding.webViewSite.settings.javaScriptEnabled = true
//        binding.webViewSite.webViewClient = WebViewClient()

        recyclerView = findViewById(R.id.recyclerView)
        searchOption = findViewById(R.id.searchOption)
        edittext = findViewById(R.id.edittext)
        wallpaperModelList = ArrayList()
        wallpaperAdapter = WallpaperAdapter(this, wallpaperModelList)

        recyclerView.adapter = wallpaperAdapter

        val gridLayoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = gridLayoutManager

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItems = gridLayoutManager.childCount
                totalItems = gridLayoutManager.itemCount
                scrollOutItems = gridLayoutManager.findFirstCompletelyVisibleItemPosition()
                if (isScrolling && ((currentItems + scrollOutItems) == totalItems)) {
                    isScrolling = false
                    fetchWallpaper()
                }
            }
        })

        fetchWallpaper()
    }

    fun fetchWallpaper() {
        val request: StringRequest = object : StringRequest(
            Method.GET,
            "https://api.pexels.com/v1/curated/?page=$pageNumber&per_page=80",  //        StringRequest request = new StringRequest(Request.Method.GET, url,
            Response.Listener<String?> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val jsonArray = jsonObject.getJSONArray("photos")
                    val length = jsonArray.length()
                    for (i in 0 until length) {
                        val `object` = jsonArray.getJSONObject(i)
                        val id = `object`.getInt("id")
                        val objectImages = `object`.getJSONObject("src")
                        val originalUrl = objectImages.getString("original")
                        val mediumUrl = objectImages.getString("medium")
                        val wallpaperModel = WallpaperModel(id, originalUrl, mediumUrl)
                        wallpaperModelList.add(wallpaperModel)
//
                    }
                    wallpaperAdapter.notifyDataSetChanged()
                    pageNumber++

                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this@MainActivity, "ERROR" + e.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }, Response.ErrorListener { }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "563492ad6f917000010000016190f711658e4eb1a96ff1213e707d57"
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(applicationContext)
        requestQueue.add(request)
    }
}*/
