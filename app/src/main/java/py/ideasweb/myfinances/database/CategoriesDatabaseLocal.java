package py.ideasweb.myfinances.database;

import android.util.Log;

import py.ideasweb.myfinances.R;
import py.ideasweb.myfinances.model.MyCategory;

import java.util.ArrayList;
import java.util.List;

public class CategoriesDatabaseLocal implements CategoriesDatabase {
    private static final String TAG = "CategoriesDatabaseLocal";
    private static CategoriesDatabaseLocal ourInstance;

    public static CategoriesDatabaseLocal getInstance() {
        if(ourInstance == null){
            ourInstance = new CategoriesDatabaseLocal();
        }
        return ourInstance;
    }

    private CategoriesDatabaseLocal() {
        if(MyCategory.count(MyCategory.class,"", null) == 0 ){
            //Agrega las categorias por omision
            List<MyCategory> defaultCategories = new ArrayList<>();
            defaultCategories.add(new MyCategory(false, "Comida", R.drawable.ic_food_24dp, "#2020AD"));
            defaultCategories.add(new MyCategory(false, "Compras", R.drawable.ic_shopping_24dp, "#03B703"));
            defaultCategories.add(new MyCategory(false, "Salud", R.drawable.ic_health_24dp, "#00BFEA"));
            defaultCategories.add(new MyCategory(false, "Transporte", R.drawable.ic_transport_24dp, "#AF0095"));
            defaultCategories.add(new MyCategory(false, "Otros", R.drawable.ic_other_24dp, "#FF1919"));
            defaultCategories.add(new MyCategory(false, "Tarjetas De Credito", R.drawable.credit_card, "#FF1919"));

            defaultCategories.add(new MyCategory(true, "Comision", R.drawable.ic_investment_24dp, "#269900"));
            defaultCategories.add(new MyCategory(true, "Otros", R.drawable.ic_other_24dp, "#FF1919"));
            defaultCategories.add(new MyCategory(true, "Regalos", R.drawable.ic_gift_24dp, "#C40089"));
            defaultCategories.add(new MyCategory(true, "Salario", R.drawable.ic_money_24dp, "#2096E5"));
            MyCategory.saveInTx(defaultCategories);
        }
    }

    @Override
    public List<MyCategory> getIncomes() {
        return MyCategory.find(MyCategory.class, "is_income = ?" , "1");
    }

    @Override
    public List<MyCategory> getExpenses() {
        return MyCategory.find(MyCategory.class, "is_income = ?" , "0");
    }

    @Override
    public MyCategory get(long id) {
        return MyCategory.findById(MyCategory.class, id);
    }

    @Override
    public boolean add(MyCategory myCategory) {
        try {
            myCategory.save();
        }catch(Exception _e){
            Log.e(TAG, "add: ", _e);
            return false;
        }
        return true;
    }

    @Override
    public boolean edit(MyCategory myCategory) {
        try {
            myCategory.save();
        }catch(Exception _e){
            Log.e(TAG, "add: ", _e);
            return false;
        }
        return true;
    }

    @Override
    public boolean remove(String title) {
        try {
            MyCategory.deleteAll(MyCategory.class, "title = ?", title);
        }catch(Exception _e){
            Log.e(TAG, "add: ", _e);
            return false;
        }
        return true;
    }
}
