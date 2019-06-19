package com.projetox.Activity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.projetox.R;
import com.projetox.Model.Post;
import com.projetox.Model.Usuario;
import com.projetox.Model.Categoria;
import com.projetox.SQLiteHelper.DatabaseHelper;


import pub.devrel.easypermissions.EasyPermissions;

public class PostUploadActivity extends AppCompatActivity {

    private TextInputLayout tilTitulo;
    private EditText tvTitulo;
    private Spinner categoria;
    private Switch ehSensivel;
    private ImageView imagemPost;
    private ImageButton btnSalvarPost;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);

        final DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());

        //associa elementos da tela de cadastro ao código java
        //btnAddImagem = findViewById(R.id.btnAddImagem);
        tilTitulo = findViewById(R.id.tilTitulo);
        tvTitulo = findViewById(R.id.edtTitulo);
        categoria = findViewById(R.id.spnCategoria);
        ehSensivel = findViewById(R.id.swcSensivel);
        imagemPost = findViewById(R.id.ivImagemPost);
        btnSalvarPost = findViewById(R.id.btnSalvarPost);

        btnSalvarPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario userLogado = new Usuario(1,"Amanda", "mayuzera", "mayu@gmail.com","soader123", 0);
                Categoria categoriaSelec = new Categoria(1, "DOGO");
                Post novoPost = new Post();
                novoPost.setId(1);
                novoPost.setTitulo(tvTitulo.getText().toString());
                novoPost.setUsuario(userLogado);
                novoPost.setCategoria(categoriaSelec);
                novoPost.setImagem(imagemPost);
                novoPost.setMediaVotos(new Double(0));


                dbHelper.persistUsuario(userLogado);
                dbHelper.persistCategoria(categoriaSelec);
                dbHelper.persistPost(novoPost);

                Toast.makeText(getApplicationContext(), "SALVOU POST NO BANCO",Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "SALVOU POST NO BANCO",Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "SALVOU POST NO BANCO",Toast.LENGTH_LONG).show();
            }
        });

        //checa permissioes do aparelho e abre a chama função pra abrir a galeria
        if (EasyPermissions.hasPermissions(getApplicationContext(), galleryPermissions)) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            final int ACTIVITY_SELECT_IMAGE = 1234;
            startActivityForResult(intent, ACTIVITY_SELECT_IMAGE);
        } else {
            ActivityCompat.requestPermissions(this, galleryPermissions, 101);
        }

    }

    //passa resultado das permissoes, pega a imagem escolhida e coloca na imageView da tela de cadastro
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 1234:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();


                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);

                    /* Now you have choosen image in Bitmap format in object "yourSelectedImage". You can use it in way you want! */
                    imagemPost.setImageBitmap(yourSelectedImage);
                }
        }

    }
}
