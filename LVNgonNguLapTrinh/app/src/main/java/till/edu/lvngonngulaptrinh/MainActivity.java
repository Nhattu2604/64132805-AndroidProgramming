package till.edu.lvngonngulaptrinh;

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

public class MainActivity extends AppCompatActivity {
    ListView ListViewNNLT;
    ArrayList<String> dsNgonNguLT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //B1: chuan bi du lieu, hard-core
        ListViewNNLT = findViewById(R.id.LV_NgonNguLapTrinh);
        dsNgonNguLT = new ArrayList<String>();
        dsNgonNguLT.add("Python");
        dsNgonNguLT.add("Php");
        dsNgonNguLT.add("Java");
        //B2:
        ArrayAdapter<String> addapterNNLT;
        addapterNNLT = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dsNgonNguLT);
        //b3 gan addapter
        ListViewNNLT.setAdapter(addapterNNLT);
        //b4 gan bo lang nghe va sly su kien
        ListViewNNLT.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // bien position da chua vi tri cua item duoc click
                String giatriduocchon = dsNgonNguLT.get(position);
                // lam theo yeu cau bat ky doi voi gia tri get duoc
                // vd toast len
                Toast.makeText(MainActivity.this,giatriduocchon,Toast.LENGTH_LONG).show();
            }
        });
    }
}