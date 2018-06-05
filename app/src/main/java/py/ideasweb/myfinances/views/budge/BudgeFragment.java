package py.ideasweb.myfinances.views.budge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import py.ideasweb.myfinances.R;
import py.ideasweb.myfinances.controller.Controller;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import py.ideasweb.myfinances.model.MyBudget;
import py.ideasweb.myfinances.utils.UtilLogger;
import py.ideasweb.myfinances.utils.Utilities;
import uk.co.markormesher.android_fab.FloatingActionButton;

public class BudgeFragment extends Fragment {

    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.addBudge)
    FloatingActionButton addBudge;

    Controller controller;
    BudgetListAdapter adapter;
    Unbinder unbinder;
    boolean edit = false;


    public BudgeFragment() {
        // Required empty public constructor
        controller = Controller.getInstance();
    }

    public static BudgeFragment newInstance() {
        BudgeFragment fragment = new BudgeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_budge, container, false);
        unbinder = ButterKnife.bind(this, view);


        adapter = new BudgetListAdapter(controller);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter);

        addBudge.setButtonBackgroundColour(getResources().getColor(R.color.colorAccent));

        // si ya hay un budget, cambiar el icono del floating
        if(controller.getAllBudget().size() > 0){
            edit = true;
            addBudge.setButtonIconResource(R.drawable.pencil_24);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.refreshData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.addBudge)
    public void onViewClicked() {
        Intent intent = new Intent(getContext(), NewBudgetActivity.class);
        //editar
        if(edit){
            UtilLogger.info("EDITANDO");
            List<MyBudget> list = controller.getAllBudget();
            intent.putExtra("value", Utilities.toStringFromFloatWithFormat(list.get(0).getAmount()));
            if(list.get(0).isNotifyMe()) intent.putExtra("notif", "true");
            if(list.get(0).isPeriodic()) intent.putExtra("periodic", "true");
        }


        startActivity(intent);
    }
}
