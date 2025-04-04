package thigk2.NguyenNhatTu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    EditText edtThang, edtNam, edtKetQua;
    Button btnKiemTra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Tìm các điều khiển
        edtThang = findViewById(R.id.editTextText1);
        edtNam = findViewById(R.id.editTextText2);
        edtKetQua = findViewById(R.id.edtKetQua);
        btnKiemTra = findViewById(R.id.btn1);

        // Xử lý sự kiện khi nhấn Button
        btnKiemTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String thangStr = edtThang.getText().toString().trim();
                String namStr = edtNam.getText().toString().trim();

                if (thangStr.equals("4") && namStr.equals("1975")) {
                    edtKetQua.setText("Đúng");
                } else {
                    edtKetQua.setText("Sai");
                }
            }
        });
    }
}
