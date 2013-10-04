package com.axioma.datamanager;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.common.collect.Lists;

/**
 * @author rkannappan
 */
public class DataElementsActivity extends ListActivity {
   
   public static final String SELECTED_DATA_ELEMENT_TYPE = "com.axioma.datamanager.selected_data_element_name";

   private List<String> dataElements;

   public final static String PORTFOLIOS = "Portfolios";
   public final static String BENCHMARKS = "Benchmarks";
   public final static String ASSET_IDENTIFIERS = "Asset Identifiers";
   public final static String ALPHAS = "Alphas";
   public final static String FUNDAMENTAL_ATTRIBUTES = "Fundamental Attributes";
   public final static String FACTOR_LIBRARIES = "Factor Libraries";
   public final static String TEXT_ATTRIBUTES = "Text Attributes";
   public final static String ETF_CONSTITUENTS = "ETF Constituents";
   public final static String CURRENCY_ATTRIBUTES = "Currency Attributes";
   public final static String FACTOR_RISK_MODELS = "Factor Risk Models";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_data_elements);

      this.addDataElements();
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.data_elements, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      // Handle presses on the action bar items
      switch (item.getItemId()) {
         case R.id.action_settings:
            Intent i = new Intent(getApplicationContext(), AppSettingsActivity.class);
            startActivity(i);
            return true;
         default:
            return super.onOptionsItemSelected(item);
      }
   }

   private void addDataElements() {
      this.dataElements = Lists.newArrayList();
      this.dataElements.add(DataElementsActivity.PORTFOLIOS);
      this.dataElements.add(DataElementsActivity.BENCHMARKS);
      this.dataElements.add(DataElementsActivity.ASSET_IDENTIFIERS);
      this.dataElements.add(DataElementsActivity.ALPHAS);
      this.dataElements.add(DataElementsActivity.FUNDAMENTAL_ATTRIBUTES);
      this.dataElements.add(DataElementsActivity.FACTOR_LIBRARIES);
      this.dataElements.add(DataElementsActivity.TEXT_ATTRIBUTES);
      this.dataElements.add(DataElementsActivity.ETF_CONSTITUENTS);
      this.dataElements.add(DataElementsActivity.CURRENCY_ATTRIBUTES);
      this.dataElements.add(DataElementsActivity.FACTOR_RISK_MODELS);

      ArrayAdapter<String> adapter =
               new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                        this.dataElements.toArray(new String[this.dataElements.size()]));

      setListAdapter(adapter);

      // selecting single ListView item
      ListView lv = getListView();

      // Launching new screen on Selecting Single ListItem
      lv.setOnItemClickListener(new OnItemClickListener() {

         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String dataElement = dataElements.get(position);

            System.out.println("dataElement is " + dataElement);

            Intent intent = new Intent(getApplicationContext(), DataElementNamesActivity.class);
            intent.putExtra(DataElementsActivity.SELECTED_DATA_ELEMENT_TYPE, dataElement);
            startActivity(intent);
         }
      });
   }
}