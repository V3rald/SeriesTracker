package com.example.seriestracker;

import com.example.seriestracker.imdbapi.models.SearchData;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchTest {
    private static final String apiKey = "k_1xg6pzjm";
    private static final String baseUrl = "https://imdb-api.com/";

    @Test
    public void successfulSearchTest(){
        String expression = "?title=Stranger things";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(baseUrl + "en/API/AdvancedSearch/" + apiKey + "/" + expression)
                .get()
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.isSuccessful();
            }
        });
    }

    @Test
    public void noResultSearchTest(){
        String expression = "?title=Stranger thingsaaaaagdgdgsdvv";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(baseUrl + "en/API/AdvancedSearch/" + apiKey + "/" + expression)
                .get()
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.isSuccessful();

                String responseString = Objects.requireNonNull(response.body()).string();
                SearchData searchData = new Gson().fromJson(responseString, SearchData.class);

                assert searchData.getResults().size() == 0;
            }
        });
    }
}
