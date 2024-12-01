package com.example.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class WebScraperTask extends AsyncTask<Void, Void, List<Restaurant>> {
    private Context context;
    private RestaurantAdapter adapter;
    private List<Restaurant> restaurantList;
    private int page;
    private boolean addToTop;

    public WebScraperTask(Context context, RestaurantAdapter adapter, List<Restaurant> restaurantList, int page) {
        this.context = context;
        this.adapter = adapter;
        this.restaurantList = restaurantList;
        this.page = page;
    }

    public WebScraperTask(Context context, RestaurantAdapter adapter, List<Restaurant> restaurantList, int page, boolean addToTop) {
        this.context = context;
        this.adapter = adapter;
        this.restaurantList = restaurantList;
        this.page = page;
        this.addToTop = addToTop;
    }

    protected List<Restaurant> doInBackground(Void... voids) {
        List<Restaurant> newData = new ArrayList<>();
        try {
            // Xây dựng URL trang để lấy dữ liệu
//            String url = "https://www.foody.vn/ho-chi-minh/nha-hang?page=" +page;
            String url="https://pasgo.vn/tim-kiem?search="+page;

            Document document = Jsoup.connect(url).get();
//            System.out.println(document.html());
            //  Duyệt qua các phần tử nhà hàng
            for (Element element : document.select(".item-child")) {
                String name = element.select(".item-child-headline").text();
                String address = element.select(".item-child-text-address").text();
                String rating =element.select(".danhgia-ct span").text();
                String imageUrl = element.select(".item-link-image img").attr("data-src");
                String description= element.select(".item-sale.overflow-ellipsis-one").text()
                        +"\n"+element.select(".item-tag.overflow-ellipsis-one").text();
                // Lấy URL của nhà hàng
                String restaurantUrl = element.select(".item-link-image").attr("href");

                // Kiểm tra nếu URL là đường dẫn tương đối và thêm domain vào
                if (!restaurantUrl.startsWith("http://") || !restaurantUrl.startsWith("https://")) {
                    String baseUrl = url;  // Base URL của trang
                    restaurantUrl = baseUrl + restaurantUrl; // Tạo thành URL tuyệt đối
                }

                // Tạo đối tượng Restaurant và thêm vào danh sách
                Restaurant restaurant = new Restaurant(name, address, rating, imageUrl, restaurantUrl);
                restaurant.setSummary(description);
                newData.add(restaurant);
            }
//            // Đặt đường dẫn đến msedgedriver.exe



        } catch (IOException e) {
            e.printStackTrace();
        }
        return newData;
    }

    @Override
    protected void onPostExecute(List<Restaurant> newData) {
        if (newData != null && !newData.isEmpty()) {
            if (addToTop) {
                restaurantList.addAll(0, newData); // Thêm vào đầu danh sách
            } else {
                restaurantList.addAll(newData); // Thêm vào cuối danh sách
            }

            // Xóa các mục trùng lặp
            Set<Restaurant> uniqueRestaurants = new LinkedHashSet<>(restaurantList);
            restaurantList.clear();
            restaurantList.addAll(uniqueRestaurants);

            // Cập nhật RecyclerView
            adapter.notifyDataSetChanged();
        } else {
            // Không còn dữ liệu để tải
            Toast.makeText(context, "Đã tải hết dữ liệu", Toast.LENGTH_SHORT).show();
            ((MainActivity) context).setHasMoreData(false);
        }

        ((MainActivity) context).setIsLoading(false); // Đặt lại trạng thái không tải
    }
}
