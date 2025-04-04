package till.edu.ontapgk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button MH1;
    Button MH2;
    Button MH3;
    Button MH4;
    void TimDieuKhien() {
    MH1 = findViewById(R.id.btnMH1);
        MH2 = findViewById(R.id.btnMH2);
        MH3 = findViewById(R.id.btnMH3);
        MH4 = findViewById(R.id.btnMH4);
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
    MH1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intentMH1 = new Intent(MainActivity.this, MainActivity1.class);
            startActivity(intentMH1);
        }
    });
        MH2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMH2 = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intentMH2);
            }
        });
        MH3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMH3 = new Intent(MainActivity.this, MainActivity3.class);
                startActivity(intentMH3);
            }
        });
        MH4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMH4 = new Intent(MainActivity.this, MainActivity4.class);
                startActivity(intentMH4);
            }
        });
    }

}