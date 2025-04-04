package till.edu.ontapgk;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity3 extends AppCompatActivity {
@Override
    protected void onCreate(Bundle savedInstanceState) {
    LandScapeAdapter landScapeAdapter;
    ArrayList<LandScape> recyclerViewDatas;
    RecyclerView recyclerViewLandScape;

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        //3
    recyclerViewDatas = getDataForRecyclerView();
    //4
    recyclerViewLandScape =findViewById(R.id.recyclerLand);
    RecyclerView.LayoutManager layoutLinear = new LinearLayoutManager(this);
    recyclerViewLandScape.setLayoutManager(layoutLinear);
    //6
    landScapeAdapter = new LandScapeAdapter(this,recyclerViewDatas);
    //7
    recyclerViewLandScape.setAdapter(landScapeAdapter);
}

    ArrayList<LandScape> getDataForRecyclerView() {
        ArrayList<LandScape> dsDuLieu = new ArrayList<LandScape>();
        LandScape landScape1 = new LandScape("hero","Loa Bluetooth");
        dsDuLieu.add(landScape1);
        dsDuLieu.add(new LandScape("keyboard","Ban Phim"));
        dsDuLieu.add(new LandScape("monitor","Man hinh"));
        dsDuLieu.add(new LandScape("iphone","Iphone"));
    return dsDuLieu;
    }

    }
