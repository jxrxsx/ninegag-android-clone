package com.projetox.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.projetox.R;


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
                signup();
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

    public void signup() {
        Log.d(TAG, "Cadastro de usuário");

        if (!validateFormCadastro()) {
            onSignupFailed();
            return;
        }

        btnCadastrar.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.Theme_AppCompat_DayNight_DarkActionBar);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Criando conta de usuário...");
        progressDialog.show();

        String name = nomeCadastro.getText().toString();
        String email = emailCadastro.getText().toString();
        String pass = senhaCadastro.getText().toString();
        String confirmaPass = confirmaSenhaCadastro.getText().toString();

        // logica de cadastro aqui

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        btnCadastrar.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Falha no login", Toast.LENGTH_LONG).show();

        btnCadastrar.setEnabled(true);
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

        return valid;
    }
}