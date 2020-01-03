package com.example.itpmappsecond;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.itpmappsecond.databases.ITPMDataOpenHelper;
import com.example.itpmappsecond.pojo.TitleDataItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
//    private ArrayAdapter<String> mAdapter;
    private MainListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 1.ListViewをレイアウトファイルから読み込む
        mListView = findViewById(R.id.main_list);

        // 2.Adapterを作成する
//        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        mAdapter = new MainListAdapter(this, R.layout.layout_title_item, new ArrayList<TitleDataItem>());

        // 3.ListViewにAdapterをセットする
        mListView.setAdapter(mAdapter);

        // 通常のクリックリスナーのセット
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TitleDataItem item =(TitleDataItem)adapterView.getItemAtPosition(position);
                Intent intent = EditActivity.createIntent(MainActivity.this,item.getId(), item.getTitle());
//                Log.d(MainActivity.class.getSimpleName(), position + "番目のリストアイテムが押されました");
//                String title = String.valueOf(adapterView.getItemAtPosition(position));
//                Intent intent = EditActivity.createIntent(MainActivity.this, title);
                startActivity(intent);
            }
        });

        // 長押しでのクリックリスナーのセット
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                TitleDataItem item = (TitleDataItem)adapterView.getItemAtPosition(position);
//                mAdapter.remove(item);
//                Log.d(MainActivity.class.getSimpleName(), position + "番目のリストアイテムが長押しされました");
//                String title = String.valueOf(adapterView.getItemAtPosition(position));
//                mAdapter.remove(title);
//                Toast.makeText(MainActivity.this, title +"を削除しました", Toast.LENGTH_SHORT).show();
//                return false;

                // 1.データベースのインスタンスを取得する
                SQLiteDatabase db = new ITPMDataOpenHelper(MainActivity.this).getWritableDatabase();

                // 2.データベースからデータを削除する処理
                db.delete(ITPMDataOpenHelper.TABLE_NAME, ITPMDataOpenHelper._ID + "=" + item.getId(), null);

                // 3.データベースを閉じる
                db.close();

                // 4.画面表示を更新する
//                displayDataList();
                new AllDataLoadTask().execute();
                Toast.makeText(MainActivity.this, item.getTitle() + "を削除しました", Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            // タップされた際の処理
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);

            }

        });
    }

    // リスト表示用メソッド
    private void displayDataList(List<TitleDataItem > titleDataItems){

        // 1.アダプター内のデータをリセットする
        mAdapter.clear();

//        // 2.新しいデータをアダプターに設定する(セットする)
//        List<String> dataList =  Arrays.asList("ホーム", "事業内容", "企業情報", "採用情報", "お問い合わせ");
//        List<TitleDataItem> dataList = Arrays.asList(
//                    new TitleDataItem(1, "ホーム"),
//                    new TitleDataItem(2, "事業内容"),
//                    new TitleDataItem(3, "企業情報"),
//                    new TitleDataItem(4, "採用情報"),
//                    new TitleDataItem(5, "お問い合わせ")
//                    );
//        // 2.表示に使うデータを入れるリスト
//        List<TitleDataItem> itemList = new ArrayList<>();
//
//        // 3.読み下記用のデータベースインスタンスを取得する
//        SQLiteDatabase db = new ITPMDataOpenHelper(this).getWritableDatabase();
//
//        // 4.データベースから欲しいデータのカーソルを取り出す処理
//        Cursor cursor = db.query(
//                ITPMDataOpenHelper.TABLE_NAME,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null
//        );
//
//        // 5.カーソルを使ってデータを取り出す処理
//        while (cursor.moveToNext()){
//            int id = cursor.getInt(cursor.getColumnIndex(ITPMDataOpenHelper._ID));
//            String title = cursor.getString(cursor.getColumnIndex(ITPMDataOpenHelper.COLUMN_TITLE));
//            itemList.add(new TitleDataItem(id, title));
//        }
//
//        // 6.カーソルを閉じる
//        cursor.close();
//
//        // 7.データベースを閉じる
//        db.close();

        // 8.新しいデータをアダプターに設定する(セットする)
//        mAdapter.addAll(dataList);
//        mAdapter.addAll(itemList);
        mAdapter.addAll(titleDataItems);

        // 9.アダプターにデータが変更されたことを通知する
        mAdapter.notifyDataSetChanged();

    }


    private class MainListAdapter extends ArrayAdapter<TitleDataItem> {

        private LayoutInflater layoutInflater;
        private int resource;
        private List<TitleDataItem> dataList;

        public MainListAdapter(@NonNull Context context, int resource, @NonNull List<TitleDataItem> objects) {
            super(context, resource, objects);
            layoutInflater = LayoutInflater.from(context);
            this.resource = resource;
            dataList = objects;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        public TitleDataItem getItem(int position){
            return dataList.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){

            TitleDataItem item = dataList.get(position);
            TextView titleTextView ;
            if(convertView == null){
                // 新規
                convertView = layoutInflater.inflate(resource, null );
                titleTextView = convertView.findViewById(R.id.titleTextView);
                convertView.setTag(titleTextView);
            } else {
                titleTextView = (TextView) convertView.getTag();
            }
            titleTextView.setText(item.getTitle());

            return convertView;

        }

    }

        @Override
    protected void onResume() {
        super.onResume();
//        displayDataList();
          new AllDataLoadTask().execute();
    }

    private class AllDataLoadTask extends AsyncTask<Void, Void, List<TitleDataItem>> {

//        private OnAllDataLoadListener listener;

        @Override
        protected List<TitleDataItem> doInBackground(Void... voids) {

            // 表示に使うデータを入れるリスト
            List<TitleDataItem> itemList = new ArrayList<>();


            // 読み書き用のデータベースのインスタンスを取得する
            SQLiteDatabase db = new ITPMDataOpenHelper(MainActivity.this ).getWritableDatabase();

            // データベースから欲しいデータのカーソルを取り出す処理
            Cursor cursor = db.query(
                    ITPMDataOpenHelper.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            // カーソルを使ってデータを取り出す処理
            while (cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndex(ITPMDataOpenHelper._ID));
                String title = cursor.getString(cursor.getColumnIndex(ITPMDataOpenHelper.COLUMN_TITLE));
                itemList.add(new TitleDataItem(id,title));
            }

            // カーソルを閉じる
            cursor.close();

            // データベースを閉じる
            db.close();
            return itemList;
        }

        @Override
        protected void onPostExecute(List<TitleDataItem> titleDataItems) {

            // 画面表示の更新
            displayDataList(titleDataItems);
        }
    }
}
