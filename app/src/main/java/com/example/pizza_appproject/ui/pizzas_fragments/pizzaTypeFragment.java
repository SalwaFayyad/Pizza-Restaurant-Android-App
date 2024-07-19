package com.example.pizza_appproject.ui.pizzas_fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.pizza_appproject.DataBaseHelper;
import com.example.pizza_appproject.LogIn_Activity;
import com.example.pizza_appproject.Order;
import com.example.pizza_appproject.OrderItem;
import com.example.pizza_appproject.Pizza;
import com.example.pizza_appproject.R;
import com.example.pizza_appproject.ui.CustomerFragments.Pizza_menuFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class pizzaTypeFragment extends Fragment {

    private static final String ARG_PIZZA = "pizza";
    private Pizza pizza;
    RadioGroup radioGroupSize;
    TextView quantityTextView;
    TextView priceTextView;
    String selectedSize;

    ImageView image_pizza;
    DataBaseHelper dbHelper;
    double price = 0.0;
    public static double extrasPrice = 0.0;
    public static List<OrderItem> selectedPizzaItems= new ArrayList<>();
    public static  List<String> extras = new ArrayList<>();
    public static pizzaTypeFragment newInstance(Pizza pizza) {
        pizzaTypeFragment fragment = new pizzaTypeFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PIZZA, pizza);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pizza = getArguments().getParcelable(ARG_PIZZA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_margherita, container, false);
        dbHelper = new DataBaseHelper(getContext(), "UserDB", null, 1);
        if (pizza != null) {
            TextView pizzaNameTextView = view.findViewById(R.id.text_pizza_name);
            TextView pizzaDescriptionTextView = view.findViewById(R.id.text_pizza_description);
            priceTextView = view.findViewById(R.id.text_price);
            quantityTextView = view.findViewById(R.id.text_quantity);
            image_pizza=view.findViewById(R.id.image_pizza);

            ImageButton incrementButton = view.findViewById(R.id.button_increment);
            ImageButton decrementButton = view.findViewById(R.id.button_decrement);
            radioGroupSize = view.findViewById(R.id.radioGroup_size);
            //-------------------------------------------------------------------------
            pizzaNameTextView.setText(pizza.getName());
            pizzaDescriptionTextView.setText(pizza.getDiscription());

            switch (pizza.getName().trim()) {
                case "Neapolitan":
                    image_pizza.setImageResource(R.drawable.neapolitan);
                    break;
                case "Hawaiian":
                    image_pizza.setImageResource(R.drawable.hawaiian);
                    break;
                case "Pepperoni":
                    image_pizza.setImageResource(R.drawable.peproni);
                    break;
                case "New York Style":
                    image_pizza.setImageResource(R.drawable.pesto1);
                    break;
                case "Calzone":
                    image_pizza.setImageResource(R.drawable.calazone);
                    break;
                case "Tandoori Chicken Pizza":
                    image_pizza.setImageResource(R.drawable.tandoori);
                    break;
                case "BBQ Chicken Pizza":
                    image_pizza.setImageResource(R.drawable.bbq);
                    break;
                case "Seafood Pizza":
                    image_pizza.setImageResource(R.drawable.seefood);
                    break;
                case "Vegetarian Pizza":
                    image_pizza.setImageResource(R.drawable.vegeterian);
                    break;
                case "Buffalo Chicken Pizza":
                    image_pizza.setImageResource(R.drawable.buffalo);
                    break;
                case "Mushroom Truffle Pizza":
                    image_pizza.setImageResource(R.drawable.mushroom);
                    break;
                case "Pesto Chicken Pizza":
                    image_pizza.setImageResource(R.drawable.pestochicken);
                    break;
                    default:
                        image_pizza.setImageResource(R.drawable.margarita);
                        break;
                }

            //-------------------------------------------------------------------------
            AtomicInteger quantity = new AtomicInteger(1);
            quantityTextView.setText(String.valueOf(quantity.get()));
            incrementButton.setOnClickListener(v -> {
                quantity.getAndIncrement();
                quantityTextView.setText(String.valueOf(quantity.get()));
            });
            decrementButton.setOnClickListener(v -> {
                if (quantity.get() > 1) {
                    quantity.getAndDecrement();
                    quantityTextView.setText(String.valueOf(quantity.get()));
                }
            });
        }


        //---------------------------------------------------------------------------------------
        radioGroupSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Get the selected RadioButton
                RadioButton radioButton = view.findViewById(checkedId);

                // Update the price TextView based on the selected size
                if (radioButton != null) {
                    selectedSize = radioButton.getText().toString();
                    switch (selectedSize) {
                        case "Small":
                            price = pizza.getSmallPrice();
                            break;
                        case "Medium":
                            price = pizza.getMediumPrice();
                            break;
                        case "Large":
                            price = pizza.getLargePrice();
                            break;
                    }

                    priceTextView.setText(String.format("Price: $%.2f", price));
                }
            }
        });
        ImageButton imgFav = view.findViewById(R.id.imgFav);
        int favoriteColor = pizza.isFavorite() ? R.color.favorite_color : R.color.not_favorite_color;
        imgFav.setColorFilter(ContextCompat.getColor(requireContext(), favoriteColor));
        imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pizza.setFavorite(!pizza.isFavorite());
                int newColor = pizza.isFavorite() ? R.color.favorite_color : R.color.not_favorite_color;
                imgFav.setColorFilter(ContextCompat.getColor(requireContext(), newColor));
                // Update the database and fav list.
                if (pizza.isFavorite()) {
                    LogIn_Activity.favoritePizza.add(pizza);
                    dbHelper.insertFavorite(LogIn_Activity.userEmail, pizza.getName());
                } else {
                    LogIn_Activity.favoritePizza.remove(pizza);
                    dbHelper.deleteFavorite(LogIn_Activity.userEmail, pizza.getName());
                }
            }
        });
        Button addPizzaButton = view.findViewById(R.id.addPizzaOrder);
        addPizzaButton.setOnClickListener(v -> {
            addPizzaToOrder();
        });
        //---------------------------------------------------------------------------------------------------
        Button orderButton = view.findViewById(R.id.button_order);
        orderButton.setOnClickListener(v -> showOrderDialog());
        return view;
    }

    private void showOrderDialog() {
        if (selectedSize == null || selectedSize.isEmpty()) {
            Toast.makeText(getContext(), "Please select a size.", Toast.LENGTH_SHORT).show();
            return; // Exit the method without showing the dialog
        }
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

    public static double calculateTotalPrice() {
        double totalPrice = 0.0;
        for (OrderItem item : selectedPizzaItems) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        totalPrice += extrasPrice;
        return totalPrice;
    }

    public static void updateOrderDetails(TextView orderDetailsTextView, String notes) {
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
    private void addPizzaToOrder() {
        OrderItem pizzaOrderItem = new OrderItem(pizza.getName(), selectedSize, Integer.parseInt(quantityTextView.getText().toString()), price);
        selectedPizzaItems.add(pizzaOrderItem);
    }
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return sdf.format(new Date());
    }

}