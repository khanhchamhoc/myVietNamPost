package com.example.myvietnampost;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myvietnampost.model.Order;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderViewHolder> {
    private List<Order> orderList;

    public OrderListAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.imageViewOrder.setImageURI(Uri.parse(order.getImageUrl()));
        holder.textViewOrderDetails.setText(order.toString());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewOrder;
        TextView textViewOrderDetails;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewOrder = itemView.findViewById(R.id.imageViewOrder);
            textViewOrderDetails = itemView.findViewById(R.id.textViewOrderDetails);
        }
    }
}

