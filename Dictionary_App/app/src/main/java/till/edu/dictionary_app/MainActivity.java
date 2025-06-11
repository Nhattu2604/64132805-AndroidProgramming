package till.edu.dictionary_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.Html; // Import Html để hiển thị HTML
import android.view.View;
// import android.widget.ArrayAdapter; // Không cần ArrayAdapter mặc định nữa
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

// Đảm bảo import đúng các class bạn đã tạo/sửa
import till.edu.dictionary_app.database.DatabaseHelper;
import till.edu.dictionary_app.models.WordEntry;
import till.edu.dictionary_app.adapters.WordSuggestionAdapter; // <-- CHỈNH SỬA IMPORT NÀY

public class MainActivity extends AppCompatActivity {

    private EditText etSearch;
    private ListView listViewWords;
    // Thay đổi từ ArrayList<String> thành ArrayList<WordEntry>
    private ArrayList<WordEntry> currentSuggestions;
    // Thay đổi từ ArrayAdapter<String> thành WordSuggestionAdapter
    private WordSuggestionAdapter suggestionAdapter;

    private DatabaseHelper databaseHelper;

    private ImageButton btnSearchBottom, btnHistoryBottom, btnFavoritesBottom, btnTranslateBottom;
    private TextView tvEnglish, tvVietnamese;
    private View ivSwap;

    private boolean isEnglishToVietnamese = true; // Cờ để xác định chiều dịch

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view
        etSearch = findViewById(R.id.etSearch);
        listViewWords = findViewById(R.id.listViewWords);

        btnSearchBottom = findViewById(R.id.btnSearch);
        btnHistoryBottom = findViewById(R.id.btnHistory);
        btnFavoritesBottom = findViewById(R.id.btnFavorites);

        tvEnglish = findViewById(R.id.tvEnglish);
        tvVietnamese = findViewById(R.id.tvVietnamese);
        ivSwap = findViewById(R.id.ivSwap);

        databaseHelper = new DatabaseHelper(this); // Khởi tạo DatabaseHelper

