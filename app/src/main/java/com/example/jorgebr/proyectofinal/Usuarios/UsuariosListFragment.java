package com.example.jorgebr.practica4.Usuarios;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.jorgebr.practica4.Constantes.G;
import com.example.jorgebr.practica4.Proveedor.Contrato;
import com.example.jorgebr.practica4.Proveedor.UsuariosProveedor;
import com.example.jorgebr.practica4.R;

//La interfaz implementada añade la funcionalidad de Cursor
public class UsuariosListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

	UsuariosCursorAdapter mAdapter;
	LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
	//Creamos una variable necesaria para el Menú conceptual al pulsar prolongado sobre elemento
	ActionMode mActionmode; //dice modo en que esta
	View viewSeleccionado;//creamos variable para saber cual hemos seleccionado


	/*PARA CREAR CADA NUEVO FRAGMENTO LLAMAMOS A NEWINSTANCE()*/
	public static UsuariosListFragment newInstance() {
		UsuariosListFragment f = new UsuariosListFragment();
		return f;
	}

	/**
	 * When creating, retrieve this instance's number from its arguments.
	 */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//añadimos para que aparezcan menús en la appbar
		setHasOptionsMenu(true);
	}

	/*SOBREESCRIBIMOS EL MÉTODO PARA LOS MENÚS*/
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem menuitem=menu.add(Menu.NONE, G.Insertar, Menu.NONE,"Insertar"); //sin grupos,identificador, da igual orden
		menuitem.setIcon(R.mipmap.ic_insertar);  //añadimos el icono
		menuitem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS); //mostramos siempre el icono
		super.onCreateOptionsMenu(menu, inflater);
	}

	/*MÉTODO PARA AÑADIR FUNCIONALIDAD AL BOTÓN DEL ACTION BAR*/

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//según selección de icono
		switch (item.getItemId()){
			case G.Insertar: //opción insertar vamos a nueva actividad donde ponemos los datos a insertar
							 Intent intent=new Intent(getActivity(),UsuariosInsertarActivity.class); //getActivity porque estoy en un fragmento
							 startActivity(intent);  //vamos al activity del DetalleActivity
							 break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * The Fragment's UI is just a simple text view showing its
	 * instance number.
	 */


	/*MÉTODO CREAR LA VISTA. TENEMOS EL LIST VIEW QUE CONTIENE LLAMADO FRAGMENT_EMPLEADOS_LIST*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_usuarios_list, container, false);  //Creamos vista auxiliar que llenamos con fragmento
		mAdapter = new UsuariosCursorAdapter(getActivity());  //Adaptador permite decir que en listview habrá información
		setListAdapter(mAdapter);
		return v;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mCallbacks = this;
		getLoaderManager().initLoader(0, null, mCallbacks);  //cargamos la información correspondiente al Cursor
		/*AL CREAR EL ACTIVITY*/
		getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
				//como no queremos que se cree siempre el ActionMode
				if (mActionmode!=null){
					return false;
				}
				//llamamos al menú contextual
				mActionmode=getActivity().startActionMode(mActionModeCallback);  //mostramos el menú contextual
				view.setSelected(true);
				viewSeleccionado=view; //asignamos la nueva vista
				return true;
			}
		}); //capturamos la pulsación larga en elemento
	}

	/*AQUÍ NOS CREAMOS UN ACTIONMODECALLBACK y sobreescribimos sus métodos*/
	ActionMode.Callback mActionModeCallback=new ActionMode.Callback(){
		/*AL CREAR MENÚ CONTEXTUAL*/
		@Override
		public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
			MenuInflater inflater= actionMode.getMenuInflater();
			inflater.inflate(R.menu.menu_contextual,menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
			return false;
		}

		/*DARLE FUNCIONALIDAD A LA PULSACIÓN DE LOS ICONOS*/
		@Override
		public boolean onActionItemClicked(ActionMode Mode, MenuItem item) {
			switch (item.getItemId()){
				case R.id.menu_borrar:	int idusuario=(Integer) viewSeleccionado.getTag(); //obtenemos la etiquete que se asignó en Bind
										UsuariosProveedor.delete(getActivity().getContentResolver(),idusuario);
										mActionmode.finish();  //cerramos el ActionMode tras borrar elemento
										break;
				case R.id.menu_editar:  Intent intenteditar=new Intent(getActivity(),UsuariosModificarActivity.class);
										idusuario=(Integer) viewSeleccionado.getTag(); //obtenemos la etiquete que se asignó en Bind
										//LE PASAMOS LOS DATOS POR DEFECTO
										intenteditar.putExtra(Contrato.Usuarios._ID,idusuario);
										startActivity(intenteditar);
										mActionmode.finish();  //cerramos el ActionMode tras borrar elemento
										break;
			}
			return true;
		}

		/*SALIR DEL MENÚ CONTEXTUAL*/
		@Override
		public void onDestroyActionMode(ActionMode actionMode) {
			mActionmode=null;
		}
	};

	/*DEFINE CURSOR*/
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// This is called when a new Loader needs to be created.  This
		// sample only has one Loader, so we don't care about the ID.
		// First, pick the base URI to use depending on whether we are
		// currently filtering.
		String columns[] = new String[] { Contrato.Usuarios._ID,   //AQUI SE ALMACENA UN STRING CON LOS NOMBRES DE LAS COLUMNAS
										  Contrato.Usuarios.USERNAME,
										  Contrato.Usuarios.PASSUSER,
				                          Contrato.Usuarios.EMAILUSER
										};
		Uri baseUri = Contrato.Usuarios.CONTENT_URI;  //AQUÍ TENEMOS LA URI PARA ACCEDER A LA TABLA empleados, TODOS empleados

		// Now create and return a CursorLoader that will take care of
		// creating a Cursor for the data being displayed.

		String selection = null;  //SE USA PARA PONER UNA CONDICIÓN DE BÚSQUEDA
		//el activity, la uri cargada, nombres columnas, la seleccion
		return new CursorLoader(getActivity(), baseUri, columns, selection, null, null);
	}



	/*método al terminar de cargar el cursor, DICE QUE ESTAMOS SUSCRITOS A ESA URI*/
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Swap the new cursor in.  (The framework will take care of closing the
		// old cursor once we return.)
		Uri laUriBase = Uri.parse("content://" + Contrato.AUTHORITY + "/Usuarios");  //almacenamos datos de empleados
		data.setNotificationUri(getActivity().getContentResolver(), laUriBase);  //suscripción a esa Uri, si hay cambios nos enteramos y se cambia la información desde PC ya que el query notifica a los demás
		mAdapter.swapCursor(data);
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		// This is called when the last Cursor provided to onLoadFinished()
		// above is about to be closed.  We need to make sure we are no
		// longer using it.
		mAdapter.swapCursor(null);
	}

	/*****************************CLASE DEL ADAPTADOR*****************************/
	public class UsuariosCursorAdapter extends CursorAdapter {
		public UsuariosCursorAdapter(Context context) {
			super(context, null, false);
		}

		/*CADA VEZ QUE SE CARGA UN ITEM */
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			//ponemos información correspondiente
			int ID = cursor.getInt(cursor.getColumnIndex(Contrato.Usuarios._ID));
			String user = cursor.getString(cursor.getColumnIndex(Contrato.Usuarios.USERNAME));
			String pass = cursor.getString(cursor.getColumnIndex(Contrato.Usuarios.PASSUSER));
			String mail = cursor.getString(cursor.getColumnIndex(Contrato.Usuarios.EMAILUSER));
			/*asignamos el valor obtenido en el layout del item*/
			TextView textviewNombre = (TextView) view.findViewById(R.id.textview_usuario_list_item_nombre);
			textviewNombre.setText(user);
			/*asignamos el valor obtenido en el layout del item*/
			TextView textviewPass = (TextView) view.findViewById(R.id.textview_usuario_list_item_password);
			textviewPass.setText(String.valueOf(pass));
			/*asignamos el valor obtenido en el layout del item*/
			TextView textviewMail = (TextView) view.findViewById(R.id.textview_usuario_list_item_email);
			textviewMail.setText(String.valueOf(mail));
			/*generación del color*/
			ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
			int color = generator.getColor(user);                    //Genera un color según el nombre
			TextDrawable drawable = TextDrawable.builder().buildRound(user.substring(0,1), color);  //GENERA EL TEXTO
			ImageView image = (ImageView) view.findViewById(R.id.image_view);
			image.setImageDrawable(drawable);
			view.setTag(ID);
		}


		/*en este método definimos 2 cosas*/
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.usuarios_list_item, parent, false); //empleados_list_item define layout para cada item
			bindView(v, context, cursor);
			return v;
		}
	}
}
