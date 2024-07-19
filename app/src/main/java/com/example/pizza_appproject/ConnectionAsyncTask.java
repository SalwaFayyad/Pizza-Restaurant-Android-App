package com.example.pizza_appproject;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;
public class ConnectionAsyncTask extends AsyncTask<String, String,
        String> {
    Activity activity;

    public ConnectionAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        ((MainActivity) activity).setButtonText("connecting");
        super.onPreExecute();
        ((MainActivity) activity).setProgress(true);
    }

    @Override
    protected String doInBackground(String... params) {
        String data = HttpManager.getData(params[0]);
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s == null) {
            Toast.makeText(activity, "Failed to fetch pizza data - link", Toast.LENGTH_LONG).show();
            ((MainActivity) activity).setProgress(false);
            ((MainActivity) activity).setButtonText("failed");
        } else {
            List<Pizza> pizzas = PizzaJsonParser.getObjectFromJson(s);
            if (pizzas != null) {
                DataBaseHelper dbHelper = new DataBaseHelper(activity, "UserDB", null, 1);

                User user=new User("adminSalwa@gmail.com","Salwa","Fayyad","0593200783","female","100430S",User.hashPassword("1200430S"),"Admin",null);
                boolean check=dbHelper.insertUser(user);

                for (Pizza pizza : pizzas) {
                    Cursor cursor = dbHelper.getPizzaByName(pizza.getName());
                    if (cursor != null && cursor.getCount() > 0) {
                        // If pizza exists, close the cursor and continue to the next pizza
                        cursor.close();
                        continue;
                    }
                    cursor.close();
                    // Insert the pizza into the database
                    boolean isInserted = dbHelper.insertPizza(pizza);
                    if (!isInserted) {
                        Toast.makeText(activity, "Failed to insert pizza into database", Toast.LENGTH_SHORT).show();
                    }
                }
                dbHelper.close();

                // Add pizzas to the list in MainActivity
                MainActivity.allPizza.addAll(pizzas);

                ((MainActivity) activity).setProgress(false);
                ((MainActivity) activity).setButtonText("connected");
                ((MainActivity) activity).Second_home();
            } else {
                Toast.makeText(activity, "Failed to parse pizza data - data", Toast.LENGTH_LONG).show();
                ((MainActivity) activity).setProgress(false);
                ((MainActivity) activity).setButtonText("failed");
            }
        }
    }
}