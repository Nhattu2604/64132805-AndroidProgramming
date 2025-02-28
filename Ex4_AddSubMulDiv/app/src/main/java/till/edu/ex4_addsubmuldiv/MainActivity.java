package till.edu.ex4_addsubmuldiv;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
 // khai bao cac doi tuong gan voi dieu khien tuong ung o day
 EditText editTextSo1;
 EditText editTextSo2;
 EditText editTextKQ;
 Button nutCong,nutTru,nutNhan,nutChia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            TimDieuKhien();
            return insets;
        });
    }
    void TimDieuKhien() {
         editTextSo1 = (EditText) findViewById(R.id.edtSo1);
         editTextSo2 = (EditText) findViewById(R.id.edtSo2);
         editTextKQ = (EditText) findViewById(R.id.edtKetQua);
         nutCong = (Button) findViewById(R.id.btnCong);
        nutTru = (Button) findViewById(R.id.btnTru);
        nutNhan = (Button) findViewById(R.id.btnNhan);
        nutChia = (Button) findViewById(R.id.btnChia);
    }

    // Xu ly Cong
   public void XuLyCong(View v){
    //b1. lay du lieu 2 so
    //b1.1 Tim EditText so 1 va 2

    //b1.2 Lay du lieu tu 2 dieu khien o tren
    String soThu1 = editTextSo1.getText().toString();
    String soThu2 = editTextSo2.getText().toString();
    //b1.3 chuyen du lieu tu chuoi sang so
    float soA = Float.parseFloat(soThu1);
    float soB = Float.parseFloat(soThu2);
    // b2 Tinh Toan
    float Tong = soA + soB;
    //b3 Hien ket qua
    // b3.1 Tim view ketqua

     //b3.2 chuan bi du lieu xuat , bien thanh dang chuoi
     String ChuoiKQ = String.valueOf(Tong);
     //B3.3 gan ket qua len dk
        editTextKQ.setText(ChuoiKQ);
    }
    public void XuLyTru(View v){
        //b1. lay du lieu 2 so
        //b1.1 Tim EditText so 1 va 2

        //b1.2 Lay du lieu tu 2 dieu khien o tren
        String soThu1 = editTextSo1.getText().toString();
        String soThu2 = editTextSo2.getText().toString();
        //b1.3 chuyen du lieu tu chuoi sang so
        float soA = Float.parseFloat(soThu1);
        float soB = Float.parseFloat(soThu2);
        // b2 Tinh Toan
        float Hieu = soA - soB;
        //b3 Hien ket qua
        // b3.1 Tim view ketqua

        //b3.2 chuan bi du lieu xuat , bien thanh dang chuoi
        String ChuoiKQ = String.valueOf(Hieu);
        //B3.3 gan ket qua len dk
        editTextKQ.setText(ChuoiKQ);
    }
    public void XuLyNhan(View v){
        //b1. lay du lieu 2 so
        //b1.1 Tim EditText so 1 va 2

        //b1.2 Lay du lieu tu 2 dieu khien o tren
        String soThu1 = editTextSo1.getText().toString();
        String soThu2 = editTextSo2.getText().toString();
        //b1.3 chuyen du lieu tu chuoi sang so
        float soA = Float.parseFloat(soThu1);
        float soB = Float.parseFloat(soThu2);
        // b2 Tinh Toan
        float Tich = soA * soB;
        //b3 Hien ket qua
        // b3.1 Tim view ketqua

        //b3.2 chuan bi du lieu xuat , bien thanh dang chuoi
        String ChuoiKQ = String.valueOf(Tich);
        //B3.3 gan ket qua len dk
        editTextKQ.setText(ChuoiKQ);
    }
    public void XulyChia(View v){
        //b1. lay du lieu 2 so
        //b1.1 Tim EditText so 1 va 2

        //b1.2 Lay du lieu tu 2 dieu khien o tren
        String soThu1 = editTextSo1.getText().toString();
        String soThu2 = editTextSo2.getText().toString();
        //b1.3 chuyen du lieu tu chuoi sang so
        float soA = Float.parseFloat(soThu1);
        float soB = Float.parseFloat(soThu2);
        // b2 Tinh Toan
        float Thuong = soA / soB;
        //b3 Hien ket qua
        // b3.1 Tim view ketqua

        //b3.2 chuan bi du lieu xuat , bien thanh dang chuoi
        String ChuoiKQ = String.valueOf(Thuong);
        //B3.3 gan ket qua len dk
        editTextKQ.setText(ChuoiKQ);
    }
}