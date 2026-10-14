
package ies.tp2.versus;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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

public class EnfrentamientosActivity extends AppCompatActivity {

    private ArrayList<String> participantes = new ArrayList<>();
    private ArrayList<String> ganadores = new ArrayList<>();
    private int numeroRonda = 1;
    private int cantidadPartidos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_enfrentamientos);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });

        recibirDatos();
        activarBotones();
        generarPrimeraRonda();
    }

    // Recibimos los datos desde TorneosActivity
    private void recibirDatos() {
        String nombreTorneo = getIntent().getStringExtra("nombreTorneo");
        ArrayList<String> listaRecibida =
                getIntent().getStringArrayListExtra("participantes");

        if (nombreTorneo == null || listaRecibida == null || listaRecibida.size() < 2) {
            Toast.makeText(this, "No se recibieron los datos del torneo", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView txtNombreTorneo = (TextView) findViewById(R.id.txtNombreTorneo);
        txtNombreTorneo.setText(nombreTorneo);

        participantes.addAll(listaRecibida);
    }

    // Configuramos los botones de la pantalla
    private void activarBotones() {
        ImageView imgVolver = (ImageView) findViewById(R.id.imgVolver);

        imgVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accionVolver();
            }
        });

        Button btnSiguienteRonda = (Button) findViewById(R.id.btnSiguienteRonda);

        btnSiguienteRonda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accionSiguienteRonda();
            }
        });
    }

    // Mezclamos los participantes para generar los primeros partidos
    private void generarPrimeraRonda() {
        if (participantes.size() < 2) {
            return;
        }

        Collections.shuffle(participantes);
        generarEnfrentamientos();
    }

    // Limpiamos los partidos anteriores y generamos los nuevos
    private void generarEnfrentamientos() {
        LinearLayout layoutLista =
                (LinearLayout) findViewById(R.id.layoutListaEnfrentamientos);

        layoutLista.removeAllViews();
        ganadores.clear();

        TextView txtRonda = (TextView) findViewById(R.id.txtRonda);
        txtRonda.setText(obtenerNombreRonda());

        cantidadPartidos = participantes.size() / 2;
        int cantidadLibres = participantes.size() % 2;

        // Si hay una cantidad impar, el último pasa directamente
        if (cantidadLibres == 1) {
            String participanteLibre = participantes.get(participantes.size() - 1);
            ganadores.add(participanteLibre);
        }

        // Tomamos los participantes de a dos para formar los partidos
        for (int i = 0; i < cantidadPartidos; i++) {
            String participante1 = participantes.get(i * 2);
            String participante2 = participantes.get(i * 2 + 1);

            crearPartido(i, participante1, participante2);
        }

        if (cantidadLibres == 1) {
            mostrarParticipanteLibre(participantes.get(participantes.size() - 1));
        }

        actualizarContador();
    }

    // Creamos la tarjeta de cada enfrentamiento
    private void crearPartido(int numeroPartido, String participante1, String participante2) {
        LinearLayout layoutLista =
                (LinearLayout) findViewById(R.id.layoutListaEnfrentamientos);

        LinearLayout tarjeta = new LinearLayout(this);
        tarjeta.setOrientation(LinearLayout.VERTICAL);
        tarjeta.setPadding(dp(12), dp(12), dp(12), dp(12));
        tarjeta.setBackgroundColor(Color.rgb(242, 247, 253));

        LinearLayout.LayoutParams parametrosTarjeta = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        parametrosTarjeta.setMargins(0, 0, 0, dp(12));

        TextView txtPartido = new TextView(this);
        txtPartido.setText("PARTIDO " + (numeroPartido + 1));
        txtPartido.setTextSize(13);
        txtPartido.setTextColor(Color.rgb(120, 134, 151));
        tarjeta.addView(txtPartido);

        LinearLayout fila = new LinearLayout(this);
        fila.setOrientation(LinearLayout.HORIZONTAL);
        fila.setGravity(Gravity.CENTER_VERTICAL);

        LinearLayout.LayoutParams parametrosFila = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        parametrosFila.setMargins(0, dp(12), 0, 0);

        Button btnParticipante1 = crearBotonParticipante(participante1);
        Button btnParticipante2 = crearBotonParticipante(participante2);

        TextView txtVS = new TextView(this);
        txtVS.setText("VS");
        txtVS.setTextSize(12);
        txtVS.setTextColor(Color.rgb(120, 134, 151));
        txtVS.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams parametrosVS =
                new LinearLayout.LayoutParams(dp(36), dp(48));

        fila.addView(btnParticipante1);
        fila.addView(txtVS, parametrosVS);
        fila.addView(btnParticipante2);

        tarjeta.addView(fila, parametrosFila);

        // Al tocar un participante lo seleccionamos como ganador
        btnParticipante1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarGanador(
                        participante1, participante2,
                        btnParticipante1, btnParticipante2
                );
            }
        });

        btnParticipante2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarGanador(
                        participante2, participante1,
                        btnParticipante2, btnParticipante1
                );
            }
        });

        // Agregamos la tarjeta al LinearLayout del XML
        layoutLista.addView(tarjeta, parametrosTarjeta);
    }

    // Creamos los botones de los participantes desde Java
    private Button crearBotonParticipante(String nombre) {
        Button boton = new Button(this);
        boton.setText(nombre);
        boton.setTextSize(13);
        boton.setAllCaps(false);
        boton.setTextColor(Color.rgb(23, 33, 47));
        boton.setBackgroundTintList(
                ColorStateList.valueOf(Color.rgb(220, 235, 252))
        );
        boton.setMinWidth(0);
        boton.setMinimumWidth(0);

        LinearLayout.LayoutParams parametros =
                new LinearLayout.LayoutParams(0, dp(48), 1);

        boton.setLayoutParams(parametros);
        return boton;
    }

    // Guardamos al ganador y actualizamos los colores
    private void seleccionarGanador(
            String ganador,
            String perdedor,
            Button botonGanador,
            Button botonPerdedor) {

        if (ganadores.contains(perdedor)) {
            ganadores.remove(perdedor);
        }

        if (!ganadores.contains(ganador)) {
            ganadores.add(ganador);
        }

        botonGanador.setBackgroundTintList(
                ColorStateList.valueOf(Color.rgb(93, 147, 208))
        );
        botonGanador.setTextColor(Color.WHITE);

        botonPerdedor.setBackgroundTintList(
                ColorStateList.valueOf(Color.rgb(220, 235, 252))
        );
        botonPerdedor.setTextColor(Color.rgb(23, 33, 47));

        actualizarContador();
    }

    // Mostramos al participante que pasa sin jugar
    private void mostrarParticipanteLibre(String nombre) {
        LinearLayout layoutLista =
                (LinearLayout) findViewById(R.id.layoutListaEnfrentamientos);

        TextView txtLibre = new TextView(this);
        txtLibre.setText(nombre + " pasa automáticamente a la siguiente ronda");
        txtLibre.setTextSize(14);
        txtLibre.setTextColor(Color.rgb(93, 147, 208));
        txtLibre.setPadding(dp(12), dp(12), dp(12), dp(12));
        txtLibre.setBackgroundColor(Color.rgb(220, 235, 252));

        layoutLista.addView(txtLibre);
    }

    // Actualizamos el contador de partidos resueltos
    private void actualizarContador() {
        TextView txtContador =
                (TextView) findViewById(R.id.txtContadorPartidos);

        int cantidadLibres = participantes.size() % 2;
        int partidosResueltos = ganadores.size() - cantidadLibres;

        txtContador.setText(partidosResueltos + "/" + cantidadPartidos);
    }

    // Validamos los ganadores y avanzamos a la siguiente ronda
    private void accionSiguienteRonda() {
        int cantidadLibres = participantes.size() % 2;

        if (ganadores.size() != cantidadPartidos + cantidadLibres) {
            Toast.makeText(
                    this,
                    "Seleccioná todos los ganadores",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        // Los ganadores pasan a ser los participantes de la nueva ronda
        participantes.clear();
        participantes.addAll(ganadores);

        if (participantes.size() == 1) {
            mostrarCampeon(participantes.get(0));
            return;
        }

        // Sorteamos nuevamente los enfrentamientos
        Collections.shuffle(participantes);

        numeroRonda++;
        generarEnfrentamientos();
    }

    // Ocultamos los partidos y mostramos al campeón
    private void mostrarCampeon(String nombre) {
        LinearLayout layoutLista =
                (LinearLayout) findViewById(R.id.layoutListaEnfrentamientos);

        LinearLayout layoutCampeon =
                (LinearLayout) findViewById(R.id.layoutCampeon);

        TextView txtNombreCampeon =
                (TextView) findViewById(R.id.txtNombreCampeon);

        TextView txtRonda =
                (TextView) findViewById(R.id.txtRonda);

        TextView txtIndicaciones =
                (TextView) findViewById(R.id.txtIndicaciones);

        Button btnSiguienteRonda =
                (Button) findViewById(R.id.btnSiguienteRonda);

        layoutLista.removeAllViews();
        txtRonda.setText("Torneo finalizado");
        txtIndicaciones.setVisibility(View.GONE);
        txtNombreCampeon.setText(nombre);
        layoutCampeon.setVisibility(View.VISIBLE);
        btnSiguienteRonda.setVisibility(View.GONE);
    }

    // Definimos el nombre de la ronda según la cantidad de participantes
    private String obtenerNombreRonda() {
        if (participantes.size() == 2) {
            return "Final";
        }

        if (participantes.size() <= 4) {
            return "Semifinales";
        }

        if (participantes.size() <= 8) {
            return "Cuartos de final";
        }

        if (participantes.size() <= 16) {
            return "Octavos de final";
        }

        return "Ronda " + numeroRonda;
    }

    private void accionVolver() {
        finish();
    }

    // Convertimos las medidas para adaptarlas a la densidad de pantalla
    private int dp(int valor) {
        return (int) (valor * getResources().getDisplayMetrics().density + 0.5f);
    }
}
