package py.ideasweb.myfinances.database;

import py.ideasweb.myfinances.model.MyBudget;

import java.util.List;

public interface BudgetDatabase {
    public List<MyBudget> get();
    public MyBudget get(long id);
    public boolean add(MyBudget budget);
    public boolean edit(MyBudget budget);
    public boolean removeAll();

    List<MyBudget> getGlobalInMonth(int month);
}
