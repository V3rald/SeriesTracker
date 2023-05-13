package com.example.seriestracker.imdbapi;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seriestracker.R;
import com.example.seriestracker.adapters.SearchRecyclerAdapter;
import com.example.seriestracker.imdbapi.models.SearchData;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class IMDBAPI {
    private static final String apiKey = "k_1xg6pzjm";
    private static final String baseUrl = "https://imdb-api.com/";

    public static void searchAll(Activity activity, String query, String genre, String type) {
        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);

        String expression = "?title=" + query;

        if(!TextUtils.isEmpty(genre)){
            expression += "&genres=" + genre;
        }
        if(!TextUtils.isEmpty(type)){
            expression += "&title_type=" + type;
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(baseUrl + "en/API/AdvancedSearch/" + apiKey + "/" + expression)
                .get()
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {

                    String responseString = Objects.requireNonNull(response.body()).string();

                    SearchData searchData = new Gson().fromJson(responseString, SearchData.class);

                    activity.runOnUiThread(() -> {
                        SearchRecyclerAdapter adapter = new SearchRecyclerAdapter(activity, searchData.getResults());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                    });
                } else {
                    Toast.makeText(activity, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}