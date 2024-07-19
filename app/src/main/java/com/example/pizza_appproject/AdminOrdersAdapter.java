package com.example.pizza_appproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminOrdersAdapter extends RecyclerView.Adapter<AdminOrdersAdapter.OrderViewHolder> {

    private List<Order> orderList;

    public AdminOrdersAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_admin, parent, false);
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

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView orderIdTextView;
        private TextView customerNameTextView;
        private TextView orderDetailsTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.text_order_id);
            customerNameTextView = itemView.findViewById(R.id.text_customer_name);
            orderDetailsTextView = itemView.findViewById(R.id.text_order_details);
        }

        public void bind(Order order) {
            if (orderIdTextView != null && customerNameTextView != null && orderDetailsTextView != null) {
                orderIdTextView.setText("Order ID: " + order.getOrderID());
                customerNameTextView.setText("Customer Name: " + order.getCustomerName());
                // Construct order details string
                StringBuilder orderDetails = new StringBuilder();
                orderDetails.append("Order Details:\n");
                // Append other order information as needed
                orderDetails.append("Total Price: $" + order.getTotalPrice());
                orderDetails.append("\nExtras: " + order.getExtras());
                orderDetails.append("\nNotes: " + order.getNotes());
                // Append order items
                List<OrderItem> orderItems = order.getOrderItems();
                for (OrderItem item : orderItems) {
                    orderDetails.append("\n- ").append(item.getName())
                            .append(" (").append(item.getSize()).append(") x")
                            .append(item.getQuantity()).append(" - $")
                            .append(item.getPrice());
                }

                orderDetailsTextView.setText(orderDetails.toString());
            } else {
                // Log a message or handle the case where TextViews are null
            }
        }

    }
}
