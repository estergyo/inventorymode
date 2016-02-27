package com.example.priansyah.demo1.Adapter;

import android.content.Context;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.util.Log;

import com.example.priansyah.demo1.Entity.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;


public class SearchItemAdapter extends SimpleCursorAdapter
{
    private static final String[] mFields  = { "_id", "result" };
    private static final String[] mVisible = { "result" };
    private static final int[]    mViewIds = { android.R.id.text1 };

    private ArrayList<String> mResults;
    private SQLiteDatabase dbSearchItem;
    private int realPosition;
    private SuggestionsCursor suggestionsCursor;

    public SearchItemAdapter(Context context, SQLiteDatabase dbSearchItem)
    {
        super(context, android.R.layout.simple_list_item_1, null, mVisible, mViewIds, 0);
        this.dbSearchItem = dbSearchItem;
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint)
    {
        suggestionsCursor = new SuggestionsCursor(constraint, dbSearchItem);
        return suggestionsCursor;
    }
    public int getRealPosition(){
        return suggestionsCursor.getRealPosition();
    }

    private static class SuggestionsCursor extends AbstractCursor
    {
        ArrayList<String> mResults;
        public SuggestionsCursor(CharSequence constraint, SQLiteDatabase dbSearchItem)
        {
            dbSearchItem.execSQL("Create table if not exists stock(sku VARCHAR, name VARCHAR, amount INTEGER, price INTEGER, category VARCHAR, supplier VARCHAR, image VARCHAR);");
            Cursor itms = dbSearchItem.rawQuery("select * from stock",null);
            this.mResults = new ArrayList<String>();
            if(itms != null)
                if(itms.moveToFirst())
                    do{
                        mResults.add(itms.getString(1));
                    }while(itms.moveToNext());

            if(!TextUtils.isEmpty(constraint)){
                String constraintString = constraint.toString().toLowerCase(Locale.ROOT);
                Iterator<String> iter = mResults.iterator();
                while(iter.hasNext()){
                    if(!iter.next().toLowerCase(Locale.ROOT).startsWith(constraintString))
                    {
                        iter.remove();
                    }
                }
            }
        }

        public int getRealPosition(){
            return 0;
        }

        @Override
        public int getCount()
        {
            return mResults.size()>3?3:mResults.size();
        }

        @Override
        public String[] getColumnNames()
        {
            return mFields;
        }

        @Override
        public long getLong(int column)
        {
            if(column == 0){
                return mPos;
            }
            throw new UnsupportedOperationException("unimplemented");
        }

        @Override
        public String getString(int column)
        {
            if(column == 1){
                return mResults.get(mPos);
            }
            throw new UnsupportedOperationException("unimplemented");
        }

        @Override
        public short getShort(int column)
        {
            throw new UnsupportedOperationException("unimplemented");
        }

        @Override
        public int getInt(int column)
        {
            throw new UnsupportedOperationException("unimplemented");
        }

        @Override
        public float getFloat(int column)
        {
            throw new UnsupportedOperationException("unimplemented");
        }

        @Override
        public double getDouble(int column)
        {
            throw new UnsupportedOperationException("unimplemented");
        }

        @Override
        public boolean isNull(int column)
        {
            return false;
        }
    }
}