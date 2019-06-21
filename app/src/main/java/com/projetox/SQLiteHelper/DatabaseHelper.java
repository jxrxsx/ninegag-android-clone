package com.projetox.SQLiteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.projetox.Adapter.AdapterPost;
import com.projetox.Model.Categoria;
import com.projetox.Model.Post;
import com.projetox.Model.Usuario;
import com.projetox.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // TAG do Logcat
    private static final String LOG = "DatabaseHelper";

    // versão do banco
    private static final int DATABASE_VERSION = 15;

    // nome do banco
    private static final String DATABASE_NAME = "ninegag";

    // nomes das tabelas
    private static final String TABLE_USUARIO = "usuario";
    private static final String TABLE_CATEGORIA = "categoria";
    private static final String TABLE_POST = "post";
    private static final String TABLE_INTERACAO_USUARIO_POST = "interacao";

    // atributo comum a todas as tabelas
    private static final String ID = "id";

    //colunas da tabela USUARIO
    private static final String NOME_USUARIO = "nome_usuario";
    private static final String USERNAME = "username";
    private static final String EMAIL = "email";
    private static final String SENHA = "senha";
    private static final String EH_ADMIN = "eh_admin";

    // colunas da tabela POST
    private static final String ID_USUARIO_POST = "id_usuario";
    private static final String ID_CATEGORIA_POST = "id_categoria";
    private static final String TITULO = "titulo";
    private static final String MEDIA_VOTOS = "media_votos";
    private static final String IMAGEM = "imagem";
    private static final String CAMINHO_IMAGEM = "caminho_imagem";
    private static final String NOME_IMAGEM = "nome_imagem";

    // colunas da tabela CATEGORIA
    private static final String NOME_CATEGORIA = "nome_categoria";

    // colunas da tabela INTERACAO
    private static final String ID_USUARIO_INTERACAO = "id_usuario";
    private static final String ID_POST_INTERACAO = "id_post";
    private static final String QTD_ESTRELAS = "qtd_estrelas";
    private static final String COMENTARIO = "comentario";


    ///////////////////////// CRIAÇÃO DAS TABELAS //////////////////////////

    // criação da tabela USUARIO
    private static final String CREATE_TABLE_USUARIO = "CREATE TABLE "
            + TABLE_USUARIO + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + NOME_USUARIO + " VARCHAR,"
            + USERNAME + " VARCHAR,"
            + EMAIL + " VARCHAR,"
            + SENHA + " VARCHAR,"
            + EH_ADMIN + " VARCHAR"
            + ")";

    // criação da tabela CATEGORIA
    private static final String CREATE_TABLE_CATEGORIA = "CREATE TABLE "
            + TABLE_CATEGORIA + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + NOME_CATEGORIA + " VARCHAR"
            + ")";

    // criação da tabela POST
    private static final String CREATE_TABLE_POST = "CREATE TABLE "
            + TABLE_POST + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ID_USUARIO_POST + " INTEGER,"
            + ID_CATEGORIA_POST + " INTEGER,"
            + TITULO + " VARCHAR,"
            + MEDIA_VOTOS + " REAL DEFAULT 0,"
            + IMAGEM + " VARCHAR,"
            + CAMINHO_IMAGEM + " VARCHAR,"
            + NOME_IMAGEM + " VARCHAR,"
            + "FOREIGN KEY (" + ID_USUARIO_POST
                + ") REFERENCES " + TABLE_USUARIO + "("+ID+"), "
            + "FOREIGN KEY (" + ID_CATEGORIA_POST
                + ") REFERENCES " + TABLE_CATEGORIA +"("+ID+")"
            + ")";


    // criação da tabela INTERACAO
    private static final String CREATE_TABLE_INTERACAO_USUARIO_POST = "CREATE TABLE "
            + TABLE_INTERACAO_USUARIO_POST + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ID_USUARIO_INTERACAO + " INTEGER,"
            + ID_POST_INTERACAO + " INTEGER,"
            + QTD_ESTRELAS + " INTEGER,"
            + COMENTARIO + " VARCHAR,"
            + "FOREIGN KEY (" + ID_USUARIO_INTERACAO
                + ") REFERENCES " + TABLE_USUARIO + "("+ID+"), "
            + "FOREIGN KEY (" + ID_POST_INTERACAO
                + ") REFERENCES " + TABLE_POST +"("+ID+")"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // on create executa as clausulas para criação das tabelas
        db.execSQL(CREATE_TABLE_USUARIO);
        db.execSQL(CREATE_TABLE_CATEGORIA);
        db.execSQL(CREATE_TABLE_POST);
        db.execSQL(CREATE_TABLE_INTERACAO_USUARIO_POST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade faz o drop nas tabelas antigas
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERACAO_USUARIO_POST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIO);

        // cria novamente
        onCreate(db);
    }

    //************************************** POST DATABASE FUNCTIONS ************************************************//

    public boolean persistPost(Post post) {
        try{

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID_USUARIO_POST, post.getUsuario().getId());
        values.put(ID_CATEGORIA_POST, post.getCategoria().getId());
        values.put(TITULO, post.getTitulo());
        values.put(MEDIA_VOTOS, post.getMediaVotos());
        values.put(CAMINHO_IMAGEM, post.getCaminhoImagem());
        values.put(NOME_IMAGEM, post.getNomeImagem());

        // insert
        long resposta = db.insert(TABLE_POST, null, values);
            db.close();

        if(resposta != -1){
            Log.d(LOG, "POST SALVO NO BANCO");
            return true;
        }
        else
            return false;
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    //buscar post pelo ID
    public Post findPostByID(int idPost) {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        int idUsuario, idCategoria;
        Usuario usuario = new Usuario();
        Categoria categoria = new Categoria();

        String selectQuery = "SELECT  * FROM " + TABLE_POST + " WHERE " + ID + " = " + idPost;

        Log.d(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        Log.d(LOG, "resultado da consulta de post pelo ID: "+c.getCount());
        Post post = new Post();
        if (c != null) {
            c.moveToFirst();

            idUsuario = c.getInt((c.getColumnIndex(ID_USUARIO_POST)));
            idCategoria = c.getInt((c.getColumnIndex(ID_CATEGORIA_POST)));

            usuario = findUsuarioByID(idUsuario);
            categoria = findCategoriaByID(idCategoria);

            post.setId(c.getInt(c.getColumnIndex(ID)));
            post.setUsuario(usuario);
            post.setCategoria(categoria);
            post.setTitulo((c.getString(c.getColumnIndex(TITULO))));
            post.setMediaVotos((c.getDouble(c.getColumnIndex(MEDIA_VOTOS))));
            post.setCaminhoImagem((c.getString(c.getColumnIndex(CAMINHO_IMAGEM))));
            post.setNomeImagem(c.getString(c.getColumnIndex(NOME_IMAGEM)));
        }

        db.close();

        return post;
    }

    //buscar post pelo ID
    public Integer findLastPostID() {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        Integer idPost;

        String selectQuery = "SELECT  * FROM " + TABLE_POST;

        Log.d(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        Post post = new Post();
        if (c != null && c.getCount() != 0) {
            c.moveToLast();
            idPost = c.getInt(c.getColumnIndex(ID));
        }
        else
            idPost = 0;

        db.close();

        return idPost;
    }



    public ArrayList<Post> getAllPosts() {
        ArrayList<Post> listaPosts = new ArrayList<Post>();
        int idUsuario, idCategoria, count = 0;
        Usuario usuario = new Usuario();
        Categoria categoria = new Categoria();

        String selectQuery = "SELECT * FROM " + TABLE_POST;

        Log.d(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        Log.d(LOG,"quantidade de posts cadastrados: "+c.getCount());
        if (c != null) {
            c.moveToFirst();
            while(c.moveToNext()){
                Post post = new Post();

                idUsuario = c.getInt((c.getColumnIndex(ID_USUARIO_POST)));
                Log.d(LOG, "idUsuario: "+idUsuario);
                idCategoria = c.getInt((c.getColumnIndex(ID_CATEGORIA_POST)));


                usuario = findUsuarioByID(idUsuario);
                categoria = findCategoriaByID(idCategoria);

                post.setId(c.getInt(c.getColumnIndex(ID)));
                post.setUsuario(usuario);
                post.setCategoria(categoria);
                post.setTitulo((c.getString(c.getColumnIndex(TITULO))));
                post.setMediaVotos((c.getDouble(c.getColumnIndex(MEDIA_VOTOS))));
                post.setCaminhoImagem((c.getString(c.getColumnIndex(CAMINHO_IMAGEM))));
                post.setNomeImagem(c.getString(c.getColumnIndex(NOME_IMAGEM)));

                // add na lista de posts
                listaPosts.add(post);
                Log.d(LOG, "Adicionou post na lista");


            }
        }
        else{
            Log.d(LOG, "SELECT NOS POSTS RETORNOU 0 LINHAS");
        }
        db.close();
        return listaPosts;
    }

    //************************************************** USUARIO DATABASE FUNCTIONS *************************************//

    public boolean persistUsuario(Usuario usuario) {
        try{

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(NOME_USUARIO, usuario.getNome());
            values.put(USERNAME, usuario.getUser());
            values.put(EMAIL, usuario.getEmail());
            values.put(SENHA, usuario.getSenha());
            values.put(EH_ADMIN, usuario.getEhAdmin());

            // insert
            long resposta = db.insert(TABLE_USUARIO, null, values);
            db.close();
            if(resposta != -1){
                Log.d(LOG, "USUARIO SALVO NO BANCO");
                Log.d(LOG, "USUARIO SALVO NO BANCO");
                Log.d(LOG, "USUARIO SALVO NO BANCO");
                Log.d(LOG, "USUARIO SALVO NO BANCO");
                return true;
            }
            else
                return false;
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    //buscar usuário pelo ID
    public Usuario findUsuarioByID(int idUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_USUARIO + " WHERE " + ID + " = " + idUsuario;

        Log.d(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        Log.d(LOG, "resultado da consulta de usuarios: "+c.getCount());
        Usuario usuario = new Usuario();
        if (c != null) {
            c.moveToFirst();

            usuario.setId((c.getInt(c.getColumnIndex(ID))));
            usuario.setNome((c.getString(c.getColumnIndex(NOME_USUARIO))));
            usuario.setEmail((c.getString(c.getColumnIndex(EMAIL))));
            usuario.setUser((c.getString(c.getColumnIndex(USERNAME))));
            usuario.setSenha((c.getString(c.getColumnIndex(SENHA))));
            usuario.setEhAdmin((c.getInt(c.getColumnIndex(EH_ADMIN))));
        }

        db.close();
        return usuario;
    }

    //************************************************** CATEGORIA DATABASE FUNCTIONS *************************************//

    public boolean persistCategoria(Categoria categoria) {
        try{

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(NOME_CATEGORIA, categoria.getNome());

            // insert
            long resposta = db.insert(TABLE_CATEGORIA, null, values);
            db.close();
            if(resposta != -1){
                Log.d(LOG, "SALVOU CATEGORIA NO BANCO");
                Log.d(LOG, "SALVOU CATEGORIA NO BANCO");
                Log.d(LOG, "SALVOU CATEGORIA NO BANCO");
                return true;
            }
            else
                return false;
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    // cadastra lista de categorias
    public boolean persistCategorias(ArrayList<Categoria> categorias) {
        try{

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            long resposta = 0;

            for(Categoria c : categorias){
                values.put(NOME_CATEGORIA, c.getNome());

                // insert
                resposta = db.insert(TABLE_CATEGORIA, null, values);
            }

            db.close();
            if(resposta != -1){
                Log.d(LOG, "SALVOU LISTA DE CATEGORIA NO BANCO");
                return true;
            }
            else
                return false;
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public Categoria findCategoriaByID(int idCategoria) {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIA + " WHERE " + ID + " = " + idCategoria;

        String queryTeste = "SELECT * FROM " + TABLE_CATEGORIA;

        Cursor c1 = db.rawQuery(queryTeste, null);
        c1.moveToFirst();
        while(c1.moveToNext()){
            int id = c1.getInt(c1.getColumnIndex(ID));
            Log.d(LOG, "id das categorias cadastradas: "+id);
        }

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        Categoria categoria = new Categoria();
        if (c != null) {
            c.moveToFirst();
            while (c.moveToNext()) {
                categoria = new Categoria();
                categoria.setId((c.getInt(c.getColumnIndex(ID))));
                categoria.setNome((c.getString(c.getColumnIndex(NOME_CATEGORIA))));
            }
        }
        else{
            return null;
        }

        db.close();
        return categoria;
    }

}
