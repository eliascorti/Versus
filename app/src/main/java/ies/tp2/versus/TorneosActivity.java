
package ies.tp2.versus;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;

import java.util.ArrayList;

public class TorneosActivity extends AppCompatActivity {

    private ArrayList<String> participantes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_torneos);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
            );

            return insets;
        });

        activarBotones();
    }

    private void activarBotones() {

        // Botón agregar participante
        Button btnAgregar = (Button) findViewById(R.id.btnAgregar);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accionAgregarParticipante();
            }
        });

        // Botón generar torneo
        Button btnGenerarTorneo = (Button) findViewById(R.id.btnGenerarTorneo);

        btnGenerarTorneo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accionGenerarTorneo();
            }
        });

        // Botón volver
        ImageView imgVolver = (ImageView) findViewById(R.id.imgVolver);

        imgVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accionVolver();
            }
        });
    }

    private void accionAgregarParticipante() {

        EditText edtParticipante =
                (EditText) findViewById(R.id.edtParticipante);

        LinearLayout layoutLista =
                (LinearLayout) findViewById(R.id.layoutListaParticipantes);

        String nombre = edtParticipante.getText().toString().trim();

        // Validar nombre vacío
        if (nombre.isEmpty()) {
            Toast.makeText(this, "Ingresá un participante", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar máximo de participantes
        if (participantes.size() >= 32) {
            Toast.makeText(this, "Máximo 32 participantes", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar nombres repetidos
        for (String participante : participantes) {
            if (participante.equalsIgnoreCase(nombre)) {Toast.makeText(this, "El participante ya existe", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Guardar participante
        participantes.add(nombre);

        // Crear fila horizontal
        LinearLayout fila = new LinearLayout(this);
        fila.setOrientation(LinearLayout.HORIZONTAL);
        fila.setGravity(Gravity.CENTER_VERTICAL);

        int paddingHorizontal = dp(12);
        int paddingVertical = dp(8);

        fila.setPadding(
                paddingHorizontal,
                paddingVertical,
                paddingVertical,
                paddingVertical
        );

        fila.setBackgroundColor(Color.rgb(242, 247, 253));

        LinearLayout.LayoutParams parametrosFila =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        parametrosFila.setMargins(0, 0, 0, dp(8));

        // Nombre del participante
        TextView txtNombre = new TextView(this);

        txtNombre.setText(nombre);
        txtNombre.setTextSize(16);
        txtNombre.setTextColor(Color.rgb(23, 33, 47));

        LinearLayout.LayoutParams parametrosNombre =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        fila.addView(txtNombre, parametrosNombre);

        // Botón eliminar
        Button btnEliminar = new Button(this);

        btnEliminar.setText("−");
        btnEliminar.setTextSize(22);
        btnEliminar.setTextColor(Color.rgb(190, 50, 65));
        btnEliminar.setAllCaps(false);
        btnEliminar.setGravity(Gravity.CENTER);

        // Quitar espacios internos predeterminados
        btnEliminar.setMinWidth(0);
        btnEliminar.setMinHeight(0);
        btnEliminar.setMinimumWidth(0);
        btnEliminar.setMinimumHeight(0);
        btnEliminar.setPadding(0, 0, 0, 0);
        btnEliminar.setIncludeFontPadding(false);

        // Fondo rojo claro
        btnEliminar.setBackgroundTintList(
                ColorStateList.valueOf(
                        Color.rgb(255, 232, 235)
                )
        );

        // Tamaño de 40dp
        LinearLayout.LayoutParams parametrosEliminar =
                new LinearLayout.LayoutParams(
                        dp(40),
                        dp(40)
                );

        parametrosEliminar.setMargins(dp(8), 0, 0, 0);

        fila.addView(btnEliminar, parametrosEliminar);

        // Evento del botón eliminar
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accionEliminarParticipante(nombre, fila);
            }
        });

        // Mostrar participante en pantalla
        layoutLista.addView(fila, parametrosFila);

        actualizarContador();

        // Limpiar campo
        edtParticipante.setText("");
    }

    private void accionEliminarParticipante(String nombre, LinearLayout fila) {

        LinearLayout layoutLista =
                (LinearLayout) findViewById(R.id.layoutListaParticipantes);

        // Eliminar participante de la lista
        participantes.remove(nombre);

        // Eliminar fila de la pantalla
        layoutLista.removeView(fila);

        actualizarContador();
    }

    private void actualizarContador() {

        TextView txtContador =
                (TextView) findViewById(R.id.txtContador);

        txtContador.setText(participantes.size() + "/32");
    }


    private void accionGenerarTorneo() {

        EditText edtNombreTorneo =
                (EditText) findViewById(R.id.edtNombreTorneo);

        String nombreTorneo =
                edtNombreTorneo.getText().toString().trim();

        if (nombreTorneo.isEmpty()) {
            Toast.makeText(
                    this,
                    "Ingresá el nombre del torneo",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (participantes.size() < 2) {
            Toast.makeText(
                    this,
                    "Se necesitan al menos 2 participantes",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        Intent intent = new Intent(
                TorneosActivity.this,
                EnfrentamientosActivity.class
        );

        intent.putExtra("nombreTorneo", nombreTorneo);
        intent.putStringArrayListExtra("participantes", participantes);

        startActivity(intent);
    }


    private void accionVolver() {
        finish();
    }

    // Convertir dp a píxeles
    private int dp(int valor) {
        return (int) (
                valor * getResources().getDisplayMetrics().density + 0.5f
        );
    }
}
