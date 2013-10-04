package com.axioma.datamanager.async;

import android.content.Context;
import android.os.AsyncTask;

import com.axioma.datamanager.DataElementsActivity;
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

      if (dataElementType.equals(DataElementsActivity.ALPHAS)) {
         url = PreferenceUtil.getBaseWSURL(this.context) + "attributeNames?tagName=Alpha";
      } else if (dataElementType.equals(DataElementsActivity.BENCHMARKS)) {
         url = PreferenceUtil.getBaseWSURL(this.context) + "attributeNames?tagName=Benchmark";
      } else if (dataElementType.equals(DataElementsActivity.ASSET_IDENTIFIERS)) {
         url = PreferenceUtil.getBaseWSURL(this.context) + "attributeNames?tagName=Asset Identifier";
      } else if (dataElementType.equals(DataElementsActivity.FUNDAMENTAL_ATTRIBUTES)) {
         url = PreferenceUtil.getBaseWSURL(this.context) + "attributeNames?tagName=Fundamental Attribute";
      } else if (dataElementType.equals(DataElementsActivity.FACTOR_LIBRARIES)) {
         url = PreferenceUtil.getBaseWSURL(this.context) + "attributeNames?tagName=Factor Library";
      } else if (dataElementType.equals(DataElementsActivity.TEXT_ATTRIBUTES)) {
         url = PreferenceUtil.getBaseWSURL(this.context) + "attributeNames?unitType=TEXT";
      } else if (dataElementType.equals(DataElementsActivity.ETF_CONSTITUENTS)) {
         url = PreferenceUtil.getBaseWSURL(this.context) + "attributeNames?tagName=Composite Constituents Attribute";
      } else if (dataElementType.equals(DataElementsActivity.PORTFOLIOS)) {
         url = PreferenceUtil.getBaseWSURL(this.context) + "portfolioNames";
      } else if (dataElementType.equals(DataElementsActivity.CURRENCY_ATTRIBUTES)) {
         url = PreferenceUtil.getBaseWSURL(this.context) + "currencyAttributeNames";
      } else if (dataElementType.equals(DataElementsActivity.FACTOR_RISK_MODELS)) {
         url = PreferenceUtil.getBaseWSURL(this.context) + "factorRiskModelNames";
      }

      return url;
   }
}