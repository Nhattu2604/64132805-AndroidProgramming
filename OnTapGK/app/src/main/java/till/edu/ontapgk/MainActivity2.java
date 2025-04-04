package till.edu.ontapgk;

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
import java.util.Timer;

public class MainActivity2 extends AppCompatActivity {
    ListView LVDonGian;
    ArrayList<String> dsnnlt;
    void TimDieuKhien() {
        LVDonGian = (ListView) findViewById(R.id.LV_Dongian);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TimDieuKhien();
        dsnnlt =new ArrayList<String>();
        dsnnlt.add("Hong");
        dsnnlt.add("Dao");
        dsnnlt.add("Lan");
        dsnnlt.add("Cuc");
        dsnnlt.add("...");
        dsnnlt.add("...");
        dsnnlt.add("...");

        ArrayAdapter<String> AdapterLV;
        AdapterLV = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dsnnlt);
        LVDonGian.setAdapter(AdapterLV);
        LVDonGian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String giatri = dsnnlt.get(position);
                Toast.makeText(MainActivity2.this, giatri, Toast.LENGTH_LONG).show();
            }
        });
    }

}