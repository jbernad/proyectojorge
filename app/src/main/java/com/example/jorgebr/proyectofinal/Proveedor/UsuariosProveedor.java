package com.example.jorgebr.practica4.Proveedor;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.example.jorgebr.practica4.Pojos.Usuarios;

/**
 * Created by jorge on 14/11/2017.
 */

public class UsuariosProveedor {
    /*****INSERTAR USUARIO*****/
    public static void insert(ContentResolver contentresolver, Usuarios usuarios){
        Uri uri= Contrato.Usuarios.CONTENT_URI;
        ContentValues values= new ContentValues();                      //contendrá todos los valores que le vamos a pasar
        values.put(Contrato.Usuarios.USERNAME,usuarios.getNuser());    // añadimos datos obtenidos del objeto
        values.put(Contrato.Usuarios.PASSUSER,usuarios.getPass());
        values.put(Contrato.Usuarios.EMAILUSER,usuarios.getEmail());
        contentresolver.insert(uri,values);                         //el contentresolver identifica al proveedor de contenido
    }

    /*****INSERTAR BORRAR USUARIO*****/
    public static void delete (ContentResolver contentresolver,int usuarioid){
        Uri uri= Uri.parse(Contrato.Usuarios.CONTENT_URI+"/"+usuarioid);
        contentresolver.delete(uri, null, null);  //el contentresolver identifica al proveedor de contenido
    }

    /*****ACTUALIZAR USUARIO*****/
    public static void update(ContentResolver contentresolver,Usuarios usuarios){
        /*PRIMER PARÁMETRO*/
        Uri uri= Uri.parse(Contrato.Usuarios.CONTENT_URI+"/"+usuarios.getID());  //ID
        /*SEGUNDO PARÁMETRO*/
        ContentValues values= new ContentValues(); //contendrá todos los valores que le vamos a pasar
        values.put(Contrato.Usuarios.USERNAME,usuarios.getNuser());    // añadimos datos obtenidos del objeto
        values.put(Contrato.Usuarios.PASSUSER,usuarios.getPass());
        values.put(Contrato.Usuarios.EMAILUSER,usuarios.getEmail());
        //LLAMADA A UPDATE
        contentresolver.update(uri, values, null,null);
    }

    /*****CONSULTAR USUARIO POR ID*****/
    static public Usuarios readRecord(ContentResolver contentresolver, int usuarioid){
        /*PRIMER PARÁMETRO*/
        Uri uri= Uri.parse(Contrato.Usuarios.CONTENT_URI+"/"+usuarioid);  //tomamos la Uri de la clase contrato aquí requerimos el número tb convertida por el int
        //CAMPOS QUE QUEREMOS QUE NOS DEVUELVA
        String[] proyection={
                Contrato.Usuarios.USERNAME,
                Contrato.Usuarios.PASSUSER,
                Contrato.Usuarios.EMAILUSER
        };
        Cursor cursor=contentresolver.query (uri,proyection,null,null,null); //devuelve un Cursor
        if (cursor.moveToFirst()) {                  //encontramos datos lo devolvemos
            Usuarios usuarios=new Usuarios();
            usuarios.setID(usuarioid);
            usuarios.setNuser(cursor.getString(cursor.getColumnIndex(Contrato.Usuarios.USERNAME)));
            usuarios.setPass(cursor.getString(cursor.getColumnIndex(Contrato.Usuarios.PASSUSER)));
            usuarios.setEmail(cursor.getString(cursor.getColumnIndex(Contrato.Usuarios.EMAILUSER)));
            return usuarios;  //lo encuentra lo devuelve
        }
        return null; //no lo encuentra     //no lo encuentra
    }

    /*****PRUEBA FUTURA****/

    /*static public Usuarios BuscaUser(ContentResolver contentresolver, String nombre ){
        *//*PRIMER PARÁMETRO*//*
        Uri uri= Uri.parse(Contrato.Usuarios.CONTENT_URI+"/"+nombre);  //tomamos la Uri de la clase contrato aquí requerimos el número tb convertida por el int
        //CAMPOS QUE QUEREMOS QUE NOS DEVUELVA
        String[] proyection={
                Contrato.Usuarios.USERNAME,
                Contrato.Usuarios.PASSUSER,
                Contrato.Usuarios.EMAILUSER
        };
        String where = Contrato.Usuarios.USERNAME + " = " + "'"+nombre+"'";
        Cursor cursor=contentresolver.query (uri,proyection,where,null,null); //devuelve un Cursor
        if (cursor.moveToFirst()) {                  //encontramos datos lo devolvemos
            Usuarios usuarios=new Usuarios();
            usuarios.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Contrato.Usuarios._ID))));
            usuarios.setNuser(cursor.getString(cursor.getColumnIndex(Contrato.Usuarios.USERNAME)));
            usuarios.setPass(cursor.getString(cursor.getColumnIndex(Contrato.Usuarios.PASSUSER)));
            usuarios.setEmail(cursor.getString(cursor.getColumnIndex(Contrato.Usuarios.EMAILUSER)));
            return usuarios;  //lo encuentra lo devuelve
        }
        }
        return null; //no lo encuentra     //no lo encuentra*/

}
