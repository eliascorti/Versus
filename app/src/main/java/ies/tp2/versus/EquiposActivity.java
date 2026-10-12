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
import androidx.core.content.ContextCompat;


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
                    getString(R.string.error_jugadores_insuficientes),
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Copiamos la lista para no modificar la original
        ArrayList<String> jugadoresMezclados =
                new ArrayList<>(jugadores);

        // Mezclamos los jugadores aleatoriamente
        Collections.shuffle(jugadoresMezclados);

        int jugadoresPorEquipo =
                jugadoresMezclados.size() / cantidadEquipos;

        int jugadoresLibres =
                jugadoresMezclados.size() % cantidadEquipos;

        // Eliminamos los resultados del sorteo anterior
        layoutResultados.removeAllViews();

        int posicionJugador = 0;

        // Crear las tarjetas de cada equipo
        for (int equipo = 1; equipo <= cantidadEquipos; equipo++) {

            LinearLayout tarjetaEquipo = crearTarjeta();

            TextView tituloEquipo = crearTituloTarjeta(
                    getString(R.string.equipo_numero, equipo)
            );

            tarjetaEquipo.addView(tituloEquipo);

            for (int j = 0; j < jugadoresPorEquipo; j++) {

                TextView jugador = crearTextoJugador(
                        jugadoresMezclados.get(posicionJugador)
                );

                tarjetaEquipo.addView(jugador);

                posicionJugador++;
            }

            layoutResultados.addView(tarjetaEquipo);
        }

        // Crear tarjeta para jugadores libres
        if (jugadoresLibres > 0) {

            LinearLayout tarjetaLibres = crearTarjeta();

            TextView tituloLibres = crearTituloTarjeta(
                    getString(R.string.jugadores_libres_titulo)
            );

            tarjetaLibres.addView(tituloLibres);

            while (posicionJugador < jugadoresMezclados.size()) {

                TextView jugadorLibre = crearTextoJugador(
                        jugadoresMezclados.get(posicionJugador)
                );

                tarjetaLibres.addView(jugadorLibre);

                posicionJugador++;
            }

            layoutResultados.addView(tarjetaLibres);
        }
    }

    private LinearLayout crearTarjeta() {

        LinearLayout tarjeta = new LinearLayout(this);

        tarjeta.setOrientation(LinearLayout.VERTICAL);

        tarjeta.setBackgroundResource(
                R.drawable.resultado_equipo_background
        );

        LinearLayout.LayoutParams parametros =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        parametros.setMargins(
                0,
                convertirDp(12),
                0,
                convertirDp(12)
        );

        tarjeta.setLayoutParams(parametros);

        tarjeta.setPadding(
                convertirDp(16),
                convertirDp(16),
                convertirDp(16),
                convertirDp(16)
        );

        return tarjeta;
    }

    private TextView crearTituloTarjeta(String titulo) {

        TextView textView = new TextView(this);

        textView.setText(titulo);

        textView.setTextSize(18);

        textView.setTextColor(
                ContextCompat.getColor(
                        this,
                        android.R.color.black
                )
        );

        textView.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        textView.setPadding(
                0,
                0,
                0,
                convertirDp(10)
        );

        return textView;
    }

    private int convertirDp(int dp) {

        float densidad = getResources().getDisplayMetrics().density;

        return (int) (dp * densidad);
    }

    private TextView crearTextoJugador(String nombre) {

        TextView textView = new TextView(this);

        textView.setText("• " + nombre);

        textView.setTextSize(16);

        textView.setTextColor(
                ContextCompat.getColor(
                        this,
                        android.R.color.black
                )
        );

        textView.setPadding(
                convertirDp(4),
                convertirDp(4),
                0,
                convertirDp(4)
        );

        return textView;
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