package com.example.seriestracker.imdbapi;

import android.app.Activity;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class IMDBAPI {
    private static final String apiKey = "k_1xg6pzjm";
    private static final String baseUrl = "https://imdb-api.com/";

    public static void searchAll(Activity activity, String expression) {
        /*String json = "{\"searchType\":\"Title\",\"expression\":\"the boys\",\"results\":[{\"id\":\"tt1190634\",\"resultType\":\"Title\",\"image\":\"https://imdb-api.com/images/original/MV5BOTEyNDJhMDAtY2U5ZS00OTMzLTkwODktMjU3MjFkZWVlMGYyXkEyXkFqcGdeQXVyMjkwOTAyMDU@._V1_Ratio0.7727_AL_.jpg\",\"title\":\"The Boys\",\"description\":\"(2019) (TV Series)\"},{\"id\":\"tt0139898\",\"resultType\":\"Title\",\"image\":\"https://imdb-api.com/images/original/MV5BMzMzNjgyZWUtOWMxMC00YTc3LWI4YjYtNjBiODc4ZmI4NjZkXkEyXkFqcGdeQXVyMTMxMTY0OTQ@._V1_Ratio0.7273_AL_.jpg\",\"title\":\"The Boys\",\"description\":\"(1998)\"},{\"id\":\"tt0054697\",\"resultType\":\"Title\",\"image\":\"https://imdb-api.com/images/original/MV5BNzljNmY5ZGQtZDIwNi00MjM0LWIwZGItMjRlZGY1OGYwNTAxXkEyXkFqcGdeQXVyMDY4MzkyNw@@._V1_Ratio0.7273_AL_.jpg\",\"title\":\"The Boys\",\"description\":\"(1962)\"},{\"id\":\"tt16350094\",\"resultType\":\"Title\",\"image\":\"https://imdb-api.com/images/original/MV5BNDM5MWRmZjYtNWE1ZS00ZWU4LWI0ZWMtOTc4ZDliYmI3NDYxXkEyXkFqcGdeQXVyODc0OTEyNDU@._V1_Ratio0.7273_AL_.jpg\",\"title\":\"The Boys Presents: Diabolical\",\"description\":\"(2022) (TV Series)\"},{\"id\":\"tt0077269\",\"resultType\":\"Title\",\"image\":\"https://imdb-api.com/images/original/MV5BZjgxYjVmODctOTRhMy00MmE1LTgwOWUtYjY1OGQwYjBiMzg1XkEyXkFqcGdeQXVyMTMxMTY0OTQ@._V1_Ratio0.7273_AL_.jpg\",\"title\":\"The Boys from Brazil\",\"description\":\"(1978)\"},{\"id\":\"tt16308040\",\"resultType\":\"Title\",\"image\":\"https://imdb-api.com/images/original/MV5BZWM4NTIzMjEtYTc3OC00OThkLWE3NmQtMTg4ZGE0YjYxMzhmXkEyXkFqcGdeQXVyMTg3NTA2NzI@._V1_Ratio0.7273_AL_.jpg\",\"title\":\"The Boys: VNN (Seven on 7)\",\"description\":\"(2021) (TV Mini Series)\"},{\"id\":\"tt10199914\",\"resultType\":\"Title\",\"image\":\"https://imdb-api.com/images/original/MV5BMDkyODhlYmUtZWU1OS00NWVhLTk3MjMtMDRjZjBiYTc0OWRhXkEyXkFqcGdeQXVyMjUxMTY3ODM@._V1_Ratio0.7273_AL_.jpg\",\"title\":\"The Boys in the Band\",\"description\":\"(2020)\"},{\"id\":\"tt1856080\",\"resultType\":\"Title\",\"image\":\"https://imdb-api.com/images/original/nopicture.jpg\",\"title\":\"The Boys in the Boat\",\"description\":\"\"},{\"id\":\"tt3882082\",\"resultType\":\"Title\",\"image\":\"https://imdb-api.com/images/original/MV5BMTc1MjcxNzcwMV5BMl5BanBnXkFtZTgwMTE0NTE2NzE@._V1_Ratio0.7273_AL_.jpg\",\"title\":\"The Boy\",\"description\":\"(2016)\"},{\"id\":\"tt1830379\",\"resultType\":\"Title\",\"image\":\"https://imdb-api.com/images/original/MV5BYzJiNDBlZDAtYTM1MS00OWQwLTg1MWItYmExMDY4YWJmNGIxXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_Ratio0.7273_AL_.jpg\",\"title\":\"Top Boy\",\"description\":\"(2011) (TV Series)\"}],\"errorMessage\":\"\"}";
        SearchData searchData = new Gson().fromJson(json, SearchData.class);

        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);
        SearchRecyclerAdapter adapter = new SearchRecyclerAdapter(activity, searchData.getResults());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));*/


        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(baseUrl + "en/API/Search/" + apiKey + "/" + expression)
                .get()
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //TODO: Error handling
                Toast.makeText(activity, "ERROR", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {

                    String responseString = response.body().string();

                    SearchData searchData = new Gson().fromJson(responseString, SearchData.class);

                    activity.runOnUiThread(() -> {
                        SearchRecyclerAdapter adapter = new SearchRecyclerAdapter(activity, searchData.getResults());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                    });
                } else {
                    //TODO: Error handling
                    Toast.makeText(activity, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}