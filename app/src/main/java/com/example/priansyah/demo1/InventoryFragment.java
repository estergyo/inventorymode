package com.example.priansyah.demo1;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Priansyah on 3/21/2016.
 */
public class InventoryFragment extends Fragment {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter adapter;

    public final static String EXTRA_MESSAGE = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inventory, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        setupViewPager();

        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Item");
        tabLayout.getTabAt(1).setText("Category");
        tabLayout.getTabAt(2).setText("Supplier");
        tabLayout.getTabAt(0).select();

    }

    private void setupViewPager(){
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ListItemFragment(), "Item");
        adapter.addFragment(new ListKategoriFragment(), "Category");
        adapter.addFragment(new ListSupplierFragment(), "Supplier");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
//    {
//        MenuInflater inflater = getActivity().getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
////        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_scan:
                IntentIntegrator scanIntegrator  = new IntentIntegrator(getActivity());
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
            if(resultCode == Activity.RESULT_OK){
                ((ListItemFragment)adapter.getItem(0)).setList();
            }else{
                Toast.makeText(getActivity(), "Scan Cancelled", Toast.LENGTH_SHORT);
            }
        }else if(requestCode == getResources().getInteger(R.integer.item_new_rq_code) || requestCode == getResources().getInteger(R.integer.item_update_rq_code)){
            if(resultCode == Activity.RESULT_OK)
                ((ListItemFragment)adapter.getItem(0)).setList();
        }else if(requestCode == getResources().getInteger(R.integer.category_new_rq_code) || requestCode == getResources().getInteger(R.integer.category_update_rq_code)){
            if(resultCode == Activity.RESULT_OK) {
                ((ListKategoriFragment) adapter.getItem(1)).setList();
            }
        }else if(requestCode == getResources().getInteger(R.integer.supplier_new_rq_code) || requestCode == getResources().getInteger(R.integer.supplier_update_rq_code)){
            if(resultCode == Activity.RESULT_OK)
                ((ListSupplierFragment)adapter.getItem(2)).setList();
        }
        else{
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if(scanningResult != null && scanningResult.getContents() != null) {
                String scanContent = scanningResult.getContents();
                inputData(scanContent);
            }
            else {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                        "No scan data received!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public void inputData(String scanContent) {
        SQLiteDatabase db = getActivity().openOrCreateDatabase("POS", getActivity().MODE_PRIVATE, null);
        db.execSQL("Create table if not exists stock(sku VARCHAR, name VARCHAR, amount INTEGER, price INTEGER, category VARCHAR, supplier VARCHAR, image VARCHAR);");
        Cursor result = db.rawQuery("select * from stock where sku = '"+scanContent+"' ",null);
        if ( result.getCount() == 0 ) {
            Intent pName = new Intent(getActivity(), InputProductName.class);
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

        @Override
        // To update fragment in ViewPager, we should override getItemPosition() method,
        // in this method, we call the fragment's public updating method.
        public int getItemPosition(Object object) {
            if (object instanceof ListItemFragment) {
                ((ListItemFragment)object).setList();
            } else if (object instanceof ListKategoriFragment) {
                ((ListKategoriFragment)object).setList();
            } else if (object instanceof ListSupplierFragment) {
                ((ListSupplierFragment)object).setList();
            }
            return super.getItemPosition(object);
        };
    }
}
