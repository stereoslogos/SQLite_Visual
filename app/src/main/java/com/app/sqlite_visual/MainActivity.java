package com.app.sqlite_visual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    TextInputEditText jetPlaca, jetMarca, jetModelo, jetValor;
    MaterialButton jbtGuardar, jbtConsultar, jbtEliminar, jbtlimpiar, jbtanular;
    Long resp;
    int sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        jetPlaca = findViewById(R.id.etPlaca);
        jetMarca = findViewById(R.id.etMarca);
        jetModelo = findViewById(R.id.etModelo);
        jetValor = findViewById(R.id.etPrecio);
        jbtGuardar = findViewById(R.id.btnGuardar);
        jbtConsultar = findViewById(R.id.btnConsultar);
        jbtEliminar = findViewById(R.id.btnElimininar);
        jbtlimpiar = findViewById(R.id.btnLimpiar);
        jbtanular = findViewById(R.id.btnAnular);

        jbtEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiar_campos();
            }
        });

    }

    public void consultar(View v) {
        String placa;
        placa = jetPlaca.getText().toString();
        if (placa.isEmpty()){
            jetPlaca.setError("La placa es requerida para la consulta");
            jetPlaca.requestFocus();
        }
        else{
            sw = 0;
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"concesionario.db",null,1);
            SQLiteDatabase db = admin.getReadableDatabase();
            Cursor fila = db.rawQuery("Select * from TblAutomovil where placa = '" + placa + "'", null);
            if(fila.moveToFirst()){
                /*
                0 = placa
                1 = marca
                2 = modelo
                3 = valor
                4 = activo
                */
                sw = 1;
                jetMarca.setText(fila.getString(1));
                jetModelo.setText(fila.getString(2));
                jetValor.setText(fila.getString(3));
            }else{
                Toast.makeText(this, "vehiculo no registrado", Toast.LENGTH_SHORT).show();
                jetPlaca.requestFocus();
            }
            db.close();
        }
    }

    public void guardar(View v) {
        String placa,marca,modelo,valor;
        placa=jetPlaca.getText().toString();
        marca=jetMarca.getText().toString();
        modelo=jetModelo.getText().toString();
        valor=jetValor.getText().toString();
        if (placa.isEmpty() || marca.isEmpty() || modelo.isEmpty() || valor.isEmpty()){
            Toast.makeText(this,"Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetPlaca.requestFocus();
        }
        else{
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"concesionario.db",null,1);
            SQLiteDatabase db = admin.getWritableDatabase();
            ContentValues dato = new ContentValues();
            dato.put("placa",placa);
            dato.put("marca",marca);
            dato.put("modelo",modelo);
            dato.put("valor",valor);
            resp = db.insert("TblAutomovil",null,dato);
            if (resp > 0){

                Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                limpiar_campos();
            }
            else {
                Toast.makeText(this, "Error guardando registro", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    }

    private void limpiar_campos(){
        jetPlaca.setError(null);
        jetPlaca.setText("");
        jetValor.setText("");
        jetModelo.setText("");
        jetMarca.setText("");
        jetPlaca.requestFocus();
    }

    public void limpiar(View view){
        limpiar_campos();
    }

    public void anular(View v){
        String placa,activo;
        placa = jetPlaca.getText().toString();
        if (placa.isEmpty()){
            jetPlaca.setError("Consulte la placa primero");
            jetPlaca.requestFocus();
        }
        else{
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"concesionario.db",null,1);
            SQLiteDatabase db = admin.getWritableDatabase();
            ContentValues dato = new ContentValues();
            activo = "no";
            dato.put("activo",activo);
            resp = db.insert("TblAutomovil",null,dato);
            if (resp > 0){
                Toast.makeText(this, "Registro anulado", Toast.LENGTH_SHORT).show();
                limpiar_campos();
            }
            else {
                Toast.makeText(this, "Error anulando registro", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    }
}