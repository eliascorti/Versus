package ies.tp2.versus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        activarTarjetas();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //comentario de prueba

    private void activarTarjetas() {
        CardView cardTorneos = findViewById(R.id.cardTorneos);
        CardView cardEquipos = findViewById(R.id.cardEquipos);

        cardTorneos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               accionTorneos();
            }
        });

        cardEquipos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accionEquipos();
            }
        });
    }

    private void accionEquipos() {
        Intent intent = new Intent(MainActivity.this, EquiposActivity.class);
        startActivity(intent);
    }

    private void accionTorneos() {
        Intent intent = new Intent(MainActivity.this, TorneosActivity.class);
        startActivity(intent);
    }
}