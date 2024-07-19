package com.example.pizza_appproject;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizza_appproject.ui.pizzas_fragments.pizzaTypeFragment;

import java.util.List;

public class SpecialOfferAdapter extends RecyclerView.Adapter<SpecialOfferAdapter.SpecialOfferViewHolder> {

    private List<SpecialOffer> specialOfferList;
    DataBaseHelper dbHelper;
    Context context;
    public SpecialOfferAdapter(Context context, List<SpecialOffer> specialOfferList) {
        this.context = context;
        this.specialOfferList = specialOfferList;
    }
    private void showOrderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.order, null);
        TextView orderDetailsTextView = dialogView.findViewById(R.id.text_order_details);
        CheckBox checkBoxPotatoes = dialogView.findViewById(R.id.checkBox_potatoes);
        CheckBox checkBoxCola = dialogView.findViewById(R.id.checkBox_cola);
        CheckBox checkBoxExtraCheese = dialogView.findViewById(R.id.checkbox_extra_cheese);
        EditText editTextNotes = dialogView.findViewById(R.id.editText_notes);

        pizzaTypeFragment.extrasPrice = 0;
        pizzaTypeFragment.updateOrderDetails(orderDetailsTextView, editTextNotes.getText().toString());

        checkBoxPotatoes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                pizzaTypeFragment.extrasPrice += 3;
                pizzaTypeFragment.extras.add("Potatoes");
            } else {
                pizzaTypeFragment.extrasPrice -= 3;
                pizzaTypeFragment.extras.remove("Potatoes");
            }
            pizzaTypeFragment.updateOrderDetails(orderDetailsTextView, editTextNotes.getText().toString());
        });

        checkBoxCola.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                pizzaTypeFragment.extrasPrice += 1;
                pizzaTypeFragment.extras.add("Chat Cola");
            } else {
                pizzaTypeFragment.extrasPrice -= 1;
                pizzaTypeFragment.extras.remove("Chat Cola");
            }
            pizzaTypeFragment.updateOrderDetails(orderDetailsTextView, editTextNotes.getText().toString());
        });

        checkBoxExtraCheese.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                pizzaTypeFragment.extrasPrice += 1;
                pizzaTypeFragment.extras.add("Extra Cheese");
            } else {
                pizzaTypeFragment.extrasPrice -= 1;
                pizzaTypeFragment.extras.remove("Extra Cheese");
            }
            pizzaTypeFragment.updateOrderDetails(orderDetailsTextView, editTextNotes.getText().toString());
        });

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        Button confirmButton = dialogView.findViewById(R.id.button_confirm);
        confirmButton.setOnClickListener(v -> {
            String notes = editTextNotes.getText().toString();
            double totalPrice = pizzaTypeFragment.calculateTotalPrice();

            // Concatenate extras list into a single string
            String extrasString = String.join(", ", pizzaTypeFragment.extras);
            String orderTime = pizzaTypeFragment.getCurrentTime();
            String customerName= dbHelper.getUserNameByEmail(LogIn_Activity.userEmail);
            Order order = new Order(LogIn_Activity.userEmail, pizzaTypeFragment.getCurrentDate(), totalPrice, extrasString, notes,orderTime,customerName); // for data base insert.
            // Add selected pizza items to the order
            for (OrderItem item : pizzaTypeFragment.selectedPizzaItems) {
                order.addOrderItem(item);
            }

            long orderId = dbHelper.insertOrder(order);
            if (orderId != -1) {
                // Insert order items into the database
                for (OrderItem item : pizzaTypeFragment.selectedPizzaItems) {
                    item.setOrderID(orderId); // Set the orderId for each item
                    dbHelper.insertOrderItem(item);
                }
            }
            pizzaTypeFragment.selectedPizzaItems.clear();
            pizzaTypeFragment.extras.clear();
            dialog.dismiss();
        });
    }
    @NonNull
    @Override
    public SpecialOfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pizza_item_offer, parent, false);
        dbHelper = new DataBaseHelper(view.getContext(),"UserDB", null, 1);
        return new SpecialOfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialOfferViewHolder holder, int position) {
        SpecialOffer specialOffer = specialOfferList.get(position);
        holder.offerNameTextView.setText(specialOffer.getName());
        // Display pizza items and quantities
        StringBuilder pizzaItemsText = new StringBuilder();
        for (SpecialOfferItem pizzaItem : specialOffer.getSpecialOfferItems()) {
            pizzaItemsText.append(pizzaItem.getPizzaName()).append(" (").append(pizzaItem.getQuantity()).append(")\n");
        }
        Log.d("PizzaItems", pizzaItemsText.toString());

        holder.pizzaItemsTextView.setText(pizzaItemsText.toString());
        holder.extrasTextView.setText(specialOffer.getExtras());
        holder.totalPriceTextView.setText(String.valueOf(specialOffer.getTotalPrice()));
        holder.endDateTextView.setText(specialOffer.getEndDate());

        holder.orderButton.setOnClickListener(v -> {
            showOrderDialog();
            Toast.makeText(v.getContext(), "Order " + specialOffer.getName(), Toast.LENGTH_SHORT).show();
        });
        holder.addToCart.setOnClickListener(v -> {
            OrderItem specialOfferItem = new OrderItem(specialOffer.getName(), "Special Offer Size", 1, specialOffer.getTotalPrice());
            pizzaTypeFragment.selectedPizzaItems.add(specialOfferItem);
            Toast.makeText(v.getContext(), "Add to cart " + specialOffer.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return specialOfferList.size();
    }

    public static class SpecialOfferViewHolder extends RecyclerView.ViewHolder {
        TextView offerNameTextView, pizzaItemsTextView, extrasTextView, totalPriceTextView, endDateTextView;
        Button orderButton;
        ImageButton addToCart;

        public SpecialOfferViewHolder(@NonNull View itemView) {
            super(itemView);
            offerNameTextView = itemView.findViewById(R.id.textView_name);
            pizzaItemsTextView = itemView.findViewById(R.id.textView_pizza_items); // Adjusted based on your item layout
            extrasTextView = itemView.findViewById(R.id.textView_extras); // Adjusted based on your item layout
            totalPriceTextView = itemView.findViewById(R.id.text_newPrice); // Adjusted based on your item layout
            endDateTextView = itemView.findViewById(R.id.textView_endDate); // Adjusted based on your item layout
            orderButton = itemView.findViewById(R.id.button_order); // Adjusted based on your item layout
            addToCart =itemView.findViewById(R.id.button_add_to_cart);
        }

    }
}
