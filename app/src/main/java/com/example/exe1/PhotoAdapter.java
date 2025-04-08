package com.example.exe1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {


    private List<Photo> photoList;
    private Context context;
    private OnPhotoClickListner listner;

    public PhotoAdapter(Context context, List<Photo> photoList, OnPhotoClickListner listner) {
        this.context = context;
        this.photoList = photoList;
        this.listner = listner;


    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.photo_item, parent, false);
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        Photo photo = photoList.get(position);

        String s=photo.getTitle();
        String concat= s.substring(0,1).toUpperCase()+ s.substring(1,s.length());
        holder.PhotoTitle.setText(concat);

        Glide.with(holder.itemView.getContext())
                .load(photo.getThumbnail())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.errorimage)
                .into(holder.Thumnailurl);
        holder.itemView.setOnClickListener(V -> listner.onPhotoClick(photo));


    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public interface OnPhotoClickListner {
        void onPhotoClick(Photo photo);
    }

    public class PhotoHolder extends RecyclerView.ViewHolder {
        TextView PhotoTitle;
        ImageView Thumnailurl;

        public PhotoHolder(@NonNull View itemView) {
            super(itemView);

            PhotoTitle = itemView.findViewById(R.id.PhotoTitle);
            Thumnailurl = itemView.findViewById(R.id.imageViewThumbnail);
        }
    }
}
