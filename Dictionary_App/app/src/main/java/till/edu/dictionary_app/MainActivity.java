package till.edu.dictionary_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.Html; // Import Html để hiển thị HTML
import android.view.View;
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

// Import các lớp tùy chỉnh của ứng dụng
import till.edu.dictionary_app.database.DatabaseHelper;
import till.edu.dictionary_app.models.WordEntry;
import till.edu.dictionary_app.adapters.WordSuggestionAdapter;

/**
 * MainActivity là màn hình chính của ứng dụng từ điển, cho phép người dùng tìm kiếm từ,
 * xem gợi ý và truy cập các chức năng khác như lịch sử, yêu thích.
 */
public class MainActivity extends AppCompatActivity {

    // Khai báo các thành phần UI
    private EditText oTimKiem; // Ô nhập liệu để người dùng gõ từ cần tìm
    private ListView danhSachTuGoiY; // ListView để hiển thị danh sách các từ gợi ý
    private ArrayList<WordEntry> goiYHienTai; // Danh sách chứa các đối tượng từ gợi ý hiện tại
    private WordSuggestionAdapter boDieuPhoiGoiY; // Adapter để kết nối dữ liệu gợi ý với ListView

    private DatabaseHelper boTroCSDL; // Đối tượng giúp tương tác với cơ sở dữ liệu

    // Các nút chức năng ở thanh điều hướng phía dưới
    private ImageButton nutTimKiemDuoi, nutLichSuDuoi, nutYeuThichDuoi, nutDichDuoi;
    // TextView hiển thị ngôn ngữ hiện tại (Anh/Việt)
    private TextView tvTiengAnh, tvTiengViet;
    private View nutHoanDoi; // Nút dùng để chuyển đổi chiều dịch (Anh-Việt hoặc Việt-Anh)

    private boolean laAnhViet = true; // Cờ boolean xác định chiều dịch hiện tại: true nếu là Anh -> Việt, false nếu là Việt -> Anh

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Gắn layout activity_main.xml cho Activity này

        // Ánh xạ các thành phần UI từ layout bằng ID của chúng
        oTimKiem = findViewById(R.id.etSearch);
        danhSachTuGoiY = findViewById(R.id.listViewWords);

        nutTimKiemDuoi = findViewById(R.id.btnSearch);
        nutLichSuDuoi = findViewById(R.id.btnHistory);
        nutYeuThichDuoi = findViewById(R.id.btnFavorites);
        // nutDichDuoi không được sử dụng trực tiếp trong MainActivity, nhưng giữ lại nếu có ý định sử dụng trong tương lai

        tvTiengAnh = findViewById(R.id.tvEnglish);
        tvTiengViet = findViewById(R.id.tvVietnamese);
        nutHoanDoi = findViewById(R.id.ivSwap);

        // Khởi tạo đối tượng DatabaseHelper để làm việc với cơ sở dữ liệu từ điển
        boTroCSDL = new DatabaseHelper(this);

        // Khối lệnh try-catch để xử lý việc tạo và mở cơ sở dữ liệu
        try {
            boTroCSDL.createDatabase(); // Tạo cơ sở dữ liệu nếu chưa tồn tại
            boTroCSDL.openDatabase(); // Mở cơ sở dữ liệu để sẵn sàng truy vấn
            Log.d("MainActivity", "Database opened successfully."); // Ghi log khi mở CSDL thành công
        } catch (IOException e) {
            e.printStackTrace(); // In lỗi ra console nếu có vấn đề khi tạo/mở CSDL
            Toast.makeText(this, "Lỗi khi tạo/mở database: " + e.getMessage(), Toast.LENGTH_LONG).show(); // Hiển thị thông báo lỗi cho người dùng
            Log.e("MainActivity", "Error creating/opening database", e); // Ghi log lỗi chi tiết
        }

