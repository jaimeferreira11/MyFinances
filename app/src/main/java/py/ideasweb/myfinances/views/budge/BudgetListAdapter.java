package py.ideasweb.myfinances.views.budge;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import py.ideasweb.myfinances.R;
import py.ideasweb.myfinances.controller.Controller;
import py.ideasweb.myfinances.model.MyBudget;
import py.ideasweb.myfinances.model.MyCategory;

import java.util.Calendar;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import py.ideasweb.myfinances.utils.Utilities;

public class BudgetListAdapter extends RecyclerView.Adapter<BudgetListAdapter.ViewHolder> {
    Controller controller;
    List<MyBudget> budgets;

    public BudgetListAdapter(Controller controller) {
        this.controller = controller;
        this.budgets = controller.getAllBudget();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_budget, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int month = Calendar.getInstance().get(Calendar.MONTH);
        MyBudget budget = budgets.get(position);
        float spent;
        if(budget.isGlobal()){
            holder.category.setText("Mi Presupuesto");
            holder.category.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_globe_24dp, 0, 0, 0);
            spent = controller.getExpensesTotalInMonth(month);
        }else{
            MyCategory category = budget.getCategory();
            holder.category.setText(category.getTitle());
            holder.category.setCompoundDrawablesWithIntrinsicBounds(category.getIcon(), 0, 0, 0);
            spent = controller.getExpensesByCategoryInMonth(category, month);
        }
        String symbol = "Gs. ";
        holder.budgetValue.setText(symbol + Utilities.toStringFromFloatWithFormat(budget.getAmount()));
        holder.budgetSpent.setText(symbol + Utilities.toStringFromFloatWithFormat(spent));
        holder.budgetRemaining.setText(symbol +   Utilities.toStringFromFloatWithFormat(budget.getAmount() - spent));
        int percentage = (int) (spent * 100 / budget.getAmount());
        holder.percentage.setText( percentage + "%");
        holder.progressBudget.setMax(100);
        holder.progressBudget.setProgress(percentage);
    }

    @Override
    public int getItemCount() {
        return budgets.size();
    }

    public void refreshData() {
        budgets = controller.getAllBudget();
        notifyDataSetChanged();
    }

    static

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;

        @BindView(R.id.category)
        TextView category;
        @BindView(R.id.budget_value)
        TextView budgetValue;
        @BindView(R.id.budget_spent)
        TextView budgetSpent;
        @BindView(R.id.budget_remaining)
        TextView budgetRemaining;
        @BindView(R.id.progressBudget)
        ProgressBar progressBudget;
        @BindView(R.id.percentage)
        TextView percentage;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
