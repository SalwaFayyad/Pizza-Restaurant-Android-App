package com.example.pizza_appproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private OnOrderClickListener onOrderClickListener;

    public OrdersAdapter(List<Order> orderList, OnOrderClickListener onOrderClickListener) {
        this.orderList = orderList;
        this.onOrderClickListener = onOrderClickListener;
    }

    // Define an interface for handling order clicks
    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView orderIdTextView;
        private TextView orderDateTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.text_order_id_c);
            orderDateTextView = itemView.findViewById(R.id.text_order_date_c);

            // Set click listener for the item
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onOrderClickListener != null) {
                    onOrderClickListener.onOrderClick(orderList.get(position));
                }
            });
        }

        public void bind(Order order) {
            orderIdTextView.setText("Order ID: "+ order.getOrderID());
            orderDateTextView.setText("Date: "+ order.getOrderDate());
        }
    }
}
