package com.example.pizza_appproject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class PizzaJsonParser {
    public static List<Pizza> getObjectFromJson(String json) {
        List<Pizza> pizzas = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("types");
            for (int i = 0; i < jsonArray.length(); i++) {
                String pizzaName = jsonArray.getString(i);
                // Create a Pizza object with default size and prices
                Pizza pizza = new Pizza();
                pizza.setName(pizzaName);
                // Set category and prices based on pizza name
                switch (pizzaName) {
                    case "Margarita":
                        pizza.setCategory("Traditional");
                        pizza.setSmallPrice(5.0);
                        pizza.setMediumPrice(7.5);
                        pizza.setLargePrice(10.0);
                        pizza.setDiscription("A classic delight with 100% real mozzarella cheese, juicy tomatoes, and fresh basil leaves."
                        );
                        break;
                    case "Neapolitan":
                        pizza.setCategory("Traditional");
                        pizza.setSmallPrice(6.0);
                        pizza.setMediumPrice(8.0);
                        pizza.setLargePrice(11.0);
                        pizza.setDiscription("Traditional pizza with San Marzano tomatoes, fresh mozzarella cheese, and fresh basil."
                        );
                        break;
                    case "Hawaiian":
                        pizza.setCategory("Meat");
                        pizza.setSmallPrice(7.0);
                        pizza.setMediumPrice(9.5);
                        pizza.setLargePrice(12.0);
                        pizza.setDiscription("Hawaiian pizza is a pizza topped with tomato sauce, mozzarella cheese, ham, and pineapple, known for its sweet and savory flavor combination. It remains a divisive but widely enjoyed pizza variety worldwide.");
                        break;
                    case "Pepperoni":
                        pizza.setCategory("Beef");
                        pizza.setSmallPrice(6.5);
                        pizza.setMediumPrice(8.5);
                        pizza.setLargePrice(11.5);
                        pizza.setDiscription("Classic pepperoni pizza with a generous layer of spicy pepperoni slices and mozzarella cheese."
                        );
                        break;
                    case "New York Style":
                        pizza.setCategory("Regional");
                        pizza.setSmallPrice(7.0);
                        pizza.setMediumPrice(9.5);
                        pizza.setLargePrice(12.0);
                        pizza.setDiscription("Thin and crispy crust topped with tomato sauce, mozzarella cheese, and classic toppings.");
                        break;
                    case "Calzone":
                        pizza.setCategory("Chicken");
                        pizza.setSmallPrice(8.0);
                        pizza.setMediumPrice(10.5);
                        pizza.setLargePrice(13.0);
                        pizza.setDiscription("Folded pizza filled with mozzarella cheese, ricotta cheese, and various toppings.");
                        break;
                    case "Tandoori Chicken Pizza":
                        pizza.setCategory("Specialty");
                        pizza.setSmallPrice(7.5);
                        pizza.setMediumPrice(10.0);
                        pizza.setLargePrice(12.5);
                        pizza.setDiscription("Spicy tandoori chicken pieces with onions, peppers, and mozzarella cheese."
                        );
                        break;
                    case "BBQ Chicken Pizza":
                        pizza.setCategory("Chicken");
                        pizza.setSmallPrice(8.5);
                        pizza.setMediumPrice(10.0);
                        pizza.setLargePrice(12.5);
                        pizza.setDiscription("Grilled chicken, BBQ sauce, onions, and cilantro on a mozzarella cheese base."
                );
                        break;
                    case "Seafood Pizza":
                        pizza.setCategory("Seafood");
                        pizza.setSmallPrice(8.0);
                        pizza.setMediumPrice(10.5);
                        pizza.setLargePrice(13.0);
                        pizza.setDiscription("A medley of seafood including shrimp, calamari, and mussels with mozzarella cheese.");
                        break;
                    case "Vegetarian Pizza":
                        pizza.setCategory("Vegetarian");
                        pizza.setSmallPrice(6.0);
                        pizza.setMediumPrice(8.5);
                        pizza.setLargePrice(11.0);
                        pizza.setDiscription( "A mix of fresh vegetables including bell peppers, onions, mushrooms, and olives.");
                        break;
                    case "Buffalo Chicken Pizza":
                        pizza.setCategory("Chicken");
                        pizza.setSmallPrice(9.5);
                        pizza.setMediumPrice(10.0);
                        pizza.setLargePrice(12.5);
                        pizza.setDiscription("Spicy buffalo chicken pieces with blue cheese crumbles and mozzarella cheese.");
                        break;
                    case "Mushroom Truffle Pizza":
                        pizza.setCategory("Vegetarian");
                        pizza.setSmallPrice(8.5);
                        pizza.setMediumPrice(11.0);
                        pizza.setLargePrice(13.5);
                        pizza.setDiscription("Gourmet pizza with truffle oil, mushrooms, and mozzarella cheese."
                        );
                        break;
                    case "Pesto Chicken Pizza":
                        pizza.setCategory("Chicken");
                        pizza.setSmallPrice(6.5);
                        pizza.setMediumPrice(10.0);
                        pizza.setLargePrice(12.5);
                        pizza.setDiscription("Chicken pieces with a basil pesto sauce, tomatoes, and mozzarella cheese."
                        );
                        break;
                    default:
                        pizza.setCategory("Unknown");
                        pizza.setSmallPrice(0.0);
                        pizza.setMediumPrice(0.0);
                        pizza.setLargePrice(0.0);
                        break;
                }
                pizzas.add(pizza);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pizzas;
    }
}