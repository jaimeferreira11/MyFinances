package py.ideasweb.myfinances.database;

import android.util.Log;

import py.ideasweb.myfinances.model.MyBudget;

import java.util.Calendar;
import java.util.List;


public class BudgetDatabaseLocal implements BudgetDatabase{
    private static final String TAG = "BudgetDatabaseLocal";
    private static BudgetDatabaseLocal ourInstance;

    public static BudgetDatabaseLocal getInstance() {
        if(ourInstance == null){
            ourInstance = new BudgetDatabaseLocal();
        }
        return ourInstance;
    }

    private BudgetDatabaseLocal() {
    }

    @Override
    public List<MyBudget> get() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //setear medianoche
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long from = calendar.getTime().getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        long to = calendar.getTime().getTime();
        return MyBudget.find(MyBudget.class, "time_stamp between ? and ?", from + "", to + "");
    }

    @Override
    public MyBudget get(long id) {
        return MyBudget.findById(MyBudget.class, id);
    }

    @Override
    public boolean add(MyBudget budget) {
        try {
            budget.save();
        }catch(Exception _e){
            Log.e(TAG, "add: ", _e);
            return false;
        }
        return true;
    }

    @Override
    public boolean edit(MyBudget budget) {
        try {
            budget.save();
        }catch(Exception _e){
            Log.e(TAG, "add: ", _e);
            return false;
        }
        return true;

    }

    @Override
    public boolean removeAll() {
        try {
            MyBudget.deleteAll(MyBudget.class);
        }catch(Exception _e){
            Log.e(TAG, "add: ", _e);
            return false;
        }
        return true;
    }

    @Override
    public List<MyBudget> getGlobalInMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //setear medianoche
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long from = calendar.getTime().getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        long to = calendar.getTime().getTime();
        return MyBudget.find(MyBudget.class, "is_global = ? AND time_stamp between ? and ?", "1", from + "", to + "");
    }
}
