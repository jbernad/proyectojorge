package com.example.jorgebr.proyectofinal;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class RegistroActivity extends AppCompatActivity {
    /*********DECLARACIÓN DE VARIABLES*****************/
    EditText txtnuser,txtpasswd,txtmail,txtdni,txtphone,txtnomdog;    //variables para coger los datos del formulario de registro
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        /*********TÍTULO PARA EL ACTIVITY******************/
        setTitle("REGISTRO DE USUARIO");

        /*********AÑADIMOS ICONO A LA APPBAR*****************/
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        /*********CAPTURAMOS LAS VISTAS NECESARIAS*****************/
        txtnuser = (EditText)findViewById(R.id.TxtNombreUsuarioReg);
        txtpasswd = (EditText)findViewById(R.id.TxtcontraseñaReg);
        txtmail = (EditText)findViewById(R.id.TxtemailReg);
        txtdni = (EditText)findViewById(R.id.TxtDNIReg);
        txtphone = (EditText)findViewById(R.id.TxtPhoneReg);
        txtnomdog = (EditText)findViewById(R.id.Txtmascota);




    }

    /*SELECCION DE FLECHA ATRAS, CIERRA EL ACTIVITY REGISTRO SIN RECORDAR DATOS*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: finish();  //volver atras cerramos el Activity
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}


