package com.elenakozachenko.githubtest.controller;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.elenakozachenko.githubtest.ItemAdapter;
import com.elenakozachenko.githubtest.R;
import com.elenakozachenko.githubtest.api.Client;
import com.elenakozachenko.githubtest.api.Service;
import com.elenakozachenko.githubtest.model.Item;
import com.elenakozachenko.githubtest.model.ItemResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
private RecyclerView recyclerView;
TextView Disconnected;
private Item item;
ProgressDialog pd;
private SwipeRefreshLayout swipeContainer;


@Override
    protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();

    swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipeContainer);
    swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh(){
            loadJSON();
Toast.makeText(MainActivity.this, "Github Users", Toast.LENGTH_SHORT).show();
        }
    });

    }
    private void initViews(){
    pd = new ProgressDialog(this);
    pd.setMessage("Searching Github Users...");
    pd.setCancelable(false);
    pd.show();
    recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    recyclerView.smoothScrollToPosition(0);
    loadJSON();

    }
    private void loadJSON(){
    Disconnected = (TextView) findViewById(R.id.disconnected);
    try{
        Client Client = new Client();
        Service apiService= Client.getClient().create(Service.class);
        Call<ItemResponse> call=apiService.getItems();
        call.enqueue(new Callback<ItemResponse>() {
            @Override
            public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                List<Item> items = response.body().getItems();
                recyclerView.setAdapter(new ItemAdapter(getApplicationContext(), items));
                recyclerView.smoothScrollToPosition(0);
                swipeContainer.setRefreshing(false);
                pd.hide();

            }

            @Override
            public void onFailure(Call<ItemResponse> call, Throwable t) {
Log.d("Error",t.getMessage());
                Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                Disconnected.setVisibility(View.VISIBLE);
                pd.hide();
            }
        });
    }catch(Exception e){
        Log.d("Error", e.getMessage());
        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
    }
    }
}
