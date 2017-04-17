package com.example.joshua.ljc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joshua.ljc.DataModel.Interfaces.IProject;
import com.example.joshua.ljc.DataModel.Project;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProjectsFragment extends Fragment {
    private Toast toast = null;
    private ListView projectsListView;
    private ProjectAdaptor newBuildAdaptor;
    private ProjectAdaptor extensionAdaptor;
    private ProjectAdaptor refurbishmentAdaptor;
    private DatabaseReference mDatabaseReference;
    private CheckBox newBuildsCheckBox;
    private CheckBox extensionsCheckBox;
    private CheckBox refurbishmentsCheckBox;
    private String projectType;
    private StorageReference mStorage;
    private ProgressDialog progressDialog;
    private View confirmationDialogContainer;
    private Button dialogCancel;
    private Button dialogConfirm;
    private TextView dialogTitle;
    private TextView dialogDescription;
    private TextView noResultsTextView;
    private Toast countToast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_projects, container, false);
        toast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        countToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        newBuildsCheckBox = (CheckBox) rootView.findViewById(R.id.projects_NewBuilds_CheckBox);
        extensionsCheckBox = (CheckBox) rootView.findViewById(R.id.projects_Extensions_CheckBox);
        refurbishmentsCheckBox = (CheckBox) rootView.findViewById(R.id.projects_Refurbishments_CheckBox);
        projectsListView = (ListView) rootView.findViewById(R.id.projects_ListView);
        newBuildAdaptor = new ProjectAdaptor(getContext(), R.layout.listview_projects, true, this);
        extensionAdaptor = new ProjectAdaptor(getContext(), R.layout.listview_projects, true, this);
        refurbishmentAdaptor = new ProjectAdaptor(getContext(), R.layout.listview_projects, true, this);
        confirmationDialogContainer = rootView.findViewById(R.id.projects_DialogueContainer_RelativeLayout);
        dialogCancel = (Button) confirmationDialogContainer.findViewById(R.id.confirmationDialog_Cancel_Button);
        dialogConfirm = (Button) confirmationDialogContainer.findViewById(R.id.confirmationDialog_Confirm_Button);
        dialogTitle = (TextView) confirmationDialogContainer.findViewById(R.id.confirmationDialog_Title_TextView);
        dialogDescription = (TextView) confirmationDialogContainer.findViewById(R.id.confirmationDialog_Description_TextView);
        noResultsTextView = (TextView) rootView.findViewById(R.id.projects_NoResults_TextView);
        projectsListView.setAdapter(newBuildAdaptor);
        newBuildsCheckBox.setChecked(true);
        newBuildsCheckBox.setEnabled(false);
        projectType = "NewBuilds";
        progressDialog = new ProgressDialog(getActivity());
        mStorage = FirebaseStorage.getInstance().getReference();
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
                    projectsListView.setAdapter(newBuildAdaptor);
                    if (newBuildAdaptor.getCount() < 1) {
                        loadProjects("");
                    }
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
                    projectsListView.setAdapter(extensionAdaptor);
                    if (extensionAdaptor.getCount() < 1) {
                        loadProjects("");
                    }
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
                    projectsListView.setAdapter(refurbishmentAdaptor);
                    if (refurbishmentAdaptor.getCount() < 1) {
                        loadProjects("");
                    }
                }
            }
        });

        return rootView;
    }

    public void loadProjects(String search) {
        progressDialog.setTitle("Loading projects");
        progressDialog.setMessage("Retrieving data...");
        progressDialog.show();
        switch (projectType) {
            case "NewBuilds":
                newBuildAdaptor.cleanAll();
                newBuildAdaptor.clear();
                break;
            case "Extensions":
                extensionAdaptor.cleanAll();
                extensionAdaptor.clear();
                break;
            case "Refurbishments":
                refurbishmentAdaptor.cleanAll();
                refurbishmentAdaptor.clear();
        }

        String formattedSearch = search;
        if (formattedSearch.length() > 0) {
            formattedSearch = formattedSearch.substring(0, 1).toUpperCase() +
                    formattedSearch.substring(1);
        }
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(projectType);
        Query queryRef = mDatabaseReference.orderByChild("name").startAt(formattedSearch).
                endAt(formattedSearch + "\uf8ff");
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    try {
                        parseProject(child);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ((MainMenuActivity)getContext()).enableSearch();
                progressDialog.hide();
                if (dataSnapshot.getChildrenCount() < 1){
                    noResultsTextView.setVisibility(View.VISIBLE);
                }else{
                    noResultsTextView.setVisibility(View.GONE);
                }
                countToast.setText("Projects returned: "+dataSnapshot.getChildrenCount());
                countToast.show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.hide();
                ((MainMenuActivity)getContext()).enableSearch();
            }
        });
    }

    private void parseProject(DataSnapshot projectData) throws Exception {
        String image = projectData.getKey();
        String title = projectData.child("name").getValue().toString();
        String description = projectData.child("description").getValue().toString();
        IProject project = new Project(title, description, image);
        switch (projectType) {
            case "NewBuilds":
                newBuildAdaptor.add(project);
                break;
            case "Extensions":
                extensionAdaptor.add(project);
                break;
            case "Refurbishments":
                refurbishmentAdaptor.add(project);
        }

    }

    public void updateProject(IProject project) {
        Intent intent = new Intent(getContext(), AddProjectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Project", project);
        bundle.putString("Type", projectType);
        intent.putExtra("Bundle", bundle);
        startActivity(intent);
    }

    public void deleteProject(final IProject project) {

        dialogTitle.setText("Confirm project removal");
        dialogDescription.setText("Are you sure you want to remove the project: "+project.getName());
        confirmationDialogContainer.setVisibility(View.VISIBLE);
        ((MainMenuActivity) getActivity()).hideFab();

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialogContainer.setVisibility(View.INVISIBLE);
                ((MainMenuActivity) getActivity()).showFab();
            }
        });

        dialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Removing project");
                progressDialog.setMessage("Verifying removal...");
                progressDialog.show();
                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(projectType).child(project.getUUID());
                mDatabaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mStorage = FirebaseStorage.getInstance().getReference();
                            mStorage.child("images").child(project.getUUID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.i("Project", "Successful removal of project image.");
                                    toast.setText("Successful removal: " + project.getName());
                                    toast.show();
                                    confirmationDialogContainer.setVisibility(View.INVISIBLE);
                                    ((MainMenuActivity) getActivity()).showFab();
                                    switch (projectType) {
                                        case "NewBuilds":
                                            newBuildAdaptor.deleteProject(project);
                                            newBuildAdaptor.remove(project);
                                            newBuildAdaptor.notifyDataSetChanged();
                                            if (newBuildAdaptor.getCount() < 1){
                                                noResultsTextView.setVisibility(View.VISIBLE);
                                            }
                                            break;
                                        case "Extensions":
                                            extensionAdaptor.deleteProject(project);
                                            extensionAdaptor.remove(project);
                                            extensionAdaptor.notifyDataSetChanged();
                                            if (extensionAdaptor.getCount() < 1){
                                                noResultsTextView.setVisibility(View.VISIBLE);
                                            }
                                            break;
                                        case "Refurbishments":
                                            refurbishmentAdaptor.deleteProject(project);
                                            refurbishmentAdaptor.remove(project);
                                            refurbishmentAdaptor.notifyDataSetChanged();
                                            if (refurbishmentAdaptor.getCount() < 1){
                                                noResultsTextView.setVisibility(View.VISIBLE);
                                            }
                                    }
                                    progressDialog.hide();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i("Project", "Unsuccessful removal of project image.");
                                    toast.setText("Unable to remove Image.");
                                    toast.show();
                                    progressDialog.hide();
                                }
                            });
                        } else {
                            toast.setText("Unable to remove Image.");
                            toast.show();
                            progressDialog.hide();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProjects("");
    }

}


