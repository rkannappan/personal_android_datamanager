package com.axioma.datamanager;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.axioma.datamanager.async.AsyncCallback;
import com.axioma.datamanager.async.GetDataElementNamesInBackground;

/**
 * @author rkannappan
 */
public class DataElementNamesActivity extends ListActivity implements AsyncCallback {

   private static final String ATTR_NAME = "name";

   //   public final static String DATES_RESULTS_MESSAGE = "com.example.myfirstapp.DATES_RESULTS";
   //   public final static String SELECTED_ATTR_NAME = "com.example.myfirstapp.SELECTED_ATTR_NAME";

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      Intent intent = getIntent();
      String dataElement = intent.getStringExtra(DataElementsActivity.SELECTED_DATA_ELEMENT_NAME);
      System.out.println(dataElement);

      new GetDataElementNamesInBackground(DataElementNamesActivity.this, dataElement, this).execute();
   }

   @Override
   public void postProcessing(String results) {
      ArrayList<HashMap<String, String>> attributeNamesList = new ArrayList<HashMap<String, String>>();

      // try parse the string to a JSON object
      try {
         JSONArray names = new JSONArray(results);

         for (int i = 0; i < names.length(); i++) {
            String name = (String) names.get(i);

            // creating new HashMap
            HashMap<String, String> map = new HashMap<String, String>();

            // adding each child node to HashMap key => value
            map.put(DataElementNamesActivity.ATTR_NAME, name);

            attributeNamesList.add(map);
         }
      } catch (JSONException e) {
         Log.e("JSON Parser", "Error parsing data " + e.toString());
      }

        /**
       * Updating parsed JSON data into ListView
       * */
      ListAdapter adapter =
               new SimpleAdapter(this, attributeNamesList, R.layout.list_item,
                        new String[] { DataElementNamesActivity.ATTR_NAME }, new int[] { R.id.name });

      setListAdapter(adapter);

      // selecting single ListView item
      ListView lv = getListView();

      // Launching new screen on Selecting Single ListItem
      lv.setOnItemClickListener(new OnItemClickListener() {

         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //            // getting values from selected ListItem
            //            String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
            //            //                String name = ((ListView) view.findViewById(android.R.id.list)).getSelectedItem().toString();
            //
            //            System.out.println("name is " + name);
            //
            //            Intent intent = new Intent(getApplicationContext(), AttributeDatesListActivity.class);
            //            intent.putExtra(AttributeNamesListActivity.SELECTED_ATTR_NAME, name);
            //            startActivity(intent);
         }
      });
   }
}