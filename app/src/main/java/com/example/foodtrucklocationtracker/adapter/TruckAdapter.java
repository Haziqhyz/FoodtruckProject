package com.example.foodtrucklocationtracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtrucklocationtracker.R;
import com.example.foodtrucklocationtracker.model.FoodTruck;

import java.util.List;

public class TruckAdapter extends RecyclerView.Adapter<TruckAdapter.ViewHolder> {

    private List<FoodTruck> truckList;

    public TruckAdapter(List<FoodTruck> truckList) {
        this.truckList = truckList;
    }

    @NonNull
    @Override
    public TruckAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_truck, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TruckAdapter.ViewHolder holder, int position) {
        FoodTruck truck = truckList.get(position);
        holder.txtType.setText(truck.getType());
        holder.txtReporter.setText("Reported by: " + truck.getReportedBy());
        holder.txtTime.setText("Last Reported: " + truck.getLastReported());
    }

    @Override
    public int getItemCount() {
        return truckList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtType, txtReporter, txtTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtType = itemView.findViewById(R.id.txtType);
            txtReporter = itemView.findViewById(R.id.txtReporter);
            txtTime = itemView.findViewById(R.id.txtTime);
        }
    }
}