        // Tạo database (nếu chưa có) và mở nó
        try {
            databaseHelper.createDatabase();
            databaseHelper.openDatabase();
            Log.d("MainActivity", "Database opened successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi tạo/mở database: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("MainActivity", "Error creating/opening database", e);
        }

        // --- SỬA ĐỔI CHÍNH TẠI ĐÂY ---
        // Thay đổi kiểu dữ liệu và khởi tạo adapter
        currentSuggestions = new ArrayList<>();
        // Sử dụng WordSuggestionAdapter với layout tùy chỉnh của bạn (R.layout.item_word_suggestion)
        suggestionAdapter = new WordSuggestionAdapter(this, R.layout.item_word_suggestion, currentSuggestions);
        listViewWords.setAdapter(suggestionAdapter);

        // Tải gợi ý mặc định khi Activity được tạo lần đầu
        loadDefaultSuggestions();
        // --- KẾT THÚC SỬA ĐỔI CHÍNH TẠI ĐÂY ---

        // listViewWords.setVisibility(View.GONE); // Dòng này không cần thiết nữa nếu bạn muốn hiển thị gợi ý mặc định ngay

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Gọi AsyncTask để lọc gợi ý từ database
                if (s.length() > 0) { // Chỉ gợi ý khi có ký tự nhập vào
                    filterSuggestionsFromDatabase(s.toString(), isEnglishToVietnamese);
                } else { // Ô tìm kiếm trống, tải lại gợi ý mặc định
                    loadDefaultSuggestions();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Sửa đổi OnItemClickListener để lấy WordEntry từ adapter
        listViewWords.setOnItemClickListener((parent, view, position, id) -> {
            WordEntry selectedEntry = currentSuggestions.get(position); // Lấy đối tượng WordEntry
            String selectedWord = selectedEntry.getWord(); // Lấy từ từ WordEntry

            etSearch.setText(selectedWord);
            etSearch.setSelection(selectedWord.length()); // Đặt con trỏ về cuối
            listViewWords.setVisibility(View.GONE); // Ẩn danh sách gợi ý sau khi chọn
            traTu(selectedWord); // Tra từ từ database
        });

        // Xử lý sự kiện cho nút Search dưới cùng (btnSearchBottom)
        btnSearchBottom.setOnClickListener(v -> {
            String word = etSearch.getText().toString().trim();
            if (word.isEmpty()) {
                Toast.makeText(MainActivity.this, "Vui lòng nhập từ cần tra", Toast.LENGTH_SHORT).show();
                return;
            }
            listViewWords.setVisibility(View.GONE); // Ẩn gợi ý khi search
            traTu(word); // Tra từ từ database
        });

        // --- SỬA ĐỔI CHÍNH TẠI ĐÂY ---
        // Nút Swap (trên header) để đổi chiều dịch
        ivSwap.setOnClickListener(v -> {
            isEnglishToVietnamese = !isEnglishToVietnamese;
            updateLanguageDisplay(); // Cập nhật giao diện ngôn ngữ
            etSearch.setText(""); // Xóa từ đang nhập
            // Tải lại gợi ý mặc định khi đổi chiều dịch
            loadDefaultSuggestions();
            Toast.makeText(this, isEnglishToVietnamese ? "Từ điển Anh -> Việt" : "Từ điển Việt -> Anh", Toast.LENGTH_SHORT).show();
        });
        // --- KẾT THÚC SỬA ĐỔI CHÍNH TẠI ĐÂY ---


        // Xử lý sự kiện cho các nút điều hướng dưới cùng (chưa triển khai chức năng)
        btnHistoryBottom.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng Lịch sử chưa triển khai", Toast.LENGTH_SHORT).show();
        });
        btnFavoritesBottom.setOnClickListener(v -> {
            // Thay vì Toast, hãy khởi tạo Intent để mở FavoritesActivity
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intent); // Bắt đầu FavoritesActivity
        });

        updateLanguageDisplay(); // Cập nhật giao diện ngôn ngữ ban đầu
    }

    // Đóng database khi activity bị hủy
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
            Log.d("MainActivity", "Database closed.");
        }
    }

    // Hàm cập nhật giao diện hiển thị ngôn ngữ và hint
    private void updateLanguageDisplay() {
        if (isEnglishToVietnamese) {
            tvEnglish.setTextColor(getResources().getColor(android.R.color.white));
            tvVietnamese.setTextColor(getResources().getColor(android.R.color.darker_gray));
            etSearch.setHint("Tìm từ tiếng Anh");
        } else {
            tvEnglish.setTextColor(getResources().getColor(android.R.color.darker_gray));
            tvVietnamese.setTextColor(getResources().getColor(android.R.color.white));
            etSearch.setHint("Tìm từ tiếng Việt");
        }
    }

    // AsyncTask để lọc gợi ý từ database trong background (đã sửa ở bước trước)
    private void filterSuggestionsFromDatabase(String text, boolean isEngToViet) {
        new AsyncTask<String, Void, List<WordEntry>>() {
            @Override
            protected List<WordEntry> doInBackground(String... params) {
                String prefix = params[0];
                if (databaseHelper == null) return new ArrayList<>();
                if (isEngToViet) {
                    return databaseHelper.getEnglishSuggestions(prefix);
                } else {
                    return databaseHelper.getVietnameseSuggestions(prefix);
                }
            }

            @Override
            protected void onPostExecute(List<WordEntry> result) {
                currentSuggestions.clear();
                if (result != null) {
                    currentSuggestions.addAll(result);
                }

                if (!currentSuggestions.isEmpty()) {
                    suggestionAdapter.notifyDataSetChanged();
                    listViewWords.setVisibility(View.VISIBLE);
                } else {
                    listViewWords.setVisibility(View.GONE);
                }
            }
        }.execute(text);
    }

    // --- THÊM PHƯƠNG THỨC MỚI NÀY VÀO MainActivity ---
    // AsyncTask để tải các gợi ý mặc định khi ô tìm kiếm trống
    private void loadDefaultSuggestions() {
        new AsyncTask<Void, Void, List<WordEntry>>() {
            @Override
            protected List<WordEntry> doInBackground(Void... voids) {
                if (databaseHelper == null) return new ArrayList<>();
                // Sử dụng phương thức getDefaultSuggestions đã thêm vào DatabaseHelper
                return databaseHelper.getDefaultSuggestions(isEnglishToVietnamese);
            }

            @Override
            protected void onPostExecute(List<WordEntry> result) {
                currentSuggestions.clear();
                if (result != null) {
                    currentSuggestions.addAll(result);
                }

                if (!currentSuggestions.isEmpty()) {
                    suggestionAdapter.notifyDataSetChanged();
                    listViewWords.setVisibility(View.VISIBLE); // Hiển thị ListView
                } else {
                    listViewWords.setVisibility(View.GONE); // Ẩn nếu không có gợi ý nào
                }
            }
        }.execute();
    }
    // --- KẾT THÚC THÊM PHƯƠNG THỨC MỚI ---

    // Hàm tra từ từ database cục bộ (phần này đã đúng)
    private void traTu(String tu) {
        new AsyncTask<String, Void, WordEntry>() {
            @Override
            protected WordEntry doInBackground(String... params) {
                String word = params[0];
                if (databaseHelper == null) return null;
                if (isEnglishToVietnamese) {
                    return databaseHelper.getEnglishToVietnameseWord(word);
                } else {
                    return databaseHelper.getVietnameseToEnglishWord(word);
                }
            }

            @Override
            protected void onPostExecute(WordEntry entry) {
                if (entry != null) {
                    Intent intent = new Intent(MainActivity.this, WordDetailActivity.class);
                    // Truyền các trường theo tên cột mới trong WordEntry
                    intent.putExtra("word_text", entry.getWord());
                    intent.putExtra("description_text", entry.getDescription());
                    intent.putExtra("pronounce_text", entry.getPronounce());
                    intent.putExtra("html_content", entry.getHtml()); // Truyền cả HTML

                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Không tìm thấy từ: '" + tu + "' trong từ điển.", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(tu);
    }
}