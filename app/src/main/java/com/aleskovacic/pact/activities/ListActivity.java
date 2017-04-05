package com.aleskovacic.pact.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.aleskovacic.pact.R;
import com.aleskovacic.pact.adapters.ListAdapter;
import com.aleskovacic.pact.pojo.DataObject;
import com.aleskovacic.pact.views.SpacesItemDecorator;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements ListAdapter.ClickListener {

    private static final int LIST_SPACE_DIV = 16;
    private RecyclerView mRecyclerView;
    private ListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ArrayList<DataObject> testing = this.getIntent().getParcelableArrayListExtra(DataObject.EXTRA_LIST_ARRAY);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }


        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mListAdapter = new ListAdapter(this, testing);

        mListAdapter.setClickListener(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpacesItemDecorator(this, LIST_SPACE_DIV));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mListAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void itemClicked(View view, int position) {
        Intent intent = new Intent(getApplicationContext(), EventActivity.class);
        intent.putExtra(DataObject.EXTRA_OBJECT, mListAdapter.getItem(position));
        startActivity(intent);
    }
}
