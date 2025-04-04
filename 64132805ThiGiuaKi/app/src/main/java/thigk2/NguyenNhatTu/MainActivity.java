package thigk2.NguyenNhatTu;

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
    Button btnMH2;
    Button btnMH3;
    Button btnMH4;
    Button btnMH5;
    Button btnMH6;

    void TimDieuKhien() {
    btnMH2 = findViewById(R.id.btnMH2);
    btnMH3 = findViewById(R.id.btnMH3);
    btnMH4 = findViewById(R.id.btnMH4);
    btnMH5 = findViewById(R.id.btnMH5);
        btnMH6 = findViewById(R.id.btnMH6);
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
        btnMH2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMH2 =new Intent(MainActivity.this,MainActivity2.class);
            startActivity(intentMH2);
            }
        });
        btnMH3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMH3 =new Intent(MainActivity.this,MainActivity3.class);
                startActivity(intentMH3);
            }
        });
        btnMH4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMH4 =new Intent(MainActivity.this,MainActivity4.class);
                startActivity(intentMH4);
            }
        });
        btnMH5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMH5 =new Intent(MainActivity.this,MainActivity5.class);
                startActivity(intentMH5);
            }
        });
        btnMH6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMH6 =new Intent(MainActivity.this,MainActivity6.class);
                startActivity(intentMH6);
            }
        });

    }

}