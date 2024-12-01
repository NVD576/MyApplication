package com.example.myapplication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RestaurantAdapter adapter;
    private List<Restaurant> restaurantList;
    private int currentPage = 1; // Trang hiện tại
    private boolean isLoading = false;
    private boolean hasMoreData = true;
    WebScraperTask webScraperTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Set padding to avoid content getting under system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the list and adapter
        restaurantList = new ArrayList<>();
        adapter = new RestaurantAdapter(restaurantList, this);
        recyclerView.setAdapter(adapter);

        // Start WebScraperTask to load data initially
        webScraperTask = new WebScraperTask(this, adapter, restaurantList, currentPage);
        webScraperTask.execute();  // Execute task to scrape data

        // Add ScrollListener to load more data when user scrolls to the end
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int totalItemCount = layoutManager.getItemCount();
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                    // Kiểm tra nếu người dùng cuộn gần đến cuối và còn dữ liệu
                    if (!isLoading && hasMoreData && totalItemCount <= (lastVisibleItemPosition + 5)) {
                        loadMoreData(adapter, restaurantList);
                    }

                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    // Nếu người dùng cuộn đến đầu danh sách
                    if (!isLoading && firstVisibleItemPosition == 0) {
                        loadPreviousData(adapter, restaurantList); // Tải thêm dữ liệu mới
                    }
                }
            }
        });

    }


    private void loadMoreData(RestaurantAdapter adapter, List<Restaurant> restaurantList) {
        if (isLoading) return; // Ngăn việc tải liên tiếp
        isLoading = true;
        // Tăng trang lên 1 sau khi gọi
        currentPage++;
        // Gọi WebScraperTask với tham số trang
        new WebScraperTask(this, adapter, restaurantList, currentPage).execute();


    }

    private void loadPreviousData(RestaurantAdapter adapter, List<Restaurant> restaurantList) {
        if (isLoading) return; // Ngăn việc tải liên tiếp
        isLoading = true;

        // Giảm số trang để lấy nội dung trước
        currentPage++;


        // Gọi WebScraperTask để lấy nội dung
        new WebScraperTask(this, adapter, restaurantList, currentPage, true).execute();
    }


    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public void setHasMoreData(boolean hasMoreData) {
        this.hasMoreData = hasMoreData;
        if (!hasMoreData) {
            // Vô hiệu hóa cuộn khi không còn dữ liệu
            recyclerView.setNestedScrollingEnabled(false);
        }
    }
}
