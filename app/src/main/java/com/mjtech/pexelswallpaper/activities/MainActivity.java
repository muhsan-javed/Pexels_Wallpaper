package com.mjtech.pexelswallpaper.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mjtech.pexelswallpaper.R;
import com.mjtech.pexelswallpaper.Adapters.WallpaperAdapter;
import com.mjtech.pexelswallpaper.Models.WallpaperModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AppCompatEditText edittext;
    AppCompatImageButton searchOption;
    WallpaperAdapter wallpaperAdapter;
    List<WallpaperModel> wallpaperModelList;
    int pageNumber = 1;
    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    //String url = "https://api.pexels.com/v1/curated/?page=\"+pageNumber+ \"&per_page=80";
//    String url = "https://api.pexels.com/v1/curated/?page="+pageNumber+"&per_page=80";
//    String url = "https://api.pexels.com/v1/curated/?page=\"+pageNumber+\"&per_page=80";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        recyclerView = findViewById(R.id.recyclerView);
        searchOption = findViewById(R.id.searchOption);
        edittext = findViewById(R.id.edittext);
        wallpaperModelList = new ArrayList<>();
        wallpaperAdapter = new WallpaperAdapter(this, wallpaperModelList);

        recyclerView.setAdapter(wallpaperAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // ScrollListener Function
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = gridLayoutManager.getChildCount();
                totalItems = gridLayoutManager.getItemCount();
                scrollOutItems = gridLayoutManager.findFirstCompletelyVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    fetchWallpaper();
                }
            }
        });

        fetchWallpaper(); // All data fetch fun api

        // Search Function
        searchOption.setOnClickListener(v -> {
            String query = edittext.getText().toString().trim().toLowerCase();
            wallpaperModelList.clear();
            StringRequest request = new StringRequest(Request.Method.GET, "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80&query=" + query,
                    new Response.Listener<String>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                JSONArray jsonArray = jsonObject.getJSONArray("photos");

                                int length = jsonArray.length();

                                for (int i = 0; i < length; i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    int id = object.getInt("id");

                                    JSONObject objectImages = object.getJSONObject("src");

                                    String originalUrl = objectImages.getString("original");
                                    String mediumUrl = objectImages.getString("medium");

                                    WallpaperModel wallpaperModel = new WallpaperModel(id, originalUrl, mediumUrl);
                                    wallpaperModelList.add(wallpaperModel);
                                }

                                wallpaperAdapter.notifyDataSetChanged();
                                pageNumber++;

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "ERROR" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, error -> {
                             Toast.makeText(this, "Data Not Fetch Api", Toast.LENGTH_SHORT).show();
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", "563492ad6f917000010000016190f711658e4eb1a96ff1213e707d57");
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(request);
        });

    }

    // Get Data Api Fun
    // Uri https://api.pexels.com/v1/curated/?page="+pageNumber+"&per_page=80
    public void fetchWallpaper() {
        StringRequest request = new StringRequest(Request.Method.GET, "https://api.pexels.com/v1/curated/?page=" + pageNumber + "&per_page=80",
//        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArray = jsonObject.getJSONArray("photos");

                            int length = jsonArray.length();

                            for (int i = 0; i < length; i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                int id = object.getInt("id");

                                JSONObject objectImages = object.getJSONObject("src");

                                String originalUrl = objectImages.getString("original");
                                String mediumUrl = objectImages.getString("medium");

                                WallpaperModel wallpaperModel = new WallpaperModel(id, originalUrl, mediumUrl);
                                wallpaperModelList.add(wallpaperModel);
                            }

                            wallpaperAdapter.notifyDataSetChanged();
                            pageNumber++;

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "ERROR" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
//                params.put("Authorization","563492ad6f917000010000016190f711658e4eb1a96ff1213e707d57");
                params.put("Authorization", "563492ad6f917000010000016190f711658e4eb1a96ff1213e707d57");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    // Old Search Button function
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_search){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            final EditText editText = new EditText(this);
            editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            alert.setMessage("Enter Category e.g. Nature");
            alert.setTitle("Search Wallpaper");

            alert.setView(editText);

            alert.setPositiveButton("Yes", (dialog, which) -> {


                String query = editText.getText().toString().toLowerCase();

//                url = "https://api.pexels.com/v1/search/?page="+pageNumber+"&per_page=80&query="+query;
                wallpaperModelList.clear();
                StringRequest request = new StringRequest(Request.Method.GET, "https://api.pexels.com/v1/search/?page="+pageNumber+"&per_page=80&query="+query,
//        StringRequest request = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    JSONArray jsonArray =  jsonObject.getJSONArray("photos");

                                    int length = jsonArray.length();

                                    for (int i =0; i<length;i++){

                                        JSONObject object = jsonArray.getJSONObject(i);

                                        int id = object.getInt("id");

                                        JSONObject objectImages = object.getJSONObject("src");

                                        String originalUrl = objectImages.getString("original");
                                        String mediumUrl = objectImages.getString("medium");

                                        WallpaperModel wallpaperModel = new WallpaperModel(id,originalUrl,mediumUrl);
                                        wallpaperModelList.add(wallpaperModel);
                                    }

                                    wallpaperAdapter.notifyDataSetChanged();
                                    pageNumber++;

                                }catch (JSONException e){
                                    e.printStackTrace();
                                    Toast.makeText(MainActivity.this, "ERROR"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {

                        Map<String, String> params = new HashMap<>();
//                params.put("Authorization","563492ad6f917000010000016190f711658e4eb1a96ff1213e707d57");
                        params.put("Authorization","563492ad6f917000010000016190f711658e4eb1a96ff1213e707d57");

                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(request);

            });


            alert.setNegativeButton("No", (dialog, which) -> {

            });
            alert.show();

        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    // Kotlin Code
    /*
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

      supportActionBar?.hide()


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
      */
}
