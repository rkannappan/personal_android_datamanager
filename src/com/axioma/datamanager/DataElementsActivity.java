package com.axioma.datamanager;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DataElementsActivity extends Activity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_data_elements);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.data_elements, menu);
      return true;
   }

}
