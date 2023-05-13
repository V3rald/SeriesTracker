package com.example.seriestracker.firebaseapi;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


import androidx.annotation.Nullable;

import com.example.seriestracker.FriendsActivity;
import com.example.seriestracker.user.Friend;
import com.example.seriestracker.imdbapi.models.SearchResult;
import com.example.seriestracker.user.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        content.setStatus("PTW");
        content.setRating(10);
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
                    }
                });

                db.collection("accounts").document("contents").get().addOnCompleteListener(contentsTask -> {
                    if(contentsTask.isSuccessful()){
                        DocumentSnapshot document = contentsTask.getResult();
                        String allContentsResult = "";
                        if (!document.exists()){
                            allContentsResult = new Gson().toJson(Collections.singletonMap(content.getId(), List.of(user)));
                        }else{
                            allContentsResult = (String) document.get("value");
                            Type contentResultListType = new TypeToken<HashMap<String, List<String>>>(){}.getType();
                            HashMap<String, List<String>> newContentsResult = new Gson().fromJson(allContentsResult, contentResultListType);
                            if(newContentsResult.containsKey(content.getId())){
                                List<String> alreadyAddedUsers = newContentsResult.get(content.getId());
                                alreadyAddedUsers.add(user);
                                newContentsResult.put(content.getId(), alreadyAddedUsers);
                            }else{
                                newContentsResult.put(content.getId(), List.of(user));
                            }

                            allContentsResult = new Gson().toJson(newContentsResult);
                        }

                        db.collection("accounts").document("contents").set(Collections.singletonMap("value", allContentsResult)).addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Toast.makeText(context, task.getException() + "", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(context, contentsTask.getException() + "", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(context, content.getTitle() + " already added!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void changeRating(Context context, String user, SearchResult media, int rating){
        getContents(context, user, callbackValue -> {
            String json = (String) callbackValue.get("watchlist");
            Type searchResultListType = new TypeToken<ArrayList<SearchResult>>() {}.getType();
            List<SearchResult> contents = new Gson().fromJson(json, searchResultListType);

            for(int i = 0; i < contents.size(); i++){
                SearchResult c = contents.get(i);

                if(c.getId().equals(media.getId())){
                    media.setRating(rating);
                    contents.set(i, media);
                    break;
                }
            }

            db.collection(user).document("content").set(Collections.singletonMap("watchlist", new Gson().toJson(contents))).addOnCompleteListener(task -> {
                if(!task.isSuccessful()){
                    Toast.makeText(context, task.getException() + "", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    public void changeStatus(Context context, String user, SearchResult media, String status){
        getContents(context, user, callbackValue -> {
            String json = (String) callbackValue.get("watchlist");
            Type searchResultListType = new TypeToken<ArrayList<SearchResult>>() {}.getType();
            List<SearchResult> contents = new Gson().fromJson(json, searchResultListType);

            for(int i = 0; i < contents.size(); i++){
                SearchResult c = contents.get(i);

                if(c.getId().equals(media.getId())){
                    media.setStatus(status);
                    contents.set(i, media);
                    break;
                }
            }

            db.collection(user).document("content").set(Collections.singletonMap("watchlist", new Gson().toJson(contents))).addOnCompleteListener(task -> {
                if(!task.isSuccessful()){
                    Toast.makeText(context, task.getException() + "", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    public void getAllChat(Context context, ChatCallback callback){
        db.collection("chat").document("messages").get().addOnCompleteListener(chatTask -> {
            if(chatTask.isSuccessful()){
                DocumentSnapshot chatResult = chatTask.getResult();

                if(chatResult.exists()){
                    String allChats = (String) chatResult.get("value");

                    Type chatResultListType = new TypeToken<List<Chat>>(){}.getType();
                    List<Chat> allChatList = new Gson().fromJson(allChats, chatResultListType);

                    callback.onCallback(Collections.singletonMap("value", allChatList));

                }else{
                    callback.onCallback(Collections.singletonMap("value", new ArrayList<>()));
                }
            }else{
                Toast.makeText(context, chatTask.getException() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateChat(Context context, String user, String friend, ChatCallback callback){
        db.collection("chat").document("messages").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if(value != null && value.exists()){
                    String allChats = (String) value.get("value");

                    Type chatResultListType = new TypeToken<List<Chat>>(){}.getType();
                    List<Chat> allChatList = new Gson().fromJson(allChats, chatResultListType);

                    List<Chat> importantChats = new ArrayList<>();
                    for(Chat c : allChatList){
                        if(c.getFriend().equals(friend) || c.getUser().equals(friend)){
                            if(c.getUser().equals(user) || c.getFriend().equals(user)){
                                importantChats.add(c);
                            }
                        }
                    }

                    callback.onCallback(Collections.singletonMap("value", importantChats));
                }else{
                    callback.onCallback(Collections.singletonMap("value", new ArrayList<>()));
                }
            }
        });
    }

    public void deleteMessage(Context context, Chat chat){
        new FB().getAllChat(context, callbackValue -> {
            List<Chat> chats =  callbackValue.get("value");

            int index = -1;
            for(Chat c : chats){
                if(c.getUser().equals(chat.getUser()) && c.getFriend().equals(chat.getFriend()) && c.getText().equals(chat.getText()) && c.getDate().equals(chat.getDate())){
                    index = chats.indexOf(c);
                    break;
                }
            }

            chats.remove(index);

            String json = new Gson().toJson(chats);

            db.collection("chat").document("messages").set(Collections.singletonMap("value", json)).addOnCompleteListener(deleteTask -> {
                if(!deleteTask.isSuccessful()){
                    Toast.makeText(context, deleteTask.getException() + "", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    public void sendChat(Context context, String user, String friend, String text){
        new FB().getAllChat(context, callbackValue -> {
            List<Chat> chats =  callbackValue.get("value");

            chats.add(new Chat(user, friend, text));

            String json = new Gson().toJson(chats);

            db.collection("chat").document("messages").set(Collections.singletonMap("value", json)).addOnCompleteListener(task -> {
                if(!task.isSuccessful()){
                    Toast.makeText(context, task.getException() + "", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    public void requestFriend(Context context, String user, Friend friend){
        friend.setStatus("Pending: Request");


        db.collection(user).document("friends").get().addOnCompleteListener(allFriendsTask -> {
            if(allFriendsTask.isSuccessful()){
                DocumentSnapshot allFriendsDocument = allFriendsTask.getResult();

                String friendRequest = "";

                if(!allFriendsDocument.exists()){
                    List<Friend> existingFriends = new ArrayList<>();
                    existingFriends.add(friend);
                    friendRequest = new Gson().toJson(existingFriends);
                }else{
                    Type allFriendsResultListType = new TypeToken<ArrayList<Friend>>(){}.getType();
                    List<Friend> existingFriends = new Gson().fromJson(String.valueOf(allFriendsDocument.get("value")), allFriendsResultListType);
                    existingFriends.add(friend);
                    friendRequest = new Gson().toJson(existingFriends);
                }

                db.collection(user).document("friends").set(Collections.singletonMap("value", friendRequest)).addOnCompleteListener(friendTask -> {
                    if (friendTask.isSuccessful()) {
                        String friendName = friend.getName();
                        friend.setName(user);
                        friend.setStatus("Pending: Requested");

                        db.collection(friendName).document("friends").get().addOnCompleteListener(AllfriendRequest -> {
                            if (AllfriendRequest.isSuccessful()) {
                                DocumentSnapshot allFriendRequestedDocument = AllfriendRequest.getResult();

                                String friendRequested = "";

                                if(!allFriendRequestedDocument.exists()){
                                    List<Friend> existingFriends = new ArrayList<>();
                                    existingFriends.add(friend);
                                    friendRequested = new Gson().toJson(existingFriends);
                                }else{
                                    Type allFriendsResultListType = new TypeToken<ArrayList<Friend>>(){}.getType();
                                    List<Friend> existingFriends = new Gson().fromJson(String.valueOf(allFriendRequestedDocument.get("value")), allFriendsResultListType);
                                    existingFriends.add(friend);
                                    friendRequested = new Gson().toJson(existingFriends);
                                }


                                db.collection(friendName).document("friends").set(Collections.singletonMap("value", friendRequested)).addOnCompleteListener(friendedTask -> {
                                    if (friendedTask.isSuccessful()) {
                                        Toast.makeText(context, friendName + " added!", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(context, friendedTask.getException() + "", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        });
                    }else{
                        Toast.makeText(context, friendTask.getException() + "", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(context, allFriendsTask.getException() + "", Toast.LENGTH_SHORT).show();
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
                    }
                });


                db.collection("accounts").document("contents").get().addOnCompleteListener(contentsTask -> {
                    if(contentsTask.isSuccessful()) {
                        DocumentSnapshot document = contentsTask.getResult();

                        String allContentsResult = (String) document.get("value");
                        Type contentResultListType = new TypeToken<HashMap<String, List<String>>>(){}.getType();
                        HashMap<String, List<String>> newContentsResult = new Gson().fromJson(allContentsResult, contentResultListType);

                        List<String> newContentsList = newContentsResult.get(id);
                        newContentsList.remove(user);
                        newContentsResult.put(id, newContentsList);
                        allContentsResult = new Gson().toJson(newContentsResult);

                        db.collection("accounts").document("contents").set(Collections.singletonMap("value", allContentsResult)).addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Toast.makeText(context, task.getException() + "", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    public void getFriends(Context context, String user, FriendsCallback callback){
        db.collection(user).document("friends").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                if(!document.exists()){
                    callback.onCallback(Collections.singletonMap("value", new ArrayList<>()));
                }else{
                    Type friendResultListType = new TypeToken<List<Friend>>(){}.getType();
                    List<Friend> friendResult = new Gson().fromJson(String.valueOf(document.get("value")), friendResultListType);
                    callback.onCallback(Collections.singletonMap("value", friendResult));
                }
            }else{
                Toast.makeText(context, task.getException() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void acceptFriend(Context context, String user, Friend friend){
        db.collection(user).document("friends").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                Type userFriendsResultListType = new TypeToken<ArrayList<Friend>>(){}.getType();
                List<Friend> userFriends = new Gson().fromJson(String.valueOf(document.get("value")), userFriendsResultListType);

                for(int i = 0; i <userFriends.size(); i++){
                    Friend f = userFriends.get(i);
                    if(f.getName().equals(friend.getName()) && f.getStatus().equals("Pending: Requested")){
                        f.setStatus("Friend");
                        userFriends.set(i, f);
                        break;
                    }
                }
                String friendRequest = new Gson().toJson(userFriends);

                db.collection(user).document("friends").set(Collections.singletonMap("value", friendRequest)).addOnCompleteListener(friendTask -> {
                    if (friendTask.isSuccessful()) {
                        db.collection(friend.getName()).document("friends").get().addOnCompleteListener(otherTask -> {
                            if(otherTask.isSuccessful()) {
                                DocumentSnapshot friendDocument = otherTask.getResult();

                                List<Friend> friendFriends = new Gson().fromJson(String.valueOf(friendDocument.get("value")), userFriendsResultListType);

                                for(int i = 0; i <friendFriends.size(); i++){
                                    Friend f = friendFriends.get(i);
                                    if(f.getName().equals(user) && f.getStatus().equals("Pending: Request")){
                                        f.setStatus("Friend");
                                        friendFriends.set(i, f);
                                        break;
                                    }
                                }
                                String friendFriendRequest = new Gson().toJson(friendFriends);

                                db.collection(friend.getName()).document("friends").set(Collections.singletonMap("value", friendFriendRequest)).addOnCompleteListener(otherFriendTask -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, friend.getName() + " accepted as friend!", Toast.LENGTH_SHORT).show();

                                        Intent friendsActivity = new Intent(context, FriendsActivity.class);
                                        context.startActivity(friendsActivity);

                                    }else{
                                        Toast.makeText(context, otherFriendTask.getException() + "", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(context, otherTask.getException() + "", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(context, task.getException() + "", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(context, task.getException() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getFriendRecommendations(Context context, String user, List<Friend> friends, FriendRecommendationCallback callback){
        db.collection("accounts").document("contents").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if(document.exists()){
                    String contentJson = (String) document.getData().get("value");
                    Type contentResultListType = new TypeToken<HashMap<String, List<String>>>(){}.getType();
                    HashMap<String, List<String>> allContents = new Gson().fromJson(contentJson, contentResultListType);

                    HashMap<String, Integer> friendMapping = new HashMap<>();
                    for(Map.Entry<String, List<String>> entry : allContents.entrySet()){
                        if(entry.getValue().contains(user)){
                            for(String friendEmail : entry.getValue()){

                                List<String> existingFriends = new ArrayList<>();

                                for(Friend f : friends){
                                    if(f.getName().equals(friendEmail)){
                                        existingFriends.add(f.getName());
                                    }
                                }

                                if(!friendEmail.equals(user) && !existingFriends.contains(friendEmail)){
                                    if(friendMapping.containsKey(friendEmail)){
                                        friendMapping.put(friendEmail, friendMapping.get(friendEmail) + 1);
                                    }else{
                                        friendMapping.put(friendEmail, 1);
                                    }
                                }
                            }
                        }
                    }
                    callback.onCallback(friendMapping);
                }
            }else{
                Toast.makeText(context, task.getException() + "", Toast.LENGTH_SHORT).show();
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
            }
        });
    }

    public interface FriendsCallback {
        void onCallback(Map<String, List<Friend>> value);
    }

    public interface ChatCallback {
        void onCallback(Map<String, List<Chat>> value);
    }

    public interface FriendRecommendationCallback {
        void onCallback(Map<String, Integer> value);
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
            }
        });

        db.collection("accounts").document("emails").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(context, task.getException() + "", Toast.LENGTH_SHORT).show();
            }else{
                DocumentSnapshot document = task.getResult();
                ArrayList<String> allEmails = new ArrayList<>();

                if (document.exists()){
                    allEmails = (ArrayList<String>) document.getData().get("emails");
                }

                allEmails.add(user);

                db.collection("accounts").document("emails").set(Collections.singletonMap("emails", allEmails)).addOnCompleteListener(taskAdd -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(context, task.getException() + "", Toast.LENGTH_SHORT).show();
                    }
                });
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
            }
        });
    }

    public interface DateJoinedCallback {
        void onCallback(Map<String, Object> value);
    }
}
