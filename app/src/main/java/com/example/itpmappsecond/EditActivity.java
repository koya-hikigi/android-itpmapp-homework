package com.example.itpmappsecond;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.itpmappsecond.databases.ITPMDataOpenHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    private static final String KEY_ID = "key_id";
    private static final String KEY_TITLE = "key_title";
    private EditText mTitleEditText;
    private int selectId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitleEditText = findViewById(R.id.titleEditText);

        selectId = getIntent().getIntExtra(KEY_ID, -1);
        ActionBar actionBar = getSupportActionBar();
        if(selectId == -1 && actionBar != null){
            actionBar.setTitle(R.string.a_new);
        }else if(actionBar != null){
            actionBar.setTitle(R.string.edit);
        }

        //        Log.d(EditActivity.class.getSimpleName(), "取得したID: " + id);

        String title = getIntent().getStringExtra(KEY_TITLE);
        if (title != null){
            mTitleEditText.setText(title);
        } else {
            mTitleEditText.setText("");
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1.タイトルに項目が入力されているかチェックする
                String title = mTitleEditText.getText().toString();
                if(title.isEmpty()){
                    // タイトルが未入力の場合
                    Toast toast = Toast.makeText(EditActivity.this,
                            getString(R.string.error_no_title),
                            Toast.LENGTH_SHORT
                    );
                    toast.show();
                } else {
                    // タイトルが入力されている場合
                    if (selectId == -1) {
                        // タイトルが入力されている場合
                        // データベースに新規のデータを追加する
                        // 1.ContentValuesのインスタンスに追加したいデータを入れる
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(ITPMDataOpenHelper.COLUMN_TITLE, mTitleEditText.getText().toString());

                        // 2.SQLiteDatabaseのインスタンスを用意する
                        SQLiteDatabase db = new ITPMDataOpenHelper(EditActivity.this).getWritableDatabase();

                        // 3.SQLiteDatabaseインスタンスの「insert」メソッドを利用して
                        //   ContentValuesのデータをデータベースに追加する
                        db.insert(ITPMDataOpenHelper.TABLE_NAME, null, contentValues);

                        // 4.データベースを閉じる
                        db.close();
                        finish();
                    } else {
                        // データベースのデータを更新する
                        // 1.ContentValuesのインスタンスに変更したい行のプライマリーキーとデータを入れる

                        // 2.SQLiteDatabaseのインスタンスを用意する
                        // 3.SQLiteDatabaseインスタンスの「update」メソッドを利用してContentValuesの
                        //   データでデータベースの内容を更新する
                        // 4.
                    }
                }
            }
        });
    }

    public static Intent createIntent(Context context,int id, String title){
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(KEY_ID, id);
        intent.putExtra(KEY_TITLE, title);
        return intent;
    }

}
