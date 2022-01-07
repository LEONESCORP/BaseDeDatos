package com.jesusleon.basededatos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText et_codigo, et_descripcion, et_precio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_codigo = (EditText) findViewById(R.id.txt_codigo);
        et_descripcion = (EditText) findViewById(R.id.txt_descripcion);
        et_precio = (EditText) findViewById(R.id.txt_precio);
    }

    //CON ESTE MÉTODO SE DA DE ALTA EL PRODUCTO
    public void Registrar (View view){
        AdminSQL admin = new AdminSQL(this,"administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();
        String codigo = et_codigo.getText().toString();
        String descripcion = et_descripcion.getText().toString();
        String precio = et_precio.getText().toString();

        //Para corroborar que si se hayan ingresado datos en los espacios de la aplicación
        if(!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()){

            //Para ingresar los datos en sus espacios dentro del a tabla de la base de datos
            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo);
            registro.put("descripcion", descripcion);
            registro.put("precio", precio);

            //Para mandarlos a la base de datos
            BaseDeDatos.insert("articulos", null, registro);
            BaseDeDatos.close();
            et_codigo.setText("");
            et_descripcion.setText("");
            et_precio.setText("");

            //Mensaje en pantalla
            Toast.makeText(this, "Registro exitos", Toast.LENGTH_LONG).show();
        }else{
            //Mensaje en pantalla
            Toast.makeText(this,"Debes llenar todos los campos", Toast.LENGTH_LONG).show();
        }
    }

    //METODO PARA CONSULTAR UN ARTICULO O PRODUCTO
    public void Buscar (View view){
        AdminSQL admin = new AdminSQL(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigo = et_codigo.getText().toString();

        if(!codigo.isEmpty()){
            Cursor fila = BaseDeDatos.rawQuery
                    ("select descripcion, precio from articulos where codigo ="+codigo, null);
            if(fila.moveToFirst()){
                et_descripcion.setText(fila.getString(0));//Siempre debe tener un cero
                et_precio.setText(fila.getString(1));
                BaseDeDatos.close();
            }else{
                Toast.makeText(this,"No existe el artículo", Toast.LENGTH_LONG).show();
                BaseDeDatos.close();
            }

        }else{
            Toast.makeText(this, "Debes introducir el código del artículo", Toast.LENGTH_LONG).show();
            BaseDeDatos.close();
        }
    }

    //METODO PARA ELIMINAR UN PRODUCTO O ARTICULO
    public void Eliminar (View view){
        AdminSQL admin = new AdminSQL(this, "administracion", null,1);
        //Para abrir la base de datos en modo lectura y escritura
        SQLiteDatabase BaseDatabase = admin.getWritableDatabase();
        String codigo = et_codigo.getText().toString();
        if(!codigo.isEmpty()){
            int cantidad = BaseDatabase.delete("articulos", "codigo="+codigo,null);
            BaseDatabase.close();

            et_codigo.setText("");
            et_descripcion.setText("");
            et_precio.setText("");

            if(cantidad == 1){
                Toast.makeText(this, "Artículo eliminado con exito", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"El artículo no existe", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Debes introducir el código del artículo", Toast.LENGTH_LONG).show();
        }
    }

    //METODO PARA MODIFICAR UN ARTICULO O PRODDUCTO
    public void Modificar (View view){
        AdminSQL admin = new AdminSQL(this, "administracion", null, 1);

        //Aquí abrimos la base de datos
        SQLiteDatabase BaseDataBase = admin.getWritableDatabase();

        String codigo = et_codigo.getText().toString();
        String descripcion = et_descripcion.getText().toString();
        String precio = et_precio.getText().toString();

        if(!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()){
            ContentValues registro = new ContentValues();

            registro.put("codigo", codigo);
            registro.put("descripcion", descripcion);
            registro.put("precio", precio);

            //Esta es la linea de codigo para modificar los balo
            int cantidad = BaseDataBase.update
                    ("articulos", registro,"codigo="+codigo,null);
            //Aquí cerramos la base de datos
            BaseDataBase.close();

            if(cantidad == 1){
                Toast.makeText(this, "Artículo modificado correctamente", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "El artículo no existe", Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_LONG).show();
        }
    }
}