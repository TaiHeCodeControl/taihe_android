package com.taihe.eggshell.job.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.taihe.eggshell.R;
import com.taihe.eggshell.widget.swipecard.SwipeFlingAdapterView;

import java.util.ArrayList;

public class SwipecardsActivity extends Activity {

    private ArrayList<JobInfo> al = new ArrayList<JobInfo>();
    private  CardsDataAdapter arrayAdapter;
    private int i;
    private Context context;

    private SwipeFlingAdapterView flingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipecard);
        context = this;
        for(int i=0;i<10;i++){
            JobInfo ji = new JobInfo();
            ji.setId(i);
            ji.setName("CARD " + i);
            ji.setAge("2"+i);
            al.add(ji);
        }
        flingContainer = (SwipeFlingAdapterView)findViewById(R.id.frame);
        arrayAdapter =  new CardsDataAdapter(getApplicationContext(),al);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                al.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                makeToast(SwipecardsActivity.this, "Left!");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                makeToast(SwipecardsActivity.this, "Right!");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                for(int i=10;i<20;i++){
                    JobInfo ji = new JobInfo();
                    ji.setId(i);
                    ji.setName("CARD " + i);
                    ji.setAge("3"+i);
                    al.add(ji);
                }
//                al.add("XML ".concat(String.valueOf(i)));
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                makeToast(SwipecardsActivity.this, "Clicked!");
            }
        });

    }

    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }


   /* @OnClick(R.id.right)
    public void right() {
        *//**
         * Trigger the right event manually.
         *//*
        flingContainer.getTopCardListener().selectRight();
    }

    @OnClick(R.id.left)
    public void left() {
        flingContainer.getTopCardListener().selectLeft();
    }*/




}
