package com.hog2020.ex86firebasestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.local.QueryResult;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView tv;

    //Firebase Cloud Store: 실시간 DB 처럼 DB 목적이긴 하지만 실시간 리스너는 아닌 방식

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=findViewById(R.id.tv);
    }

    public void clicksave(View view) {
        //Firestore DB 에 저장 [Map collection 단위로 저장]
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
//        Map<String, Object> user = new HashMap<>();
//        user.put("name","sam");
//        user.put("age",20);
//        user.put("address","seoul");
        Persons p = new Persons("Sam","seoul",20);
        Map<String,Persons> user = new HashMap<>();
        user.put("person",p);

        //DocumentReference id = firebaseFirestore.collection("Name").document("persons");


        CollectionReference userRef = firebaseFirestore.collection("person");
        Task task = userRef.add(user);
        task.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void clickload(View view) {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference userRef = firebaseFirestore.collection("users");

        Task<QuerySnapshot> task =userRef.get();
        task.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot snapshots = task.getResult();

                for(QueryDocumentSnapshot snapshot : snapshots){
                    Map<String, Object> user = snapshot.getData();
                    String name= user.get("name").toString();
                    int age= Integer.parseInt(user.get("age").toString());
                    String address= user.get("address").toString();

                    tv.append(name+","+age+","+address);
                }
            }
        });
    }
}