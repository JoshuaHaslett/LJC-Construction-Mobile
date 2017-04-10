package com.example.joshua.ljc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.joshua.ljc.DataModel.Interfaces.IProject;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joshua on 31/01/2017.
 */

public class ProjectAdaptor extends ArrayAdapter {

    private List projectList = new ArrayList<>();
    private StorageReference mStorage;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuthorisation;
    private Context mContext;
    private ProjectsFragment fragment;

    public ProjectAdaptor(Context context, int resource, boolean check, ProjectsFragment frag) {
        super(context, resource);
        mStorage = FirebaseStorage.getInstance().getReference();
        mAuthorisation = FirebaseAuth.getInstance();
        this.mContext = context;
        fragment = frag;
    }


    /**
     * Add object to adapter
     *
     * @param object to be added
     */
    @Override
    public void add(Object object) {
        super.add(object);
        projectList.add(object);
    }

    /**
     * Get the size of the list
     *
     * @return Integer value of the size of the list
     */
    @Override
    public int getCount() {
        return this.projectList.size();
    }

    /**
     * Get the object at a particular position within the list
     *
     * @param position the location of the selected item
     * @return  selected object
     */
    @Override
    public Object getItem(int position) {
        return this.projectList.get(position);
    }

    /**
     * Retrieve adapter UI
     *
     * @param position current position with the list
     * @param convertView view
     * @param parent container
     * @return view, UI elements
     */
    @Override
    public View getView(int position, final View convertView, final ViewGroup parent) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuthorisation.getCurrentUser().getUid());
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        final DataHandler handler;
        handler = new DataHandler();

        //start
        View row = convertView;
        if(row == null) {
            LayoutInflater inflator = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflator.inflate(R.layout.listview_projects, parent, false);

        }
        row.setTag(handler);

        final IProject rowProject = (IProject) super.getItem(position); // INDEX OUT OF BOUNDS ERROR , java.lang.IndexOutOfBoundsException: Index: 13, Size: 13
        if(rowProject!= null) {

            handler.titleTextView = (TextView) row.findViewById(R.id.projectTitle_TextView);
            handler.pictureImageView = (ImageView) row.findViewById(R.id.projectPicture_ImageView);
            handler.editImageButton = (ImageButton) row.findViewById(R.id.projectEdit_ImageButton);
            handler.removeImageButton = (ImageButton) row.findViewById(R.id.projectRemove_ImageButton);
            handler.titleTextView.setText(rowProject.getName());

            StorageReference filePath = mStorage.child("images").child(rowProject.getUUID());
            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(getContext()).load(uri).fit().into(handler.pictureImageView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });

        }

        handler.editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.updateProject(rowProject);

            }
        });

        handler.removeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.deleteProject(rowProject);
            }
        });



        return row;
    }


    /**
     * Removes all objects from the events list
     *
     * @return boolean value signifying whether the event list clearing process was completed
     */
    public Boolean cleanAll(){
        try {
            projectList.clear();
            notifyDataSetChanged();
            return true;
        }catch (Exception ex){
            Log.i("cleanAll",ex.getMessage());
            return false;
        }
    }

    public void deleteProject(IProject rowProject){
        projectList.remove(rowProject);
    }

    /**
     * The visual components for each row
     */
    private class DataHandler
    {
        TextView titleTextView;
        ImageView pictureImageView;
        ImageButton editImageButton;
        ImageButton removeImageButton;
    }

}
