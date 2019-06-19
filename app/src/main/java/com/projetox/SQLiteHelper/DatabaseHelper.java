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
import android.widget.ImageView;
import android.widget.Toast;

import com.projetox.Model.Categoria;
import com.projetox.Model.Post;
import com.projetox.Model.Usuario;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // TAG do Logcat
    private static final String LOG = "DatabaseHelper";

    // versão do banco
    private static final int DATABASE_VERSION = 2;

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
            + IMAGEM + " BLOB,"
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
        values.put(ID_USUARIO_POST, post.getId());
        values.put(ID_USUARIO_POST, post.getUsuario().getId());
        values.put(ID_CATEGORIA_POST, post.getCategoria().getId());
        values.put(TITULO, post.getTitulo());
        values.put(MEDIA_VOTOS, post.getMediaVotos());

        BitmapDrawable drawable = (BitmapDrawable) post.getImagem().getDrawable();
        Bitmap bitmapImagem = drawable.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapImagem.compress(Bitmap.CompressFormat.PNG, 100, baos); // Could be Bitmap.CompressFormat.PNG or Bitmap.CompressFormat.WEBP
        byte[] bai = baos.toByteArray();

        values.put(IMAGEM, bai);

        // insert
        long resposta = db.insert(TABLE_POST, null, values);

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

    public ArrayList<Post> getAllPosts() {
        ArrayList<Post> listaPosts = new ArrayList<Post>();
        int idUsuario, idCategoria;
        ImageView imagemSalva = null;

        String selectQuery = "SELECT  * FROM " + TABLE_POST;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        c.moveToFirst();
        if (c != null) {

            do {
                Post post = new Post();
                //Usuario usuario = findUsuarioByID()
                //Categoria categoria = new Categoria();

               // idUsuario = c.getInt((c.getColumnIndex(ID_USUARIO_POST)));
                //idCategoria = c.getInt((c.getColumnIndex(ID_CATEGORIA_POST)));

                idUsuario = 1;
                idCategoria = 1;

                post.setId(c.getInt(c.getColumnIndex(ID)));
                post.setUsuario(findUsuarioByID(idUsuario));
                post.setCategoria(findCategoriaByID(idCategoria));
                post.setTitulo((c.getString(c.getColumnIndex(TITULO))));
                post.setMediaVotos((c.getDouble(c.getColumnIndex(MEDIA_VOTOS))));

                Bitmap bm = BitmapFactory.decodeByteArray((c.getBlob(c.getColumnIndex(IMAGEM))), 0, (c.getBlob(c.getColumnIndex(IMAGEM))).length);
                imagemSalva.setImageBitmap(bm);

                post.setImagem(imagemSalva);

                // add na lista de posts
                listaPosts.add(post);
            } while (c.moveToNext());
        }

        return listaPosts;
    }

    //************************************************** USUARIO DATABASE FUNCTIONS *************************************//

    public boolean persistUsuario(Usuario usuario) {
        try{

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ID, usuario.getId());
            values.put(NOME_USUARIO, usuario.getNome());
            values.put(USERNAME, usuario.getUser());
            values.put(EMAIL, usuario.getEmail());
            values.put(EH_ADMIN, usuario.getEhAdmin());

            // insert
            long resposta = db.insert(TABLE_USUARIO, null, values);

            if(resposta != -1){
                Log.d(LOG, "USUARIO SALVO NO BANCO");
                Log.e(LOG, "USUARIO SALVO NO BANCO");
                Log.i(LOG, "USUARIO SALVO NO BANCO");
                Log.v(LOG, "USUARIO SALVO NO BANCO");
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
    public Usuario findUsuarioByID(Integer idUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USUARIO + " WHERE " + ID + " = " + idUsuario;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
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
        c.close();
        return usuario;
    }

    //************************************************** CATEGORIA DATABASE FUNCTIONS *************************************//

    public boolean persistCategoria(Categoria categoria) {
        try{

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ID, categoria.getId());
            values.put(NOME_CATEGORIA, categoria.getNome());

            // insert
            long resposta = db.insert(TABLE_CATEGORIA, null, values);

            if(resposta != -1)
                return true;
            else
                return false;
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public Categoria findCategoriaByID(Integer idCategoria) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORIA + " WHERE "
                + ID + " = " + idCategoria;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Categoria categoria = new Categoria();
        categoria.setId((c.getInt(c.getColumnIndex(ID))));
        categoria.setNome((c.getString(c.getColumnIndex(NOME_CATEGORIA))));


        return categoria;
    }
}
