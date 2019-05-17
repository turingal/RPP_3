package com.example.lab3_1.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lab3_1.R;

import java.util.ArrayList;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewAdapter>  {

    private int itemCounts;
    private ArrayList<String> names;

    public CardAdapter(int n, ArrayList<String> names){
        this.itemCounts = n;
        this.names = names;
    }

    @NonNull
    @Override
    public CardViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_view, parent, false);
        return new CardViewAdapter(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewAdapter holder, int position) {
        TextView text = holder.text;
        text.setText(this.names.get(position));
    }

    @Override
    public int getItemCount() {
        return itemCounts;
    }


    class CardViewAdapter extends RecyclerView.ViewHolder {

        public View linearLayout;
        private ImageView image;
        private TextView text;

        public CardViewAdapter(View itemView, int number) {
            super(itemView);
            image = itemView.findViewById(R.id.my_image);
            text = itemView.findViewById(R.id.my_text);
            linearLayout = itemView.findViewById(R.id.line);
        }

    }
}
