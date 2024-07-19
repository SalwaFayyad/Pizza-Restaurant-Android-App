package com.example.pizza_appproject;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizza_appproject.ui.pizzas_fragments.pizzaTypeFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShowCart extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    DataBaseHelper dbHelper;
    Button orderButton;
    double price = 0.0;
    private double extrasPrice = 0.0;
    private  static List<String> extras = new ArrayList<>();
    public static List<OrderItem> selectedPizzaItems= new ArrayList<>();


    public ShowCart() {
        // Required empty public constructor
    }

    public static ShowCart newInstance(String param1, String param2) {
        ShowCart fragment = new ShowCart();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dbHelper = new DataBaseHelper(getContext(), "UserDB", null, 1);

        View view = inflater.inflate(R.layout.fragment_show_cart, container, false);
        recyclerView = view.findViewById(R.id.recycler_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderButton = view.findViewById(R.id.button_ordercart);

        selectedPizzaItems=pizzaTypeFragment.selectedPizzaItems;

        cartAdapter = new CartAdapter(pizzaTypeFragment.selectedPizzaItems, new CartAdapter.OnCartClickListener() {
            @Override
            public void onCartClick(OrderItem cart) {
                // Handle cart item click
            }
        });
        recyclerView.setAdapter(cartAdapter);
        orderButton.setOnClickListener(v -> showOrderDialog());

        return view;
    }

    private void showOrderDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.order, null);

        TextView orderDetailsTextView = dialogView.findViewById(R.id.text_order_details);
        CheckBox checkBoxPotatoes = dialogView.findViewById(R.id.checkBox_potatoes);
        CheckBox checkBoxCola = dialogView.findViewById(R.id.checkBox_cola);
        CheckBox checkBoxExtraCheese = dialogView.findViewById(R.id.checkbox_extra_cheese);
        EditText editTextNotes = dialogView.findViewById(R.id.editText_notes);

        // Initialize extrasPrice
        extrasPrice = 0;

        updateOrderDetails(orderDetailsTextView, editTextNotes.getText().toString());

        checkBoxPotatoes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                extrasPrice += 3;
                extras.add("Potatoes");
            } else {
                extrasPrice -= 3;
                extras.remove("Potatoes");
            }
            updateOrderDetails(orderDetailsTextView, editTextNotes.getText().toString());
        });

        checkBoxCola.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                extrasPrice += 1;
                extras.add("Chat Cola");
            } else {
                extrasPrice -= 1;
                extras.remove("Chat Cola");
            }
            updateOrderDetails(orderDetailsTextView, editTextNotes.getText().toString());
        });

        checkBoxExtraCheese.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                extrasPrice += 1;
                extras.add("Extra Cheese");
            } else {
                extrasPrice -= 1;
                extras.remove("Extra Cheese");
            }
            updateOrderDetails(orderDetailsTextView, editTextNotes.getText().toString());
        });

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        Button confirmButton = dialogView.findViewById(R.id.button_confirm);
        confirmButton.setOnClickListener(v -> {
            String notes = editTextNotes.getText().toString();
            double totalPrice = calculateTotalPrice();

            // Concatenate extras list into a single string
            String extrasString = String.join(", ", extras);
            String orderTime = getCurrentTime();
            String customerName= dbHelper.getUserNameByEmail(LogIn_Activity.userEmail);
            Order order = new Order(LogIn_Activity.userEmail, getCurrentDate(), totalPrice, extrasString, notes,orderTime,customerName); // for data base insert.
            // Add selected pizza items to the order
            for (OrderItem item : selectedPizzaItems) {
                order.addOrderItem(item);
            }

            long orderId = dbHelper.insertOrder(order);
            if (orderId != -1) {
                // Insert order items into the database
                for (OrderItem item : selectedPizzaItems) {
                    item.setOrderID(orderId); // Set the orderId for each item
                    dbHelper.insertOrderItem(item);
                }
            }
            selectedPizzaItems.clear();
            extras.clear();
            dialog.dismiss();
        });
    }

    private double calculateTotalPrice() {
        double totalPrice = 0.0;
        for (OrderItem item : selectedPizzaItems) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        totalPrice += extrasPrice;
        return totalPrice;
    }
    private void updateOrderDetails(TextView orderDetailsTextView, String notes) {
        StringBuilder orderDetails = new StringBuilder();

        // Build the order details string
        orderDetails.append("Order Details:\n");
        for (OrderItem item : selectedPizzaItems) {
            orderDetails.append(String.format("- Pizza: %s, Size: %s, Quantity: %d, Price per one: $%.2f\n",
                    item.getName(), item.getSize(), item.getQuantity(), item.getPrice()));
        }

        if (extrasPrice > 0) {
            orderDetails.append(String.format("Extras: $%.2f\n", extrasPrice));
        }

        // Calculate total price
        double totalPrice = calculateTotalPrice();
        orderDetails.append(String.format("\nTotal Price: $%.2f", totalPrice));

        // Include additional notes
        if (!notes.isEmpty()) {
            orderDetails.append(String.format("\nNotes: %s", notes));
        }

        orderDetailsTextView.setText(orderDetails.toString());
    }
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return sdf.format(new Date());
    }
}
