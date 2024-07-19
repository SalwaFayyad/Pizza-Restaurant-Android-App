package com.example.pizza_appproject.ui.AdminFragments;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pizza_appproject.AdminOrdersAdapter;
import com.example.pizza_appproject.DataBaseHelper;
import com.example.pizza_appproject.Order;
import com.example.pizza_appproject.OrderItem;
import com.example.pizza_appproject.R;
import java.util.ArrayList;
import java.util.List;

public class AdminViewOrders extends Fragment {

    private RecyclerView recyclerView;
    private AdminOrdersAdapter ordersAdapter;
    private List<Order> allOrders = new ArrayList<>();
    private Button calculateStatisticsButton;

    private String[] pizzasNames = {
            "Margarita",
            "Neapolitan",
            "Hawaiian",
            "Pepperoni",
            "New York Style",
            "Calzone",
            "Tandoori Chicken Pizza",
            "BBQ Chicken Pizza",
            "Seafood Pizza",
            "Vegetarian Pizza",
            "Buffalo Chicken Pizza",
            "Mushroom Truffle Pizza",
            "Pesto Chicken Pizza"
    };

    public AdminViewOrders() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_view_orders, container, false);

        recyclerView = view.findViewById(R.id.recycler_pizza_menu);
        calculateStatisticsButton = view.findViewById(R.id.calculate_statistics_button);
        calculateStatisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalculateStatisticsDialog();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allOrders.clear();
        allOrders.addAll(getAllOrders());
        ordersAdapter = new AdminOrdersAdapter(allOrders);
        recyclerView.setAdapter(ordersAdapter);

        return view;
    }

    private List<Order> getAllOrders() {
        DataBaseHelper dbHelper = new DataBaseHelper(getContext(), "UserDB", null, 1);
        return dbHelper.getAllOrders();
    }

    private void showCalculateStatisticsDialog() {
        // Inflate the dialog view
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_calculate_statistics, null);

        // Initialize views
        Spinner spinnerPizzaType = dialogView.findViewById(R.id.spinner_pizza_type);
        TextView textViewOrdersCount = dialogView.findViewById(R.id.text_view_orders_count);
        TextView textViewTotalIncome = dialogView.findViewById(R.id.text_view_total_income);
        TextView textViewTotalIncomeAllTypes = dialogView.findViewById(R.id.text_view_total_income_all_types);

        // Setup Spinner adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, pizzasNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPizzaType.setAdapter(adapter);

        // Calculate statistics when item is selected in Spinner
        spinnerPizzaType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPizzaType = (String) parent.getItemAtPosition(position);

                // Example: Perform calculations based on selectedPizzaType
                int ordersCount = calculateOrdersCount(selectedPizzaType);
                double totalIncome = calculateTotalIncome(selectedPizzaType);
                double totalIncomeAllTypes = calculateTotalIncomeAllTypes();

                // Update TextViews with calculated values
                textViewOrdersCount.setText("Number of Orders: " + ordersCount);
                textViewTotalIncome.setText("Total Income: $" + totalIncome);
                textViewTotalIncomeAllTypes.setText("Total Income (All Types): $" + totalIncomeAllTypes);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected
            }
        });

        // Build and show the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);
        builder.setTitle("Calculate Statistics");
        builder.setPositiveButton("OK", null); // You can add an OnClickListener here if needed
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Method to calculate number of orders for a specific pizza type
    private int calculateOrdersCount(String pizzaType) {
        int count = 0;
        for (Order order : allOrders) {
            for (OrderItem item : order.getOrderItems()) {
                if (item.getName().equals(pizzaType)) {
                    count += item.getQuantity();
                }
            }
        }
        return count;
    }

    // Method to calculate total income for a specific pizza type
    private double calculateTotalIncome(String pizzaType) {
        double totalIncome = 0.0;
        for (Order order : allOrders) {
            for (OrderItem item : order.getOrderItems()) {
                if (item.getName().equals(pizzaType)) {
                    totalIncome += item.getPrice() * item.getQuantity();
                }
            }
        }
        return totalIncome;
    }

    // Method to calculate total income for all pizza types together
    private double calculateTotalIncomeAllTypes() {
        double totalIncomeAllTypes = 0.0;
        for (Order order : allOrders) {
            for (OrderItem item : order.getOrderItems()) {
                totalIncomeAllTypes += item.getPrice() * item.getQuantity();
            }
        }
        return totalIncomeAllTypes;
    }
}
