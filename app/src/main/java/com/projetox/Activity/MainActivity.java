package com.projetox.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.webkit.PermissionRequest;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.projetox.Adapter.AdapterPost;
import com.projetox.Model.Categoria;
import com.projetox.Model.Post;
import com.projetox.Model.Usuario;
import com.projetox.R;
import com.projetox.SQLiteHelper.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerDados;
    private ArrayList<Post> posts = new ArrayList<Post>();
    private FloatingActionButton postUpload;
    private ImageView imageView;
    private static final String LOG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fabPostUpload);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        /*********************************  FIM DO LAYOUT PRONTO *********************************************/

        /********************************* INICIO CÓDIGO ****************************************************/

        DatabaseHelper dbHelper = new DatabaseHelper(this.getApplicationContext());

     /*   Usuario usuario1 = new Usuario("isabel", "bel", "bel@gmail.com", "123", 0);
        Usuario usuario2 = new Usuario("jonatan", "jon", "jon@gmail.com", "123", 1);
        Categoria categoria1 = new Categoria("dogo");
        Categoria categoria2 = new Categoria("narutinho");
        dbHelper.persistCategoria(categoria1);
        dbHelper.persistCategoria(categoria2);
        dbHelper.persistUsuario(usuario1);
        dbHelper.persistUsuario(usuario2);

        Log.d(LOG, "passou das inserçõoooooooooooooooooooooooooooooooooesssssssssssssssss");
        Log.d(LOG, "passou das inserçõoooooooooooooooooooooooooooooooooesssssssssssssssss");
    */

        recyclerDados = findViewById(R.id.rvPosts);
        postUpload = findViewById(R.id.fabPostUpload);

        //no onclick do botão de upload, chama activity de cadastro de post
        postUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chama tela de cadastro de posts
                startActivity(new Intent(MainActivity.this, PostUploadActivity.class));
            }
        });

        // Chama função que busca todos os posts cadastrados para mostrar na recycler view
        posts = dbHelper.getAllPosts();
        Log.d(LOG, "lista de posta: "+posts.toArray().toString());

        // Configurar adapter
        AdapterPost adapter = new AdapterPost(posts);
        Log.d(LOG, "passou da dbHelper.getAllPosts()");

        // Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerDados.setLayoutManager(layoutManager);
        // fixa o tamanho para otimizar
        recyclerDados.setHasFixedSize(true);

        // adiciona linha separadora dos elementos
        recyclerDados.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        recyclerDados.setAdapter(adapter);


        /* Adicionando eventos de clique a partir de classe já estabelecida
        recyclerDados.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerDados, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
                )
        );*/
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
