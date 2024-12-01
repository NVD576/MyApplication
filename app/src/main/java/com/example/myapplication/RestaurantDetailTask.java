package com.example.myapplication;

import android.content.Context;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailTask extends AsyncTask<Void, Void, Restaurant> {
    private String url;
    Restaurant re;
    private Context context;
    private OnDetailLoadedListener listener;

    public interface OnDetailLoadedListener {
        void onDetailLoaded(Restaurant detail);
    }

    public RestaurantDetailTask(String url,Restaurant re, Context context, OnDetailLoadedListener listener) {
        this.url = url;
        this.re= re;
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected Restaurant doInBackground(Void... voids) {
        try {
            Document document = Jsoup.connect(url).get();

            // Lấy thông tin chi tiết
//            System.out.println(document.html());
            // Lấy danh sách bình luận

//            System.out.println("description:" +description);
//            System.out.println(document.html());
            List<UserComment> comments = new ArrayList<>();
            for (Element reviewElement : document.select("._3-8y._5nz1.clearfix")) {  // Thay .comment-item bằng .review-item
                // Lấy tên người dùng

                String user = reviewElement.select(". UFICommentActorName").text();  // Thay .user-name bằng .ru-username

                // Lấy nội dung bình luận
                String commentText = reviewElement.select("._5mdd").text();  // Thay .comment-content bằng .rd-des

                // Lấy thời gian bình luận
                String date = reviewElement.select(".uiLinkSubtle a").text();  // Có thể thay .comment-time bằng .ru-time hoặc ngược lại

                // Thêm vào danh sách bình luận
                comments.add(new UserComment(user, commentText, date));
            }

            // Trả về dữ liệu
            Restaurant rs= new Restaurant(re.getName(),re.getAddress(),re.getRating(), re.getImageUrl(), re.getUrl(),comments);

            return rs;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Restaurant detail) {
        if (listener != null) {
            listener.onDetailLoaded(detail);
        }
    }
}
