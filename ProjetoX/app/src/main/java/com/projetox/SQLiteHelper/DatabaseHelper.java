package com.projetox.SQLiteHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // TAG do Logcat
    private static final String LOG = "DatabaseHelper";

    // versão do banco
    private static final int DATABASE_VERSION = 1;

    // nome do banco
    private static final String DATABASE_NAME = "contactsManager";

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
}
