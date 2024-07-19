package com.example.pizza_appproject.ui.CustomerFragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizza_appproject.DataBaseHelper;
import com.example.pizza_appproject.LogIn_Activity;
import com.example.pizza_appproject.Order;
import com.example.pizza_appproject.R;
import com.example.pizza_appproject.OrdersAdapter;

import java.util.ArrayList;
import java.util.List;
import com.example.pizza_appproject.OrderItem;

public class CustomerOrders extends Fragment {
    private RecyclerView recyclerView;
    private OrdersAdapter ordersAdapter;
    public static List<Order> userOrders = new ArrayList<>();

    public CustomerOrders() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_orders, container, false);
        recyclerView = view.findViewById(R.id.recycler_customer_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext(), "UserDB", null, 1);
        userOrders.clear();
        userOrders.addAll(dataBaseHelper.getUserOrders(LogIn_Activity.userEmail));
        ordersAdapter = new OrdersAdapter(userOrders, order -> {
            // Handle order click
            showOrderDetails(order);
        });
        recyclerView.setAdapter(ordersAdapter);
        return view;
    }

    private void showOrderDetails(Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Order Details");

        // Construct the message with order details
        StringBuilder message = new StringBuilder();
        message.append("Order ID: ").append(order.getOrderID()).append("\n");
        message.append("Date: ").append(order.getOrderDate()).append("\n");
        message.append("Time: ").append(order.getOrderTime()).append("\n");
        message.append("Total Price: $").append(order.getTotalPrice()).append("\n\n");

        // Append details of each order item
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem item : orderItems) {
            message.append("- ").append(item.getName()).append(" (").append(item.getSize()).append(") x")
                    .append(item.getQuantity()).append(" - $").append(item.getPrice()).append("\n");
        }

        // Append extras
        String extras = order.getExtras();
        if (!extras.isEmpty()) {
            message.append("\nExtras: ").append(extras).append("\n");
        }

        // Append notes
        String notes = order.getNotes();
        if (!notes.isEmpty()) {
            message.append("Notes: ").append(notes);
        }
        builder.setMessage(message.toString());

        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
