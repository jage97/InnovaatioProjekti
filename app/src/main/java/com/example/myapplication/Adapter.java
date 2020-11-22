package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static java.lang.Double.parseDouble;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable {
    LayoutInflater inflater;
    List<String> titles, addresses, cities, rating;
    List<String> sports;
    List<LatLng> coordinates;
    String location;
    LocationManager locationManager;
    int[] images;
    private OnItemClickListener mListener;
    private List<String> titlesF, addressesF, citiesF, ratingF, sportsF;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    public Filter getCropFilter(){
        return cropFilter;
    }

    private Filter cropFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<String> filteredList = new ArrayList<>();
            String filterPattern = constraint.toString().toLowerCase().trim();
            Log.d(TAG, filterPattern);
            if (titles.size() != titlesF.size()){
                titles.clear();
                titles.addAll(titlesF);
            }

            if (filterPattern == "icesports") {
                for (int i = 0; i < sports.size(); i++) {
                    if (sports.get(i).toLowerCase().trim().contains("jäälajit")) {
                        String s =titles.get(i)+"  " + sports.get(i) + "  " + addresses.get(i) + "  " + i;
                        Log.d(TAG, s);
                        filteredList.add(titles.get(i));
                    }
                }

            }else if(filterPattern == "all"){
                filteredList.addAll(titlesF);

            }else if (filterPattern == "skiing"){
                for (int i = 0; i < sports.size(); i++) {
                    if (sports.get(i).toLowerCase().trim().contains("hiihto")) {
                        String s =titles.get(i)+"  " + sports.get(i) + "  " + addresses.get(i) + "  " + i;
                        Log.d(TAG, s);
                        filteredList.add(titles.get(i));
                    }
                }
            }else if (filterPattern == "sticksports"){
                for (int i = 0; i < sports.size(); i++) {
                    if (sports.get(i).toLowerCase().trim().contains("mailapelit")) {
                        String s =titles.get(i)+"  " + sports.get(i) + "  " + addresses.get(i) + "  " + i;
                        Log.d(TAG, s);
                        filteredList.add(titles.get(i));
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            titles.clear();
            titles.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };




    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<String> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(titlesF);

            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (int i = 0; i < titles.size(); i++) {

                    if (titles.get(i).toLowerCase().startsWith(filterPattern)){
                        filteredList.add(titles.get(i));
                    }
                }

            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            titles.clear();
            titles.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public void changeInfo(String text){
        location = text;
    }

    public Adapter(Context context, List<String> titles, List<String> addresses, List<String> cities, List<String> sports, int[] images, List<String> rating, String location){
        this.inflater = LayoutInflater.from(context);
        this.titles = titles;
        this.addresses = addresses;
        this.cities = cities;
        this.sports = sports;
        this.images = images;
        this.coordinates = coordinates;
        this.rating = rating;
        this.location = location;

        titlesF = new ArrayList<>(titles);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_layout, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = "N/A";
        String city = "N/A";
        title = titles.get(position);

        //Log.d("TAG","60.251028 24.771102" + " + " + coordinates.get(position).latitude +" + "+ coordinates.get(position).longitude);
        //String city = distance(coordinates.get(position).latitude, coordinates.get(position).longitude,60.242233000000006,24.771102);
        city = cities.get(position);
        //holder.sportIcon1.setImageResource(images[0]);
        //holder.sportIcon2.setImageResource(images[1]);
        //holder.sportIcon3.setImageResource(images[2]);
        //holder.sportIcon4.setImageResource(images[3]);
        //holder.sportIcon5.setImageResource(images[4]);
        //holder.sportIcon6.setImageResource(images[5]);
        holder.title.setText(title);
        holder.city.setText(city);
        try {
            if (parseDouble(rating.get(position)) >= 7) {
                holder.ratingcolor.setBackgroundColor(Color.parseColor("#306722"));
            } else if(parseDouble(rating.get(position)) > 4){
                holder.ratingcolor.setBackgroundColor(Color.parseColor("#ffa600"));
            } else {
                holder.ratingcolor.setBackgroundColor(Color.parseColor("#e60404"));
            };

        } catch (NumberFormatException e){

        };
    }


    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, city;
        ImageView sportIcon1, sportIcon2, sportIcon3, sportIcon4, sportIcon5, sportIcon6;
        View ratingcolor;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            sportIcon1 = itemView.findViewById(R.id.icon1);
            sportIcon2 = itemView.findViewById(R.id.icon2);
            sportIcon3 = itemView.findViewById(R.id.icon3);
            sportIcon4 = itemView.findViewById(R.id.icon4);
            sportIcon5 = itemView.findViewById(R.id.icon5);
            sportIcon6 = itemView.findViewById(R.id.icon6);
            ratingcolor = itemView.findViewById(R.id.ratingcolor);
            title = itemView.findViewById(R.id.title);
            city = itemView.findViewById(R.id.city);

            itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(listener != null){
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION){
                                listener.onItemClick(position);
                            }
                        }
                }
            });
        }
    }
    private String distance(double lat1, double long1, double lat2, double long2){
        double longDiff = long1 - long2;

        double distance = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(longDiff));
        distance = Math.acos(distance);
        distance = rad2deg(distance);
        distance = distance * 60 * 1.1515;
        //in kilometers
        distance = distance * 1.609344;
        double roundOff = Math.round(distance * 100.0) / 100.0;
        return roundOff + "KM";
    }

    private double rad2deg(double distance) {
        return (distance * 180.0 / Math.PI);
    }

    private double deg2rad(double lat1) {
        return (lat1*Math.PI/180.0);
    }

}