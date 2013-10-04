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
import com.axioma.datamanager.async.GetDataElementDatesInBackground;

/**
 * @author rkannappan
 */
public class DataElementDatesActivity extends ListActivity implements AsyncCallback {

   private static final String ELEMENT_DATE = "date";

   private String dataElementType;
   private String dataElementName;

   //   public final static String SELECTED_ATTR_DATE = "com.example.myfirstapp.SELECTED_ATTR_DATE";

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      //        setContentView(R.layout.pure_list_item);

      // Get the message from the intent
      Intent intent = getIntent();
      String dataElementType = intent.getStringExtra(DataElementNamesActivity.SELECTED_DATA_ELEMENT_TYPE);
      String dataElementName = intent.getStringExtra(DataElementNamesActivity.SELECTED_DATA_ELEMENT_NAME);

      this.dataElementType = dataElementType;
      this.dataElementName = dataElementName;

      new GetDataElementDatesInBackground(DataElementDatesActivity.this, dataElementType, dataElementName, this).execute();
   }

   @Override
   public void postProcessing(final String results) {
      ArrayList<HashMap<String, String>> attributeDatesList = new ArrayList<HashMap<String, String>>();

      // try parse the string to a JSON object
      try {
         JSONArray dates = new JSONArray(results);

         for (int i = 0; i < dates.length(); i++) {
            String date = (String) dates.get(i);

            // creating new HashMap
            HashMap<String, String> map = new HashMap<String, String>();

            // adding each child node to HashMap key => value
            map.put(DataElementDatesActivity.ELEMENT_DATE, date);

            attributeDatesList.add(map);
         }
      } catch (JSONException e) {
         Log.e("JSON Parser", "Error parsing data " + e.toString());
      }

      /**
       * Updating parsed JSON data into ListView
       * */
      ListAdapter adapter =
               new SimpleAdapter(this, attributeDatesList, R.layout.list_item,
                        new String[] { DataElementDatesActivity.ELEMENT_DATE }, new int[] { R.id.name });

      setListAdapter(adapter);

      // selecting single ListView item
      ListView lv = getListView();

      // Launching new screen on Selecting Single ListItem
      lv.setOnItemClickListener(new OnItemClickListener() {

         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //            // getting values from selected ListItem
            //            String date = ((TextView) view.findViewById(R.id.name)).getText().toString();
            //            //                String date = ((ListView) view.findViewById(android.R.id.list)).getSelectedItem().toString();               
            //
            //            System.out.println("date is " + date);
            //
            //            Intent intent = new Intent(getApplicationContext(), AttributeValuesListActivity.class);
            //            intent.putExtra(AttributeNamesListActivity.SELECTED_ATTR_NAME, attrName);
            //            intent.putExtra(DataElementDatesActivity.SELECTED_ATTR_DATE, date);
            //            startActivity(intent);
         }
      });
   }
}