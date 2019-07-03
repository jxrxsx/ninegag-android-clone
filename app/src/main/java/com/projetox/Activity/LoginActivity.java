package com.projetox.Activity;

import android.app.ProgressDialog;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.projetox.Model.Categoria;
import com.projetox.Model.Usuario;
import com.projetox.R;
import com.projetox.SQLiteHelper.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private TextView cadastrar;
    private EditText emailLogin;
    private EditText senhaLogin;
    private Button btnLogin;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        cadastrar = findViewById(R.id.tvCadastreSe);
        emailLogin = findViewById(R.id.tvEmailLogin);
        senhaLogin = findViewById(R.id.tvSenhaLogin);
        btnLogin = findViewById(R.id.btnEfetuaLogin);

        //fazInsercoesIniciais();


        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "Login");

                btnLogin.setEnabled(false);
                if (!validaForm()) {
                    Toast.makeText(getBaseContext(), "Preencha os campos corretamente e tente novamente!", Toast.LENGTH_LONG).show();
                    btnLogin.setEnabled(true);
                }
                else {
                    // lógica do login
                    String email = emailLogin.getText().toString();
                    String password = senhaLogin.getText().toString();
                    DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());

                    try{
                        //chama função que verifica se o usuario com email e senha digitados está cadastrado
                        Usuario usuarioLogado = dbHelper.findUsuarioByEmail(email);

                        if(usuarioLogado == null || !usuarioLogado.getEmail().equals(email) || !usuarioLogado.getSenha().equals(password)){
                            emailLogin.setError("Credenciais não cadastradas");
                            btnLogin.setEnabled(true);
                        }
                        else {
                            //se estiver, a função retorna true
                            Log.d(TAG, "Email do usuario login: " + usuarioLogado.getEmail());
                            Log.d(TAG, "Senha do usuario login: " + usuarioLogado.getSenha());
                            if (usuarioLogado.getEmail().equals(email) && usuarioLogado.getSenha().equals(password)) {
                                Log.d(TAG, "usuario cadastrado!!! entrou no if da logica de login");
                                btnLogin.setEnabled(true);
                                //chama tela inicial do app
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("idUsuarioLogado", usuarioLogado.getId()+"");
                                intent.putExtra("nomeUsuarioLogado", usuarioLogado.getNome());
                                intent.putExtra("ehAdminUsuarioLogado", usuarioLogado.getEhAdmin()+"");
                                startActivity(intent);
                            }
                        }
                    }
                    catch(SQLiteException e){
                        Log.d(TAG, "DEU MERDA NA VERIFICAÇÃO DOS DADOS DO LOGIN");
                        e.printStackTrace();
                    }
                }
            }
        });

        cadastrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // chama a tela de cadastro
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // IMPORTANTE!! desabilita voltar pra MainActivity sem fazer o login
        moveTaskToBack(true);
    }

    public boolean validaForm() {
        boolean valida = true;

        String email = emailLogin.getText().toString();
        String password = senhaLogin.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLogin.setError("E-mail inválido");
            valida = false;
        } else {
            emailLogin.setError(null);
        }

        if (password.isEmpty() || password.length() < 3 || password.length() > 10) {
            senhaLogin.setError("Senha deve conter de 3 a 10 dígitos");
            valida = false;
        } else {
            senhaLogin.setError(null);
        }

        return valida;
    }

    public void fazInsercoesIniciais(){
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        Usuario usuarioAdmin = new Usuario(1,"jonatan", "jon", "jonatan.rodrigues@unesp.br", "12345", 1);
        Usuario usuario = new Usuario(2,"amanda", "mayu", "mayu@gmail.com", "1234", 0);
        ArrayList<Categoria> categorias = new ArrayList<>();

        Categoria c1 = new Categoria(1,"dogo");
        Categoria c2 = new Categoria(2,"narutinho");
        Categoria c3 = new Categoria(3,"got");
        Categoria c4 = new Categoria(4,"nsfw");
        Categoria c5 = new Categoria(5,"food");
        Categoria c6 = new Categoria(6,"pokémon");
        Categoria c7 = new Categoria(7,"wtf");
        Categoria c8 = new Categoria(8,"star wars");
        Categoria c9 = new Categoria(9,"IT nerd");
        Categoria c10 = new Categoria(10,"black humor");
        Categoria c11 = new Categoria(11,"stuff");

        categorias.add(c1);
        categorias.add(c2);
        categorias.add(c3);
        categorias.add(c4);
        categorias.add(c5);
        categorias.add(c6);
        categorias.add(c7);
        categorias.add(c8);
        categorias.add(c9);
        categorias.add(c10);
        categorias.add(c11);

        dbHelper.persistUsuario(usuarioAdmin);
        dbHelper.persistUsuario(usuario);
        dbHelper.persistCategorias(categorias);

        Log.d(TAG, "passou das inserçõooeess");
    }

}