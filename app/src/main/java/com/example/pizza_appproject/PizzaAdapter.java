package com.example.pizza_appproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizza_appproject.ui.pizzas_fragments.pizzaTypeFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.PizzaViewHolder> {
    private List<Pizza> pizzaList;
    private List<Pizza> favoritePizzas;
    private List<Pizza> displayList;
    private Context context;
    private LayoutInflater inflater;
    private Set<String> favoritePizzaNames; // --> Set to store favorite pizza names
    private DataBaseHelper dbHelper;
    private String userEmail; // --> Current user email.
    private boolean showFavoritesOnly; // --> Flag to indicate whether to show only favorite pizzas


    public interface OnPizzaSizeSelectedListener {
        void onPizzaSizeSelected(Pizza pizza, String size);
    }

    private OnPizzaSizeSelectedListener listener;

    public void setOnPizzaSizeSelectedListener(OnPizzaSizeSelectedListener listener) {
        this.listener = listener;
    }

    public PizzaAdapter(List<Pizza> pizzaList, Context context, List<Pizza> favoritePizzas, String userEmail, boolean showFavoritesOnly) {
        this.pizzaList = pizzaList;
        this.favoritePizzas= favoritePizzas;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.favoritePizzaNames = new HashSet<>();
        for (Pizza pizza : favoritePizzas) {
            this.favoritePizzaNames.add(pizza.getName());
        }
        this.dbHelper = new DataBaseHelper(context, "UserDB", null, 1);
        this.userEmail = userEmail;
        this.showFavoritesOnly = showFavoritesOnly;
        this.displayList = new ArrayList<>(pizzaList);
        initializePizzaFavorites(); // Initialize pizza favorite status
        if (showFavoritesOnly) {
            filterFavorites();
        }
    }
    @NonNull
    @Override
    public PizzaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.pizza_item, parent, false);
        return new PizzaViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PizzaViewHolder holder, int position) {
        Pizza pizza = displayList.get(position);
        int favoriteColor = favoritePizzaNames.contains(pizza.getName()) ? R.color.favorite_color : R.color.not_favorite_color;
        holder.imgFav.setColorFilter(ContextCompat.getColor(context, favoriteColor));
        holder.pizzaName.setText(pizza.getName());
        // Set the appropriate image for the pizza
        if (holder.pizzaName.getText().equals("Neapolitan")) {
            holder.pizzaImage.setImageResource(R.drawable.neapolitan);
        } else if (holder.pizzaName.getText().equals("Hawaiian")) {
            holder.pizzaImage.setImageResource(R.drawable.hawaiian);
        }
        else if (holder.pizzaName.getText().equals("Pepperoni")) {
            holder.pizzaImage.setImageResource(R.drawable.peproni);
        }
        else if (holder.pizzaName.getText().equals("New York Style")) {
            holder.pizzaImage.setImageResource(R.drawable.pesto1);
        }
        else if (holder.pizzaName.getText().equals("Calzone")) {
            holder.pizzaImage.setImageResource(R.drawable.calazone);
        }else if (holder.pizzaName.getText().equals("Tandoori Chicken Pizza")) {
            holder.pizzaImage.setImageResource(R.drawable.tandoori);
        }
        else if (holder.pizzaName.getText().equals("BBQ Chicken Pizza")) {
            holder.pizzaImage.setImageResource(R.drawable.bbq);
        }
        else if (holder.pizzaName.getText().equals("Seafood Pizza")) {
            holder.pizzaImage.setImageResource(R.drawable.seefood);
        }
        else if (holder.pizzaName.getText().equals("Vegetarian Pizza")) {
            holder.pizzaImage.setImageResource(R.drawable.vegeterian);
        }else if (holder.pizzaName.getText().equals("Buffalo Chicken Pizza")) {
            holder.pizzaImage.setImageResource(R.drawable.buffalo);
        }
        else if (holder.pizzaName.getText().equals("Mushroom Truffle Pizza")) {
            holder.pizzaImage.setImageResource(R.drawable.mushroom);
        }
        else if (holder.pizzaName.getText().equals("Pesto Chicken Pizza")) {
            holder.pizzaImage.setImageResource(R.drawable.pestochicken);
        }
        else
            holder.pizzaImage.setImageResource(R.drawable.margarita);


        holder.radioSmall.setChecked("Small".equals(pizza.getSize()));
        holder.radioMedium.setChecked("Medium".equals(pizza.getSize()));
        holder.radioLarge.setChecked("Large".equals(pizza.getSize()));

        // Set the price based on the selected size
        if ("Small".equals(pizza.getSize())) {
            holder.pizzaPrice.setText(String.format("$%.2f", pizza.getSmallPrice()));
        } else if ("Medium".equals(pizza.getSize())) {
            holder.pizzaPrice.setText(String.format("$%.2f", pizza.getMediumPrice()));
        } else if ("Large".equals(pizza.getSize())) {
            holder.pizzaPrice.setText(String.format("$%.2f", pizza.getLargePrice()));
        } else {
            holder.pizzaPrice.setText("Select Size");
        }

        holder.radioSmall.setOnClickListener(v -> {
            pizza.setSize("Small");
            holder.radioSmall.setChecked(true);
            holder.radioMedium.setChecked(false);
            holder.radioLarge.setChecked(false);
            holder.pizzaPrice.setText(String.format("$%.2f", pizza.getSmallPrice()));
        });

        holder.radioMedium.setOnClickListener(v -> {
            pizza.setSize("Medium");
            holder.radioSmall.setChecked(false);
            holder.radioMedium.setChecked(true);
            holder.radioLarge.setChecked(false);
            holder.pizzaPrice.setText(String.format("$%.2f", pizza.getMediumPrice()));
        });

        holder.radioLarge.setOnClickListener(v -> {
            pizza.setSize("Large");
            holder.radioSmall.setChecked(false);
            holder.radioMedium.setChecked(false);
            holder.radioLarge.setChecked(true);
            holder.pizzaPrice.setText(String.format("$%.2f", pizza.getLargePrice()));
        });

        holder.imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the favorite status
                if (favoritePizzaNames.contains(pizza.getName())) {
                    favoritePizzaNames.remove(pizza.getName());
                    pizza.setFavorite(false); // Update favorite status in pizza object
                    dbHelper.deleteFavorite(userEmail, pizza.getName()); // Remove from favorites
                } else {
                    favoritePizzaNames.add(pizza.getName());
                    pizza.setFavorite(true); // Update favorite status in pizza object
                    dbHelper.insertFavorite(userEmail, pizza.getName()); // Insert into favorites
                }
                // Change the color of the favorite button
                int newColor = favoritePizzaNames.contains(pizza.getName()) ? R.color.favorite_color : R.color.not_favorite_color;
                holder.imgFav.setColorFilter(ContextCompat.getColor(context, newColor));
                if (showFavoritesOnly) {
                    filterFavorites();
                }
                updateFavoritePizzas();
            }
        });

        holder.pizzaName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pizza selectedPizza = displayList.get(holder.getAdapterPosition());
                if (selectedPizza != null) {
                    pizzaTypeFragment fragment = pizzaTypeFragment.newInstance(selectedPizza);
                    FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_containerp, fragment);
                    transaction.addToBackStack(null).commit();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return displayList.size();
    }

    private void filterFavorites() {
        displayList.clear();
        for (Pizza pizza : pizzaList) {
            if (favoritePizzaNames.contains(pizza.getName())) {
                displayList.add(pizza);
            }
        }
        notifyDataSetChanged();
    }

    private void initializePizzaFavorites() {
        for (Pizza pizza : pizzaList) {
            pizza.setFavorite(favoritePizzaNames.contains(pizza.getName()));
        }
    }

    private void updateFavoritePizzas() {
        favoritePizzas.clear();
        for (Pizza pizza : pizzaList) {
            if (favoritePizzaNames.contains(pizza.getName())) {
                favoritePizzas.add(pizza);
            }
        }
    }

    public static class PizzaViewHolder extends RecyclerView.ViewHolder {
        ImageView pizzaImage;
        TextView pizzaName,text_price;
        RadioButton radioSmall, radioMedium, radioLarge;
        TextView pizzaPrice;
        ImageButton imgFav;

        public PizzaViewHolder(@NonNull View itemView) {
            super(itemView);
            pizzaImage = itemView.findViewById(R.id.image_pizza);
            pizzaName = itemView.findViewById(R.id.text_pizza_name);
            radioSmall = itemView.findViewById(R.id.radio_bt_small);
            radioMedium = itemView.findViewById(R.id.radio_bt_medium);
            radioLarge = itemView.findViewById(R.id.radio_bt_large);
            pizzaPrice = itemView.findViewById(R.id.pizza_price);
            imgFav = itemView.findViewById(R.id.imgFav);
            text_price=itemView.findViewById(R.id.text_price);
        }

    }
    public void setPizzaList(List<Pizza> filteredPizzaList) {
        this.displayList = filteredPizzaList;
        notifyDataSetChanged();
    }

    public List<Pizza> getPizzaList() {
        return pizzaList;
    }


}
