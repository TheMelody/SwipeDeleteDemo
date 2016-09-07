package org.devloper.melody.swipedeletedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity  extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_cancle).setOnClickListener(this);
        findViewById(R.id.tv_remove).setOnClickListener(this);
        findViewById(R.id.tv_content).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tv_cancle){
            Toast.makeText(v.getContext(),"我是cancle",Toast.LENGTH_SHORT).show();
        }else if(v.getId()==R.id.tv_remove){
            Toast.makeText(v.getContext(),"我是remove",Toast.LENGTH_SHORT).show();
        }else if(v.getId()==R.id.tv_content){
            Toast.makeText(v.getContext(),"我是内容",Toast.LENGTH_SHORT).show();
        }
    }
}