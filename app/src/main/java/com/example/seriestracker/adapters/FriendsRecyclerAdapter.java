package com.example.seriestracker.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seriestracker.MessageActivity;
import com.example.seriestracker.R;
import com.example.seriestracker.user.Friend;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FriendsRecyclerAdapter extends RecyclerView.Adapter<FriendsRecyclerAdapter.WatchlistViewHolder> {
    private final Context context;
    private final List<Friend> items;


    public FriendsRecyclerAdapter(Context context, List<Friend> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @NotNull
    @Override
    public WatchlistViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.friend_card, parent, false);
        return new WatchlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull WatchlistViewHolder holder, int position) {
        Friend item = items.get(position);
        holder.friend.setText(item.getName());

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String language = sharedPref.getString("language", "Magyar");

        Button messageFriendButton = holder.messageFriendButton.findViewById(R.id.deleteMessageButton);

        if(language.equals("Magyar")){
            messageFriendButton.setText(context.getResources().getString(R.string.message_text_hu));
        }else{
            messageFriendButton.setText(context.getResources().getString(R.string.message_text));
        }

        holder.messageFriendButton.setOnClickListener(view -> {
            Intent messageActvity = new Intent(context, MessageActivity.class);
            messageActvity.putExtra("friend", holder.friend.getText().toString());
            context.startActivity(messageActvity);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class WatchlistViewHolder extends RecyclerView.ViewHolder {

        TextView friend;
        Button messageFriendButton;

        public WatchlistViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            friend = itemView.findViewById(R.id.senderName);
            messageFriendButton = itemView.findViewById(R.id.deleteMessageButton);
        }
    }
}
