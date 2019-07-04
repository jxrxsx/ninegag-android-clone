package com.projetox.SQLiteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.projetox.Model.Categoria;
import com.projetox.Model.Post;
import com.projetox.Model.Reacao;
import com.projetox.Model.Usuario;

import java.util.ArrayList;
import java.util.Collections;

public class DatabaseHelper extends SQLiteOpenHelper {

    // TAG do Logcat
    private static final String LOG = "DatabaseHelper";

    // versão do banco
    private static final int DATABASE_VERSION = 33;

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
            + ") REFERENCES " + TABLE_USUARIO + "(" + ID + "), "
            + "FOREIGN KEY (" + ID_CATEGORIA_POST
            + ") REFERENCES " + TABLE_CATEGORIA + "(" + ID + ")"
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
            + ") REFERENCES " + TABLE_USUARIO + "(" + ID + "), "
            + "FOREIGN KEY (" + ID_POST_INTERACAO
            + ") REFERENCES " + TABLE_POST + "(" + ID + ")"
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
        try {

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ID_USUARIO_POST, post.getUsuario().getId());
            values.put(ID_CATEGORIA_POST, post.getCategoria().getId());
            values.put(TITULO, post.getTitulo());
            values.put(MEDIA_VOTOS, post.getMediaVotos());
            values.put(CAMINHO_IMAGEM, post.getCaminhoImagem());
            values.put(NOME_IMAGEM, post.getNomeImagem());

        /*
        String query = "SELECT * FROM " + TABLE_POST;
        Cursor c = db.rawQuery(query, null);

        if(c.getCount() == 0)
            values.put(ID, 1);
        */

            // insert
            long resposta = db.insert(TABLE_POST, null, values);
            db.close();

            if (resposta != -1) {
                Log.d(LOG, "POST SALVO NO BANCO");
                return true;
            } else
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //buscar post pelo ID
    public Post findPostByID(int idPost) {
        SQLiteDatabase db = this.getReadableDatabase();
        int idUsuario, idCategoria;
        Usuario usuario = new Usuario();
        Categoria categoria = new Categoria();

        String selectQuery = "SELECT  * FROM " + TABLE_POST + " WHERE " + ID + " = " + idPost;

        Log.d(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        Log.d(LOG, "resultado da consulta de post pelo ID: " + c.getCount());
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
        } else
            idPost = 1;

        db.close();

        return idPost;
    }

    //carrega lista de posts pra mostrar na página inicial
    public ArrayList<Post> getAllPosts() {

        ArrayList<Post> listaPosts = new ArrayList<Post>();
        int idUsuario, idCategoria, count = 0;
        Usuario usuario = new Usuario();
        Categoria categoria = new Categoria();
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            String selectQuery = "SELECT * FROM " + TABLE_POST;

            Log.d(LOG, selectQuery);

            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            Log.d(LOG, "quantidade de posts cadastrados: " + c.getCount());
            if (c != null) {
                c.moveToFirst();
                while (c.moveToNext()) {
                    Post post = new Post();

                    idUsuario = c.getInt((c.getColumnIndex(ID_USUARIO_POST)));
                    Log.d(LOG, "idUsuario: " + idUsuario);
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
            } else {
                Log.d(LOG, "SELECT NOS POSTS RETORNOU 0 LINHAS");
            }
            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
            db.close();
        }
        Collections.reverse(listaPosts);
        return listaPosts;
    }

    //carrega lista de posts de uma categoria especifica
    public ArrayList<Post> getPostsByCategoriaID(int idCategoria) {

        ArrayList<Post> listaPostsCategoria = new ArrayList<Post>();
        listaPostsCategoria.clear();

        try {
            for (Post p : getAllPosts()) {
                if (p.getCategoria().getId() != idCategoria) {
                    listaPostsCategoria.add(p);
                }
            }
            Log.d(LOG, "vai retornar lista de posts da categoria");
            return listaPostsCategoria;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return listaPostsCategoria;
        }
    }

