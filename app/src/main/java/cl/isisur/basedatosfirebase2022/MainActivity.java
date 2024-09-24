package cl.isisur.basedatosfirebase2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cl.isisur.basedatosfirebase2022.Clases.Generacion;

public class MainActivity extends AppCompatActivity {
    private List<Generacion> Listcompus = new ArrayList<>();
    private List<String> Listcompusmarca = new ArrayList<>();
    ArrayAdapter<String> arrayAdapterString;

    EditText eTMarca, eTModelo;
    Button bTBoton, btEliminar;
    ListView lvListadoPcs;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eTMarca = findViewById(R.id.eTMarca);
        eTModelo = findViewById(R.id.eTModelo);
        bTBoton = findViewById(R.id.bTAgregar);
        btEliminar = findViewById(R.id.btEliminar);
        lvListadoPcs = findViewById(R.id.lvListadoPcs);

        inicializarFireBase();
        listarDatos();

        bTBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String marca = eTMarca.getText().toString().trim();
                String modelo = eTModelo.getText().toString().trim();

                if (marca.isEmpty() || modelo.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                Generacion generacion = new Generacion();
                generacion.setIdGeneracion(UUID.randomUUID().toString());
                generacion.setNombre(marca);
                generacion.setEstado(modelo);

                databaseReference.child("pcs").child(generacion.getIdGeneracion()).setValue(generacion)
                        .addOnSuccessListener(aVoid -> {
                            eTMarca.setText("");
                            eTModelo.setText("");
                            Toast.makeText(MainActivity.this, "PC agregado", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(MainActivity.this, "Error al agregar", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        btEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Listcompus.isEmpty()) {
                    Generacion generacion = Listcompus.get(0);
                    databaseReference.child("pcs").child(generacion.getIdGeneracion()).removeValue()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(MainActivity.this, "PC eliminado", Toast.LENGTH_SHORT).show();
                                listarDatos();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(MainActivity.this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(MainActivity.this, "No hay elementos para eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void listarDatos() {
        databaseReference.child("pcs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Listcompus.clear();
                Listcompusmarca.clear();

                for (DataSnapshot objs : snapshot.getChildren()) {
                    Generacion li = objs.getValue(Generacion.class);
                    if (li != null) {
                        Listcompus.add(li);
                        Listcompusmarca.add(li.getNombre() + " " + li.getEstado());
                    }
                }

                arrayAdapterString = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, Listcompusmarca);
                lvListadoPcs.setAdapter(arrayAdapterString);

                Toast.makeText(MainActivity.this, "Datos cargados correctamente", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void inicializarFireBase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }
}
