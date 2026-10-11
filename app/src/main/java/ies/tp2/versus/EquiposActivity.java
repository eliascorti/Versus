package ies.tp2.versus;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Collections;


public class EquiposActivity extends AppCompatActivity {

    private EditText edtJugador;

    private Button btnAgregarJugador;
    private Button btnRestarEquipo;
    private Button btnSumarEquipo;
    private Button btnGenerarEquipos;

    private TextView txtContadorJugadores;
    private TextView txtCantidadEquipos;
    private TextView txtJugadoresPorEquipo;
    private TextView txtJugadoresLibres;

    private LinearLayout layoutListaJugadores;

    private ImageView imgVolver;

    private ArrayList<String> jugadores;

    private int cantidadEquipos = 2;

    private static final int MAX_JUGADORES = 32;

    private LinearLayout layoutResultados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_equipos);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars =
                            insets.getInsets(WindowInsetsCompat.Type.systemBars());

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        // Inicializamos la lista de jugadores
        jugadores = new ArrayList<>();

        // Relacionamos Java con los elementos del XML
        edtJugador = findViewById(R.id.edtJugador);

        btnAgregarJugador = findViewById(R.id.btnAgregarJugador);
        btnRestarEquipo = findViewById(R.id.btnRestarEquipo);
        btnSumarEquipo = findViewById(R.id.btnSumarEquipo);
        btnGenerarEquipos = findViewById(R.id.btnGenerarEquipos);

        txtContadorJugadores = findViewById(R.id.txtContadorJugadores);
        txtCantidadEquipos = findViewById(R.id.txtCantidadEquipos);
        txtJugadoresPorEquipo = findViewById(R.id.txtJugadoresPorEquipo);
        txtJugadoresLibres = findViewById(R.id.txtJugadoresLibres);

        layoutListaJugadores = findViewById(R.id.layoutListaJugadores);

        imgVolver = findViewById(R.id.imgVolver);

        // Botón para volver
        imgVolver.setOnClickListener(v -> finish());

        // Botón agregar jugador
        btnAgregarJugador.setOnClickListener(v -> agregarJugador());

        // Restar equipos
        btnRestarEquipo.setOnClickListener(v -> {

            if (cantidadEquipos > 2) {
                cantidadEquipos--;

                txtCantidadEquipos.setText(
                        String.valueOf(cantidadEquipos)
                );

                actualizarDistribucion();
            }

        });

        // Sumar equipos
        btnSumarEquipo.setOnClickListener(v -> {

            cantidadEquipos++;

            txtCantidadEquipos.setText(
                    String.valueOf(cantidadEquipos)
            );

            actualizarDistribucion();

        });

        actualizarDistribucion();

        // Generar equipos
        btnGenerarEquipos.setOnClickListener(v -> generarEquipos());

        actualizarDistribucion();

        layoutResultados = findViewById(R.id.layoutResultados);
    }

    private void generarEquipos() {

        if (jugadores.size() < cantidadEquipos) {
            Toast.makeText(
                    this,
                    "No hay suficientes jugadores para generar los equipos",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Copiamos la lista para no modificar la original
        ArrayList<String> jugadoresMezclados = new ArrayList<>(jugadores);

        // Mezclamos aleatoriamente
        Collections.shuffle(jugadoresMezclados);

        int jugadoresPorEquipo =
                jugadoresMezclados.size() / cantidadEquipos;

        int jugadoresLibres =
                jugadoresMezclados.size() % cantidadEquipos;

        // Borramos el resultado anterior
        layoutResultados.removeAllViews();

        int posicionJugador = 0;

        // Creamos cada equipo
        for (int equipo = 1; equipo <= cantidadEquipos; equipo++) {

            TextView tituloEquipo = new TextView(this);

            tituloEquipo.setText("Equipo " + equipo);
            tituloEquipo.setTextSize(18);
            tituloEquipo.setTextColor(
                    getResources().getColor(android.R.color.black)
            );
            tituloEquipo.setPadding(0, 20, 0, 8);

            layoutResultados.addView(tituloEquipo);

            for (int j = 0; j < jugadoresPorEquipo; j++) {

                TextView jugador = new TextView(this);

                jugador.setText(
                        "• " + jugadoresMezclados.get(posicionJugador)
                );

                jugador.setTextSize(16);
                jugador.setPadding(8, 4, 0, 4);

                layoutResultados.addView(jugador);

                posicionJugador++;
            }
        }

        // Mostramos los jugadores que sobraron
        if (jugadoresLibres > 0) {

            TextView tituloLibres = new TextView(this);

            tituloLibres.setText("Jugadores libres");
            tituloLibres.setTextSize(18);
            tituloLibres.setTextColor(
                    getResources().getColor(android.R.color.black)
            );
            tituloLibres.setPadding(0, 24, 0, 8);

            layoutResultados.addView(tituloLibres);

            while (posicionJugador < jugadoresMezclados.size()) {

                TextView jugadorLibre = new TextView(this);

                jugadorLibre.setText(
                        "• " + jugadoresMezclados.get(posicionJugador)
                );

                jugadorLibre.setTextSize(16);
                jugadorLibre.setPadding(8, 4, 0, 4);

                layoutResultados.addView(jugadorLibre);

                posicionJugador++;
            }
        }
    }

    private void agregarJugador() {

        String nombre = edtJugador
                .getText()
                .toString()
                .trim();

        if (nombre.isEmpty()) {

            Toast.makeText(
                    this,
                    "Ingresá un nombre",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (jugadores.size() >= MAX_JUGADORES) {

            Toast.makeText(
                    this,
                    "Máximo 32 jugadores",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        jugadores.add(nombre);

        edtJugador.setText("");

        actualizarListaJugadores();
        actualizarContador();
        actualizarDistribucion();
    }

    private void actualizarListaJugadores() {

        layoutListaJugadores.removeAllViews();

        for (int i = 0; i < jugadores.size(); i++) {

            String nombre = jugadores.get(i);

            LinearLayout fila = new LinearLayout(this);

            fila.setOrientation(LinearLayout.HORIZONTAL);
            fila.setPadding(8, 8, 8, 8);

            TextView txtNombre = new TextView(this);

            txtNombre.setText(nombre);
            txtNombre.setTextSize(16);

            txtNombre.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1
                    )
            );

            Button btnEliminar = new Button(this);

            btnEliminar.setText("X");

            int posicion = i;

            btnEliminar.setOnClickListener(v -> {

                jugadores.remove(posicion);

                actualizarListaJugadores();
                actualizarContador();
                actualizarDistribucion();

            });

            fila.addView(txtNombre);
            fila.addView(btnEliminar);

            layoutListaJugadores.addView(fila);
        }
    }

    private void actualizarContador() {

        txtContadorJugadores.setText(
                jugadores.size() + "/" + MAX_JUGADORES
        );
    }

    private void actualizarDistribucion() {

        int totalJugadores = jugadores.size();

        int jugadoresPorEquipo =
                totalJugadores / cantidadEquipos;

        int jugadoresLibres =
                totalJugadores % cantidadEquipos;

        txtJugadoresPorEquipo.setText(
                getString(
                        R.string.formato_jugadores_por_equipo,
                        jugadoresPorEquipo
                )
        );

        txtJugadoresLibres.setText(
                getString(
                        R.string.formato_jugadores_libres,
                        jugadoresLibres
                )
        );
    }
}