    //get posts by categoria
    //carrega lista de posts pra mostrar na página inicial
    public ArrayList<Post> postsPorCategoria(int idCategoria) {

        ArrayList<Post> listaPosts = new ArrayList<Post>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            String selectQuery = "SELECT * FROM " + TABLE_POST + " WHERE " + ID_CATEGORIA_POST + " = " + idCategoria;

            Log.d(LOG, selectQuery);

            Cursor c = db.rawQuery(selectQuery, null);
            Log.d(LOG, "numero de linhas da consulta pelos posts por categoria: " + c.getCount());
            if (c != null) {
                c.moveToFirst();
                Post post;
                while (c.moveToNext()) {
                    int idPost = c.getInt(c.getColumnIndex(ID));
                    post = findPostByID(idPost);
                    Log.d(LOG, "titulo do post na busca pelas categorias: " + post.getTitulo());
                    // add na lista de posts
                    listaPosts.add(post);
                }
                Log.d(LOG, "criou lista de posts por categoria");
            } else {
                Log.d(LOG, "SELECT NOS POSTS RETORNOU 0 LINHAS");
            }
            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
            db.close();
        }
        return listaPosts;
    }

    public boolean deletePostByID(int idPost) {
        //ver de mudar nome da tabelaaaa
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            int resposta = db.delete(TABLE_POST, "ID=?", new String[]{String.valueOf(idPost)});
            //significa que a query afetou alguma tupla
            if (resposta != 0) {
                Log.d(LOG, "deu certo a deleção, vai retornar true");
                return true;
            } else {
                Log.d(LOG, "DEU ERRO AO DELETAR POST. vai retornar false");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //************************************************** USUARIO DATABASE FUNCTIONS *************************************//

    public boolean persistUsuario(Usuario usuario) {
        try {
            boolean taSalvo = verificaUsuarioCadastrado(usuario.getEmail());
            //se a resposta for false, significa que pode cadastrar o usuário
            if (taSalvo == false) {
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
                if (resposta == -1) {
                    Log.d(LOG, "SALVOU USUARIO NO BANCO. TUDO OK!");
                    return true;
                } else {
                    Log.d(LOG, "PROBLEMA NO PERSIST DO USUARIO NO BANCO.");
                    return false;
                }

            } else {
                Log.d(LOG, "USUARIO JÁ CADASTRADO. retorna true!!");
                return true;
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //buscar usuário pelo ID
    public Usuario findUsuarioByID(int idUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Usuario usuario = new Usuario();
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_USUARIO + " WHERE " + ID + " = " + idUsuario;

            Log.d(LOG, selectQuery);

            Cursor c = db.rawQuery(selectQuery, null);
            Log.d(LOG, "resultado da consulta de usuarios: " + c.getCount());
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
        } catch (SQLiteException e) {
            e.printStackTrace();
            return usuario;
        }
    }

    public Usuario findUsuarioByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Usuario usuario = null;
        try {
            String query = "SELECT * FROM " + TABLE_USUARIO + " WHERE " + EMAIL + " = '" + email + "'";
            Log.d(LOG, query);
            Cursor c = db.rawQuery(query, null);
            if (c.getCount() != 0) {
                c.moveToFirst();
                Log.d(LOG, "Entrou no if. Quantidade de usuarios com o email no login: " + c.getCount());
                int id = c.getInt(c.getColumnIndex(ID));
                usuario = findUsuarioByID(id);
                db.close();
                return usuario;
            } else {
                Log.d(LOG, "Entrou no else. lista de usuários com esse email é vazia. retorona usuario nulo.");
                db.close();
                return usuario;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            db.close();
            return usuario;
        }
    }

    public boolean verificaUsuarioCadastrado(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean resposta = true;
        try {
            String query = "SELECT * FROM " + TABLE_USUARIO + " WHERE " + EMAIL + " = '" + email + "'";
            Log.d(LOG, query);
            Cursor c = db.rawQuery(query, null);
            if (c.getCount() != 0) {
                Log.d(LOG, "Entrou no if. Existe usuário cadastrado com esse email. Retornou true");
                return resposta;
            } else {
                Log.d(LOG, "Entrou no else.  Não tem usuario com o email cadastrado. retorna false");
                db.close();
                resposta = false;
                return resposta;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            db.close();
        }

        return resposta;
    }

    //************************************************** CATEGORIA DATABASE FUNCTIONS *************************************//

    public boolean persistCategoria(Categoria categoria) {
        try {

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(NOME_CATEGORIA, categoria.getNome());

            // insert
            long resposta = db.insert(TABLE_CATEGORIA, null, values);
            if (resposta != -1) {
                Log.d(LOG, "SALVOU CATEGORIA NO BANCO");
                db.close();
                return true;
            } else {
                db.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // cadastra lista de categorias
    public boolean persistCategorias(ArrayList<Categoria> categorias) {
        try {

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            long resposta = 0;

            for (Categoria c : categorias) {
                values.put(NOME_CATEGORIA, c.getNome());

                // insert
                resposta = db.insert(TABLE_CATEGORIA, null, values);

                if (resposta != -1)
                    Log.d(LOG, "Salvou a categoria " + c.getNome() + " com id: " + c.getId());
                else
                    return false;
            }

            db.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        Log.d(LOG, "SALVOU TODAS AS CATEGORIAS NO BANCO");
        return true;
    }

    //buscar usuário pelo ID
    public Categoria findCategoriaByID(int idCategoria) {
        SQLiteDatabase db = this.getReadableDatabase();
        Categoria categoria = new Categoria();
        try {
            String selectQuery = "SELECT * FROM " + TABLE_CATEGORIA + " WHERE " + ID + " = " + idCategoria;

            Log.d(LOG, selectQuery);

            Cursor c = db.rawQuery(selectQuery, null);
            Log.d(LOG, "resultado da consulta de categorias: " + c.getCount());
            if (c != null) {
                c.moveToFirst();

                categoria.setId((c.getInt(c.getColumnIndex(ID))));
                categoria.setNome((c.getString(c.getColumnIndex(NOME_CATEGORIA))));
            }

            db.close();
            return categoria;
        } catch (SQLiteException e) {
            e.printStackTrace();
            db.close();
            return categoria;
        }
    }

    //************************************************** REACOES DATABASE FUNCTIONS *************************************//

    public boolean persistReacao(Reacao reacao) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ID_POST_INTERACAO, reacao.getPost().getId());
            values.put(ID_USUARIO_INTERACAO, reacao.getUsuario().getId());
            values.put(QTD_ESTRELAS, reacao.getQtdEstrelas());
            values.put(COMENTARIO, reacao.getComentario());

            // insert
            long resposta = db.insert(TABLE_INTERACAO_USUARIO_POST, null, values);
            if (resposta != -1) {
                Log.d(LOG, "SALVOU REACAO NO BANCO");
                db.close();
                return true;
            } else {
                db.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //carrega lista de posts pra mostrar na página inicial
    public ArrayList<Reacao> getReacoesByPostID(int idPost) {
        ArrayList<Reacao> listaReacoes = new ArrayList<Reacao>();
        int idUsuario;
        Usuario usuario = new Usuario();
        Post post = findPostByID(idPost);
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            //ver de mudar nome da tabelaaaa
            String selectQuery = "SELECT * FROM " + TABLE_INTERACAO_USUARIO_POST + " WHERE " + ID_POST_INTERACAO + " = " + idPost;

            Log.d(LOG, selectQuery);

            Cursor c = db.rawQuery(selectQuery, null);

            Log.d(LOG, "quantidade de reacoes cadastradas: " + c.getCount());
            if (c != null) {
                c.moveToFirst();
                Reacao reacao;
                while (c.moveToNext()) {
                    reacao = new Reacao();

                    //procura usuário autor de cada reação para setar no objeto reação
                    idUsuario = c.getInt((c.getColumnIndex(ID_USUARIO_INTERACAO)));
                    usuario = findUsuarioByID(idUsuario);

                    reacao.setId(c.getInt(c.getColumnIndex(ID)));
                    reacao.setPost(post);
                    reacao.setUsuario(usuario);
                    reacao.setQtdEstrelas(c.getInt(c.getColumnIndex(QTD_ESTRELAS)));
                    reacao.setComentario(c.getString(c.getColumnIndex(COMENTARIO)));

                    // add na lista de reacoes
                    listaReacoes.add(reacao);
                    Log.d(LOG, "Adicionou reação na lista");


                }
            } else {
                Log.d(LOG, "SELECT NAS REACOES RETORNOU 0 LINHAS");
            }
            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
            db.close();
        }
        //retorna lista de reações que alimentará o adapterReacoes
        Collections.reverse(listaReacoes);
        return listaReacoes;
    }

    //carrega lista de reacoes pra mostrar na página inicial
    public ArrayList<Reacao> getAllReacoes() {

        ArrayList<Reacao> listaReacoes = new ArrayList<Reacao>();
        int idUsuario, idPost;

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            String selectQuery = "SELECT * FROM " + TABLE_INTERACAO_USUARIO_POST;

            Log.d(LOG, selectQuery);

            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            Log.d(LOG, "quantidade de reacoes cadastrados: " + c.getCount());
            if (c != null) {
                c.moveToFirst();
                Reacao reacao;
                Usuario usuario;
                Post post;
                while (c.moveToNext()) {
                    reacao = new Reacao();
                    usuario = new Usuario();
                    post = new Post();

                    idUsuario = c.getInt((c.getColumnIndex(ID_USUARIO_INTERACAO)));
                    Log.d(LOG, "idUsuario: " + idUsuario);
                    idPost = c.getInt((c.getColumnIndex(ID_POST_INTERACAO)));

                    usuario = findUsuarioByID(idUsuario);
                    post = findPostByID(idPost);

                    reacao.setId(c.getInt(c.getColumnIndex(ID)));
                    reacao.setUsuario(usuario);
                    reacao.setPost(post);
                    reacao.setQtdEstrelas(c.getInt(c.getColumnIndex(QTD_ESTRELAS)));
                    reacao.setComentario(c.getString(c.getColumnIndex(COMENTARIO)));

                    // add na lista de posts
                    listaReacoes.add(reacao);
                    Log.d(LOG, "Adicionou reacao na lista de todas as reações");
                }
            } else {
                Log.d(LOG, "SELECT NAS REACOES RETORNOU 0 LINHAS");
            }
            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
            db.close();
        }
        Collections.reverse(listaReacoes);
        return listaReacoes;
    }

    //pega total de reações de um post
    public Integer getTotalReacoesByID(int idPost) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_INTERACAO_USUARIO_POST + " WHERE " + ID_POST_INTERACAO + " = " + idPost;;
            Log.d(LOG, selectQuery);
            Cursor c = db.rawQuery(selectQuery, null);
            Log.d(LOG, "quantidade de reacoes desse post: " + c.getCount());
            return c.getCount();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
