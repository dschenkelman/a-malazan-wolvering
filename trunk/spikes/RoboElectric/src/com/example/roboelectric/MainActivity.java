package com.example.roboelectric;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

 Button login;
 EditText name;
 EditText password;

 @Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);

  login = (Button) findViewById(R.id.login);
  name = (EditText) findViewById(R.id.name);
  password = (EditText) findViewById(R.id.password);

  login.setOnClickListener(new OnClickListener() {

   @Override
   public void onClick(View v) {
    Intent home = new Intent(MainActivity.this, Home.class);
    startActivity(home);
   }
  });

 }

 @Override
 public boolean onCreateOptionsMenu(Menu menu) {
  // Inflate the menu; this adds items to the action bar if it is present.
  getMenuInflater().inflate(R.menu.main, menu);
  return true;
 }

}