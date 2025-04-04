package thigk2.NguyenNhatTu;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity6 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main6);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void XuLyCong(View view) {
        //Tìm tham chiếu đến điều khiên trên tệp XML,mapping sang java file
        EditText editTextSoA = findViewById(R.id.edtA);
        EditText editTextSoB = findViewById(R.id.edtB);
        EditText editTextketQua = findViewById(R.id.edtKQ);

        // lay du lieu ve o dieu khien so A
        String strA = editTextSoA.getText().toString(); //strA ="2"
        // lay du lieu ve o dieu khien so B
        String strB = editTextSoB.getText().toString(); //strB ="4"
        // chuyen du lieu sang dang so
        int so_A = Integer.parseInt(strA);
        int so_B = Integer.parseInt(strB);

        // Tinh toan theo yeu cau
        int tong = so_A + so_B;     //6
        String strTong = String.valueOf(tong); // chuyen sang dang chuoi "6"
        // hien ra man hinh
        editTextketQua.setText(strTong);
    }
}