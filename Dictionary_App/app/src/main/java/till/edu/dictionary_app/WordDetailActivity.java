package till.edu.dictionary_app;

import android.os.Bundle;
import android.text.Html;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;
import java.util.Locale;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat; // Thêm import này

import till.edu.dictionary_app.database.DatabaseHelper; // <-- THÊM IMPORT NÀY
import till.edu.dictionary_app.R; // Đảm bảo import R nếu chưa có

public class WordDetailActivity extends AppCompatActivity {

    private static final String TAG = "WordDetailActivity";

    private TextView tvWord, tvPhonetic, tvMeaningsContainer, tvWordTitle;
    private ImageButton btnBack, btnFavoriteDetail, btnPlayAudio;

    private TextToSpeech textToSpeech;
    private String currentWordToSpeak = "";
    private boolean isEnglishToVietnameseMode = true;

    private DatabaseHelper databaseHelper; // <-- KHAI BÁO DATABASE HELPER
    private String currentWord = ""; // <-- LƯU TỪ HIỆN TẠI
    private boolean isFavoriteStatus = false; // <-- TRẠNG THÁI YÊU THÍCH HIỆN TẠI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);

        // Khởi tạo DatabaseHelper
        databaseHelper = new DatabaseHelper(this); // <-- KHỞI TẠO DATABASE HELPER

        // Ánh xạ View
        tvWord = findViewById(R.id.tvWord);
        tvPhonetic = findViewById(R.id.tvPhonetic);
        tvMeaningsContainer = findViewById(R.id.tvMeaningsContainer);
        tvWordTitle = findViewById(R.id.tvWordTitle);

        btnBack = findViewById(R.id.btnBack);
        btnFavoriteDetail = findViewById(R.id.btnFavoriteDetail);
        btnPlayAudio = findViewById(R.id.btnPlayAudio);

        // Xử lý nút Back
        btnBack.setOnClickListener(v -> finish());

        // Nhận dữ liệu từ Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String word = extras.getString("word_text");
            String description = extras.getString("description_text");
            String pronounce = extras.getString("pronounce_text");
            String htmlContent = extras.getString("html_content");
            isEnglishToVietnameseMode = extras.getBoolean("is_english_to_vietnamese", true);

            currentWordToSpeak = word != null ? word : "";
            currentWord = word != null ? word : ""; // <-- LƯU TỪ VÀO BIẾN currentWord

            // Hiển thị dữ liệu
            tvWord.setText(word != null ? word : "N/A");
            tvWordTitle.setText(word != null ? word : "Chi tiết từ");
            tvPhonetic.setText(pronounce != null ? pronounce : "");

            if (htmlContent != null && !htmlContent.isEmpty()) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    tvMeaningsContainer.setText(Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_LEGACY));
                } else {
                    tvMeaningsContainer.setText(Html.fromHtml(htmlContent));
                }
            } else if (description != null && !description.isEmpty()) {
                tvMeaningsContainer.setText(description);
            } else {
                tvMeaningsContainer.setText("Không có thông tin chi tiết.");
            }

            // --- KIỂM TRA TRẠNG THÁI YÊU THÍCH VÀ CẬP NHẬT ICON BAN ĐẦU ---
            if (!currentWord.isEmpty()) {
                isFavoriteStatus = databaseHelper.isFavorite(currentWord);
                updateFavoriteIcon();
            }

        } else {
            Toast.makeText(this, "Không có dữ liệu từ để hiển thị", Toast.LENGTH_SHORT).show();
            tvMeaningsContainer.setText("Không có dữ liệu từ để hiển thị.");
        }

        // --- KHỞI TẠO TEXT-TO-SPEECH ENGINE ---
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result;
                if (isEnglishToVietnameseMode) {
                    result = textToSpeech.setLanguage(Locale.US);
                    Log.d(TAG, "TTS language set to English (US)");
                } else {
                    result = textToSpeech.setLanguage(new Locale("vi", "VN"));
                    Log.d(TAG, "TTS language set to Vietnamese (VN)");
                }

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e(TAG, "Ngôn ngữ không được hỗ trợ hoặc thiếu dữ liệu ngôn ngữ.");
                    Toast.makeText(WordDetailActivity.this, "Ngôn ngữ phát âm không được hỗ trợ hoặc thiếu dữ liệu.", Toast.LENGTH_LONG).show();
                    btnPlayAudio.setEnabled(false);
                } else {
                    btnPlayAudio.setEnabled(true);
                }
            } else {
                Log.e(TAG, "Khởi tạo TextToSpeech thất bại.");
                Toast.makeText(WordDetailActivity.this, "Không thể khởi tạo chức năng phát âm.", Toast.LENGTH_SHORT).show();
                btnPlayAudio.setEnabled(false);
            }
        });

        // --- XỬ LÝ NÚT PHÁT ÂM ---
        btnPlayAudio.setOnClickListener(v -> {
            if (textToSpeech != null && textToSpeech.isSpeaking()) {
                textToSpeech.stop();
            }
            if (textToSpeech != null && !currentWordToSpeak.isEmpty()) {
                textToSpeech.speak(currentWordToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                Toast.makeText(this, "Không có từ để phát âm.", Toast.LENGTH_SHORT).show();
            }
        });

        // --- XỬ LÝ NÚT YÊU THÍCH ---
        btnFavoriteDetail.setOnClickListener(v -> {
            if (currentWord.isEmpty()) {
                Toast.makeText(this, "Không có từ để thêm vào yêu thích.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isFavoriteStatus) { // Nếu đang là yêu thích, thì xóa
                if (databaseHelper.removeFavorite(currentWord)) {
                    isFavoriteStatus = false;
                    Toast.makeText(this, "'" + currentWord + "' đã xóa khỏi yêu thích.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Lỗi khi xóa khỏi yêu thích.", Toast.LENGTH_SHORT).show();
                }
            } else { // Nếu chưa là yêu thích, thì thêm
                if (databaseHelper.addFavorite(currentWord, isEnglishToVietnameseMode)) {
                    isFavoriteStatus = true;
                    Toast.makeText(this, "'" + currentWord + "' đã thêm vào yêu thích.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Lỗi khi thêm vào yêu thích.", Toast.LENGTH_SHORT).show();
                }
            }
            updateFavoriteIcon(); // Cập nhật icon sau khi thay đổi trạng thái
        });
    }

    // --- PHƯƠNG THỨC MỚI: CẬP NHẬT ICON YÊU THÍCH ---
    private void updateFavoriteIcon() {
        if (isFavoriteStatus) {
            btnFavoriteDetail.setImageResource(R.drawable.ic_star_filled); // Icon sao đầy
            // Đảm bảo bạn có file ic_star_filled.xml trong drawable
        } else {
            btnFavoriteDetail.setImageResource(R.drawable.ic_star); // Icon sao rỗng (mặc định)
        }
    }

    // --- GIẢI PHÓNG TÀI NGUYÊN KHI ACTIVITY BỊ HỦY ---
    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            Log.d(TAG, "TextToSpeech shutdown.");
        }
        // Đóng database helper khi không dùng nữa
        if (databaseHelper != null) {
            databaseHelper.close();
            Log.d(TAG, "DatabaseHelper closed.");
        }
        super.onDestroy();
    }
}