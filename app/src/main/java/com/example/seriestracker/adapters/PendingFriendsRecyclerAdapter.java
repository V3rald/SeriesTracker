package com.example.seriestracker.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seriestracker.R;
import com.example.seriestracker.firebaseapi.FB;
import com.example.seriestracker.user.Friend;
import com.example.seriestracker.user.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PendingFriendsRecyclerAdapter extends RecyclerView.Adapter<PendingFriendsRecyclerAdapter.WatchlistViewHolder> {
    private final Context context;
    private final List<Friend> items;


    public PendingFriendsRecyclerAdapter(Context context, List<Friend> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @NotNull
    @Override
    public WatchlistViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pending_card, parent, false);
        return new WatchlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull WatchlistViewHolder holder, int position) {
        Friend item = items.get(position);
        holder.friend.setText(item.getName());

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String language = sharedPref.getString("language", "Magyar");

        Button messageFriendButton = holder.acceptFriendButton.findViewById(R.id.deleteMessageButton);

        if(language.equals("Magyar")){
            messageFriendButton.setText(context.getResources().getString(R.string.accept_text_hu));
            holder.description.setText(context.getResources().getString(R.string.number_of_text_hu) + ": " + item.getNumberOfMatches());
        }else{
            messageFriendButton.setText(context.getResources().getString(R.string.accept_text));
            holder.description.setText(context.getResources().getString(R.string.number_of_text) + ": " + item.getNumberOfMatches());
        }

        holder.acceptFriendButton.setOnClickListener(view -> {
            new FB().acceptFriend(context, User.userLoggedIn(context), item);
            items.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class WatchlistViewHolder extends RecyclerView.ViewHolder {

        TextView friend, description;
        Button acceptFriendButton;

        public WatchlistViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            friend = itemView.findViewById(R.id.senderName);
            description = itemView.findViewById(R.id.FriendDescriptionCardText);
            acceptFriendButton = itemView.findViewById(R.id.deleteMessageButton);
        }
    }
}
