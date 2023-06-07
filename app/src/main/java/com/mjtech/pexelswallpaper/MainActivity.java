package com.mjtech.pexelswallpaper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
        fetchWallpaper();

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
}