        // Khởi tạo danh sách gợi ý và thiết lập adapter cho ListView
        goiYHienTai = new ArrayList<>(); // Khởi tạo ArrayList rỗng để chứa các từ gợi ý
        // Khởi tạo WordSuggestionAdapter với ngữ cảnh, layout item tùy chỉnh và danh sách gợi ý
        boDieuPhoiGoiY = new WordSuggestionAdapter(this, R.layout.item_word_suggestion, goiYHienTai);
        danhSachTuGoiY.setAdapter(boDieuPhoiGoiY); // Gắn adapter vào ListView

        // Tải các từ gợi ý mặc định khi Activity mới được tạo
        taiGoiYMacDinh();

        // Thêm TextWatcher để theo dõi sự thay đổi văn bản trong ô tìm kiếm
        oTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không làm gì trước khi văn bản thay đổi
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Khi văn bản thay đổi, kiểm tra để lọc gợi ý hoặc tải lại mặc định
                if (s.length() > 0) { // Nếu có ký tự trong ô tìm kiếm
                    locGoiYTuCSDL(s.toString(), laAnhViet); // Gọi hàm để lọc gợi ý từ cơ sở dữ liệu
                } else { // Nếu ô tìm kiếm trống
                    taiGoiYMacDinh(); // Tải lại danh sách gợi ý mặc định
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không làm gì sau khi văn bản thay đổi
            }
        });

        // Thiết lập OnItemClickListener cho ListView gợi ý
        danhSachTuGoiY.setOnItemClickListener((parent, view, position, id) -> {
            WordEntry selectedEntry = goiYHienTai.get(position); // Lấy đối tượng WordEntry được chọn từ danh sách gợi ý
            String selectedWord = selectedEntry.getWord(); // Lấy từ (string) từ đối tượng WordEntry

            oTimKiem.setText(selectedWord); // Đặt từ đã chọn vào ô tìm kiếm
            oTimKiem.setSelection(selectedWord.length()); // Di chuyển con trỏ về cuối từ
            danhSachTuGoiY.setVisibility(View.GONE); // Ẩn danh sách gợi ý sau khi người dùng chọn
            traCuuTu(selectedWord); // Gọi hàm để tra cứu nghĩa của từ
        });

        // Thiết lập OnClickListener cho nút tìm kiếm ở thanh dưới cùng
        nutTimKiemDuoi.setOnClickListener(v -> {
            String word = oTimKiem.getText().toString().trim(); // Lấy từ trong ô tìm kiếm và loại bỏ khoảng trắng thừa
            if (word.isEmpty()) { // Kiểm tra nếu ô tìm kiếm rỗng
                Toast.makeText(MainActivity.this, "Vui lòng nhập từ cần tra", Toast.LENGTH_SHORT).show(); // Yêu cầu người dùng nhập từ
                return;
            }
            danhSachTuGoiY.setVisibility(View.GONE); // Ẩn danh sách gợi ý khi thực hiện tìm kiếm
            traCuuTu(word); // Thực hiện tra cứu từ
        });

        // Thiết lập OnClickListener cho nút hoán đổi chiều dịch (Anh-Việt / Việt-Anh)
        nutHoanDoi.setOnClickListener(v -> {
            laAnhViet = !laAnhViet; // Đảo ngược trạng thái chiều dịch
            capNhatHienThiNgonNgu(); // Cập nhật giao diện (màu chữ, hint) theo chiều dịch mới
            oTimKiem.setText(""); // Xóa nội dung ô tìm kiếm khi đổi chiều dịch
            taiGoiYMacDinh(); // Tải lại gợi ý mặc định phù hợp với chiều dịch mới
            Toast.makeText(this, laAnhViet ? "Từ điển Anh -> Việt" : "Từ điển Việt -> Anh", Toast.LENGTH_SHORT).show(); // Thông báo chiều dịch hiện tại
        });

        // Xử lý sự kiện cho các nút điều hướng phía dưới
        nutLichSuDuoi.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng Lịch sử chưa triển khai", Toast.LENGTH_SHORT).show(); // Thông báo chức năng chưa triển khai
        });

        nutYeuThichDuoi.setOnClickListener(v -> {
            // Tạo Intent để chuyển sang FavoritesActivity (màn hình từ yêu thích)
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intent); // Bắt đầu Activity mới
        });

        // Cập nhật giao diện hiển thị ngôn ngữ ban đầu khi Activity được tạo
        capNhatHienThiNgonNgu();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Đóng kết nối cơ sở dữ liệu khi Activity bị hủy để giải phóng tài nguyên
        if (boTroCSDL != null) {
            boTroCSDL.close();
            Log.d("MainActivity", "Database closed."); // Ghi log khi đóng CSDL
        }
    }

    /**
     * Cập nhật giao diện hiển thị ngôn ngữ (màu chữ của "English", "Vietnamese" và hint của ô tìm kiếm)
     * dựa trên chiều dịch hiện tại (laAnhViet).
     */
    private void capNhatHienThiNgonNgu() {
        if (laAnhViet) { // Nếu chiều dịch là Anh -> Việt
            tvTiengAnh.setTextColor(getResources().getColor(android.R.color.white)); // Đặt màu trắng cho "English"
            tvTiengViet.setTextColor(getResources().getColor(android.R.color.darker_gray)); // Đặt màu xám cho "Vietnamese"
            oTimKiem.setHint("Tìm từ tiếng Anh"); // Đặt gợi ý cho ô tìm kiếm
        } else { // Nếu chiều dịch là Việt -> Anh
            tvTiengAnh.setTextColor(getResources().getColor(android.R.color.darker_gray)); // Đặt màu xám cho "English"
            tvTiengViet.setTextColor(getResources().getColor(android.R.color.white)); // Đặt màu trắng cho "Vietnamese"
            oTimKiem.setHint("Tìm từ tiếng Việt"); // Đặt gợi ý cho ô tìm kiếm
        }
    }

    /**
     * Thực hiện lọc các từ gợi ý từ cơ sở dữ liệu trong background
     * dựa trên chuỗi tìm kiếm và chiều dịch hiện tại.
     * Sử dụng AsyncTask để tránh chặn luồng UI.
     * @param chuoiTimKiem Chuỗi mà người dùng đang nhập vào ô tìm kiếm.
     * @param laAnhVietDich Chiều dịch hiện tại (true: Anh-Việt, false: Việt-Anh).
     */
    private void locGoiYTuCSDL(String chuoiTimKiem, boolean laAnhVietDich) {
        new AsyncTask<String, Void, List<WordEntry>>() {
            @Override
            protected List<WordEntry> doInBackground(String... params) {
                String tienTo = params[0]; // Lấy chuỗi tiền tố từ tham số
                if (boTroCSDL == null) return new ArrayList<>(); // Trả về danh sách rỗng nếu CSDL chưa được khởi tạo
                if (laAnhVietDich) { // Nếu chiều dịch là Anh -> Việt
                    return boTroCSDL.getEnglishSuggestions(tienTo); // Lấy gợi ý từ tiếng Anh
                } else { // Nếu chiều dịch là Việt -> Anh
                    return boTroCSDL.getVietnameseSuggestions(tienTo); // Lấy gợi ý từ tiếng Việt
                }
            }

            @Override
            protected void onPostExecute(List<WordEntry> ketQua) {
                goiYHienTai.clear(); // Xóa các gợi ý cũ
                if (ketQua != null) {
                    goiYHienTai.addAll(ketQua); // Thêm các gợi ý mới vào danh sách
                }

                if (!goiYHienTai.isEmpty()) { // Nếu có gợi ý
                    boDieuPhoiGoiY.notifyDataSetChanged(); // Cập nhật dữ liệu cho adapter để hiển thị trên ListView
                    danhSachTuGoiY.setVisibility(View.VISIBLE); // Hiển thị ListView gợi ý
                } else { // Nếu không có gợi ý nào
                    danhSachTuGoiY.setVisibility(View.GONE); // Ẩn ListView gợi ý
                }
            }
        }.execute(chuoiTimKiem); // Thực thi AsyncTask với chuỗi tìm kiếm
    }

    /**
     * Tải các từ gợi ý mặc định khi ô tìm kiếm trống hoặc khi khởi tạo Activity.
     * Sử dụng AsyncTask để lấy dữ liệu từ cơ sở dữ liệu trong background.
     */
    private void taiGoiYMacDinh() {
        new AsyncTask<Void, Void, List<WordEntry>>() {
            @Override
            protected List<WordEntry> doInBackground(Void... voids) {
                if (boTroCSDL == null) return new ArrayList<>(); // Trả về danh sách rỗng nếu CSDL chưa được khởi tạo
                // Lấy các từ gợi ý mặc định từ DatabaseHelper dựa trên chiều dịch hiện tại
                return boTroCSDL.getDefaultSuggestions(laAnhViet);
            }

            @Override
            protected void onPostExecute(List<WordEntry> ketQua) {
                goiYHienTai.clear(); // Xóa các gợi ý cũ
                if (ketQua != null) {
                    goiYHienTai.addAll(ketQua); // Thêm các gợi ý mới vào danh sách
                }

                if (!goiYHienTai.isEmpty()) { // Nếu có gợi ý
                    boDieuPhoiGoiY.notifyDataSetChanged(); // Cập nhật adapter để hiển thị trên ListView
                    danhSachTuGoiY.setVisibility(View.VISIBLE); // Hiển thị ListView gợi ý
                } else { // Nếu không có gợi ý nào
                    danhSachTuGoiY.setVisibility(View.GONE); // Ẩn ListView gợi ý
                }
            }
        }.execute(); // Thực thi AsyncTask
    }

    /**
     * Thực hiện tra cứu một từ trong cơ sở dữ liệu và hiển thị chi tiết từ đó
     * trong WordDetailActivity.
     * Sử dụng AsyncTask để thực hiện truy vấn CSDL trong background.
     * @param tuCanTra Từ cần tra cứu.
     */
    private void traCuuTu(String tuCanTra) {
        new AsyncTask<String, Void, WordEntry>() {
            @Override
            protected WordEntry doInBackground(String... params) {
                String tu = params[0]; // Lấy từ cần tra từ tham số
                if (boTroCSDL == null) return null; // Trả về null nếu CSDL chưa được khởi tạo
                if (laAnhViet) { // Nếu chiều dịch là Anh -> Việt
                    return boTroCSDL.getEnglishToVietnameseWord(tu); // Tra từ Anh sang Việt
                } else { // Nếu chiều dịch là Việt -> Anh
                    return boTroCSDL.getVietnameseToEnglishWord(tu); // Tra từ Việt sang Anh
                }
            }

            @Override
            protected void onPostExecute(WordEntry mucTu) {
                if (mucTu != null) { // Nếu tìm thấy từ
                    // Tạo Intent để chuyển sang WordDetailActivity
                    Intent intent = new Intent(MainActivity.this, WordDetailActivity.class);
                    // Truyền các thông tin chi tiết của từ qua Intent
                    intent.putExtra("word_text", mucTu.getWord());
                    intent.putExtra("description_text", mucTu.getDescription());
                    intent.putExtra("pronounce_text", mucTu.getPronounce());
                    intent.putExtra("html_content", mucTu.getHtml()); // Truyền cả nội dung HTML của nghĩa
                    intent.putExtra("is_english_to_vietnamese", laAnhViet); // Truyền chiều dịch hiện tại

                    startActivity(intent); // Bắt đầu WordDetailActivity
                } else { // Nếu không tìm thấy từ
                    Toast.makeText(MainActivity.this, "Không tìm thấy từ: '" + tuCanTra + "' trong từ điển.", Toast.LENGTH_SHORT).show(); // Thông báo cho người dùng
                }
            }
        }.execute(tuCanTra); // Thực thi AsyncTask với từ cần tra
    }
}