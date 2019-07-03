package com.projetox.Activity;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import pub.devrel.easypermissions.EasyPermissions;

public class PostUploadActivity extends AppCompatActivity {

    private TextInputLayout tilTitulo;
    private EditText tvTitulo;
    private Spinner categoria;
    private Switch ehSensivel;
    private ImageView imagemPost;
    private ImageButton btnSalvarPost;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final String TAG = "PostUploadActivity";

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
                Usuario userLogado = dbHelper.findUsuarioByID(1);
                int idCatSelecionada = categoria.getSelectedItemPosition() + 1;
                Categoria categoriaSelec = dbHelper.findCategoriaByID(idCatSelecionada);
                Post novoPost = new Post();
                novoPost.setTitulo(tvTitulo.getText().toString());
                novoPost.setUsuario(userLogado);
                novoPost.setCategoria(categoriaSelec);
                novoPost.setMediaVotos(new Double(0));
                novoPost.setNomeImagem("post"+dbHelper.findLastPostID());
                Log.d(TAG, "LastPostID: "+dbHelper.findLastPostID());
                Log.d(TAG, "nome da imagem do post: "+novoPost.getNomeImagem());
                Log.d(TAG, "id da categoria selecionada: "+ idCatSelecionada);
                Log.d(TAG, "categoria do post salvo: "+categoriaSelec.getNome());


                //salva a imagem na memória interna e o caminho dela no banco
                Bitmap bitmap = ((BitmapDrawable) imagemPost.getDrawable()).getBitmap();
                try {
                    novoPost.setCaminhoImagem(saveToInternalStorage(bitmap, novoPost.getNomeImagem()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                boolean respPost;
                respPost = dbHelper.persistPost(novoPost);
                if (respPost == true){
                    Toast.makeText(getApplicationContext(), "SALVOOOOUUU POOOOSTTT", Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });

        categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "categoria: "+categoria.getSelectedItem().toString(), Toast.LENGTH_SHORT);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //checa permissioes do aparelho e chama função pra abrir a galeria
        if (EasyPermissions.hasPermissions(getApplicationContext(), galleryPermissions)) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_COMPONENT_NAME, MediaStore.Images.Media.DISPLAY_NAME);
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

    private String saveToInternalStorage(Bitmap bitmapImage, String nomeImagem) throws IOException {
        Log.d(TAG, "nome da imagem que chegou na SaveToInternalStorage: "+nomeImagem);
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, nomeImagem);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return directory.getAbsolutePath();
    }



}
