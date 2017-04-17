package com.example.joshua.ljc;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joshua.ljc.DataModel.Interfaces.ITestimonial;
import com.example.joshua.ljc.DataModel.Testimonial;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class TestimonialsFragment extends Fragment {
    private Toast toast = null;
    private ListView testimonialsListView;
    private TestimonialAdaptor adaptor;
    private DatabaseReference mDatabaseReference;
    private View alertDialogContainer;
    private TextView testimonialTitleTextView;
    private EditText clientsNameEditText;
    private EditText clientsQuoteEditText;
    private Button cancelButton;
    private Button addButton;
    private View alertDialogConfirmation;
    private ProgressDialog progressDialog;
    private Button confirmationCancel;
    private Button confirmationConfirm;
    private TextView confirmationTitle;
    private TextView confirmationDescription;
    private TextView noResultsTextView;
    private Toast countToast;
    private View rootView;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_testimonials, container, false);
        toast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        countToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        testimonialsListView = (ListView) rootView.findViewById(R.id.testimonials_ListView);
        adaptor = new TestimonialAdaptor(getContext(), R.layout.listview_testimonials, true, this);
        testimonialsListView.setAdapter(adaptor);
        alertDialogContainer = rootView.findViewById(R.id.alertDialogContainer);

        progressDialog = new ProgressDialog(getActivity());
        //Testimonial container UI
        testimonialTitleTextView = (TextView) rootView.findViewById(R.id.testimonialDialog_Title_TextView);
        clientsNameEditText = (EditText) rootView.findViewById(R.id.testimonialDialog_ClientName_EditText);
        clientsQuoteEditText = (EditText) rootView.findViewById(R.id.testimonialDialog_Quote_EditText);
        cancelButton = (Button) rootView.findViewById(R.id.testimonialDialog_Cancel_Button);
        addButton = (Button) rootView.findViewById(R.id.testimonialDialog_Add_Button);
        noResultsTextView = (TextView) rootView.findViewById(R.id.testimonials_NoResults_TextView);
        alertDialogConfirmation = rootView.findViewById(R.id.testimonials_DialogueContainer_RelativeLayout);
        confirmationCancel = (Button) alertDialogConfirmation.findViewById(R.id.confirmationDialog_Cancel_Button);
        confirmationConfirm = (Button) alertDialogConfirmation.findViewById(R.id.confirmationDialog_Confirm_Button);
        confirmationTitle = (TextView) alertDialogConfirmation.findViewById(R.id.confirmationDialog_Title_TextView);
        confirmationDescription = (TextView) alertDialogConfirmation.findViewById(R.id.confirmationDialog_Description_TextView);

        confirmationTitle.setText("Remove testimonial");

        confirmationCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogConfirmation.setVisibility(View.INVISIBLE);
                ((MainMenuActivity) getActivity()).showFab();
            }
        });
        return rootView;
    }

    public void loadTestimonials(String search) {
        progressDialog.setTitle("Loading testimonials");
        progressDialog.setMessage("Retrieving data...");
        progressDialog.show();
        adaptor.cleanAll();
        adaptor.clear();
        String formattedSearch = search;
        if (formattedSearch.length() > 0) {
            formattedSearch = formattedSearch.substring(0, 1).toUpperCase() +
                    formattedSearch.substring(1);
        }
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Testimonials");
        Query queryRef = mDatabaseReference.orderByChild("name").startAt(formattedSearch).
                endAt(formattedSearch + "\uf8ff");
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    parseTestimonial(child);
                }
                progressDialog.hide();
                ((MainMenuActivity)getContext()).enableSearch();
                if (dataSnapshot.getChildrenCount() < 1){
                    noResultsTextView.setVisibility(View.VISIBLE);
                }else{
                    noResultsTextView.setVisibility(View.GONE);
                }
                countToast.setText("Testimonials returned: "+dataSnapshot.getChildrenCount());
                countToast.show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.hide();
                ((MainMenuActivity)getContext()).enableSearch();
            }
        });
    }

    private void parseTestimonial(DataSnapshot data) {
        String name = data.child("name").getValue().toString();
        String quote = data.child("quote").getValue().toString();
        String id = data.getKey();
        try {
            ITestimonial testimonial = new Testimonial(name, quote, id);
            adaptor.add(testimonial);
        } catch (Exception ex) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTestimonials("");
    }

    public void addTestimonial(final ITestimonial oldTestimonial) {
        if (oldTestimonial == null){
            testimonialTitleTextView.setText("Add testimonial:");
            addButton.setText("Add");
        }
        testimonialsListView.setEnabled(false);
        alertDialogContainer.setVisibility(View.VISIBLE);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = true;
                try {
                    final ITestimonial checkableTestimonial = new Testimonial(clientsNameEditText.getText().toString(), clientsQuoteEditText.getText().toString());

                } catch (Exception ex) {
                    toast.setText(ex.getMessage());
                    toast.show();
                    resetAlertDialogue(false);
                    addTestimonial(oldTestimonial);
                    check = false;
                }

                if (check) {
                    clientsNameEditText.setVisibility(View.GONE);
                    clientsQuoteEditText.setVisibility(View.GONE);
                    if (oldTestimonial == null) {
                        testimonialTitleTextView.setText("Are you sure you want to add this testimonial?");
                        addButton.setText("Confirm addition");
                        addButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    final DatabaseReference postRef = mDatabaseReference.push();
                                    final ITestimonial testimonial = new Testimonial(clientsNameEditText.getText().toString(), clientsQuoteEditText.getText().toString());
                                    postRef.setValue(testimonial).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            try {
                                                testimonial.setUUID(postRef.getKey());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            adaptor.add(testimonial);
                                            noResultsTextView.setVisibility(View.GONE);
                                            adaptor.notifyDataSetChanged();
                                            toast.setText("You added the testimonial by: " + testimonial.getName());
                                            toast.show();
                                            clearData();
                                            resetAlertDialogue(false);
                                            alertDialogContainer.setVisibility(View.INVISIBLE);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            toast.setText("Unable to add the testimonial by " + testimonial.getName() + " at this time.");
                                            toast.show();
                                            resetAlertDialogue(false);
                                            addTestimonial(oldTestimonial);
                                        }
                                    });
                                } catch (Exception ex) {
                                    toast.setText(ex.getMessage());
                                    toast.show();
                                    resetAlertDialogue(false);
                                    addTestimonial(oldTestimonial);
                                }
                            }
                        });

                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                resetAlertDialogue(false);
                                addTestimonial(oldTestimonial);
                            }
                        });

                    } else {
                        testimonialTitleTextView.setText("Are you sure you want to update this testimonial?");
                        addButton.setText("Confirm update");
                        cancelButton.setText("Back");
                        addButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    final DatabaseReference postRef = mDatabaseReference.child(oldTestimonial.getUUID());
                                    final ITestimonial testimonial = new Testimonial(clientsNameEditText.getText().toString(), clientsQuoteEditText.getText().toString());
                                    postRef.setValue(testimonial).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            try {
                                                testimonial.setUUID(oldTestimonial.getUUID());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            adaptor.removeTestimonial(oldTestimonial);
                                            adaptor.remove(oldTestimonial);
                                            adaptor.add(testimonial);
                                            adaptor.notifyDataSetChanged();
                                            toast.setText("You updated the testimonial by: " + testimonial.getName());
                                            toast.show();
                                            clearData();
                                            resetAlertDialogue(true);
                                            alertDialogContainer.setVisibility(View.INVISIBLE);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            toast.setText("Unable to update the testimonial by " + testimonial.getName() + " at this time.");
                                            toast.show();
                                            resetAlertDialogue(true);
                                            addTestimonial(oldTestimonial);
                                        }
                                    });;

                                } catch (Exception ex) {
                                    toast.setText(ex.getMessage());
                                    toast.show();
                                    resetAlertDialogue(true);
                                    addTestimonial(oldTestimonial);
                                }

                            }
                        });

                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                resetAlertDialogue(true);
                                addTestimonial(oldTestimonial);
                            }
                        });
                    }
                }
            }
        });
    }

    private void resetAlertDialogue(boolean update){
        clientsNameEditText.setVisibility(View.VISIBLE);
        clientsQuoteEditText.setVisibility(View.VISIBLE);

        if (update){
            testimonialTitleTextView.setText("Update testimonial:");
            addButton.setText("Update");
        }else{
            testimonialTitleTextView.setText("Add testimonial:");
            addButton.setText("Add");
        }

    }

    private void clearData() {
        testimonialsListView.setEnabled(true);
        testimonialTitleTextView.setText("Add testimonial:");
        clientsNameEditText.setText("");
        clientsQuoteEditText.setText("");
        alertDialogContainer.setVisibility(View.INVISIBLE);
        addButton.setText("add");
        ((MainMenuActivity) getActivity()).showFab();
    }

    public void editTestimonial(ITestimonial rowTestimonial) {
        addTestimonial(rowTestimonial);
        testimonialTitleTextView.setText("Update testimonial:");
        clientsNameEditText.setText(rowTestimonial.getName());
        clientsQuoteEditText.setText(rowTestimonial.getQuote());
        addButton.setText("update");
        ((MainMenuActivity) getActivity()).hideFab();

    }

    public void removeTestimonial(final ITestimonial rowTestimonial) {
        alertDialogConfirmation.setVisibility(View.VISIBLE);
        ((MainMenuActivity) getActivity()).hideFab();
        confirmationDescription.setText("Are you sure you want to remove the testimonial by " + rowTestimonial.getName()+"?");
        confirmationConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference removeRef = mDatabaseReference.child(rowTestimonial.getUUID());
                removeRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        toast.setText("" + rowTestimonial.getName() + "'s testimonial was removed.");
                        toast.show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Testimonial", "Unsuccessful removal of testimonial.");
                        toast.setText("Unable to remove testimonial.");
                        toast.show();
                    }
                });
                adaptor.deleteTestimonialFromList(rowTestimonial);
                adaptor.remove(rowTestimonial);
                adaptor.notifyDataSetChanged();
                alertDialogConfirmation.setVisibility(View.INVISIBLE);
                ((MainMenuActivity) getActivity()).showFab();
                if (adaptor.getCount() < 1){
                    noResultsTextView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

}

