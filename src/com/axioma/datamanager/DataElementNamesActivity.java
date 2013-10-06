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
import android.widget.TextView;

import com.axioma.datamanager.async.AsyncCallback;
import com.axioma.datamanager.async.GetDataElementNamesInBackground;

/**
 * @author rkannappan
 */
public class DataElementNamesActivity extends ListActivity implements AsyncCallback {

   private static final String ELEMENT_NAME = "name";

   private String dataElementType = null;

   public final static String SELECTED_DATA_ELEMENT_TYPE = "com.axioma.datamanager.selected_data_element_type";
   public final static String SELECTED_DATA_ELEMENT_NAME = "com.axioma.datamanager.selected_data_element_name";

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      Intent intent = getIntent();
      String dataElementType = intent.getStringExtra(DataElementsActivity.SELECTED_DATA_ELEMENT_TYPE);
      System.out.println(dataElementType);

      this.dataElementType = dataElementType;

      new GetDataElementNamesInBackground(DataElementNamesActivity.this, dataElementType, this).execute();
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
            map.put(DataElementNamesActivity.ELEMENT_NAME, name);

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
                        new String[] { DataElementNamesActivity.ELEMENT_NAME }, new int[] { R.id.name });

      setListAdapter(adapter);

      // selecting single ListView item
      ListView lv = getListView();

      // Launching new screen on Selecting Single ListItem
      lv.setOnItemClickListener(new OnItemClickListener() {

         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // getting values from selected ListItem
            String dataElementName = ((TextView) view.findViewById(R.id.name)).getText().toString();
            //                String name = ((ListView) view.findViewById(android.R.id.list)).getSelectedItem().toString();

            System.out.println("dataElementName is " + dataElementName);

            Intent intent = new Intent(getApplicationContext(), DataElementDatesActivity.class);
            intent.putExtra(DataElementNamesActivity.SELECTED_DATA_ELEMENT_TYPE, dataElementType);
            intent.putExtra(DataElementNamesActivity.SELECTED_DATA_ELEMENT_NAME, dataElementName);
            startActivity(intent);
         }
      });
   }
}