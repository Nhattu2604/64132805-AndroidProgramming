package till.edu.vd22;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {
    private Button btnOk;
    private EditText edtUserName, edtPass, edtMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login); // Đúng layout của màn hình đăng nhập

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các view từ layout
        edtUserName = findViewById(R.id.edtUserName);
        edtPass = findViewById(R.id.edtPass);
        edtMail = findViewById(R.id.edtMail);
        btnOk = findViewById(R.id.btnOK);

        // Thiết lập sự kiện click
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnOK) {
            login();
        }
    }

    private void login() {
        String username = edtUserName.getText().toString().trim();
        String password = edtPass.getText().toString().trim();
        String email = edtMail.getText().toString().trim();

        // Kiểm tra nhập liệu
        if (username.isEmpty()) {
            edtUserName.setError("Vui lòng nhập tên đăng nhập!");
            edtUserName.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            edtPass.setError("Vui lòng nhập mật khẩu!");
            edtPass.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            edtMail.setError("Vui lòng nhập email!");
            edtMail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtMail.setError("Email không hợp lệ!");
            edtMail.requestFocus();
            return;
        }

        // Kiểm tra đăng nhập
        if (username.equals("NguyenNhatTu") && password.equals("ABCDEF") && email.equals("NguyenNhatTu@ntu.edu.vn")) {
            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

            // Chuyển sang màn hình chính
            Intent iManHinhLogin = new Intent(ActivityLogin.this, ActivityHome.class);
            iManHinhLogin.putExtra("ten_dang_nhap", username);
            iManHinhLogin.putExtra("mat_khau", password);
            startActivity(iManHinhLogin);
            finish(); // Đóng màn hình login sau khi đăng nhập thành công
        } else {
            Toast.makeText(this, "Tên đăng nhập, mật khẩu hoặc email sai!", Toast.LENGTH_SHORT).show();
        }
    }
}
