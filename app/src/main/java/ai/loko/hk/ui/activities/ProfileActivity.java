package ai.loko.hk.ui.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

import ai.loko.hk.ui.db.AppDatabase;
import ai.loko.hk.ui.db.ProfileEntity;
import ai.loko.hk.ui.model.Profile;
import ui.R;


public class ProfileActivity extends AppCompatActivity implements ListItemSwipeListener.RecyclerItemTouchHelperListener {
    List<Profile> profiles = new ArrayList<>();

    private CoordinatorLayout coordinatorLayout;

    private RecyclerView mRecyclerView;
    private ProfileAdapter mProfileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_main);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getString(R.string.title_activity_profile));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        coordinatorLayout=findViewById(R.id.coordinator_layout);

        mRecyclerView = findViewById(R.id.recycler_view);
        mProfileAdapter = new ProfileAdapter(getApplicationContext(),profiles);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.addOnItemTouchListener(new ListItemTouchListener(getApplicationContext(), mRecyclerView, new ListItemTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        ItemTouchHelper.SimpleCallback itemSimpleCallback=new ListItemSwipeListener(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemSimpleCallback).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mProfileAdapter);
        setUpSomeDummyData();
    }


    private void setUpSomeDummyData() {

    // AppDatabase db=AppDatabase.getDatabase(getApplicationContext());
     //db.profileDAO().
        profiles.add(new Profile("loko",12.0f,81.8f,98.3f,81.0f));
        profiles.add(new Profile("loko",10.0f,86.8f,68.9f,97.0f));
        profiles.add(new Profile("loko",13.0f,89.8f,68.3f,79.0f));
        mProfileAdapter.notifyDataSetChanged();

    }

    private Profile getProfileForView(ProfileEntity profileEntity){
       return new Profile(profileEntity.getName(),profileEntity.getX1(),profileEntity.getY1(),profileEntity.getX2(),profileEntity.getY2());
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ProfileAdapter.MyViewHolder){
            final int deletedIndex=viewHolder.getAdapterPosition();
            String name=profiles.get(deletedIndex).getName();
            final Profile deletedProfile= profiles.get(deletedIndex);
            mProfileAdapter.removeProfile(deletedIndex);

            Snackbar snackbar=Snackbar.make(coordinatorLayout,name+" removed from list",Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mProfileAdapter.restoreProfile(deletedProfile,deletedIndex);
                }
            });

        }
    }
}
