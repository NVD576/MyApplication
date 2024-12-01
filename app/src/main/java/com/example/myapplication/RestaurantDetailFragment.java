package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RestaurantDetailFragment extends Fragment {
    private RecyclerView commentsRecyclerView;
    private EditText commentEditText;
    private Button submitCommentButton;
    private CommentsAdapter commentsAdapter;
    private List<UserComment> commentsList = new ArrayList<>();
    ImageView imgRestaurant;
    TextView txtResName, txtResAddress, txtResRating;
    Restaurant rs;
    TextView txtSummary;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);

        imgRestaurant= view.findViewById(R.id.imgRestaurant);
        txtResName= view.findViewById(R.id.txtResName);
        txtResAddress= view.findViewById(R.id.txtResAddress);
        txtResRating= view.findViewById(R.id.txtResRating);

        commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView);
        commentEditText = view.findViewById(R.id.commentEditText);
        submitCommentButton = view.findViewById(R.id.submitCommentButton);

        // Set up RecyclerView
        commentsAdapter = new CommentsAdapter(commentsList);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentsRecyclerView.setAdapter(commentsAdapter);

        txtSummary = view.findViewById(R.id.txtSummary);
        TextView txtToggle = view.findViewById(R.id.txtToggle);
        //chức năng mở rộng text
        txtToggle.setOnClickListener(v -> {
            if (txtToggle.getText().toString().equals("Xem thêm")) {
                txtSummary.setMaxLines(Integer.MAX_VALUE); // Mở rộng toàn bộ nội dung
                txtToggle.setText("Thu gọn");
            } else {
                txtSummary.setMaxLines(3); // Thu gọn lại 3 dòng
                txtToggle.setText("Xem thêm");
            }
        });
        // Load restaurant details
        String url = getArguments().getString("url");
        String name= getArguments().getString("name");
        String address= getArguments().getString("address");
        String imageurl= getArguments().getString("image");
        String rating= "Đánh giá: " +getArguments().getString("rating");
        String des= getArguments().getString("description");
        txtResName.setText(name);
        txtResAddress.setText(address);
        txtResRating.setText(rating);
        txtSummary.setText(des);
//        txtSummary.setText(rs.getSummary());
        // Sử dụng Picasso để tải hình ảnh từ URL
        Picasso.get()
                .load(imageurl) // URL của hình ảnh
                .into(imgRestaurant);

//        rs =new Restaurant(name,address,rating,imageurl);

        new RestaurantDetailTask(url,rs,getContext(), detail -> {
            if (detail != null) {

                commentsList.addAll(detail.getUserComments());
                commentsAdapter.notifyDataSetChanged();
//                Log.e("rs",detail.getDescription());
            } else {
                // Handle null detail, maybe show a default message or error
            }
        }).execute();


        // Handle comment submission
        submitCommentButton.setOnClickListener(v -> {
            String newComment = commentEditText.getText().toString().trim();
            if (!newComment.isEmpty()) {
                String currentTime = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault()).format(new Date());

                // Thêm bình luận vào danh sách
                commentsList.add(new UserComment("Bạn", newComment, currentTime));
                commentsAdapter.notifyDataSetChanged();
                commentEditText.setText("");
                rs.setUserComments(commentsList);
                // Lưu bình luận vào cơ sở dữ liệu (nếu cần)
                saveCommentToDatabase(rs);
            }
        });



        return view;
    }

    public static RestaurantDetailFragment newInstance(String name, String address, String image, String rating, String url, String description) {
        RestaurantDetailFragment fragment = new RestaurantDetailFragment();
        Bundle args = new Bundle();
        args.putString("name",name);
        args.putString("address",address);
        args.putString("image",image);
        args.putString("rating",rating);
        args.putString("url", url);
        args.putString("description", description);
        fragment.setArguments(args);
        return fragment;
    }

    private void saveCommentToDatabase(Restaurant rs) {
        // Triển khai lưu bình luận vào cơ sở dữ liệu (SQLite, Firebase, v.v.)
    }
}
