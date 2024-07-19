package com.example.pizza_appproject;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends android.database.sqlite.SQLiteOpenHelper{
    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE User(email  TEXT PRIMARY KEY, firstName TEXT,lastNAME TEXT " +
                ",phoneNumber TEXT,gender TEXT, password TEXT, hashedPassword TEXT,permission TEXT,ProfilePicture BLOB)");
        sqLiteDatabase.execSQL("CREATE TABLE Pizza(name TEXT PRIMARY KEY, " +
                "category TEXT, size TEXT, smallPrice DOUBLE ,mediumPrice DOUBLE,largePrice DOUBLE, discription TEXT )");
        sqLiteDatabase.execSQL("DELETE FROM Pizza");
        sqLiteDatabase.execSQL(CREATE_FAVORITES_TABLE);
        sqLiteDatabase.execSQL(CREATE_ORDERS_TABLE);
        sqLiteDatabase.execSQL(CREATE_ORDER_ITEMS_TABLE);
        sqLiteDatabase.execSQL(CREATE_SPECIAL_OFFERS_TABLE);
        sqLiteDatabase.execSQL(CREATE_SPECIAL_OFFER_ITEMS_TABLE);


    }
    //-------------------Special offer--------------------------------------------
    private static final String CREATE_SPECIAL_OFFERS_TABLE = "CREATE TABLE SpecialOffers (" +
            "offer_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT, " +
            "startDate TEXT, " +
            "endDate TEXT, " +
            "extras TEXT," +
            "totalPrice DOUBLE);";

    private static final String CREATE_SPECIAL_OFFER_ITEMS_TABLE = "CREATE TABLE SpecialOfferItems (" +
            "specialOfferItemID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "offer_id INTEGER, " +
            "pizzaName TEXT, " +
            "size TEXT, " +
            "quantity INTEGER, " +
            "FOREIGN KEY(offer_id) REFERENCES SpecialOffers(offer_id) ON DELETE CASCADE, " +
            "FOREIGN KEY(pizzaName) REFERENCES Pizza(name));";

    public long insertSpecialOffer(SpecialOffer specialOffer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", specialOffer.getName());
        values.put("startDate", specialOffer.getStartDate());
        values.put("endDate", specialOffer.getEndDate());
        values.put("totalPrice", specialOffer.getTotalPrice());
        values.put("extras", specialOffer.getExtras());
        long offerId = db.insert("SpecialOffers", null, values);
        db.close();
        return offerId;
    }

    public long insertSpecialOfferItem(SpecialOfferItem specialOfferItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("offer_id", specialOfferItem.getOfferId());
        values.put("pizzaName", specialOfferItem.getPizzaName());
        values.put("size", specialOfferItem.getSize());
        values.put("quantity", specialOfferItem.getQuantity());
        long offerItemId = db.insert("SpecialOfferItems", null, values);
        db.close();
        return offerItemId;
    }
    public List<SpecialOffer> getAllSpecialOffers() {
        List<SpecialOffer> specialOffers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM SpecialOffers", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long offerId = cursor.getLong(cursor.getColumnIndex("offer_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String startDate = cursor.getString(cursor.getColumnIndex("startDate"));
                String endDate = cursor.getString(cursor.getColumnIndex("endDate"));
                double totalPrice = cursor.getDouble(cursor.getColumnIndex("totalPrice"));
                String extras = cursor.getString(cursor.getColumnIndex("extras"));
                SpecialOffer specialOffer = new SpecialOffer(offerId, name, startDate, endDate, totalPrice, extras);
                specialOffer.setSpecialOfferItems(getSpecialOfferItems(offerId));
                specialOffers.add(specialOffer);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return specialOffers;
    }

    public List<SpecialOfferItem> getSpecialOfferItems(long offerId) {
        List<SpecialOfferItem> specialOfferItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM SpecialOfferItems WHERE offer_id = ?", new String[]{String.valueOf(offerId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String pizzaName = cursor.getString(cursor.getColumnIndex("pizzaName"));
                String size = cursor.getString(cursor.getColumnIndex("size"));
                int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));

                SpecialOfferItem specialOfferItem = new SpecialOfferItem(offerId, pizzaName, size, quantity);
                specialOfferItems.add(specialOfferItem);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return specialOfferItems;
    }
    //--------------
    private static final String CREATE_ORDERS_TABLE = "CREATE TABLE Orders (" +
            "orderID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "email TEXT," +
            "orderDate TEXT," +
            "orderTime TEXT," +
            "totalPrice DOUBLE," +
            "extras TEXT," +
            "notes TEXT," +
            "customerName TEXT," +
            "FOREIGN KEY(email) REFERENCES User(email) ON DELETE CASCADE);";


    private static final String CREATE_ORDER_ITEMS_TABLE = "CREATE TABLE OrderItems (" +
            "orderItemID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "orderID INTEGER," +
            "name TEXT," +
            "size TEXT," +
            "quantity INTEGER," +
            "price DOUBLE," +
            "FOREIGN KEY(orderID) REFERENCES Orders(orderID) ON DELETE CASCADE," +
            "FOREIGN KEY(name) REFERENCES Pizza(name));";
    public long insertOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", order.getEmail());
        values.put("orderDate", order.getOrderDate());
        values.put("orderTime", order.getOrderTime());
        values.put("totalPrice", order.getTotalPrice());
        values.put("extras", order.getExtras());
        values.put("notes", order.getNotes());
        values.put("customerName",order.getCustomerName());
        long id = db.insert("Orders", null, values);
        db.close();
        return id;
    }

    public long insertOrderItem(OrderItem orderItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues itemValues = new ContentValues();
        itemValues.put("orderID", orderItem.getOrderID());
        itemValues.put("name", orderItem.getName());
        itemValues.put("size", orderItem.getSize());
        itemValues.put("quantity", orderItem.getQuantity());
        itemValues.put("price", orderItem.getPrice());
        long orderItemId = db.insert("OrderItems", null, itemValues);
        db.close();
        return orderItemId;
    }

    private static final String CREATE_FAVORITES_TABLE = "CREATE TABLE Fav (" +
            "FavoriteID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "email TEXT," +
            "name TEXT," +
            "FOREIGN KEY(email) REFERENCES User(email) ON DELETE CASCADE," +
            "FOREIGN KEY(name) REFERENCES Pizza(name));";
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insertUser(User user){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", user.getEmail());
        contentValues.put("firstName",user.getFirstName());
        contentValues.put("lastName",user.getLastName());
        contentValues.put("phoneNumber", user.getPhoneNumber());
        contentValues.put("gender", user.getGender());
        contentValues.put("password", user.getPassword());
        contentValues.put("hashedPassword", user.getHashPassword());
        contentValues.put("permission", user.getPermission());
        contentValues.put("ProfilePicture", user.getProfilePicture());

//        contentValues.put("ProfilePicture", user.getProfilePicture());
        if (sqLiteDatabase.insert("User",null,contentValues) == -1)
            return false;
        else
            return true;
    }
    public boolean insertPizza(Pizza pizza){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",pizza.getName());
        contentValues.put("category",pizza.getCategory());
        contentValues.put("size",pizza.getSize());
        contentValues.put("smallPrice",pizza.getSmallPrice());
        contentValues.put("mediumPrice",pizza.getMediumPrice());
        contentValues.put("largePrice",pizza.getLargePrice());
        contentValues.put("discription",pizza.getDiscription());
        if (sqLiteDatabase.insert("Pizza",null,contentValues) == -1)
            return false;
        else
            return true;
    }
    public Cursor getAllPizzas() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM Pizza", null);
    }
    public Cursor getAllUsers() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM User", null);
    }
    // get user by email
    public Cursor getUserByEmail(String email){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM User WHERE email = '"+email+"'",null);
    }
    public Cursor getPizzaByName(String name){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM Pizza WHERE name = '"+name+"'",null);
    }

    public void updateUserInfo(User user){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("firstName",user.getFirstName());
        contentValues.put("lastName",user.getLastName());
        contentValues.put("phoneNumber", user.getPhoneNumber());
        sqLiteDatabase.update("User",contentValues,"email = '"+user.getEmail()+"'",null);
    }

    // Update the user Password
    public void updateUserPassword(String email,String hashedPassword){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("hashedPassword", hashedPassword);
        sqLiteDatabase.update("User",contentValues,"email = '"+email+"'",null);
    }
    public void updateUserProfilePicture(String email, byte[] profilePicture){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ProfilePicture",profilePicture);
        sqLiteDatabase.update("User",contentValues,"email = '"+email+"'",null);
    }
    // Insert favorite pizza
    public boolean insertFavorite(String userEmail, String pizzaName) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", userEmail);
        contentValues.put("name", pizzaName);
        return sqLiteDatabase.insert("Fav", null, contentValues) != -1;
    }
    public void deleteFavorite(String email, String name){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete("Fav","email = ? AND name = ?", new String[]{email, name});
    }

    // delete a pizza from favorite lists for all users
    public void deletePizzaFromAllFavorites(String name){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete("Fav","name = ?", new String[]{name});
    }

    // get favorites with pizza info by user email
    public Cursor getFavoritesWithPizzaInfoByEmail(String email){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM Fav INNER JOIN Pizza ON Fav.name = Pizza.name WHERE email = '"+email+"'",null);
    }

    public boolean isFavorite(String email, String name){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Fav WHERE email = ? AND name = ?", new String[]{email, name});
        return cursor.getCount() > 0;
    }
    public List<String> getFavoritePizzaTypesByEmail(String email) {
        List<String> favoriteTypes = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT DISTINCT Pizza.name FROM Fav INNER JOIN Pizza ON Fav.name = Pizza.name WHERE Fav.email = ?", new String[]{email});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String category = cursor.getString(0);
                favoriteTypes.add(category);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return favoriteTypes;
    }

    public List<Order> getUserOrders(String email) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Orders WHERE email = ?", new String[]{email});

        if (cursor != null&& cursor.moveToFirst()) {
            do {
                long orderId = cursor.getLong(cursor.getColumnIndex("orderID"));
                String orderDate = cursor.getString(cursor.getColumnIndex("orderDate"));
                double totalPrice = cursor.getDouble(cursor.getColumnIndex("totalPrice"));
                String orderTime = cursor.getString(cursor.getColumnIndex("orderTime"));
                String extras = cursor.getString(cursor.getColumnIndex("extras"));
                String notes = cursor.getString(cursor.getColumnIndex("notes"));
                String customerName =cursor.getString(cursor.getColumnIndex("customerName"));
                Order order = new Order(email, orderDate, totalPrice, extras, notes, orderId, orderTime, customerName);
                order.setOrderID(orderId);
                order.setOrderItems(getOrderItems(orderId)); // Fetch the order items
                orders.add(order);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return orders;
    }

    public List<OrderItem> getOrderItems(long orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM OrderItems WHERE orderID = ?", new String[]{String.valueOf(orderId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String size = cursor.getString(cursor.getColumnIndex("size"));
                int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));

                OrderItem orderItem = new OrderItem(name, size, quantity, price);
                orderItem.setOrderID(orderId);
                orderItems.add(orderItem);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return orderItems;
    }
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Orders", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long orderId = cursor.getLong(cursor.getColumnIndex("orderID"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String customerName = cursor.getString(cursor.getColumnIndex("customerName"));
                String orderDate = cursor.getString(cursor.getColumnIndex("orderDate"));
                String orderTime = cursor.getString(cursor.getColumnIndex("orderTime"));
                double totalPrice = cursor.getDouble(cursor.getColumnIndex("totalPrice"));
                String extras = cursor.getString(cursor.getColumnIndex("extras"));
                String notes = cursor.getString(cursor.getColumnIndex("notes"));
                Order order = new Order(email, orderDate, totalPrice, extras, notes, orderTime,customerName);
                order.setOrderID(orderId);
                order.setOrderItems(getOrderItems(orderId));  // Fetch the order items
                orders.add(order);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return orders;
    }
    public String getUserNameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String userName = null;
        Cursor cursor = db.rawQuery("SELECT firstName FROM User WHERE email = ?", new String[]{email});

        if (cursor != null && cursor.moveToFirst()) {
            int firstNameIndex = cursor.getColumnIndex("firstName");
            if (firstNameIndex != -1) {
                userName = cursor.getString(firstNameIndex);
            }
            cursor.close();
        }
        db.close();
        return userName;
    }


}
