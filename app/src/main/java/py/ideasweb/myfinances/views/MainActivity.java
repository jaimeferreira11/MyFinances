package py.ideasweb.myfinances.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.evernote.android.job.JobManager;

import java.util.Currency;
import java.util.Locale;

import py.ideasweb.myfinances.R;
import py.ideasweb.myfinances.controller.Controller;
import py.ideasweb.myfinances.utils.Utilities;
import py.ideasweb.myfinances.views.balance.TransactionFragment;
import py.ideasweb.myfinances.views.budge.BudgeFragment;
import py.ideasweb.myfinances.views.categories.ListCategoryActivity;
import py.ideasweb.myfinances.views.dashboard.DashboardFragment;

public class MainActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    Controller controller;
    SharedPreferences prefs ;

    public MainActivity() {
        controller = Controller.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JobManager.create(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }


    @Override
    protected void onResume() {
        super.onResume();
        TextView mTotalBalance = findViewById(R.id.total_balance);
       // mTotalBalance.setText(Currency.getInstance(Locale.getDefault()).getSymbol()  + Utilities.toStringFromFloatWithFormat(controller.getBalance()));
        mTotalBalance.setText("Gs. "  + Utilities.toStringFromFloatWithFormat(controller.getBalance()));
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return DashboardFragment.newInstance();
                case 1:
                    return TransactionFragment.newInstance();
                case 2:
                    return BudgeFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_category) {
            Intent i = new Intent(this, ListCategoryActivity.class);
            startActivity(i);
        }
        if(id == R.id.salir){
            if(!prefs.getBoolean("pin",false)){
                finish();
            }else {
                Intent i = new Intent(this, PinActivity.class);
                startActivity(i);
                finish();
            }

        }
       /* if(id == R.id.add_payment){
            Intent i = new Intent(this,AltaTipoPago.class);
            startActivity(i);
        }
        if(id == R.id.remove_payment){
            Intent i = new Intent(this,RemoveTipoPago.class);
            startActivity(i);
        }
        if(id == R.id.add){
            Intent i = new Intent(this,AltaGasto.class);
            startActivity(i);
        }
        if(id == R.id.remove){
            Intent i = new Intent(this,RemoveGasto.class);
            startActivity(i);
        }
        if(id == R.id.history){
            Intent i = new Intent(this,HistorialActivity.class);
            startActivity(i);
        }*/
        if(id == R.id.settings){
            Intent i = new Intent(this,Preferencias.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
