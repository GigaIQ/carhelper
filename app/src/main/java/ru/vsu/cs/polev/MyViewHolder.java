package ru.vsu.cs.polev;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView literView, distanceView, priceView, allCoastView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        literView = itemView.findViewById(R.id.liter);
        distanceView = itemView.findViewById(R.id.distance);
        priceView = itemView.findViewById(R.id.cost);
        allCoastView = itemView.findViewById(R.id.allCost);

    }
}
