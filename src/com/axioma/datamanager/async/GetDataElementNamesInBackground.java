package com.axioma.datamanager.async;

import android.app.ProgressDialog;
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

   private ProgressDialog dialog;

   private final String dataElementType;

   public GetDataElementNamesInBackground(final Context context, final String dataElementType, final AsyncCallback callback) {
      this.context = context;
      this.callback = callback;

      this.dataElementType = dataElementType;
      this.url = this.getURL(dataElementType);

      System.out.println(url);
   }

   @Override
   protected void onPreExecute() {
      this.dialog = new ProgressDialog(this.context);
      this.dialog.setMessage("Getting " + this.dataElementType + " ...");
      this.dialog.show();
   }

   @Override
   protected String doInBackground(Void... params) {
      String json = RestClientUtil.getJSONFromUrl(url, this.context);

      String internaljson = null;
      if (DataElementsActivity.CLASSIFICATION_SCHEMES.equals(dataElementType)
               || DataElementsActivity.CLASSIFICATIONS.equals(dataElementType)) {
         String internalurl = url + "?tagName=RiskAnalysis Internal";
         internaljson = RestClientUtil.getJSONFromUrl(internalurl, this.context);
         return json + "~" + internaljson;
      } else {
         return json;
      }
   }

   @Override
   protected void onPostExecute(String results) {
      super.onPostExecute(results);
      System.out.println(results);
      this.dialog.dismiss();
      callback.postProcessing(results);
   }

   private String getURL(final String dataElementType) {
      String url = null;

      String elementType = null;

      if (dataElementType.equals(DataElementsActivity.ALPHAS)) {
         elementType = "attributeNames?tagName=Alpha";
      } else if (dataElementType.equals(DataElementsActivity.BENCHMARKS)) {
         elementType = "attributeNames?tagName=Benchmark";
      } else if (dataElementType.equals(DataElementsActivity.ASSET_IDENTIFIERS)) {
         elementType = "attributeNames?tagName=Asset Identifier";
      } else if (dataElementType.equals(DataElementsActivity.FUNDAMENTAL_ATTRIBUTES)) {
         elementType = "attributeNames?tagName=Fundamental Attribute";
      } else if (dataElementType.equals(DataElementsActivity.FACTOR_LIBRARIES)) {
         elementType = "attributeNames?tagName=Factor Library";
      } else if (dataElementType.equals(DataElementsActivity.TEXT_ATTRIBUTES)) {
         elementType = "attributeNames?unitType=TEXT";
      } else if (dataElementType.equals(DataElementsActivity.ETF_CONSTITUENTS)) {
         elementType = "attributeNames?tagName=Composite Constituents Attribute";
      } else if (dataElementType.equals(DataElementsActivity.PORTFOLIOS)) {
         elementType = "portfolioNames";
      } else if (dataElementType.equals(DataElementsActivity.CURRENCY_ATTRIBUTES)) {
         elementType = "currencyAttributeNames";
      } else if (dataElementType.equals(DataElementsActivity.COUNTRY_ATTRIBUTES)) {
         elementType = "countryAttributeNames";
      } else if (dataElementType.equals(DataElementsActivity.FACTOR_ATTRIBUTES)) {
         elementType = "factorAttributeNames";
      } else if (dataElementType.equals(DataElementsActivity.FACTOR_RISK_MODELS)) {
         elementType = "factorRiskModelNames";
      } else if (dataElementType.equals(DataElementsActivity.CLASSIFICATION_SCHEMES)) {
         elementType = "classificationSchemeNames";
      } else if (dataElementType.equals(DataElementsActivity.CLASSIFICATIONS)) {
         elementType = "classificationNames";
      }

      url = PreferenceUtil.getBaseWSURL(this.context) + elementType;

      return url;
   }
}