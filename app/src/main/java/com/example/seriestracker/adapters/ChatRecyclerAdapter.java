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
import com.example.seriestracker.firebaseapi.FB;
import com.example.seriestracker.user.Chat;
import com.example.seriestracker.user.User;
import com.google.type.DateTime;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.WatchlistViewHolder> {
    private final Context context;
    private final List<Chat> items;


    public ChatRecyclerAdapter(Context context, List<Chat> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @NotNull
    @Override
    public WatchlistViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.chat_card, parent, false);
        return new WatchlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull WatchlistViewHolder holder, int position) {
        Chat item = items.get(position);
        holder.sender.setText(item.getUser());
        holder.text.setText(item.getText());
        holder.dateText.setText(item.getDate());

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String language = sharedPref.getString("language", "Magyar");

        Button deleteMessageButton = holder.deleteMessageButton.findViewById(R.id.deleteMessageButton);

        if(User.userLoggedIn(context).equals(item.getFriend())){
            deleteMessageButton.setVisibility(View.INVISIBLE);
        }

        if(language.equals("Magyar")){
            deleteMessageButton.setText(context.getResources().getString(R.string.remove_button_hu));
        }else{
            deleteMessageButton.setText(context.getResources().getString(R.string.remove_button));
        }

        holder.deleteMessageButton.setOnClickListener(view -> {
            new FB().deleteMessage(context, item);
            items.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class WatchlistViewHolder extends RecyclerView.ViewHolder {

        TextView sender;
        TextView text;
        TextView dateText;
        Button deleteMessageButton;

        public WatchlistViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            sender = itemView.findViewById(R.id.senderName);
            text = itemView.findViewById(R.id.chatting_text);
            dateText = itemView.findViewById(R.id.dateText);
            deleteMessageButton = itemView.findViewById(R.id.deleteMessageButton);
        }
    }
}
