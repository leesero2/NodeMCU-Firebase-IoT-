package com.example.firebaseproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ListViewAutoScrollHelper;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ImageView imgSet;
    double discomfortIndex = 0;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef = db.getReference("logDHT");
    //DatabaseReference myRef1 = db.getReference (temperature);
    Button button;
    TextView textView, tv, texttime , textV, textA , indexText, timeText,humiText,tempText;
    EditText editText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView myWebView = findViewById(R.id.WebView);   //웹뷰 생성 소스
        myWebView.setWebViewClient(new WebViewClient());

        WebSettings myWebsettings = myWebView.getSettings();   //웹뷰 생성 소스
        myWebsettings.setJavaScriptEnabled(true);

        myWebView.loadUrl("http://huny10000.dothome.co.kr/");  // 닷홈 링크


        setTitle("Hello Firebase");
        imgSet = (ImageView) findViewById(R.id.imgset);

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Map<String, Object> dataObj = new HashMap<>();      // : HashMap은 Map을 구현한다. Key와 value를 묶어 하나의 entry로 저장한다는 특징을 갖는다.
                // 그리고 hashing을 사용하기 때문에 많은양의 데이터를 검색하는데 뛰어난 성능을 보인다.
                dataObj = (Map<String, Object>) dataSnapshot.getValue();

                String data = dataObj.get("humidity").toString();
                textView.setText("humi : " + data + "%" + "\n");

                String data1 = dataObj.get("temperature").toString();
                tv.setText("temp : " + data1 + "°C" + "\n");

                String data2 = dataObj.get("time").toString();
                texttime.setText("time : " + data2 + "\n");

                String data3 = dataObj.get("index").toString();
                textV.setText("불쾌지수: " + data3 + "\n");



            double a2 = Double.parseDouble(data3); // String 을 정수형으로 갖고오기위해 해당 함수를 이용해 변환
                    //불쾌지수 계산
                    if (a2 > 0 && a2 < 67) {
                        textA.setText("불쾌지수 낮음");
                        imgSet.setImageResource(R.drawable.lovee); // 해당 이미지 변환
                    }else if (a2 > 68 && a2 <74) {
                        textA.setText("불쾌지수 보통");
                        imgSet.setImageResource(R.drawable.goodd); // 해당 이미지 변환
                    }else if (a2 > 75 && a2 < 79) {
                        textA.setText("불쾌감을 나타내기 시작합니다");
                        imgSet.setImageResource(R.drawable.sosoo); // 헤당 이미지 변환
                    }else if (a2 > 80 ) {
                    textA.setText("불쾌지수 매우 나쁨");
                    imgSet.setImageResource(R.drawable.badd); // 해당 이미지 변환
                }

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });

        final Button dataButton = (Button) findViewById(R.id.button); //리스트뷰로 화면을 전환하는 버튼 객체

        dataButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, Main2Activity.class);
                MainActivity.this.startActivity(registerIntent);
                finish();
            }
        });

        textView = (TextView) findViewById(R.id.textView);
        tv = (TextView) findViewById(R.id.textView5);
        texttime = (TextView) findViewById(R.id.texttime);
        textV = (TextView) findViewById(R.id.textV);
        textA = (TextView) findViewById(R.id.textA);

    }


    public void get_btn_Click(View view) {

    }

}