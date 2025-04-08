package com.example.exe1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private Context context;
    private List<Post> postList;
    private OnPostClickListener listener;
    private List<Post> filteredPostList;

    public PostAdapter(List<Post> postList, Context context, OnPostClickListener listener) {
        this.postList = postList;
        this.context = context;
        this.listener = listener;
        this.filteredPostList = new ArrayList<>(postList);
    }


    // Code for searching
    public void filter(String query) {
        filteredPostList.clear();
        if (query.isEmpty()) {
            filteredPostList.addAll(postList);
        } else {
            for (Post post : postList) {
                if (post.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        post.getBody().trim().contains(query.toLowerCase())) {
                    filteredPostList.add(post);
                }
            }
        }
        notifyDataSetChanged();

    }



    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        //  View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);

        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);

        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostHolder holder, int position) {

        Post post = filteredPostList.get(position);

        holder.etTittle.setText(post.getTitle().toUpperCase());

        String str= post.getBody();
        String concat=str.substring(0,1).toUpperCase()+ str.substring(1,str.length()-1);
        holder.etContent.setText(concat);

        holder.itemView.setOnClickListener(V -> listener.OnPostClick(post));


    }

    @Override
    public int getItemCount() {
        return filteredPostList.size();
    }

    public interface OnPostClickListener {
        void OnPostClick(Post post);
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        TextView etTittle;
        TextView etContent;


        public PostHolder(@NonNull View itemView) {
            super(itemView);

            etTittle = itemView.findViewById(R.id.ettitle_tv);
            etContent = itemView.findViewById(R.id.etContent_tv);


        }
    }
}

