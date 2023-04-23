package com.example.seriestracker.firebaseapi;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.example.seriestracker.imdbapi.models.SearchResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.type.DateTime;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FB {
    public static final FirebaseAuth auth = FirebaseAuth.getInstance();
    public final FirebaseFirestore db;

    public FB(){
        db = FirebaseFirestore.getInstance();
    }

    public void addContent(Context context, String user, SearchResult content){
        getContents(context, user, callbackValue -> {
            String json = (String) callbackValue.get("watchlist");
            Type searchResultListType = new TypeToken<ArrayList<SearchResult>>(){}.getType();
            List<SearchResult> contents = new Gson().fromJson(json, searchResultListType);

            if (contents == null){
                contents = new ArrayList<>();
            }

            if (contents.stream().noneMatch(o -> o.getId().equals(content.getId()))){
                contents.add(content);

                Map<String, String> result = new HashMap<>();
                result.put("watchlist", new Gson().toJson(contents));

                db.collection(user).document("content").set(result).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(context, content.getTitle() + " added!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, task.getException() + "", Toast.LENGTH_SHORT).show();
                        //TODO: Error handling
                    }
                });
            }else{
                Toast.makeText(context, content.getTitle() + " already added!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removeContent(Context context, String user, String id){
        getContents(context, user, callbackValue -> {
            String json = (String) callbackValue.get("watchlist");
            Type searchResultListType = new TypeToken<ArrayList<SearchResult>>(){}.getType();
            List<SearchResult> contents = new Gson().fromJson(json, searchResultListType);

            if (contents == null){
                contents = new ArrayList<>();
            }

            List<SearchResult> newContents = new ArrayList<>();
            SearchResult removedContent = null;
            for (SearchResult content : contents){
                if (!content.getId().equals(id)){
                    newContents.add(content);
                }else{
                    removedContent = content;
                }
            }

            if (removedContent != null){
                Map<String, String> result = new HashMap<>();
                result.put("watchlist", new Gson().toJson(newContents));

                SearchResult finalRemovedContent = removedContent;
                db.collection(user).document("content").set(result).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(context, finalRemovedContent.getTitle() + " removed!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, task.getException() + "", Toast.LENGTH_SHORT).show();
                        //TODO: Error handling
                    }
                });
            }
        });
    }

    public void getContents(Context context, String user, ContentCallback callback){
        db.collection(user).document("content").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if (document.exists()){
                    callback.onCallback(document.getData());
                }else{
                    callback.onCallback(Collections.emptyMap());
                }
            }else{
                Toast.makeText(context, task.getException() + "", Toast.LENGTH_SHORT).show();
                //TODO: Error handling
            }
        });
    }

    public interface ContentCallback {
        void onCallback(Map<String, Object> value);
    }

    public void setDateJoined(Context context, String user){
        Map<String, String> result = new HashMap<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        result.put("date_joined", dtf.format(LocalDateTime.now()));

        db.collection(user).document("account").set(result).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(context, task.getException() + "", Toast.LENGTH_SHORT).show();
                //TODO: Error handling
            }
        });
    }

    public void getDateJoined(Context context, String user, DateJoinedCallback callback){
        db.collection(user).document("account").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if (document.exists()){
                    callback.onCallback(document.getData());
                }
            }else{
                Toast.makeText(context, task.getException() + "", Toast.LENGTH_SHORT).show();
                //TODO: Error handling
            }
        });
    }

    public interface DateJoinedCallback {
        void onCallback(Map<String, Object> value);
    }
}
