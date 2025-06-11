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
import androidx.core.content.ContextCompat;

import till.edu.dictionary_app.database.DatabaseHelper;
import till.edu.dictionary_app.R;

public class WordDetailActivity extends AppCompatActivity {


    private static final String TAG = "ChiTietTuActivity"; // TAG -> ChiTietTuActivity

    private TextView tvTuHienThi, tvPhienAm, tvNoiDungNghia, tvTieuDeTu; // tvWord, tvPhonetic, tvMeaningsContainer, tvWordTitle
    private ImageButton nutQuayLai, nutYeuThichChiTiet, nutPhatAm; // btnBack, btnFavoriteDetail, btnPlayAudio

    private TextToSpeech congCuPhatAm; // textToSpeech -> congCuPhatAm
    private String tuHienTaiDePhatAm = ""; // currentWordToSpeak -> tuHienTaiDePhatAm
    private boolean laCheDoAnhViet = true; // isEnglishToVietnameseMode -> laCheDoAnhViet

    private DatabaseHelper boTroCSDL; // databaseHelper -> boTroCSDL (nhất quán với MainActivity và FavoritesActivity)
    private String tuDangXem = ""; // currentWord -> tuDangXem
    private boolean laTrangThaiYeuThich = false; // isFavoriteStatus -> laTrangThaiYeuThich

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);

        // Khởi tạo DatabaseHelper
        boTroCSDL = new DatabaseHelper(this);

        // Ánh xạ View (đã đổi tên)
        tvTuHienThi = findViewById(R.id.tvWord);
        tvPhienAm = findViewById(R.id.tvPhonetic);
        tvNoiDungNghia = findViewById(R.id.tvMeaningsContainer);
        tvTieuDeTu = findViewById(R.id.tvWordTitle);

        nutQuayLai = findViewById(R.id.btnBack);
        nutYeuThichChiTiet = findViewById(R.id.btnFavoriteDetail);
        nutPhatAm = findViewById(R.id.btnPlayAudio);

        // Xử lý nút Back (đã đổi tên biến)
        nutQuayLai.setOnClickListener(v -> finish());

        // Nhận dữ liệu từ Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String word = extras.getString("word_text");
            String description = extras.getString("description_text");
            String pronounce = extras.getString("pronounce_text");
            String htmlContent = extras.getString("html_content");
            laCheDoAnhViet = extras.getBoolean("is_english_to_vietnamese", true); // laCheDoAnhViet thay cho isEnglishToVietnameseMode

            tuHienTaiDePhatAm = word != null ? word : ""; // tuHienTaiDePhatAm thay cho currentWordToSpeak
            tuDangXem = word != null ? word : ""; // tuDangXem thay cho currentWord

            // Hiển thị dữ liệu
            tvTuHienThi.setText(word != null ? word : "N/A");
            tvTieuDeTu.setText(word != null ? word : "Chi tiết từ");
            tvPhienAm.setText(pronounce != null ? pronounce : "");

            if (htmlContent != null && !htmlContent.isEmpty()) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    tvNoiDungNghia.setText(Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_LEGACY));
                } else {
                    tvNoiDungNghia.setText(Html.fromHtml(htmlContent));
                }
            } else if (description != null && !description.isEmpty()) {
                tvNoiDungNghia.setText(description);
            } else {
                tvNoiDungNghia.setText("Không có thông tin chi tiết.");
            }

            // --- KIỂM TRA TRẠNG THÁI YÊU THÍCH VÀ CẬP NHẬT ICON BAN ĐẦU --- (đã đổi tên biến/hàm)
            if (!tuDangXem.isEmpty()) {
                laTrangThaiYeuThich = boTroCSDL.isFavorite(tuDangXem);
                capNhatIconYeuThich(); // Gọi hàm mới
            }

        } else {
            Toast.makeText(this, "Không có dữ liệu từ để hiển thị", Toast.LENGTH_SHORT).show();
            tvNoiDungNghia.setText("Không có dữ liệu từ để hiển thị.");
        }

        // --- KHỞI TẠO TEXT-TO-SPEECH ENGINE
        congCuPhatAm = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result;
                if (laCheDoAnhViet) { // laCheDoAnhViet thay cho isEnglishToVietnameseMode
                    result = congCuPhatAm.setLanguage(Locale.US);
                    Log.d(TAG, "TTS language set to English (US)");
                } else {
                    result = congCuPhatAm.setLanguage(new Locale("vi", "VN"));
                    Log.d(TAG, "TTS language set to Vietnamese (VN)");
                }

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e(TAG, "Ngôn ngữ không được hỗ trợ hoặc thiếu dữ liệu ngôn ngữ.");
                    Toast.makeText(WordDetailActivity.this, "Ngôn ngữ phát âm không được hỗ trợ hoặc thiếu dữ liệu.", Toast.LENGTH_LONG).show();
                    nutPhatAm.setEnabled(false); // nutPhatAm thay cho btnPlayAudio
                } else {
                    nutPhatAm.setEnabled(true); // nutPhatAm thay cho btnPlayAudio
                }
            } else {
                Log.e(TAG, "Khởi tạo TextToSpeech thất bại.");
                Toast.makeText(WordDetailActivity.this, "Không thể khởi tạo chức năng phát âm.", Toast.LENGTH_SHORT).show();
                nutPhatAm.setEnabled(false); // nutPhatAm thay cho btnPlayAudio
            }
        });

        // --- XỬ LÝ NÚT PHÁT ÂM
        nutPhatAm.setOnClickListener(v -> {
            if (congCuPhatAm != null && congCuPhatAm.isSpeaking()) {
                congCuPhatAm.stop();
            }
            if (congCuPhatAm != null && !tuHienTaiDePhatAm.isEmpty()) {
                congCuPhatAm.speak(tuHienTaiDePhatAm, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                Toast.makeText(this, "Không có từ để phát âm.", Toast.LENGTH_SHORT).show();
            }
        });

        // --- XỬ LÝ NÚT YÊU THÍCH ---
        nutYeuThichChiTiet.setOnClickListener(v -> { // nutYeuThichChiTiet thay cho btnFavoriteDetail
            if (tuDangXem.isEmpty()) { // tuDangXem thay cho currentWord
                Toast.makeText(this, "Không có từ để thêm vào yêu thích.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (laTrangThaiYeuThich) { // laTrangThaiYeuThich thay cho isFavoriteStatus (Nếu đang là yêu thích, thì xóa)
                if (boTroCSDL.removeFavorite(tuDangXem)) { // boTroCSDL thay cho databaseHelper, tuDangXem thay cho currentWord
                    laTrangThaiYeuThich = false; // laTrangThaiYeuThich thay cho isFavoriteStatus
                    Toast.makeText(this, "'" + tuDangXem + "' đã xóa khỏi yêu thích.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Lỗi khi xóa khỏi yêu thích.", Toast.LENGTH_SHORT).show();
                }
            } else { // Nếu chưa là yêu thích, thì thêm
                if (boTroCSDL.addFavorite(tuDangXem, laCheDoAnhViet)) { // boTroCSDL thay cho databaseHelper, tuDangXem thay cho currentWord, laCheDoAnhViet thay cho isEnglishToVietnameseMode
                    laTrangThaiYeuThich = true; // laTrangThaiYeuThich thay cho isFavoriteStatus
                    Toast.makeText(this, "'" + tuDangXem + "' đã thêm vào yêu thích.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Lỗi khi thêm vào yêu thích.", Toast.LENGTH_SHORT).show();
                }
            }
            capNhatIconYeuThich(); // Cập nhật icon sau khi thay đổi trạng thái (gọi hàm mới)
        });
    }

    // --- PHƯƠNG THỨC MỚI: CẬP NHẬT ICON YÊU THÍCH --- (đã đổi tên hàm và biến)
    private void capNhatIconYeuThich() { // updateFavoriteIcon -> capNhatIconYeuThich
        if (laTrangThaiYeuThich) { // laTrangThaiYeuThich thay cho isFavoriteStatus
            nutYeuThichChiTiet.setImageResource(R.drawable.ic_star_filled); // nutYeuThichChiTiet thay cho btnFavoriteDetail
            // Đảm bảo bạn có file ic_star_filled.xml trong drawable
        } else {
            nutYeuThichChiTiet.setImageResource(R.drawable.ic_star); // Icon sao rỗng (mặc định) (nutYeuThichChiTiet thay cho btnFavoriteDetail)
        }
    }

    // --- GIẢI PHÓNG TÀI NGUYÊN KHI ACTIVITY BỊ HỦY
    @Override
    protected void onDestroy() {
        if (congCuPhatAm != null) { // congCuPhatAm thay cho textToSpeech
            congCuPhatAm.stop();
            congCuPhatAm.shutdown();
            Log.d(TAG, "TextToSpeech shutdown.");
        }
        // Đóng database helper khi không dùng nữa
        if (boTroCSDL != null) {
            boTroCSDL.close();
            Log.d(TAG, "DatabaseHelper closed.");
        }
        super.onDestroy();
    }
}