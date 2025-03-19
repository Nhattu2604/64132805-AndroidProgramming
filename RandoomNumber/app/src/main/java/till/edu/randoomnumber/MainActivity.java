package till.edu.randoomnumber;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button btnKiemTra, btnSo1, btnSo2, btnSo3, btnSo4, btnSo5, btnSo6, btnSo7, btnSo8, btnSo9;
    TextView tvSoA, tvSoB, tvKetQua, tvDapAn;
    int kqDung;

    void TimDieuKhien() {
        btnKiemTra = findViewById(R.id.btnKiemTra);
        btnSo1 = findViewById(R.id.btnSo1);
        btnSo2 = findViewById(R.id.btnSo2);
        btnSo3 = findViewById(R.id.btnSo3);
        btnSo4 = findViewById(R.id.btnSo4);
        btnSo5 = findViewById(R.id.btnSo5);
        btnSo6 = findViewById(R.id.btnSo6);
        btnSo7 = findViewById(R.id.btnSo7);
        btnSo8 = findViewById(R.id.btnSo8);
        btnSo9 = findViewById(R.id.btnSo9);
        tvSoA = findViewById(R.id.tvSoA);
        tvSoB = findViewById(R.id.tvSoB);
        tvKetQua = findViewById(R.id.tvKetQua);
        tvDapAn = findViewById(R.id.tvDapAn);
    }
    void taoSoNgauNhien() {
        int a = (int) (Math.random() * 5);
        int b = (int) (Math.random() * 5);
        kqDung = a + b;

        tvSoA.setText(String.valueOf(a));
        tvSoB.setText(String.valueOf(b));
        tvDapAn.setText(""); // Xóa đáp án trước đó
        tvKetQua.setText(""); // Xóa thông báo kết quả
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TimDieuKhien();

        taoSoNgauNhien();

        // Xử lý sự kiện khi bấm các nút số
        btnSo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDapAn.setText("1");
            }
        });
        btnSo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDapAn.setText("2");
            }
        });
        btnSo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDapAn.setText("3");
            }
        });
        btnSo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDapAn.setText("4");
            }
        });
        btnSo5.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                tvDapAn.setText("5");
            }
        });
        btnSo6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDapAn.setText("6");
            }
        });
        btnSo7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDapAn.setText("7");
            }
        });
        btnSo8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDapAn.setText("8");
            }
        });
        btnSo9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDapAn.setText("9");
            }
        });

        btnKiemTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //lấy dữ liệu từ tvDapAn về
                String strDapAn = tvDapAn.getText().toString();

                if(strDapAn.isEmpty()){
                    tvKetQua.setText("Bạn chưa chọn đáp án!");
                    return;
                }
                int dapan = Integer.parseInt(strDapAn);

                if(dapan == kqDung){
                    tvKetQua.setText("Kết quả đúng!");
                    taoSoNgauNhien(); // Sinh số mới khi trả lời đúng
                }
                else tvKetQua.setText("Kết quả sai!");
            }
        });
    }
}