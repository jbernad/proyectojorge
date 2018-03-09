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
import com.example.jorgebr.practica4.Proveedor.EmpleadosProveedor;
import com.example.jorgebr.practica4.Proveedor.UsuariosProveedor;
import com.example.jorgebr.practica4.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsuariosInsertarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_detalle);
        /*AÑADIMOS LA TOOLBAR*/
        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar_detalleactivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //flechita para ir al padre
        setTitle("INSERTAR USUARIO");
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
            case G.Guardar: attemptGuardar();//llamamos a un métofo para guardar
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*MÉTODO LLAMADO PARA GUARDAR DATOS*/
    private void attemptGuardar() {
        /*eSTE MÉTODO SE USA PARA VALIDAR EL FORMULARIO*/
        //variables que toman datos de sus campos
        EditText edittextNombre=(EditText)findViewById(R.id.editTextUsuarioNombre);
        EditText edittextPasswd=(EditText)findViewById(R.id.editTextUsuariosPassword);
        EditText editTextmail=(EditText)findViewById(R.id.editTextUsuariosMail);
        //borramos posibles mensajes que tengamos
        edittextNombre.setError(null);
        edittextPasswd.setError(null);
        editTextmail.setError(null);
        //tomamos los datos de los campos
        String nombre=String.valueOf(edittextNombre.getText());
        String passwd=String.valueOf(edittextPasswd.getText());
        String mail=String.valueOf(editTextmail.getText());
        //Empezamos la validación
        if (TextUtils.isEmpty(nombre)){  //no se insertó nombre
            edittextNombre.setError(getString(R.string.requerido));
            edittextNombre.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(passwd)){  //no se insertó nombre
            edittextPasswd.setError(getString(R.string.requerido));
            edittextPasswd.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mail)){  //no se insertó nombre
            editTextmail.setError(getString(R.string.requerido));
            edittextNombre.requestFocus();
            return;
        }
        if (!validaEmail(mail)) {
            editTextmail.setError(getString(R.string.formatoIncorrecto));
            editTextmail.requestFocus(); //mantiene el foco
            return;  //vuelve a previa validacion
        }
        //creamos un nuevo cliente con los datos obtenidos
        Usuarios usuarios=new Usuarios(G.SIN_VALOR_INT,nombre,passwd,mail);
        //Creamos una nueva clase que se encarga de la inserción, se llama ClientesProveedor
        UsuariosProveedor.insert(getContentResolver(),usuarios);
        //Salimos de la actividad tras insertar
        finish();
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
