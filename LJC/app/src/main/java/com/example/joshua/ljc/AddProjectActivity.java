package com.example.joshua.ljc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joshua.ljc.DataModel.Interfaces.IProject;
import com.example.joshua.ljc.DataModel.Project;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.content.ContentValues.TAG;

public class AddProjectActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText descriptionEditText;
    private CheckBox newBuildsCheckBox;
    private CheckBox extensionsCheckBox;
    private CheckBox refurbishmentsCheckBox;
    private ImageButton pictureImageButton;
    private ImageButton cancelImageButton;
    private ImageButton addImageButton;
    private Toast toast;
    private final int MY_READ_EXTERNAL_STORAGE = 1;
    private static final int GALLERY_INTENT = 2;
    private Uri uri;
    private StorageReference mStorage;
    private DatabaseReference mDatabaseReference;
    private String projectType;
    private IProject updatedProject;
    private boolean updating;
    private String originalType;
    private ProgressDialog progressDialog;
    private View confirmationDialogContainer;
    private Button dialogCancel;
    private Button dialogConfirm;
    private TextView dialogTitle;
    private TextView dialogDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        nameEditText = (EditText) findViewById(R.id.addProject_Name_EditText);
        descriptionEditText = (EditText) findViewById(R.id.addProject_Description_EditText);
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        newBuildsCheckBox = (CheckBox) findViewById(R.id.addProject_NewBuilds_CheckBox);
        extensionsCheckBox = (CheckBox) findViewById(R.id.addProject_Extensions_CheckBox);
        refurbishmentsCheckBox = (CheckBox) findViewById(R.id.addProject_Refurbishments_CheckBox);
        pictureImageButton = (ImageButton) findViewById(R.id.addProject_Picture_ImageButton);
        cancelImageButton = (ImageButton) findViewById(R.id.addProject_Cancel_ImageButton);
        addImageButton = (ImageButton) findViewById(R.id.addProject_Add_ImageButton);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        confirmationDialogContainer = findViewById(R.id.addProject_DialogueContainer_RelativeLayout);
        dialogCancel = (Button) confirmationDialogContainer.findViewById(R.id.confirmationDialog_Cancel_Button);
        dialogConfirm = (Button) confirmationDialogContainer.findViewById(R.id.confirmationDialog_Confirm_Button);
        dialogTitle = (TextView) confirmationDialogContainer.findViewById(R.id.confirmationDialog_Title_TextView);
        dialogDescription = (TextView) confirmationDialogContainer.findViewById(R.id.confirmationDialog_Description_TextView);

        if (getIntent().hasExtra("Bundle")) {
            updatedProject = (Project) getIntent().getBundleExtra("Bundle").getSerializable("Project");

            if (getIntent().getBundleExtra("Bundle").getString("Type") != null) {
                originalType = getIntent().getBundleExtra("Bundle").getString("Type");
            }
            if (originalType != null) {
                switch (originalType) {
                    case "NewBuilds":
                        newBuildsCheckBox.setChecked(true);
                        newBuildsCheckBox.setEnabled(false);
                        projectType = "NewBuilds";
                        break;
                    case "Extensions":
                        extensionsCheckBox.setChecked(true);
                        extensionsCheckBox.setEnabled(false);
                        projectType = "Extensions";
                        break;
                    case "Refurbishments":
                        refurbishmentsCheckBox.setChecked(true);
                        refurbishmentsCheckBox.setEnabled(false);
                        projectType = "Refurbishments";
                        break;
                }
            }
            updating = true;
        }

        newBuildsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    newBuildsCheckBox.setEnabled(false);
                    extensionsCheckBox.setChecked(false);
                    extensionsCheckBox.setEnabled(true);
                    refurbishmentsCheckBox.setChecked(false);
                    refurbishmentsCheckBox.setEnabled(true);
                    projectType = "NewBuilds";
                }
            }
        });
        extensionsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    extensionsCheckBox.setEnabled(false);

                    newBuildsCheckBox.setChecked(false);
                    newBuildsCheckBox.setEnabled(true);
                    refurbishmentsCheckBox.setChecked(false);
                    refurbishmentsCheckBox.setEnabled(true);
                    projectType = "Extensions";
                }
            }
        });
        refurbishmentsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    refurbishmentsCheckBox.setEnabled(false);

                    extensionsCheckBox.setChecked(false);
                    extensionsCheckBox.setEnabled(true);
                    newBuildsCheckBox.setChecked(false);
                    newBuildsCheckBox.setEnabled(true);
                    projectType = "Refurbishments";
                }
            }
        });

        requestPermission();

        pictureImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

        cancelImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed();
            }
        });

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updating){
                    dialogTitle.setText("Confirm project update");
                    dialogDescription.setText("Are you sure you want to update the project: " + nameEditText.getText().toString());
                }else{
                    dialogTitle.setText("Confirm project creation");
                    dialogDescription.setText("Are you sure you want to post the project: " + nameEditText.getText().toString());
                }
                confirmationDialogContainer.setVisibility(View.VISIBLE);
                dialogCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmationDialogContainer.setVisibility(View.INVISIBLE);
                    }
                });

                dialogConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postProject();
                        confirmationDialogContainer.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

        if (updating) {
            nameEditText.setText(updatedProject.getName());
            descriptionEditText.setText(updatedProject.getDescription());
            // get image
            StorageReference filePath = mStorage.child("images").child(updatedProject.getUUID());
            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri loadedUri) {
                    uri = loadedUri;
                    pictureImageButton.setScaleType(ImageView.ScaleType.FIT_XY);
                    Picasso.with(AddProjectActivity.this).load(loadedUri).fit().into(pictureImageButton);
                }
            });
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            uri = data.getData();
            if (uri != null) {
                // Get the path from the Uri
                String path = getPathFromURI(uri);
                Log.i(TAG, "Image Path : " + path);
                // Set the image in ImageView
                pictureImageButton.setScaleType(ImageView.ScaleType.FIT_XY);
                pictureImageButton.setImageURI(uri);
            }

        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    boolean requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_READ_EXTERNAL_STORAGE);
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    }
                    //do nothing
                } else {
                    toast.setText("Please give permission for reading external storage.");
                    toast.show();
                    onBackPressed();
                }
            }
        }
    }

    private boolean postProject() {
        progressDialog.setTitle("Posting project");
        progressDialog.setMessage("Verifying data...");
        progressDialog.show();
        boolean check = true;
        if (projectType == null) {
            toast.setText("Please select a project type.");
            toast.show();
            check = false;
            progressDialog.hide();
        } else if (uri == null) {
            toast.setText("Please select a display image.");
            toast.show();
            check = false;
            progressDialog.hide();
        }
        if (check) {
            if (updating) {
                try {
                    updatedProject.setName(nameEditText.getText().toString());
                    updatedProject.setDescription(descriptionEditText.getText().toString());
                    if (!projectType.equals(originalType)) {
                        mDatabaseReference.child(originalType).child(updatedProject.getUUID()).removeValue();
                    }
                    DatabaseReference postRef = mDatabaseReference.child(projectType).child(updatedProject.getUUID());
                    postRef.setValue(updatedProject);
                    StorageReference filePath = mStorage.child("images").child(updatedProject.getImage());
                    filePath.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            progressDialog.hide();
                            toast.setText("You updated: " + updatedProject.getName());
                            toast.show();
                            finish();
                            onBackPressed();
                        }
                    });
                } catch (Exception ex) {
                    progressDialog.hide();
                    toast.setText(ex.getMessage());
                    toast.show();
                }

            } else {
                try {
                    IProject validateProject = new Project(nameEditText.getText().toString(), descriptionEditText.getText().toString(), "example");
                    DatabaseReference postRef = mDatabaseReference.child(projectType).push();
                    IProject project = new Project(nameEditText.getText().toString(), descriptionEditText.getText().toString(), postRef.getKey());
                    postRef.setValue(project);
                    StorageReference filePath = mStorage.child("images").child(postRef.getKey());
                    filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            finish();
                            onBackPressed();
                            progressDialog.hide();
                            toast.setText("You successfully added the project.");
                            toast.show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            toast.setText("Oops, something went wrong. Please try again later.");
                            toast.show();
                            progressDialog.hide();
                        }
                    });
                    toast.setText("You added: " + project.getName());
                    toast.show();
                } catch (Exception ex) {
                    toast.setText(ex.getMessage());
                    toast.show();
                    progressDialog.hide();
                }
            }
        }
        return true;
    }

}
