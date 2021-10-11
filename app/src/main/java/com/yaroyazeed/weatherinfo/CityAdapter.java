package com.yaroyazeed.weatherinfo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> implements Filterable {

    private ArrayList<CityItem> mCityList;
    public List<CityItem> cityListFull;
    private Context context;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appID = "24db1a52d7cc15f7fd1824a9801c2285";

    private ViewHolder.ClickListener mClickListener;



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView rowName, rowCountry, weatherSummary;
        ImageView rowImage;
        ClickListener clickListener;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView, ClickListener clickListener) {
            super(itemView);
            rowName = itemView.findViewById(R.id.cityName);
            rowCountry = itemView.findViewById(R.id.cityCountry);
            weatherSummary = itemView.findViewById(R.id.weatherSummary);
            rowImage = itemView.findViewById(R.id.cityImage);

            progressBar = itemView.findViewById(R.id.progBar);

            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition());
        }

        public interface ClickListener {
            void onItemClick(int position);
        }
    }

    //CityAdapter contructor
    public CityAdapter(ArrayList<CityItem> cityList, Context context, ViewHolder.ClickListener clickListener){
        this.mCityList = cityList;
        this.context = context;
        this.mClickListener = clickListener;
        cityListFull = new ArrayList<>(mCityList);
    }

    @NonNull
    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate Item layout for recyclerview
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_layout,
                parent,
                false);
        return new CityAdapter.ViewHolder(view, mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CityAdapter.ViewHolder holder, int position) {
        CityItem currentItem = mCityList.get(position);

        holder.rowImage.setImageResource(currentItem.getCityImage());
        holder.rowName.setText(currentItem.getCityName());
        holder.rowCountry.setText(currentItem.getCityCountry());

        holder.weatherSummary.setVisibility(View.GONE);


        String city = currentItem.getCityName().trim();
        String tempUrl  = url + "?q=" +city+ "&appid=" + appID;

        StringRequest stringRequest =  new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("weather");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String desc = object.getString("description");
                            String upperDesc = desc.substring(0,1).toUpperCase() + desc.substring(1).toLowerCase();
                            holder.progressBar.setVisibility(View.GONE);
                            holder.weatherSummary.setText(upperDesc);
                            holder.weatherSummary.setVisibility(View.VISIBLE);
                        }
                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                 Toast.makeText(holder.itemView.getContext(), error.toString().trim(), Toast.LENGTH_LONG).show();
//                 Display useful error to user
                Toast.makeText(holder.itemView.getContext(), "No internet connection", Toast.LENGTH_LONG).show();

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(holder.itemView.getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return mCityList.size();
    }

    @Override
    public Filter getFilter() {
        return cityFilter;
    }

    private Filter cityFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<CityItem> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(cityListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (CityItem item : cityListFull){
                    if (item.getCityName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mCityList.clear();
            mCityList.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };

}
