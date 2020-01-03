package com.example.itpmappsecond;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.itpmappsecond.databases.ITPMDataOpenHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    private static final String KEY_TITLE = "key_title";
    private EditText mTitleEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitleEditText = findViewById(R.id.titleEditText);

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

                }

                // 2.データベースに新規のデータを通知する


                // 3.画面を終了する
                finish();
            }
        });
    }

    public static Intent createIntent(Context context, String title){
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(KEY_TITLE, title);
        return intent;
    }

}
