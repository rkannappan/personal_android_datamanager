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
import android.widget.Toast;

import com.axioma.datamanager.async.AsyncCallback;
import com.axioma.datamanager.async.GetDataElementDatesInBackground;

/**
 * @author rkannappan
 */
public class DataElementDatesActivity extends ListActivity implements AsyncCallback {

   private static final String ELEMENT_DATE = "date";

   private String dataElementType;
   private String dataElementName;

   public final static String SELECTED_DATA_ELEMENT_DATE = "com.axioma.datamanager.selected_data_element_date";

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

      try {
         JSONArray dates = new JSONArray(results);

         for (int i = 0; i < dates.length(); i++) {
            String date = (String) dates.get(i);

            HashMap<String, String> map = new HashMap<String, String>();

            // adding each child node to HashMap key => value
            map.put(DataElementDatesActivity.ELEMENT_DATE, date);

            attributeDatesList.add(map);
         }
      } catch (JSONException e) {
         Log.e("JSON Parser", "Error parsing data " + e.toString());
      }

      ListView lv = getListView();
      TextView tv = new TextView(this);
      StringBuilder sb = new StringBuilder();
      sb.append("\n");
      sb.append("   Type: ").append(this.dataElementType).append("\n");
      sb.append("   Name: ").append(this.dataElementName).append("\n");
      sb.append("   Start Date: ").append(attributeDatesList.get(0).get(DataElementDatesActivity.ELEMENT_DATE)).append("\n");
      sb.append("   End Date:   ")
               .append(attributeDatesList.get(attributeDatesList.size() - 1).get(DataElementDatesActivity.ELEMENT_DATE))
               .append("\n");
      sb.append("   Number of Dates: ").append(attributeDatesList.size()).append("\n");
      tv.setText(sb.toString());
      lv.addHeaderView(tv);

      /**
       * Updating parsed JSON data into ListView
       * */
      ListAdapter adapter =
               new SimpleAdapter(this, attributeDatesList, R.layout.list_item,
                        new String[] { DataElementDatesActivity.ELEMENT_DATE }, new int[] { R.id.name });

      setListAdapter(adapter);

      // Launching new screen on Selecting Single ListItem
      lv.setOnItemClickListener(new OnItemClickListener() {

         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // We don't support getting full data for FRM.
            if (DataElementsActivity.FACTOR_RISK_MODELS.equals(dataElementType)
                     || DataElementsActivity.CLASSIFICATION_SCHEMES.equals(dataElementType)
                     || DataElementsActivity.CLASSIFICATIONS.equals(dataElementType)) {
               Toast.makeText(getApplicationContext(), "Full Data View is not supported for " + dataElementType,
                        Toast.LENGTH_SHORT).show();
               return;
            }

            // getting values from selected ListItem
            String dataElementDate = ((TextView) view.findViewById(R.id.name)).getText().toString();

            System.out.println("date is " + dataElementDate);

            Intent intent = new Intent(getApplicationContext(), DataElementFullAttributeActivity.class);
            intent.putExtra(DataElementNamesActivity.SELECTED_DATA_ELEMENT_TYPE, dataElementType);
            intent.putExtra(DataElementNamesActivity.SELECTED_DATA_ELEMENT_NAME, dataElementName);
            intent.putExtra(DataElementDatesActivity.SELECTED_DATA_ELEMENT_DATE, dataElementDate);
            startActivity(intent);
         }
      });
   }
}