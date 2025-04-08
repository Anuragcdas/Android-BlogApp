package com.example.exe1;

public class Post {
    private int id;  // This should be the post ID
    private int userId;
    private String title;
    private String body;

    public Post(int id, int userId, String title, String body) {
        this.id = id;  // Ensure 'id' is the post ID, not user ID
        this.userId = userId;
        this.title = title;
        this.body = body;
    }

    public int getId() {
        return id;  // ✅ This should return the post ID
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
