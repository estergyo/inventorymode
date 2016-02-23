package com.example.priansyah.demo1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter adapter;

    public final static String EXTRA_MESSAGE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager();

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Item");
        tabLayout.getTabAt(1).setText("Category");
        tabLayout.getTabAt(2).setText("Supplier");
        tabLayout.getTabAt(0).select();

    }

    private void setupViewPager(){
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ListItemFragment(), "Item");
        adapter.addFragment(new ListKategoriFragment(), "Category");
        adapter.addFragment(new ListSupplierFragment(), "Supplier");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_scan:
                IntentIntegrator scanIntegrator  = new IntentIntegrator(this);
                scanIntegrator.initiateScan();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == getResources().getInteger(R.integer.qrcode_rq_code)){
            if(resultCode == RESULT_OK){
                ((ListItemFragment)adapter.getItem(0)).setList();
            }else{
                Toast.makeText(this,"Scan Cancelled",Toast.LENGTH_SHORT);
            }
        }else if(requestCode == getResources().getInteger(R.integer.item_new_rq_code) || requestCode == getResources().getInteger(R.integer.item_update_rq_code)){
            if(resultCode == RESULT_OK)
                ((ListItemFragment)adapter.getItem(0)).setList();
        }else if(requestCode == getResources().getInteger(R.integer.category_new_rq_code) || requestCode == getResources().getInteger(R.integer.category_update_rq_code)){
            if(resultCode == RESULT_OK)
                ((ListKategoriFragment)adapter.getItem(1)).setList();
        }else if(requestCode == getResources().getInteger(R.integer.supplier_new_rq_code) || requestCode == getResources().getInteger(R.integer.supplier_update_rq_code)){
            if(resultCode == RESULT_OK)
                ((ListSupplierFragment)adapter.getItem(2)).setList();
        }
        else{
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if(scanningResult != null && scanningResult.getContents() != null) {
                String scanContent = scanningResult.getContents();
                inputData(scanContent);
            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "No scan data received!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public void inputData(String scanContent) {
        SQLiteDatabase db = openOrCreateDatabase("POS", MODE_PRIVATE, null);
        db.execSQL("Create table if not exists stock(sku VARCHAR, name VARCHAR, amount INTEGER, price INTEGER, category VARCHAR, supplier VARCHAR, image VARCHAR);");
        Cursor result = db.rawQuery("select * from stock where sku = '"+scanContent+"' ",null);
        if ( result.getCount() == 0 ) {
            Intent pName = new Intent(this, InputProductName.class);
            pName.putExtra(EXTRA_MESSAGE, scanContent);
            startActivityForResult(pName, getResources().getInteger(R.integer.qrcode_rq_code));
        }else{
            result.moveToFirst();
            int jumlah = result.getInt(2)+1;
            db.execSQL("update stock set amount = " + jumlah + " where sku = '" + scanContent + "'");
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
