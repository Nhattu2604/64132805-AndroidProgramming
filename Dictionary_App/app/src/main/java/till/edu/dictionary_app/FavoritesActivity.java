package till.edu.dictionary_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import till.edu.dictionary_app.adapters.FavoriteWordAdapter; // <-- DÒNG NÀY PHẢI LÀ FavoriteWordAdapter
import till.edu.dictionary_app.database.DatabaseHelper;
import till.edu.dictionary_app.models.WordEntry;

public class FavoritesActivity extends AppCompatActivity {

    private static final String TAG = "FavoritesActivity";

    private RecyclerView rvFavorites;
    private TextView tvEmptyFavorites;
    private ImageButton btnBackFromFavorites;

    private DatabaseHelper databaseHelper;
    private FavoriteWordAdapter favoritesAdapter; // <-- DÒNG NÀY PHẢI LÀ FavoriteWordAdapter
    private List<WordEntry> favoriteWords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Ánh xạ View
        rvFavorites = findViewById(R.id.rvFavorites);
        tvEmptyFavorites = findViewById(R.id.tvEmptyFavorites);
        btnBackFromFavorites = findViewById(R.id.btnBackFromFavorites);

        // Khởi tạo DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Cấu hình RecyclerView
        rvFavorites.setLayoutManager(new LinearLayoutManager(this));
        // KHỞI TẠO ĐÚNG FAVORITEWORDADAPTER VỚI HAI ĐỐI SỐ
        favoritesAdapter = new FavoriteWordAdapter(this, favoriteWords); // <-- DÒNG NÀY PHẢI NHƯ THẾ NÀY
        rvFavorites.setAdapter(favoritesAdapter);

        // Xử lý nút Back
        btnBackFromFavorites.setOnClickListener(v -> finish());

        // Xử lý sự kiện click cho từng item trong RecyclerView
        favoritesAdapter.setOnItemClickListener(wordEntry -> {
            Intent intent = new Intent(FavoritesActivity.this, WordDetailActivity.class);
            intent.putExtra("word_text", wordEntry.getWord());
            intent.putExtra("description_text", wordEntry.getDescription());
            intent.putExtra("pronounce_text", wordEntry.getPronounce());
            intent.putExtra("html_content", wordEntry.getHtml());
            intent.putExtra("is_english_to_vietnamese", wordEntry.isEnglishToVietnamese()); // Giờ WordEntry đã có trường này
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavoriteWords(); // Tải lại danh sách yêu thích mỗi khi Activity được hiển thị lại
    }

    private void loadFavoriteWords() {
        // Xóa danh sách cũ
        favoriteWords.clear();
        // Lấy danh sách từ yêu thích từ database
        List<WordEntry> newFavorites = databaseHelper.getAllFavorites();
        if (newFavorites != null) {
            favoriteWords.addAll(newFavorites);
        }

        // Cập nhật Adapter
        favoritesAdapter.notifyDataSetChanged();

        // Hiển thị thông báo khi danh sách trống
        if (favoriteWords.isEmpty()) {
            rvFavorites.setVisibility(View.GONE);
            tvEmptyFavorites.setVisibility(View.VISIBLE);
        } else {
            rvFavorites.setVisibility(View.VISIBLE);
            tvEmptyFavorites.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        if (databaseHelper != null) {
            databaseHelper.close();
            Log.d(TAG, "DatabaseHelper closed from FavoritesActivity.");
        }
        super.onDestroy();
    }
}