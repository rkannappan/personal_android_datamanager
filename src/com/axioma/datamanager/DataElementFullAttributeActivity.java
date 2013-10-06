package com.axioma.datamanager;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.axioma.datamanager.async.AsyncCallback;
import com.axioma.datamanager.async.GetAttributeDataInBackground;

/**
 * @author rkannappan
 */
public class DataElementFullAttributeActivity extends Activity implements AsyncCallback {

   private String dataElementType;

   private static final String asset = "asset";

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      // Get the message from the intent
      Intent intent = getIntent();
      String type = intent.getStringExtra(DataElementNamesActivity.SELECTED_DATA_ELEMENT_TYPE);
      String name = intent.getStringExtra(DataElementNamesActivity.SELECTED_DATA_ELEMENT_NAME);
      String date = intent.getStringExtra(DataElementDatesActivity.SELECTED_DATA_ELEMENT_DATE);

      System.out.println("type in intent " + type);
      System.out.println("name in intent " + name);
      System.out.println("date in intent " + date);

      this.dataElementType = type;

      new GetAttributeDataInBackground(DataElementFullAttributeActivity.this, type, name, date, this).execute();
   }

   @Override
   public void postProcessing(String results) {
      String[] assetValues = null;

      // try parse the string to a JSON object
      try {
         JSONObject attribute = new JSONObject(results);
         
         if (DataElementsActivity.PORTFOLIOS.equals(this.dataElementType)) {
            attribute = attribute.getJSONObject("holdings");
         }

         String unitType = "NUMBER"; // For currency attributes
         if (!DataElementsActivity.CURRENCY_ATTRIBUTES.equals(this.dataElementType)) {
            unitType = attribute.getString("unitType");
            System.out.println(unitType);
         }

         JSONObject values = attribute.getJSONObject("values");

         boolean currencyValueAttribute = isCurrencyValueAttribute(unitType);
         if (currencyValueAttribute) {
            assetValues = new String[values.length() * 3];
         } else {
            assetValues = new String[values.length() * 2];
         }

         int i = 0;

         Iterator it = values.keys();
         while (it.hasNext()) {
            String symbol = it.next().toString();
            assetValues[i++] = symbol;

            if (currencyValueAttribute) {
               JSONArray currVal = values.getJSONArray(symbol);
               String value = currVal.getString(0);
               String curr = currVal.getString(1);

               assetValues[i++] = value;
               assetValues[i++] = curr;
            } else {
               String value = values.getString(symbol);
               System.out.println(value);

               assetValues[i++] = value;
            }
         }

         if (currencyValueAttribute) {
            setContentView(R.layout.currency_attribute);
         } else {
            setContentView(R.layout.double_text_attribute);
         }
      } catch (JSONException e) {
         Log.e("JSON Parser", "Error parsing data " + e.toString());
      }

      GridView gridView = (GridView) findViewById(R.id.assets);

      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, assetValues);

      gridView.setAdapter(adapter);
   }

   private boolean isCurrencyValueAttribute(final String unitType) {
      if (unitType.equals("PRICE") || unitType.equals("CURRENCY")) {
         return true;
      }

      return false;
   }
}