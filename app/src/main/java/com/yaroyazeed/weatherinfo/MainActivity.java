package com.yaroyazeed.weatherinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements CityAdapter.ViewHolder.ClickListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CityAdapter adapter;
    ArrayList<CityItem> cityItems = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cityItems.add(new CityItem(R.drawable.kenya, "Kenya", "Kenya"));
        cityItems.add(new CityItem(R.drawable.lesotho, "Lesotho", "Lesotho"));
        cityItems.add(new CityItem(R.drawable.egypt, "Cairo", "Egypt"));
        cityItems.add(new CityItem(R.drawable.indonesia, "Jakarta", "Indonesia"));
        cityItems.add(new CityItem(R.drawable.nigeria, "Lagos", "Nigeria"));
        cityItems.add(new CityItem(R.drawable.turkey, "Ankara", "Turkey"));
        cityItems.add(new CityItem(R.drawable.nigeria, "Abuja", "Nigeria"));
        cityItems.add(new CityItem(R.drawable.nigeria, "Kano", "Nigeria"));
        cityItems.add(new CityItem(R.drawable.usa, "New York", "USA"));
        cityItems.add(new CityItem(R.drawable.peru, "Peru", "Peru"));
        cityItems.add(new CityItem(R.drawable.usa, "Texas", "USA"));
        cityItems.add(new CityItem(R.drawable.canada, "Winnipeg", "Canada"));
        cityItems.add(new CityItem(R.drawable.brazil, "Amazon", "Brazil"));
        cityItems.add(new CityItem(R.drawable.iraq, "Bagdad", "Iraq"));
        cityItems.add(new CityItem(R.drawable.belarus, "Belarus", "Belarus"));
        cityItems.add(new CityItem(R.drawable.england, "Westham", "England"));

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        adapter = new CityAdapter(cityItems, this, this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemViewCacheSize(60);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                recyclerView.removeAllViews();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search city");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onItemClick(int position) {
        cityItems.get(position);

        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, Details.class);
        intent.putExtra("CityName", cityItems.get(position).getCityName());
        intent.putExtra("CountryName", cityItems.get(position).getCityCountry());
        bundle.putInt("CityImage", cityItems.get(position).getCityImage());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}