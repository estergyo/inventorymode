package com.example.priansyah.demo1.Adapter;

import android.content.Context;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.priansyah.demo1.Entity.Item;
import com.example.priansyah.demo1.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;


public class SearchItemAdapter extends SimpleCursorAdapter
{
    private static final String[] mFields  = { "_id", "image", "nama", "harga" };
    private static final String[] mVisible = { "sku","image", "nama", "harga" };
    private static final int[]    mViewIds = { R.id.imageViewSearch, R.id.textViewSearchNama, R.id.textViewSearchHarga };

    public SearchItemAdapter(Context context, String[] columns) {
        super(context, R.layout.listitem_search, null, columns, null, -1000);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageViewSearch=(ImageView)view.findViewById(R.id.imageViewSearch);
        TextView textViewSearchNama=(TextView)view.findViewById(R.id.textViewSearchNama);
        TextView textViewSearchHarga=(TextView)view.findViewById(R.id.textViewSearchHarga);
        textViewSearchNama.setText(cursor.getString(2));
        textViewSearchHarga.setText(cursor.getString(3));
        imageViewSearch.setImageBitmap(decodeBase64(cursor.getString(4)));
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public Item get(int position) {
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        return new Item(cursor.getString(1), cursor.getString(0), "" + cursor.getInt(2), "" + cursor.getInt(3), cursor.getString(5), cursor.getString(4), cursor.getString(6));
    }

//    @Override
//    public Cursor runQueryOnBackgroundThread(CharSequence constraint)
//    {
//        suggestionsCursor = new SuggestionsCursor(constraint, dbSearchItem);
//        return suggestionsCursor;
//    }
//    public int getRealPosition(){
//        return suggestionsCursor.getRealPosition();
//    }
//
//    private static class SuggestionsCursor extends AbstractCursor
//    {
//        ArrayList<String> mResults;
//        public SuggestionsCursor(CharSequence constraint, SQLiteDatabase dbSearchItem)
//        {
//            dbSearchItem.execSQL("Create table if not exists stock(sku VARCHAR, name VARCHAR, amount INTEGER, price INTEGER, category VARCHAR, supplier VARCHAR, image VARCHAR);");
//            Cursor itms = dbSearchItem.rawQuery("select * from stock",null);
//            this.mResults = new ArrayList<String>();
//            if(itms != null)
//                if(itms.moveToFirst())
//                    do{
//                        mResults.add(itms.getString(1));
//                    }while(itms.moveToNext());
//
//            if(!TextUtils.isEmpty(constraint)){
//                String constraintString = constraint.toString().toLowerCase(Locale.ROOT);
//                Iterator<String> iter = mResults.iterator();
//                while(iter.hasNext()){
//                    if(!iter.next().toLowerCase(Locale.ROOT).startsWith(constraintString))
//                    {
//                        iter.remove();
//                    }
//                }
//            }
//        }
//
//        public int getRealPosition(){
//            return 0;
//        }
//
//        @Override
//        public int getCount()
//        {
//            return mResults.size()>3?3:mResults.size();
//        }
//
//        @Override
//        public String[] getColumnNames()
//        {
//            return mFields;
//        }
//
//        @Override
//        public long getLong(int column)
//        {
//            if(column == 0){
//                return mPos;
//            }
//            throw new UnsupportedOperationException("unimplemented");
//        }
//
//        @Override
//        public String getString(int column)
//        {
//            if(column == 1){
//                return mResults.get(mPos);
//            }
//            throw new UnsupportedOperationException("unimplemented");
//        }
//
//        @Override
//        public short getShort(int column)
//        {
//            throw new UnsupportedOperationException("unimplemented");
//        }
//
//        @Override
//        public int getInt(int column)
//        {
//            throw new UnsupportedOperationException("unimplemented");
//        }
//
//        @Override
//        public float getFloat(int column)
//        {
//            throw new UnsupportedOperationException("unimplemented");
//        }
//
//        @Override
//        public double getDouble(int column)
//        {
//            throw new UnsupportedOperationException("unimplemented");
//        }
//
//        @Override
//        public boolean isNull(int column)
//        {
//            return false;
//        }
//    }
}