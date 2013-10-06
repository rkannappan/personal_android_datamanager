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
public class GetAttributeDataInBackground extends AsyncTask<Void, Void, String> {
   private final Context context;
   private final AsyncCallback callback;
   private final String url;

   private ProgressDialog dialog;
   private final String dataElementName;
   private final String dataElementDate;

   public GetAttributeDataInBackground(final Context context, final String dataElementType, final String dataElementName,
            final String dataElementDate, final AsyncCallback callback) {
      this.context = context;
      this.callback = callback;

      this.dataElementName = dataElementName;
      this.dataElementDate = dataElementDate;

      this.url = this.getURL(dataElementType, dataElementName, dataElementDate);

      System.out.println(url);
   }

   @Override
   protected void onPreExecute() {
      this.dialog = new ProgressDialog(this.context);
      this.dialog.setMessage("Getting values for " + this.dataElementName + " on " + this.dataElementDate + "...");
      this.dialog.show();
   }

   @Override
   protected String doInBackground(Void... params) {
      return RestClientUtil.getJSONFromUrl(url, this.context);
   }

   @Override
   protected void onPostExecute(String results) {
      super.onPostExecute(results);
      System.out.println(results);
      this.dialog.dismiss();
      callback.postProcessing(results);
   }

   private String getURL(final String dataElementType, final String dataElementName, final String dataElementDate) {
      String url = null;

      String elementType = null;

      if (dataElementType.equals(DataElementsActivity.ALPHAS) || dataElementType.equals(DataElementsActivity.BENCHMARKS)
               || dataElementType.equals(DataElementsActivity.ASSET_IDENTIFIERS)
               || dataElementType.equals(DataElementsActivity.FUNDAMENTAL_ATTRIBUTES)
               || dataElementType.equals(DataElementsActivity.FACTOR_LIBRARIES)
               || dataElementType.equals(DataElementsActivity.TEXT_ATTRIBUTES)
               || dataElementType.equals(DataElementsActivity.ETF_CONSTITUENTS)) {
         elementType = "attributes";
      } else if (dataElementType.equals(DataElementsActivity.PORTFOLIOS)) {
         elementType = "portfolios";
      } else if (dataElementType.equals(DataElementsActivity.CURRENCY_ATTRIBUTES)) {
         elementType = "currencyAttributes";
      } else if (dataElementType.equals(DataElementsActivity.FACTOR_RISK_MODELS)) {
         elementType = "factorRiskModels";
      }

      url = PreferenceUtil.getBaseWSURL(this.context) + elementType + "/" + dataElementName + "/" + dataElementDate;

      return url;
   }
}