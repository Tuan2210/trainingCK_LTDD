package com.example.training_ktck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class QuanLy_Activity extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference reference;

    private ThongTinChung thongTinChung;
    private Account account;
    private Book book;

    private ArrayList<String> arrayList=new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;

    private DatabaseReference ref;
    private SQLiteDBHandler db;

    EditText edtTenSach, edtTheLoai, edtGia;
    Button btnSignout, btnLuu, btnXoa, btnCapNhat, btnHuy;
    ListView lvItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly);

        edtTenSach = findViewById(R.id.edtTenSach);
        edtTheLoai = findViewById(R.id.edtTheLoai);
        edtGia = findViewById(R.id.edtGia);

        btnLuu = findViewById(R.id.btnLuu);
        btnXoa = findViewById(R.id.btnXoa);
        btnCapNhat = findViewById(R.id.btnCapNhat);
        btnHuy = findViewById(R.id.btnHuy);
        btnSignout = findViewById(R.id.btnSignout);

        lvItem = findViewById(R.id.lvItem);

        auth = FirebaseAuth.getInstance();
        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangXuat();
            }
        });

        db = new SQLiteDBHandler(getApplicationContext());
        showDataLV(); //c?? h??m show listview v?? update data

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertBook();
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBook();
            }
        });
    }

    private void dangXuat() {
        auth.signOut();
        startActivity(new Intent(QuanLy_Activity.this, Login_MainActivity.class));
        Toast.makeText(this, "????ng xu???t th??nh c??ng", Toast.LENGTH_SHORT).show();
    }

    private void showDataLV() {
        ref = FirebaseDatabase.getInstance().getReference("Th??ng tin chung")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("C??c th??ng tin s??ch");

        arrayAdapter=new ArrayAdapter<String>(this, R.layout.item, R.id.tvInfoBook, arrayList);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //String ngayKham = dataSnapshot.getKey();
                    book = dataSnapshot.getValue(Book.class);
                    String ten = ""+book.getTenSach(),
                            theloai = ""+book.getTheLoai(),
                            gia = ""+book.getGia();

                    String infoBook = "T??n s??ch: "+ten +"   "
                                    +"\nTh??? lo???i: " +theloai+"   "
                                    +"\nGi??: " +gia +"    ";
                    arrayList.removeAll(Collections.singleton(infoBook));
                    arrayList.add(infoBook);

                    //java.util.Collections.reverse(arrayList); //sort descending; firebase auto ascending

                    String checkBook = "" +infoBook;
                    Log.d("CHECK book: ", checkBook);

                    chooseInfoToUpdate(arrayList);
                }
                lvItem.setAdapter(arrayAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void insertBook() {
        String txtTenSach = edtTenSach.getText().toString().trim(),
                txtTheLoai = edtTheLoai.getText().toString().trim(),
                txtGia = edtGia.getText().toString().trim().concat("??");

        if(txtTenSach.equals("") || txtTheLoai.equals("") || txtGia.equals(""))
            Toast.makeText(this, "Vui l??ng nh???p v??o ch??? tr???ng", Toast.LENGTH_SHORT).show();
        else{
            //realtime db
            book = new Book(txtTenSach, txtTheLoai, txtGia);
            ref = FirebaseDatabase.getInstance().getReference("Th??ng tin chung");
            ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("C??c th??ng tin s??ch")
                    .child(""+book.getTenSach()).setValue(book);

            //sqlite db
            ref = FirebaseDatabase.getInstance().getReference("Th??ng tin chung")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("T??i kho???n");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Account acc = snapshot.getValue(Account.class);
                        account = new Account(acc.getEmail());

                        book = new Book(txtTenSach, txtTheLoai, txtGia);

                        thongTinChung = new ThongTinChung(account, book);
                        db.addBook(thongTinChung);
                    //}
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            Toast.makeText(this, "L??u th??nh c??ng", Toast.LENGTH_SHORT).show();
            resetAll();
        }
    }

    private void deleteBook() {
        String txtTenSachXoa = edtTenSach.getText().toString().trim();
        if(txtTenSachXoa.equals(""))
            Toast.makeText(QuanLy_Activity.this, "Nh???p t??n s??ch c???n x??a", Toast.LENGTH_SHORT).show();
        else {
            //String txtTenSachLV = "T??n s??ch: " +txtTenSachXoa.substring(0, 5); //l???y k?? t??? 0 ?????n 4 ??? edtTenSach

            ref.child("" +txtTenSachXoa).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        //delete sqliteDB
                        db.deleteBookInfo(new ThongTinChung(new Book(txtTenSachXoa)));

                        Toast.makeText(QuanLy_Activity.this, "X??a th??nh c??ng", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //reload activity
            finish();
            startActivity(getIntent());
            resetAll();
        }
    }

    private void chooseInfoToUpdate(ArrayList<String> arrayList) {
        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String getInfoLV = adapterView.getItemAtPosition(i).toString(),
                        getTenSachLV = getInfoLV.substring(10, 15); //l???y 1 ??o???n chu???i t??? k?? t??? 10 - 14
                edtTenSach.setText(getTenSachLV);
                edtTenSach.setEnabled(false);
                edtTheLoai.requestFocus();
                        //getInfoSQLite = getInfo.substring(11, 34);

                ref = FirebaseDatabase.getInstance().getReference("Th??ng tin chung")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("C??c th??ng tin s??ch");

                //c???p nh???t
                if(arrayList.contains(getInfoLV) == true){ //check getInfo c?? trong arrayList hay ko
                    btnCapNhat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String txtTenSach = edtTenSach.getText().toString().trim(),
                                    txtTheLoai = edtTheLoai.getText().toString().trim(),
                                    txtGia = edtGia.getText().toString().trim().concat("??");

                            if (txtTenSach.equals("") || txtTheLoai.equals("") || txtGia.equals(""))
                                Toast.makeText(QuanLy_Activity.this, "Vui l??ng nh???p v??o ch??? tr???ng", Toast.LENGTH_SHORT).show();
                            else {
                                book = new Book(txtTenSach, txtTheLoai, txtGia);

                                // update realtimeDB
                                ref = FirebaseDatabase.getInstance().getReference("Th??ng tin chung");
                                ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("C??c th??ng tin s??ch")
                                        .child(""+book.getTenSach()).setValue(book);

                                //update sqliteDB
                                db.updateBookInfo(new ThongTinChung(new Book(txtTenSach, txtTheLoai, txtGia)));

                                Toast.makeText(QuanLy_Activity.this, "C???p nh???t th??nh c??ng", Toast.LENGTH_SHORT).show();
                                resetAll();

                                //reload activity
                                finish();
                                startActivity(getIntent());
                            }
                        }
                    });

                    String log = "" + getInfoLV;
                    Log.d("check get 1 info: ", log);
                    Toast.makeText(QuanLy_Activity.this, ""+getInfoLV, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void resetAll() {
        edtTenSach.requestFocus();
        edtTenSach.setText("");
        edtTheLoai.setText("");
        edtGia.setText("");
    }
}