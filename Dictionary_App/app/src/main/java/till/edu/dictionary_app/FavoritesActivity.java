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

import till.edu.dictionary_app.adapters.FavoriteWordAdapter;
import till.edu.dictionary_app.database.DatabaseHelper;
import till.edu.dictionary_app.models.WordEntry;

public class FavoritesActivity extends AppCompatActivity {
    private static final String TAG = "HoatDongYeuThich"; // TAG -> HoatDongYeuThich

    private RecyclerView danhSachYeuThichRecycler; // rvFavorites -> danhSachYeuThichRecycler
    private TextView tvKhongCoTuYeuThich; // tvEmptyFavorites -> tvKhongCoTuYeuThich
    private ImageButton nutQuayLaiTuYeuThich; // btnBackFromFavorites -> nutQuayLaiTuYeuThich

    private DatabaseHelper boTroCSDL; // databaseHelper -> boTroCSDL (giữ nhất quán với MainActivity)
    private FavoriteWordAdapter boDieuPhoiTuYeuThich; // favoritesAdapter -> boDieuPhoiTuYeuThich
    private List<WordEntry> cacTuYeuThich = new ArrayList<>(); // favoriteWords -> cacTuYeuThich

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Ánh xạ View (đã đổi tên)
        danhSachYeuThichRecycler = findViewById(R.id.rvFavorites);
        tvKhongCoTuYeuThich = findViewById(R.id.tvEmptyFavorites);
        nutQuayLaiTuYeuThich = findViewById(R.id.btnBackFromFavorites);

        // Khởi tạo DatabaseHelper
        boTroCSDL = new DatabaseHelper(this);

        // Cấu hình RecyclerView
        danhSachYeuThichRecycler.setLayoutManager(new LinearLayoutManager(this));
        // KHỞI TẠO ĐÚNG FAVORITEWORDADAPTER VỚI HAI ĐỐI SỐ (đã đổi tên biến)
        boDieuPhoiTuYeuThich = new FavoriteWordAdapter(this, cacTuYeuThich);
        danhSachYeuThichRecycler.setAdapter(boDieuPhoiTuYeuThich);

        // Xử lý nút Back
        nutQuayLaiTuYeuThich.setOnClickListener(v -> finish());

        // Xử lý sự kiện click cho từng item trong RecyclerView
        boDieuPhoiTuYeuThich.setOnItemClickListener(mucTu -> { // wordEntry -> mucTu (tương tự như WordDetailActivity)
            Intent intent = new Intent(FavoritesActivity.this, WordDetailActivity.class);
            intent.putExtra("word_text", mucTu.getWord());
            intent.putExtra("description_text", mucTu.getDescription());
            intent.putExtra("pronounce_text", mucTu.getPronounce());
            intent.putExtra("html_content", mucTu.getHtml());
            intent.putExtra("is_english_to_vietnamese", mucTu.isEnglishToVietnamese());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        taiCacTuYeuThich(); // Tải lại danh sách yêu thích mỗi khi Activity được hiển thị lại (đã đổi tên hàm)
    }


    private void taiCacTuYeuThich() { // loadFavoriteWords -> taiCacTuYeuThich
        // Xóa danh sách cũ (đã đổi tên biến)
        cacTuYeuThich.clear();
        // Lấy danh sách từ yêu thích từ database (đã đổi tên biến)
        List<WordEntry> cacTuYeuThichMoi = boTroCSDL.getAllFavorites(); // newFavorites -> cacTuYeuThichMoi
        if (cacTuYeuThichMoi != null) {
            cacTuYeuThich.addAll(cacTuYeuThichMoi);
        }

        // Cập nhật Adapter
        boDieuPhoiTuYeuThich.notifyDataSetChanged();

        // Hiển thị thông báo khi danh sách trống (đã đổi tên biến)
        if (cacTuYeuThich.isEmpty()) {
            danhSachYeuThichRecycler.setVisibility(View.GONE);
            tvKhongCoTuYeuThich.setVisibility(View.VISIBLE);
        } else {
            danhSachYeuThichRecycler.setVisibility(View.VISIBLE);
            tvKhongCoTuYeuThich.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        // Đã đổi tên biến
        if (boTroCSDL != null) {
            boTroCSDL.close();
            Log.d(TAG, "DatabaseHelper closed from FavoritesActivity.");
        }
        super.onDestroy();
    }
}