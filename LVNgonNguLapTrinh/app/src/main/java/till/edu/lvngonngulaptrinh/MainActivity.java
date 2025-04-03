package till.edu.lvngonngulaptrinh;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ListView LV_NgonNguLapTrinh;
    ArrayList<String> dsnnlt;

    void TimDieuKhien(){
        LV_NgonNguLapTrinh = (ListView) findViewById(R.id.LV_NgonNguLapTrinh);
    }
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
         TimDieuKhien();
         dsnnlt = new ArrayList<String>();
         dsnnlt.add("Hong");
         dsnnlt.add("Dao");
         dsnnlt.add("Lan");
         dsnnlt.add("Cuc");
         dsnnlt.add("...");
         dsnnlt.add("...");
         dsnnlt.add("...");

        ArrayAdapter<String> AdapterLV;
        AdapterLV = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dsnnlt);
        LV_NgonNguLapTrinh.setAdapter(AdapterLV);
        LV_NgonNguLapTrinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String giatri = dsnnlt.get(position);
                Toast.makeText(MainActivity.this,giatri, Toast.LENGTH_LONG).show();
            }
        });
    }
}