package com.example.priansyah.demo1;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.Toast;

import com.example.priansyah.demo1.Adapter.ListDetilTransaksiAdapter;
import com.example.priansyah.demo1.Adapter.ListItemTransaksiBaruAdapter;
import com.example.priansyah.demo1.Adapter.SearchItemAdapter;
import com.example.priansyah.demo1.Adapter.SearchTransaksiAdapter;
import com.example.priansyah.demo1.Entity.Item;
import com.example.priansyah.demo1.Entity.TransDetail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Priansyah on 3/21/2016.
 */
public class TransaksiFragment extends Fragment {
    RecyclerView recList;
    ListItemTransaksiBaruAdapter adapter;
    Button buttonTambahItemTr;
    Button buttonLanjutTransaksi;

    ArrayList<TransDetail> listOfTransactionDetail;
    ArrayList<TransDetail> itemsPendingRemoval;
    SQLiteDatabase dbSearchItem;
    SQLiteDatabase dbListItemTr;
    AutoCompleteTextView search;
    SimpleCursorAdapter searchCursorAdapter;
    private static final String[] columns = { "_id" , "sku" , "image" , "nama" , "harga" };
    int amount = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaksi, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listOfTransactionDetail = new ArrayList<>();
        itemsPendingRemoval = new ArrayList<>();

        dbListItemTr = getActivity().openOrCreateDatabase("POS", getActivity().MODE_PRIVATE, null);
        dbListItemTr.execSQL("Create table if not exists stock(sku VARCHAR, name VARCHAR, amount INTEGER, price INTEGER, category VARCHAR, supplier VARCHAR, image VARCHAR);");

        buttonTambahItemTr = (Button) getActivity().findViewById(R.id.buttonTambahItemTr);

        buttonTambahItemTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor itmscheck = dbSearchItem.rawQuery("select * from stock", null);
                if (itmscheck.getCount() == 0) {
                    Toast.makeText(getActivity(), "Tidak ada barang yang dapat dipilih", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity().getBaseContext(), PilihItemTransaksiActivity.class);
                    startActivityForResult(intent, getResources().getInteger(R.integer.item_transaksi_rq_code));
                }
            }
        });

//        dbListItemTr.execSQL("DROP TABLE IF EXISTS transaction_detail");
//        dbListItemTr.execSQL("DROP TABLE IF EXISTS stock_transaction");

        recList = (RecyclerView) getActivity().findViewById(R.id.listViewItemTransaksi);
        setUpRecyclerView();
