package thigk2.NguyenNhatTu;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity3 extends AppCompatActivity {
    ListView LVDonGian;
    ArrayList<String> dsnnlt;  // Khai báo danh sách

    void TimDieuKhien() {
        LVDonGian = findViewById(R.id.LV_Dongian);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main3);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TimDieuKhien();


        dsnnlt = new ArrayList<>();
        dsnnlt.add("                        Tiến về Sài Gòn");
        dsnnlt.add("                        Giải phóng Miền nam");
        dsnnlt.add("                        Đất nước trọn niềm vui");
        dsnnlt.add("                        Bài ca thống nhất");
        dsnnlt.add("                        Mùa xuân trên thành phố HCM");
        dsnnlt.add("                                                    ...");

        // GÁN ADAPTER CHO LISTVIEW
        LVDonGian.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dsnnlt));

        // XỬ LÝ SỰ KIỆN CLICK ITEM
        LVDonGian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String giatri = dsnnlt.get(position);
                Toast.makeText(MainActivity3.this, giatri, Toast.LENGTH_LONG).show();
            }
        });
    }
}
