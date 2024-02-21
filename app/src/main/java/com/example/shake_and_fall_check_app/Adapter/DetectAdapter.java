package com.example.shake_and_fall_check_app.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shake_and_fall_check_app.Database.Detect_Data_Model;
import com.example.shake_and_fall_check_app.R;

import java.util.ArrayList;
import java.util.List;

public class DetectAdapter extends RecyclerView.Adapter<DetectAdapter.ViewHolder> {
    private Context context;
    private List<Detect_Data_Model> modelList;
    private List<Detect_Data_Model> filterList;
    private OnItemClickListener itemClickListener;
    public interface OnItemClickListener {
        void onItemClick(Detect_Data_Model item);
    }

    public DetectAdapter(Context context, List<Detect_Data_Model> modelList, OnItemClickListener itemClickListener) {
        this.context = context;
        this.modelList = modelList;
        this.itemClickListener = itemClickListener;
        this.filterList = new ArrayList<>(modelList);
    }
    public void filteredList(String title){
        filterList.clear();
        for (Detect_Data_Model item:modelList){
            if (item.getDetectTitle().equalsIgnoreCase(title)){
                filterList.add(item);
            }
        }
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public DetectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detect_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetectAdapter.ViewHolder holder, int position) {

        Detect_Data_Model model = filterList.get(position);
        holder.title.setText(model.getDetectTitle() + " Detected");
        holder.time.setText("Time: "+model.getTime());
        holder.acceleration.setText("Acceleration: "+String.valueOf(model.getAcceleration()));
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(model);
            }
        });

        Log.e("MyApp","list title "+model.getDetectTitle());

    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title,time,acceleration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.detect_item_title);
            time = itemView.findViewById(R.id.detect_item_time);
            acceleration = itemView.findViewById(R.id.detect_item_acceleration);
        }
    }
}
