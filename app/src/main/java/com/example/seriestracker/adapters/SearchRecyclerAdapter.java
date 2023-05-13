package com.example.seriestracker.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
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
import com.example.seriestracker.R;
import com.example.seriestracker.firebaseapi.FB;
import com.example.seriestracker.imdbapi.models.SearchResult;
import com.example.seriestracker.user.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.SearchViewHolder> {
    private final Context context;
    private final List<SearchResult> items;


    public SearchRecyclerAdapter(Context context, List<SearchResult> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @NotNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchViewHolder holder, int position) {
        SearchResult item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        Glide.with(context).load(item.getImage()).apply(new RequestOptions().override(300, 400)).into(holder.image);

        Button watchlistButton = holder.watchlistButton.findViewById(R.id.WatchlistAddButton);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String language = sharedPref.getString("language", "Magyar");

        if(language.equals("Magyar")){
            watchlistButton.setText(context.getResources().getString(R.string.watchlist_button_hu));
        }else{
            watchlistButton.setText(context.getResources().getString(R.string.watchlist_button));
        }


        watchlistButton.setOnClickListener(v -> {
            new FB().addContent(context, User.userLoggedIn(context), item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView title, description;
        ImageView image;
        Button watchlistButton;

        public SearchViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.senderName);
            description = itemView.findViewById(R.id.FriendDescriptionCardText);
            image = itemView.findViewById(R.id.imageCard);
            watchlistButton = itemView.findViewById(R.id.WatchlistAddButton);

            String userLoggedIn = User.userLoggedIn(context);

            if (TextUtils.isEmpty(userLoggedIn)){
                watchlistButton.setEnabled(false);
            }
        }
    }
}