//        recList.setHasFixedSize(true);
//        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recList.setLayoutManager(llm);

        buttonLanjutTransaksi = (Button) getActivity().findViewById(R.id.buttonLanjutTransaksi);
        buttonLanjutTransaksi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (listOfTransactionDetail.size() == 0) {
                    Toast.makeText(getActivity(), "Tidak ada barang yang dipilih", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(getActivity().getBaseContext(), DiskonActivity.class);
                    intent.putExtra("TRANSDETLIST", listOfTransactionDetail);
                    startActivity(intent);
                }
            }
        });
        dbSearchItem = getActivity().openOrCreateDatabase("POS", getActivity().MODE_PRIVATE, null);

        search = (AutoCompleteTextView) getActivity().findViewById(R.id.searchViewTransaksi);

        search.setHint("Search...");
        search.setThreshold(1);
        searchCursorAdapter = new SearchItemAdapter(getActivity(), columns);
        searchCursorAdapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                return cursor.getString(cursor.getColumnIndex("nama"));
            }
        });
        search.setAdapter(searchCursorAdapter);

        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                search.setHint("Search...");
                search.setText("");
                suggestionClick((Cursor) parent.getItemAtPosition(position));
            }
        });
        searchCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return convertToCursor(constraint.toString());
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString();
                if (query.length() > 0) {
                    searchCursorAdapter.getFilter().filter(s.toString());
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_transaksi_baru, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.menu_item_undo_checkbox) {
//            item.setChecked(!item.isChecked());
//            ((ListItemTransaksiBaruAdapter)recList.getAdapter()).setUndoOn(item.isChecked());
//        }
        return super.onOptionsItemSelected(item);
    }


    private void setUpRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.setAdapter(adapter);
        recList.setHasFixedSize(true);
        setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();
}

    private MatrixCursor convertToCursor(String constraint) {
        MatrixCursor cursor = new MatrixCursor(columns);
        dbSearchItem.execSQL("Create table if not exists stock(sku VARCHAR, name VARCHAR, amount INTEGER, price INTEGER, category VARCHAR, supplier VARCHAR, image VARCHAR);");
        Cursor itms = dbSearchItem.rawQuery("select * from stock",null);
        ArrayList<Item> mResults = new ArrayList<Item>();
        if(itms != null)
            if(itms.moveToFirst())
                do{
                    if(!TextUtils.isEmpty(constraint)){
                        String constraintString = constraint.toString().toLowerCase(Locale.ROOT);
                        if(!itms.getString(1).toLowerCase(Locale.ROOT).startsWith(constraintString)){
                        }else {
                            mResults.add(new Item(itms.getString(1), itms.getString(0), "" + itms.getInt(2), "" + itms.getInt(3), itms.getString(5), itms.getString(4), itms.getString(6)));
                        }
                    }
                }while(itms.moveToNext());
        int i = 0;
        for (Item item : mResults) {
            String[] temp = new String[5];
            i = i + 1;
            temp[0] = Integer.toString(i);
            temp[1] = item.getTextSKU();
            temp[2] = item.getTextNama();
            temp[3] = "Rp " + item.getTextHarga() + ",-";
            temp[4] = item.getTextImage();
            cursor.addRow(temp);
        }
        return cursor;
    }

    public boolean suggestionClick(Cursor cursor){

        search.clearFocus();

        dbListItemTr.execSQL("Create table if not exists stock(sku VARCHAR, name VARCHAR, amount INTEGER, price INTEGER, category VARCHAR, supplier VARCHAR, image VARCHAR);");
        Cursor items = dbListItemTr.rawQuery("select * from stock where sku = '"+cursor.getString(1)+"' ",null);
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH)+1;
        String date = c.get(Calendar.DATE) +"/" + month +"/" + c.get(Calendar.YEAR);

        Log.d("date", date);
        if(items != null)
            if(items.moveToFirst()) {
                Boolean isAvailable = false;
                for (int i=0; i<listOfTransactionDetail.size(); i++) {
                    if (listOfTransactionDetail.get(i).getTextSKU().equals(cursor.getString(1))) {
                        int jumlah =Integer.parseInt(listOfTransactionDetail.get(i).getTextJumlah());
                        listOfTransactionDetail.get(i).setTextJumlah("" + (jumlah + 1));
                        Log.d("substring",listOfTransactionDetail.get(i).getTextHarga().substring(0,1) );
                        if (listOfTransactionDetail.get(i).getTextHarga().substring(0,1).equals("R")) {
                            String helper = listOfTransactionDetail.get(i).getTextHarga().substring(3, listOfTransactionDetail.get(i).getTextHarga().length()-2);
                            int hargaSatuan = (Integer.parseInt(helper)) / jumlah;
                            listOfTransactionDetail.get(i).setTextHarga("Rp "+ (hargaSatuan * (jumlah+1)) + ",-");
                        }
                        else {
                            int hargaSatuan = (Integer.parseInt(listOfTransactionDetail.get(i).getTextHarga())) / jumlah;
                            listOfTransactionDetail.get(i).setTextHarga(""+hargaSatuan * (jumlah+1));
                        }
                        isAvailable = true;
                        break;
                    }
                }
                if (!isAvailable)
                    listOfTransactionDetail.add(new TransDetail("" + (listOfTransactionDetail.size()+1), items.getString(0), items.getString(1), "" + amount,"Rp "+ (items.getInt(3) * amount) + ",-", date));
            }
        setList();

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == getResources().getInteger(R.integer.item_transaksi_rq_code))
            if(resultCode == Activity.RESULT_OK){
                Calendar c = Calendar.getInstance();
                String date = c.get(Calendar.DATE) +"/" + c.get(Calendar.MONTH)+1 +"/" + c.get(Calendar.YEAR);
                ArrayList<Item> listItem = intent.getParcelableArrayListExtra("PILIHITEMLIST");
                ArrayList<String> listHarga = intent.getStringArrayListExtra("LISTHARGA");
                for (int i=0; i<listItem.size(); i++) {
                    int jml = Integer.parseInt(listItem.get(i).getTextJumlah().toString());
                    String helperHrg = listHarga.get(i).toString().substring(2, listHarga.get(i).length() - 2);
                    Log.d("helperHrg", helperHrg);
                    int hrg = Integer.parseInt(helperHrg);
                    if(jml != 0) {
                        boolean isItem = false;
                        for (int j = 0; j < listOfTransactionDetail.size(); j++) {
                            if (listItem.get(i).getTextSKU().equals(listOfTransactionDetail.get(j).getTextSKU())) {
                                isItem = true;
                                listOfTransactionDetail.get(j).setTextJumlah((jml + Integer.parseInt(listOfTransactionDetail.get(j).getTextJumlah())) + "");
                                Log.d("sbs", listOfTransactionDetail.get(j).getTextHarga().substring(0,1));
                                if (listOfTransactionDetail.get(j).getTextHarga().substring(0,1).equals("R")) {
                                    String helper = listOfTransactionDetail.get(j).getTextHarga().substring(3, listOfTransactionDetail.get(j).getTextHarga().length()-2);
                                    listOfTransactionDetail.get(j).setTextHarga("Rp "+ (Integer.parseInt(helper) + hrg) + ",-");
                                }
                                else {
//                                    listOfTransactionDetail.get(j).setTextHarga( (jml * (Integer.parseInt(listOfTransactionDetail.get(j).getTextHarga()))) + "");
                                    listOfTransactionDetail.get(j).setTextHarga(  "Rp " + (hrg + (Integer.parseInt(listOfTransactionDetail.get(j).getTextHarga()))) + ",-");
                                }
                                break;
                            }
                        }
                        if (!isItem)
                            if (listItem.get(i).getTextHarga().substring(0,1).equals("R")) {
                                String helper2 = listItem.get(i).getTextHarga().substring(3, listItem.get(i).getTextHarga().length()-2);
                                listOfTransactionDetail.add(new TransDetail("" + (listOfTransactionDetail.size() + 1), listItem.get(i).getTextSKU(),  listItem.get(i).getTextNama(), "" + listItem.get(i).getTextJumlah(),"Rp " + (Integer.parseInt(helper2) * Integer.parseInt(listItem.get(i).getTextJumlah()))+ ",-", date));
                            }
                            else {
                                listOfTransactionDetail.add(new TransDetail("" + (listOfTransactionDetail.size() + 1), listItem.get(i).getTextSKU(),  listItem.get(i).getTextNama(), "" + listItem.get(i).getTextJumlah(),"Rp " + (Integer.parseInt(listItem.get(i).getTextHarga()) * Integer.parseInt(listItem.get(i).getTextJumlah()))+ ",-", date));
                            }
                    }
                }
                setList();
            }
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        // Inflate menu to add items to action bar if it is present.
//        inflater.inflate(R.menu.menu_transaksi, menu);
//
//        return true;
//    }
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu)
//    {
//        // Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        final SearchView searchView =
//                (SearchView) menu.findItem(R.id.menu_search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
//
//        searchView.setSuggestionsAdapter(new SearchItemAdapter(this, dbSearchItem));
//        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
//            @Override
//            public boolean onSuggestionClick(int position) {
//                Toast.makeText(TransaksiActivity.this, "Position: " + position, Toast.LENGTH_SHORT).show();
//                searchView.clearFocus();
//                return true;
//            }
//
//            @Override
//            public boolean onSuggestionSelect(int position) {
//                return false;
//            }
//        });
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
//        {
//            @Override
//            public boolean onQueryTextSubmit(String query)
//            {
//                Toast.makeText(TransaksiActivity.this, query, Toast.LENGTH_SHORT).show();
//                searchView.clearFocus();
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText)
//            {
//                return false;
//            }
//        });
//        return super.onPrepareOptionsMenu(menu);
//    }

    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.parseColor("#1CC09F"));
                xMark = ContextCompat.getDrawable(getActivity(), R.drawable.ic_clear_24dp);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) getActivity().getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                adapter = (ListItemTransaksiBaruAdapter)recyclerView.getAdapter();
                if (adapter.isUndoOn() && adapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                adapter = (ListItemTransaksiBaruAdapter)recList.getAdapter();
                boolean undoOn = adapter.isUndoOn();
                if (undoOn) {
                    adapter.pendingRemoval(swipedPosition);
                } else {
                    adapter.remove(swipedPosition);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(recList);
    }

    /**
     * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions
     * after an item is removed.
     */
    private void setUpAnimationDecoratorHelper() {
        recList.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.parseColor("#1CC09F"));
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0;
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }

    public void setList(){
        adapter = new ListItemTransaksiBaruAdapter(getActivity(), getContext(), listOfTransactionDetail, itemsPendingRemoval);
//        adapter.setOnItemClickListener(new ListItemAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(getActivity(), DetailItemActivity.class);
//                intent.putExtra("Item", listOfItems.get(position));
//                getActivity().startActivityForResult(intent, getResources().getInteger(R.integer.item_update_rq_code));
//            }
//        });
//        recList.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        adapter.setUndoOn(true);
        recList.setAdapter(adapter);
    }
}
