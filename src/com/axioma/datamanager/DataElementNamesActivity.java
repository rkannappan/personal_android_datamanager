package com.axioma.datamanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedSet;

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
import com.google.common.collect.Sets;

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
      ArrayList<HashMap<String, String>> namesList = new ArrayList<HashMap<String, String>>();

      // This is a quick hack to remove all the internal elements
      String[] jsons = results.split("~");
      SortedSet<String> elementNames = this.getNames(jsons[0]);
      if (jsons.length == 2) {
         SortedSet<String> internalNames = this.getNames(jsons[1]);
         elementNames.removeAll(internalNames);
      }

      for (String name : elementNames) {
         // creating new HashMap
         HashMap<String, String> map = new HashMap<String, String>();

         // adding each child node to HashMap key => value
         map.put(DataElementNamesActivity.ELEMENT_NAME, name);

         namesList.add(map);
      }

      ListView lv = getListView();
      TextView tv = new TextView(this);
      tv.setText("\n" + "   Number of " + this.dataElementType + " - " + namesList.size() + "\n");
      lv.addHeaderView(tv);

      /**
       * Updating parsed JSON data into ListView
       * */
      ListAdapter adapter =
               new SimpleAdapter(this, namesList, R.layout.list_item,
                        new String[] { DataElementNamesActivity.ELEMENT_NAME }, new int[] { R.id.name });

      setListAdapter(adapter);

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

   private SortedSet<String> getNames(final String json) {
      SortedSet<String> elementNames = Sets.newTreeSet();

      // try parse the string to a JSON object
      try {
         JSONArray names = new JSONArray(json);

         for (int i = 0; i < names.length(); i++) {
            String name = (String) names.get(i);
            elementNames.add(name);
         }
      } catch (JSONException e) {
         Log.e("JSON Parser", "Error parsing data " + e.toString());
      }

      return elementNames;
   }
}