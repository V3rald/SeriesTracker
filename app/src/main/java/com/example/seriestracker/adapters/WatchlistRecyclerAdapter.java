package com.example.seriestracker.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.seriestracker.EditMediaActivity;
import com.example.seriestracker.R;
import com.example.seriestracker.firebaseapi.FB;
import com.example.seriestracker.imdbapi.models.SearchResult;
import com.example.seriestracker.user.User;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WatchlistRecyclerAdapter extends RecyclerView.Adapter<WatchlistRecyclerAdapter.WatchlistViewHolder> {
    private final Context context;
    private final List<SearchResult> items;


    public WatchlistRecyclerAdapter(Context context, List<SearchResult> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @NotNull
    @Override
    public WatchlistViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.watchlist_card, parent, false);
        return new WatchlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull WatchlistViewHolder holder, int position) {
        SearchResult item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        Glide.with(context).load(item.getImage()).apply(new RequestOptions().override(300, 400)).into(holder.image);
        holder.rating.setText(item.getRating() + " / 10");

        Button editButton = holder.editWatchlist.findViewById(R.id.editWatchlistButton);
        Button RemoveButton = holder.removeWatchlist.findViewById(R.id.deleteMessageButton);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String language = sharedPref.getString("language", "Magyar");

        if(language.equals("Magyar")){
            editButton.setText(context.getResources().getString(R.string.edit_button_hu));
            RemoveButton.setText(context.getResources().getString(R.string.remove_button_hu));

            if(item.getStatus().equals("PTW")){
                holder.status.setText(context.getResources().getString(R.string.ptw_hu));
            }else if(item.getStatus().equals("C")){
                holder.status.setText(context.getResources().getString(R.string.c_hu));
            }else{
                holder.status.setText(context.getResources().getString(R.string.w_hu));
            }
        }else{
            editButton.setText(context.getResources().getString(R.string.edit_button));
            RemoveButton.setText(context.getResources().getString(R.string.remove_button));

            if(item.getStatus().equals("PTW")){
                holder.status.setText(context.getResources().getString(R.string.ptw));
            }else if(item.getStatus().equals("C")){
                holder.status.setText(context.getResources().getString(R.string.c));
            }else{
                holder.status.setText(context.getResources().getString(R.string.w));
            }
        }
        
        holder.removeWatchlist.setOnClickListener(view -> {
            new FB().removeContent(context, User.userLoggedIn(context), item.getId());
            items.remove(position);
            notifyDataSetChanged();
        });

        holder.editWatchlist.setOnClickListener(view -> {
            Intent editMediaActivity = new Intent(context, EditMediaActivity.class);
            editMediaActivity.putExtra("media", new Gson().toJson(item));
            context.startActivity(editMediaActivity);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class WatchlistViewHolder extends RecyclerView.ViewHolder {

        TextView title, description, status, rating;
        ImageView image;
        Button removeWatchlist;
        Button editWatchlist;

        public WatchlistViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.senderName);
            description = itemView.findViewById(R.id.FriendDescriptionCardText);
            status = itemView.findViewById(R.id.statusText);
            rating = itemView.findViewById(R.id.ratingText);
            image = itemView.findViewById(R.id.imageCard);
            removeWatchlist = itemView.findViewById(R.id.deleteMessageButton);
            editWatchlist = itemView.findViewById(R.id.editWatchlistButton);
        }
    }
}
