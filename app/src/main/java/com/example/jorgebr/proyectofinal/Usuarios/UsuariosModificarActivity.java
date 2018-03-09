/*ACTIVITY CREADO PARA INSERTAR*/
package com.example.jorgebr.practica4.Usuarios;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.jorgebr.practica4.Constantes.G;
import com.example.jorgebr.practica4.Pojos.Empleados;
import com.example.jorgebr.practica4.Pojos.Usuarios;
import com.example.jorgebr.practica4.Proveedor.Contrato;
import com.example.jorgebr.practica4.Proveedor.EmpleadosProveedor;
import com.example.jorgebr.practica4.Proveedor.UsuariosProveedor;
import com.example.jorgebr.practica4.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsuariosModificarActivity extends AppCompatActivity {
    EditText edittextNombre;
    EditText edittextPassed;
    EditText mail;
    int idusuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_detalle);
        /*AÑADIMOS LA TOOLBAR*/
        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar_detalleactivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //flechita para ir al padre
        setTitle("MODIFICAR USUARIO");
        //variables que toman datos de sus campos
        edittextNombre=(EditText)findViewById(R.id.editTextUsuarioNombre);
        edittextPassed=(EditText)findViewById(R.id.editTextUsuariosPassword);
        mail=(EditText)findViewById(R.id.editTextUsuariosMail);
        idusuario= this.getIntent().getExtras().getInt(Contrato.Usuarios._ID); //obtenemos el ID
        //Leemos el registro
        Usuarios usuarios= UsuariosProveedor.readRecord(getContentResolver(),idusuario);
        //ya tenemos el cliente y debemos cargarlo
        edittextNombre.setText(usuarios.getNuser());
        edittextPassed.setText(usuarios.getPass());
        mail.setText(usuarios.getEmail());
    }

    /*AÑADIMOS PARA QUE APAREZCA EL BOTÓN DE GUARDAR*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuitem=menu.add(Menu.NONE, G.Guardar, Menu.NONE,"Guardar"); //sin grupos,identificador, da igual orden
		menuitem.setIcon(R.mipmap.ic_guardar);  //añadimos el icono
		menuitem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS); //mostramos siempre el icono
        return super.onCreateOptionsMenu(menu);
    }

    /*MÉTODO PARA TRATAR QUE HACER AL PULSAR GUARDAR*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case G.Guardar: attemptEditar();//llamamos a un métofo para guardar
                            break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*MÉTODO LLAMADO PARA EDITAR DATOS*/
    private void attemptEditar() {
        //borramos posibles mensajes que tengamos
        edittextNombre.setError(null);
        edittextPassed.setError(null);
        mail.setError(null);
        //tomamos los datos de los campos
        String nombre=String.valueOf(edittextNombre.getText());
        String pass=String.valueOf(edittextPassed.getText());
        String email=String.valueOf(mail.getText());
        //Empezamos la validación
        if (TextUtils.isEmpty(nombre)){  //no se insertó nombre
            edittextNombre.setError(getString(R.string.requerido));
            edittextNombre.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pass)){  //no se insertó nombre
            edittextPassed.setError(getString(R.string.requerido));
            edittextPassed.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)){  //no se insertó nombre
            mail.setError(getString(R.string.requerido));
            edittextNombre.requestFocus();
            return;
        }
        if (!validaEmail(email)) {
            mail.setError(getString(R.string.formatoIncorrecto));
            mail.requestFocus(); //mantiene el foco
            return;  //vuelve a previa validacion
        }
        Usuarios usuarios=new Usuarios(idusuario,nombre,pass,email);         //creamos un nuevo EMPLEADOS con los datos obtenidos
        UsuariosProveedor.update(getContentResolver(),usuarios); //Creamos una nueva clase que se encarga de la ACTUALIZACIÓN
        finish();  //Salimos de la actividad tras insertar
    }

    /*******************FUNCIÓN QUE RECOGE EL INTENT DEL OTRO ACTIVITY Y RELLENA CAMPOS***************************************************/
    public static boolean validaEmail(String email) {
        //Expresión regular para validar un Email
        String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
