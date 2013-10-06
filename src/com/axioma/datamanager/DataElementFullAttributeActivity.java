package com.axioma.datamanager;

import java.text.DecimalFormat;
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
import android.widget.TextView;

import com.axioma.datamanager.async.AsyncCallback;
import com.axioma.datamanager.async.GetAttributeDataInBackground;

/**
 * @author rkannappan
 */
public class DataElementFullAttributeActivity extends Activity implements AsyncCallback {

   private String dataElementType;
   private String dataElementName;
   private String dataElementDate;

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
      this.dataElementName = name;
      this.dataElementDate = date;

      new GetAttributeDataInBackground(DataElementFullAttributeActivity.this, type, name, date, this).execute();
   }

   @Override
   public void postProcessing(String results) {
      String[] assetValues = null;
      int numAssets = 0;
      String unitType = "NUMBER"; // For currency attributes

      DecimalFormat df = new DecimalFormat("#.####");

      // try parse the string to a JSON object
      try {
         JSONObject attribute = new JSONObject(results);
         
         if (DataElementsActivity.PORTFOLIOS.equals(this.dataElementType)) {
            attribute = attribute.getJSONObject("holdings");
         }

         if (!DataElementsActivity.CURRENCY_ATTRIBUTES.equals(this.dataElementType)) {
            unitType = attribute.getString("unitType");
            System.out.println(unitType);
         }

         JSONObject values = attribute.getJSONObject("values");
         numAssets = values.length();

         boolean currencyValueAttribute = isCurrencyValueAttribute(unitType);
         if (currencyValueAttribute) {
            assetValues = new String[numAssets * 3];
         } else {
            assetValues = new String[numAssets * 2];
         }

         int i = 0;

         boolean doubleValueAttribute = isDoubleValueAttribute(unitType);

         Iterator it = values.keys();
         while (it.hasNext()) {
            String symbol = it.next().toString();
            assetValues[i++] = symbol;

            if (currencyValueAttribute) {
               JSONArray currVal = values.getJSONArray(symbol);
               String value = currVal.getString(0);
               String curr = currVal.getString(1);

               assetValues[i++] = this.formatValue(df, value);
               assetValues[i++] = curr;
            } else {
               String value = values.getString(symbol);
               System.out.println(value);

               assetValues[i++] = doubleValueAttribute ? this.formatValue(df, value) : value;
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

      TextView textView = (TextView) findViewById(R.id.header);
      StringBuilder sb = new StringBuilder();
      sb.append("\n");
      sb.append("   Type: ").append(this.dataElementType).append("\n");
      sb.append("   Name: ").append(this.dataElementName).append("\n");
      sb.append("   Date: ").append(this.dataElementDate).append("\n");
      sb.append("   Unit Type: ").append(unitType).append("\n");
      sb.append("   Number of Assets: ").append(numAssets);
      textView.setText(sb.toString());

      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, assetValues);

      GridView gridView = (GridView) findViewById(R.id.assets);
      gridView.setAdapter(adapter);
   }

   private boolean isCurrencyValueAttribute(final String unitType) {
      if (unitType.equals("PRICE") || unitType.equals("CURRENCY")) {
         return true;
      }

      return false;
   }

   private boolean isDoubleValueAttribute(final String unitType) {
      if (unitType.equals("SHARES") || unitType.equals("NUMBER")) {
         return true;
      }

      return false;
   }

   private String formatValue(final DecimalFormat df, final String value) {
      return df.format(Double.valueOf(value));
   }
}