package com.example.pizza_appproject.ui.CustomerFragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizza_appproject.LogIn_Activity;
import com.example.pizza_appproject.MainActivity;
import com.example.pizza_appproject.Pizza;
import com.example.pizza_appproject.PizzaAdapter;
import com.example.pizza_appproject.R;
import com.example.pizza_appproject.ShowCart;
import com.example.pizza_appproject.ui.pizzas_fragments.pizzaTypeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Pizza_menuFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private PizzaAdapter pizzaAdapter;
    private ImageView imageView;
    private TextView textViewFavouriteAlert;

    private HorizontalScrollView horizontalScrollView;
    private EditText editTextSearch;
    private ProgressBar progressBarPizzaMenu;
    Button buttonAll, buttonTraditional, buttonSpecialty, buttonMeat, buttonRegional, buttonSeafood, buttonVegetarian, buttonChicken, buttonBeef;
    private List<Button> pizzasButton = new ArrayList<>();
    // Initialize the last clicked button
    Button lastClickedButton = null;
    private SearchView searchView;
    private Button lastPressedButton; // To store the reference of the last pressed button
    private int originalButtonColor; // To store the original color of the button
    private static final String TAG = "PizzaActivity";

    private FloatingActionButton fabCart;

    public static Pizza_menuFragment newInstance(String param1, String param2) {
        Pizza_menuFragment fragment = new Pizza_menuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Pizza_menuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_pizza_menu, container, false);
        searchView = root.findViewById(R.id.search_viewid);
        searchView.setQuery("", false);
        searchView.clearFocus();
        initializeLayout(root);

        return root;
    }

    public void initializeLayout(View root) {
        imageView = root.findViewById(R.id.imageView);
        horizontalScrollView = root.findViewById(R.id.horizontalScrollView);

// Initialize buttons with findViewById
        buttonAll = root.findViewById(R.id.button_all);
        buttonTraditional = root.findViewById(R.id.button_Traditional);
        buttonChicken = root.findViewById(R.id.button_Chicken);
        buttonMeat = root.findViewById(R.id.button_Meat);
        buttonBeef = root.findViewById(R.id.button_Beef);
        buttonRegional = root.findViewById(R.id.button_Regional);
        buttonSeafood = root.findViewById(R.id.button_Seafood);
        buttonVegetarian = root.findViewById(R.id.button_Vegetarian);
        buttonSpecialty = root.findViewById(R.id.button_Specialty);
        //-----------------
        recyclerView = root.findViewById(R.id.recycler_pizza_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pizzaAdapter = new PizzaAdapter(MainActivity.allPizza, getActivity(), LogIn_Activity.favoritePizza, LogIn_Activity.userEmail, false);
        recyclerView.setAdapter(pizzaAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }

            boolean foundMatch = false;

            private void filterList(String text) {
                List<Pizza> allPizzas = pizzaAdapter.getPizzaList();
                List<Pizza> filteredPizzaList = new ArrayList<>();
                boolean foundMatch = false;

                try {
                    double enteredPrice = Double.parseDouble(text.trim());

                    for (Pizza pizza : allPizzas) {
                        String pizzaName = pizza.getName().toLowerCase().trim();
                        if (isConsecutiveSubstring(text.toLowerCase().trim(), pizzaName)) {
                            filteredPizzaList.add(pizza);
                            foundMatch = true;
                        }
                        if (enteredPrice >= pizza.getSmallPrice() && !filteredPizzaList.contains(pizza)) {
                            filteredPizzaList.add(pizza);
                            foundMatch = true;
                        }
                    }
                } catch (NumberFormatException e) {
                    Log.e(TAG, "Invalid number entered: " + text, e);
                    // Handle the case where text is not a valid number
                    for (Pizza pizza : allPizzas) {
                        String pizzaName = pizza.getName().toLowerCase().trim();
                        if (isConsecutiveSubstring(text.toLowerCase().trim(), pizzaName)) {
                            filteredPizzaList.add(pizza);
                            foundMatch = true;
                        }
                    }
                }

// Define possible substrings for "small", "medium", and "large"
                String[] smallSubstrings = {"sm", "sma", "smal", "small"};
                String[] mediumSubstrings = {"me", "med", "medi", "mediu", "medium"};
                String[] largeSubstrings = {"la", "lar", "larg", "large"};

// Convert user input to lowercase and trim it
                String userInput = text.toLowerCase().trim();

// Check if user input contains any substring of "small", "medium", or "large"
                boolean sizeMatch = false;

                for (String substring : smallSubstrings) {
                    if (userInput.contains(substring)) {
                        sizeMatch = true;
                        break;
                    }
                }

                if (!sizeMatch) {
                    for (String substring : mediumSubstrings) {
                        if (userInput.contains(substring)) {
                            sizeMatch = true;
                            break;
                        }
                    }
                }

                if (!sizeMatch) {
                    for (String substring : largeSubstrings) {
                        if (userInput.contains(substring)) {
                            sizeMatch = true;
                            break;
                        }
                    }
                }

// If sizeMatch is true, add all pizzas to the filtered list
                if (sizeMatch) {
                    filteredPizzaList.addAll(allPizzas);
                    foundMatch = true;
                }

                if (!foundMatch) {
                    Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                    filteredPizzaList = new ArrayList<>(); // Ensure the list is empty instead of null
                }

                pizzaAdapter.setPizzaList(filteredPizzaList);


// Initialize the list of buttons
                List<Button> pizzasButton = new ArrayList<>();
                pizzasButton.add(buttonAll);
                pizzasButton.add(buttonTraditional);
                pizzasButton.add(buttonChicken);
                pizzasButton.add(buttonMeat);
                pizzasButton.add(buttonBeef);
                pizzasButton.add(buttonRegional);
                pizzasButton.add(buttonSeafood);
                pizzasButton.add(buttonVegetarian);
                pizzasButton.add(buttonSpecialty);


// Set click listeners for each button
                for (Button button : pizzasButton) {
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchView.setQuery("", false);
                            searchView.clearFocus();

                            // Clear the previously filtered pizzas
                            List<Pizza> filteredPizzaList = new ArrayList<>();

                            // Get the category based on the button clicked
                            String category = getCategoryFromButtonId(v.getId());

                            // Filter the pizzas based on the category
                            for (Pizza pizza : allPizzas) {
                                if (pizza.getCategory().equalsIgnoreCase(category) || category.equalsIgnoreCase("All")) {
                                    filteredPizzaList.add(pizza);
                                }
                            }
                            // Update the adapter with the filtered list
                            pizzaAdapter.setPizzaList(filteredPizzaList);
                            pizzaAdapter.notifyDataSetChanged();
                            if (!category.equals("All")) {
                                v.setBackgroundColor(Color.GRAY);
                                if (lastClickedButton != null && lastClickedButton != v) {
                                    lastClickedButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightgrey));
                                }
                                // Update the last clicked button
                                lastClickedButton = (Button) v;
                            }
                            if (category.equals("All")) {
                                if (lastClickedButton != null && lastClickedButton != v) {
                                    lastClickedButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightgrey));
                                }
                                // Update the last clicked button
                                lastClickedButton = (Button) v;
                            }
                        }
                    });
                }
            }
        });
        fabCart = root.findViewById(R.id.cart);
        fabCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use requireActivity() to get the FragmentActivity context
                FragmentActivity fragmentActivity = requireActivity();

                // Create an instance of ShowCart fragment with arguments if necessary
                ShowCart fragment = ShowCart.newInstance("exampleParam1", "exampleParam2");

                // Perform fragment transaction
                FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_containerp, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    // Helper method to check if searchText is a subsequence of pizzaName
    private boolean isConsecutiveSubstring(String searchText, String pizzaName) {
        int searchIndex = 0;
        int pizzaIndex = 0;

        while (pizzaIndex < pizzaName.length() && searchIndex < searchText.length()) {
            if (pizzaName.charAt(pizzaIndex) == searchText.charAt(searchIndex)) {
                searchIndex++;
            } else {
                searchIndex = 0; // Reset the search index if there's a mismatch
            }
            pizzaIndex++;
        }
        return searchIndex == searchText.length();
    }


    private String getCategoryFromButtonId(int buttonId) {
        if (buttonId == R.id.button_all) {
            return "All";
        } else if (buttonId == R.id.button_Traditional) {
            return "Traditional";
        } else if (buttonId == R.id.button_Chicken) {
            return "Chicken";
        } else if (buttonId == R.id.button_Meat) {
            return "Meat";
        } else if (buttonId == R.id.button_Beef) {
            return "Beef";
        } else if (buttonId == R.id.button_Regional) {
            return "Regional";
        } else if (buttonId == R.id.button_Seafood) {
            return "Seafood";
        } else if (buttonId == R.id.button_Vegetarian) {
            return "Vegetarian";
        } else if (buttonId == R.id.button_Specialty) {
            return "Specialty";
        } else {
            return ""; // Handle unknown button IDs
        }
    }
}
