package com.example.joshua.ljc;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainMenuActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private EditText searchEditText;
    private ImageButton searchImageButton;
    private ProjectsFragment projectsFragment;
    private TestimonialsFragment testimonialsFragment;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        searchEditText = (EditText) findViewById(R.id.search_EditText);
        searchImageButton = (ImageButton) findViewById(R.id.search_ImageButton);
        searchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (tabLayout.getSelectedTabPosition()){
                    case 0:
                        projectsFragment.loadProjects(searchEditText.getText().toString());
                        break;
                    case 1:
                        testimonialsFragment.loadTestimonials(searchEditText.getText().toString());
                        break;
                }
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (tabLayout.getSelectedTabPosition()){
                    case 0:
                        startActivity(new Intent(MainMenuActivity.this, AddProjectActivity.class));
                        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        break;
                    case 1:
                        testimonialsFragment.addTestimonial(null);
                        fab.hide();
                        break;
                }

            }
        });
    }

    public void hideFab() {
        fab.hide();
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    projectsFragment = new ProjectsFragment();
                    return projectsFragment;
                case 1:
                    testimonialsFragment = new TestimonialsFragment();
                    return testimonialsFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Projects";
                case 1:
                    return "Testimonials";
            }
            return null;
        }
    }

    public void showFab(){
        fab.show();
    }
}
