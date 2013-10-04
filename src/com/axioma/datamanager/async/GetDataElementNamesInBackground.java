package com.axioma.datamanager.async;

import android.content.Context;
import android.os.AsyncTask;

import com.axioma.datamanager.util.PreferenceUtil;
import com.axioma.datamanager.util.RestClientUtil;

/**
 * @author rkannappan
 */
public class GetDataElementNamesInBackground extends AsyncTask<Void, Void, String> {
   private final Context context;
   private final AsyncCallback callback;
   private final String url;

   public GetDataElementNamesInBackground(final Context context, final String dataElementType, final AsyncCallback callback) {
      this.context = context;
      this.callback = callback;

      this.url = this.getURL(dataElementType);

      System.out.println(url);
   }

   @Override
   protected String doInBackground(Void... params) {
      return RestClientUtil.getJSONFromUrl(url, this.context);
   }

   @Override
   protected void onPostExecute(String results) {
      super.onPostExecute(results);
      System.out.println(results);
      callback.postProcessing(results);
   }

   private String getURL(final String dataElementType) {
      String url = null;

      if (dataElementType.equals("Alphas")) {
         url = PreferenceUtil.getBaseWSURL(this.context) + "attributeNames?tagName=Alpha";
      } else if (dataElementType.equals("Benchmarks")) {
         url = PreferenceUtil.getBaseWSURL(this.context) + "attributeNames?tagName=Benchmark";
      } else if (dataElementType.equals("Asset Identifiers")) {
         url = PreferenceUtil.getBaseWSURL(this.context) + "attributeNames?tagName=Asset Identifier";
      } else if (dataElementType.equals("Fundamental Attributes")) {
         url = PreferenceUtil.getBaseWSURL(this.context) + "attributeNames?tagName=Fundamental Attribute";
      } else if (dataElementType.equals("Factor Libraries")) {
         url = PreferenceUtil.getBaseWSURL(this.context) + "attributeNames?tagName=Factor Library";
      } else if (dataElementType.equals("Text Attributes")) {
         url = PreferenceUtil.getBaseWSURL(this.context) + "attributeNames?unitType=TEXT";
      } else if (dataElementType.equals("ETF Constituents")) {
         url = PreferenceUtil.getBaseWSURL(this.context) + "attributeNames?tagName=Composite Constituents Attribute";
      } else if (dataElementType.equals("Portfolios")) {
         url = PreferenceUtil.getBaseWSURL(this.context) + "portfolioNames";
      } else if (dataElementType.equals("Currency Attributes")) {
         url = PreferenceUtil.getBaseWSURL(this.context) + "currencyAttributeNames";
      } else if (dataElementType.equals("Factor Risk Models")) {
         url = PreferenceUtil.getBaseWSURL(this.context) + "factorRiskModelNames";
      }

      return url;
   }
}