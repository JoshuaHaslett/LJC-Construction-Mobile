package com.example.joshua.ljc;

import android.content.Context;
import android.net.Uri;
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
import com.example.joshua.ljc.DataModel.Interfaces.ITestimonial;
import com.google.android.gms.actions.ItemListIntents;
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

public class TestimonialAdaptor extends ArrayAdapter {

    private List testimonialList = new ArrayList<>();
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuthorisation;
    private Context mContext;
    private TestimonialsFragment fragment;

    public TestimonialAdaptor(Context context, int resource, boolean check, TestimonialsFragment frag) {
        super(context, resource);
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
        testimonialList.add(object);
    }

    /**
     * Get the size of the list
     *
     * @return Integer value of the size of the list
     */
    @Override
    public int getCount() {
        return this.testimonialList.size();
    }

    /**
     * Get the object at a particular position within the list
     *
     * @param position the location of the selected item
     * @return  selected object
     */
    @Override
    public Object getItem(int position) {
        return this.testimonialList.get(position);
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
        final DataHandler handler;
        handler = new DataHandler();
        View row = convertView;
        if(row == null) {
            LayoutInflater inflator = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflator.inflate(R.layout.listview_testimonials, parent, false);

        }
        row.setTag(handler);

        final ITestimonial rowTestimonial = (ITestimonial) super.getItem(position); // INDEX OUT OF BOUNDS ERROR , java.lang.IndexOutOfBoundsException: Index: 13, Size: 13
        if(rowTestimonial!= null) {

            handler.nameTextView = (TextView) row.findViewById(R.id.testimonial_Name_TextView);
            handler.quoteTextView = (TextView) row.findViewById(R.id.testimonial_Quote_TextView);
            handler.editImageButton = (ImageButton) row.findViewById(R.id.testimonial_Edit_ImageButton);
            handler.removeImageButton = (ImageButton) row.findViewById(R.id.testimonial_Remove_ImageButton);
            handler.nameTextView.setText(rowTestimonial.getName());
            handler.quoteTextView.setText(rowTestimonial.getQuote());
        }

        handler.editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.editTestimonial(rowTestimonial);
            }
        });

        handler.removeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.removeTestimonial(rowTestimonial);
                testimonialList.remove(rowTestimonial);
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
            testimonialList.clear();
            notifyDataSetChanged();
            return true;
        }catch (Exception ex){
            Log.i("cleanAll",ex.getMessage());
            return false;
        }
    }

    public void removeTestimonial(ITestimonial oldTestimonial) {
        int index = 0;
        for (int i = 0; i < testimonialList.size(); i++){
            if (oldTestimonial.getUUID().equals(((ITestimonial) testimonialList.get(i)).getUUID())){
                index = i;
            }
        }
        testimonialList.remove(index);
        notifyDataSetChanged();
    }

    /**
     * The visual components for each row
     */
    private class DataHandler
    {
        TextView nameTextView;
        TextView quoteTextView;
        ImageButton editImageButton;
        ImageButton removeImageButton;
    }

}
