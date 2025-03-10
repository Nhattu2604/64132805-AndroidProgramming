package till.edu.vd22;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivityLogin extends AppCompatActivity {
    //button
    private Button btnOk;

    private EditText edtUserName, edtPass, edtMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Ánh xạ các view từ layout
        edtUserName = findViewById(R.id.edtUserName);
        edtPass = findViewById(R.id.edtPass);
        edtMail = findViewById(R.id.edtMail);
        btnOk = findViewById(R.id.btnOK);

        // Thiết lập sự kiện click cho nút đăng nhập
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUserName.getText().toString().trim();
                String password = edtPass.getText().toString().trim();
                String email = edtMail.getText().toString().trim();

                // Kiểm tra thông tin đăng nhập
                if (username.equals("NguyenNhatTu") && password.equals("ABCDEF")&&email.equals("NguyenNhatTu@ntu.edu.vn")) {
                    // Đăng nhập thành công
                    Toast.makeText(ActivityLogin.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                //goi du lieu vao iQuiz dang key va value dung de loc cac ra du lieu

                    // Chuyển sang màn hình chính
                    Intent iManHinhLogin = new Intent(ActivityLogin.this, ActivityHome.class);
                    //goi du lieu vao iQuiz dang key va value dung de loc cac ra du lieu
                    iManHinhLogin.putExtra("ten_dang_nhap",username);
                    iManHinhLogin.putExtra("ten_dang_nhap",password);
                    //gui di
                    startActivity(iManHinhLogin);
                } else {
                    // Đăng nhập thất bại
                    Toast.makeText(ActivityLogin.this, "Tên đăng nhập hoặc mật khẩu sai!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
