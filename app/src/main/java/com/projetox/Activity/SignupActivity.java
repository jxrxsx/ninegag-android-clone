package com.projetox.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.projetox.Model.Usuario;
import com.projetox.R;
import com.projetox.SQLiteHelper.DatabaseHelper;


public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    private EditText nomeCadastro;
    private EditText emailCadastro;
    private EditText senhaCadastro;
    private EditText confirmaSenhaCadastro;
    private Button btnCadastrar;
    private TextView voltarLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nomeCadastro = findViewById(R.id.tvNomeCadastro);
        emailCadastro = findViewById(R.id.tvEmailCadastro);
        senhaCadastro = findViewById(R.id.tvSenhaCadastro);
        confirmaSenhaCadastro = findViewById(R.id.tvConfirmaSenhaCadastro);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        voltarLogin = findViewById(R.id.tvVoltarLogin);


        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Cadastro de usuário");
                btnCadastrar.setEnabled(false);

                if (!validateFormCadastro()) {
                    Toast.makeText(getBaseContext(), "Preencha os campos corretamente e tente novamente!", Toast.LENGTH_LONG).show();
                    btnCadastrar.setEnabled(true);
                }
                else {
                    String name = nomeCadastro.getText().toString();
                    String email = emailCadastro.getText().toString();
                    String pass = senhaCadastro.getText().toString();
                    //String confirmaPass = confirmaSenhaCadastro.getText().toString();
                    DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                    Usuario usuarioCadasto = new Usuario(name, email, pass, 0);

                    // logica de cadastro aqui
                    try{
                        //testa se o usuário foi cadastrado
                        if(!dbHelper.persistUsuario(usuarioCadasto)){
                            Toast.makeText(getApplicationContext(), "Cadastro efetuado com sucesso!", Toast.LENGTH_LONG).show();
                            //usuário n existia ainda, então cadastra ele
                            Log.d(TAG, "Entrou no if do Try. Cadastrou o usuário e vai voltar pra tela de login");
                            btnCadastrar.setEnabled(true);
                            //volta pra tela de login
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Já existe uma conta associada a esse e-mail!", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Entrou no else do Try. Não cadastrou o usuário. Se pa já existe!!");
                        }
                    }
                    catch(Exception e){
                        Log.d(TAG, "Entrou no catch. Deu erro no cadastro do usuário.");
                        e.printStackTrace();
                    }
                }
            }
        });

        voltarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // termina a tela de cadastro e volta pra de login
                finish();
            }
        });
    }

    public boolean validateFormCadastro() {
        boolean valid = true;

        String name = nomeCadastro.getText().toString();
        String email = emailCadastro.getText().toString();
        String password = senhaCadastro.getText().toString();
        String confirmaPass = confirmaSenhaCadastro.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nomeCadastro.setError("Pelo menos 3 caracteres no nome!");
            valid = false;
        } else {
            nomeCadastro.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailCadastro.setError("Digite um email válido");
            valid = false;
        } else {
            emailCadastro.setError(null);
        }

        if (password.isEmpty() || password.length() < 3 || password.length() > 10) {
            senhaCadastro.setError("Senha deve conter entre 3 e 10 caracteres");
            valid = false;
        } else {
            senhaCadastro.setError(null);
        }

        if (confirmaPass.isEmpty() || confirmaPass.length() < 3 || confirmaPass.length() > 10) {
            confirmaSenhaCadastro.setError("Senha deve conter entre 3 e 10 caracteres");
            valid = false;
        } else {
            confirmaSenhaCadastro.setError(null);
        }

        if (!confirmaPass.equals(password)) {
            confirmaSenhaCadastro.setError("Confirmação de senha incorreta!");
            valid = false;
        } else {
            confirmaSenhaCadastro.setError(null);
        }

        return valid;
    }
}