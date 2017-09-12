package com.mobile_computing;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

/**
 * Refer to http://www.truiton.com/2015/03/android-cardview-example/ for more details
 */

public class DatumAdapter extends RecyclerView.Adapter<DatumAdapter.DataObjectHolder> {
    private static String LOG_TAG = "DATUM ADAPTER";
    private ArrayList<Datum> m_data;
    private ImageLoader imgLoad;
    private static DatumClickListener m_clickListener;
    private static Context m_context;

    // Constructors
    public DatumAdapter(Context con) {
        m_context = con;
        m_data    = new ArrayList<Datum>();

        imgLoad = VolleySingleton.getInstance(con).getImageLoader();
    }

    public DatumAdapter(Context con, ArrayList<Datum> data) {
        m_context = con;
        m_data    = data;

        imgLoad = VolleySingleton.getInstance(con).getImageLoader();
    }

    // Responsible for creating the individual view per Datum
    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView date;
        NetworkImageView img;

        public DataObjectHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            date  = (TextView) itemView.findViewById(R.id.date);
            img   = (NetworkImageView) itemView.findViewById(R.id.img);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            m_clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    // Sets the callback function to handle clicking on individual datum
    public void setOnItemClickListener(DatumClickListener clickListener) {
        this.m_clickListener = clickListener;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        SharedPreferences sp = m_context.getSharedPreferences("Fav", MODE_PRIVATE);
        String list = sp.getString("list", "");

        String[] fArray = list.split(",");
        ArrayList<String> fList = new ArrayList<String>(Arrays.asList(fArray));

        if(fList.contains(m_data.get(position).id()+""))
        {

            holder.title.setTextColor(Color.rgb(179,132,5));
        }


        holder.title.setText(m_data.get(position).title());//dba102
        holder.date.setText(m_data.get(position).date());
        holder.img.setImageUrl(m_data.get(position).imageUrl(), imgLoad);
    }

    public void addItem(Datum datum, int index) {
        m_data.add(index, datum);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        m_data.remove(index);
        notifyItemRemoved(index);
    }

    public void clear() {
        m_data.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return m_data.size();
    }

    // Get a specific datum
    public Datum getItem(int position) {
        if (position > -1 && position < m_data.size())
            return m_data.get(position);
        else
            return null;
    }

    public interface DatumClickListener {
        public void onItemClick(int position, View v);
    }
}