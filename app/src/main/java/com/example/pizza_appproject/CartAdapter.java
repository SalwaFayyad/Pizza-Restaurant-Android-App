package com.example.pizza_appproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<OrderItem> cartItemList;
    private OnCartClickListener onCartClickListener;

    public CartAdapter(List<OrderItem> cartItemList, OnCartClickListener onCartClickListener) {
        this.cartItemList = cartItemList;
        this.onCartClickListener = onCartClickListener;
    }

    public interface OnCartClickListener {
        void onCartClick(OrderItem cart);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        OrderItem cartItem = cartItemList.get(position);
        holder.bind(cartItem);
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView image_pizza;
        TextView pizzaName;
        TextView quantity,price,size;
        ImageButton deleteButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            image_pizza = itemView.findViewById(R.id.image_pizza);
            pizzaName = itemView.findViewById(R.id.text_pizza_name);
            quantity = itemView.findViewById(R.id.quantity_id);
            deleteButton = itemView.findViewById(R.id.deletecart_id);
            price= itemView.findViewById(R.id.price_id);
            size=itemView.findViewById(R.id.size_id);


            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onCartClickListener != null) {
                    onCartClickListener.onCartClick(cartItemList.get(position));
                }
            });
        }

        public void bind(OrderItem cartItem) {
            // Assuming OrderItem has getName, getQuantity and getImageResource methods
            pizzaName.setText(cartItem.getName());
            quantity.setText(String.valueOf(cartItem.getQuantity()));
            switch(cartItem.getSize().trim()){
                case "Small":
                    size.setText("S");
                    break;
                case "Medium":
                    size.setText("M");
                    break;
                case "Large":
                    size.setText("L");
                    break;
            }

            price.setText(String.valueOf(cartItem.getPrice()*cartItem.getQuantity()));

            switch (cartItem.getName().trim()) {
                case "Margarita":
                    image_pizza.setImageResource(R.drawable.margarita);
                    break;
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
                    image_pizza.setImageResource(R.drawable.offer);
                    size.setText("Size");
                    break;
            }

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    cartItemList.remove(position);
                    notifyItemRemoved(position);
                }
            });
        }
    }
}
