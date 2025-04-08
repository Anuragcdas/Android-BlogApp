package com.example.exe1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private List<Comment> commentList;

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.tvName.setText(comment.getName().toUpperCase());
        holder.tvEmail.setText(comment.getEmail());
        holder.tvBody.setText(comment.getBody());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    static class CommentHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvBody;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvBody = itemView.findViewById(R.id.tvBody);
        }
    }
}
