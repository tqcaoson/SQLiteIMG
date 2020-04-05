package com.example.appthuvienanh;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText edtTenAnh, edtMoTa;
    Button btnThem, btnSua;
    ListView lvAnh;
    int vitri = -1;
    ArrayList<Anh> arrayAnh;
    AnhAdapter adapter;
    public static Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtTenAnh = (EditText) findViewById(R.id.editTextTenAnh);
        edtMoTa = (EditText) findViewById(R.id.editTextMoTa);
//        btnSua = (Button) findViewById(R.id.buttonSua);
        btnThem = (Button) findViewById(R.id.buttonThemAnh);
        lvAnh = (ListView) findViewById(R.id.listViewAnh);
        arrayAnh = new ArrayList<>();

        adapter = new AnhAdapter(this, R.layout.dong_anh, arrayAnh);
        lvAnh.setAdapter(adapter);

        database = new Database(this, "QuanLy.sqlite", null, 1);

        database.QueryData("CREATE TABLE IF NOT EXISTS Anh(Id INTEGER PRIMARY KEY AUTOINCREMENT, Ten VARCHAR(150), MoTa VARCHAR(250), HinhAnh BLOB)");

        final Cursor cursor = database.GetData("SELECT * FROM Anh");
        while (cursor.moveToNext()){
            arrayAnh.add(new Anh(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getBlob(3)
            ));
        }

        lvAnh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogCapNhat(arrayAnh.get(position).getId());
            }
        });

        lvAnh.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                XacNhanXoa(arrayAnh.get(position).getId());
                return false;
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ThemAnhActivity.class ));
            }
        });
    }

    private void DialogCapNhat(final int id){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom);
        dialog.setCanceledOnTouchOutside(false);

        final EditText edtTenAnh = (EditText) dialog.findViewById(R.id.editTextTenAnh);
        final EditText edtMoTa = (EditText) dialog.findViewById(R.id.editTextMoTa);
        Button btnDangNhap = (Button) dialog.findViewById(R.id.buttonCapNhat);
        Button btnHuy = (Button) dialog.findViewById(R.id.buttonHuy);

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenanh = edtTenAnh.getText().toString().trim();
                String mota = edtMoTa.getText().toString().trim();


                if (tenanh.isEmpty() && mota.isEmpty()){
                    Toast.makeText(MainActivity.this, "Bại không được để trống.", Toast.LENGTH_SHORT).show();
                }else {
                    database.QueryData("UPDATE Anh SET Ten = '"+ tenanh +"', MoTa = '"+ mota +"' WHERE Id = '"+ id +"'");
                    arrayAnh.clear();
                    final Cursor cursor = database.GetData("SELECT * FROM Anh");
                    while (cursor.moveToNext()){
                        arrayAnh.add(new Anh(
                                cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getBlob(3)
                        ));
                    }
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Đã cập nhật.", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
//                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void XacNhanXoa(final int id){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Thông báo !");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setMessage("Bạn có muốn xóa ảnh này không ?");

        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.QueryData("DELETE FROM Anh WHERE Id = '"+ id +"'");
                arrayAnh.clear();
                final Cursor cursor = database.GetData("SELECT * FROM Anh");
                while (cursor.moveToNext()){
                    arrayAnh.add(new Anh(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getBlob(3)
                    ));
                }
                adapter.notifyDataSetChanged();
            }
        });

        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();
    }
}
