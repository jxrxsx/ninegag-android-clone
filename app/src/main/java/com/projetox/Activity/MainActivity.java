package com.projetox.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.projetox.Adapter.AdapterPost;
import com.projetox.Model.Post;
import com.projetox.R;
import com.projetox.SQLiteHelper.DatabaseHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerDados;
    private ArrayList<Post> listaPostsMostrando = new ArrayList<Post>();
    private ArrayList<Post> listaBckpPosts = new ArrayList<Post>();
    private AdapterPost adapter;
    private FloatingActionButton postUpload;
    private ImageView imageView;
    private static final String TAG = "MainActivity";

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
        String idUsuarioLogado = getIntent().getStringExtra("idUsuarioLogado");
        String nomeUsuarioLogado = getIntent().getStringExtra("nomeUsuarioLogado");
        String ehAdminUsuarioLogado = getIntent().getStringExtra("ehAdminUsuarioLogado");
        Toast.makeText(getApplicationContext(), "Bem-vindo(a) de volta, "+nomeUsuarioLogado+"!", Toast.LENGTH_LONG).show();


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
        listaBckpPosts = dbHelper.getAllPosts();
        listaPostsMostrando = listaBckpPosts;
        // Configurar adapter
        adapter = new AdapterPost(listaPostsMostrando);
        Log.d(TAG, "passou da dbHelper.getAllPosts()");

        // Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerDados.setLayoutManager(layoutManager);
        // fixa o tamanho para otimizar
        recyclerDados.setHasFixedSize(true);

        // adiciona linha separadora dos elementos
        recyclerDados.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        recyclerDados.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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
    public void onResume(){
        super.onResume();

        // Configurar adapter
   /*     listaPostsMostrando = listaBckpPosts;
        adapter.notifyDataSetChanged();
        Log.d(TAG, "Voltou a mostra todos os posts originais");
        */
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
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());


        if (id == R.id.nav_dogo) {
            Toast.makeText(getApplicationContext(), "clicou na categoria dogo", Toast.LENGTH_LONG).show();
            listaPostsMostrando.clear();
            listaPostsMostrando = dbHelper.postsPorCategoria(1);

        } else if (id == R.id.nav_narutinho) {
            Toast.makeText(getApplicationContext(), "clicou na categoria narutinho", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_got) {
            Toast.makeText(getApplicationContext(), "clicou na categoria got", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_nsfw) {
            Toast.makeText(getApplicationContext(), "clicou na categoria nsfw", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_food) {
            Toast.makeText(getApplicationContext(), "clicou na categoria food", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_pokemon) {
            Toast.makeText(getApplicationContext(), "clicou na categoria pokemon", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_wtf) {
            Toast.makeText(getApplicationContext(), "clicou na categoria wtf", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_star_wars) {
            Toast.makeText(getApplicationContext(), "clicou na categoria star wars", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_it_nerd) {
            Toast.makeText(getApplicationContext(), "clicou na categoria it nerd", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_black_humor) {
            Toast.makeText(getApplicationContext(), "clicou na categoria black humor", Toast.LENGTH_LONG).show();
        }else if (id == R.id.nav_stuff) {
            Toast.makeText(getApplicationContext(), "clicou na categoria stuff", Toast.LENGTH_LONG).show();
        }

        adapter.notifyDataSetChanged();
        Log.d(TAG, "passou da filtragem de posts");


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
