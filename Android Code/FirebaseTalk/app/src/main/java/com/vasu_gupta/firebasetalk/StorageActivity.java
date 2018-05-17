package com.vasu_gupta.firebasetalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class StorageActivity extends AppCompatActivity {

    private StorageReference mStorageRef;

    Button btnUpload, btnChoose;

    TextView tvName;

    private static final int FILE_SELECT_CODE = 0;

    Uri fileUri = null;

    String fileName = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        btnUpload = findViewById(R.id.btnUpload);
        btnChoose = findViewById(R.id.btnChoose);

        tvName = findViewById(R.id.tvFile);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileUri != null){
                    upload(fileUri);
                }
            }
        });

    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    fileUri = data.getData();
                    fileName = getFileName(fileUri) ;
                    tvName.setText(fileName);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public void upload(Uri tempUri) {

        final ProgressDialog ringProgressDialog = ProgressDialog.show(StorageActivity.this, "Please Wait", "While Uploading File...", true);
        ringProgressDialog.setCancelable(false);

        StorageReference riversRef = mStorageRef.child("images/" + fileName);

        riversRef.putFile(tempUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (ringProgressDialog.isShowing())ringProgressDialog.dismiss();
                        Toast.makeText(StorageActivity.this, "File Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        tvName.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        if (ringProgressDialog.isShowing())ringProgressDialog.dismiss();
                        Toast.makeText(StorageActivity.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
