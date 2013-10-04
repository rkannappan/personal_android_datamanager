package com.axioma.datamanager.async;

import android.content.Context;
import android.os.AsyncTask;

import com.axioma.datamanager.DataElementsActivity;
import com.axioma.datamanager.util.PreferenceUtil;
import com.axioma.datamanager.util.RestClientUtil;

/**
 * @author rkannappan
 */
public class GetDataElementDatesInBackground extends AsyncTask<Void, Void, String> {
   private final Context context;
   private final AsyncCallback callback;
   private final String url;

   public GetDataElementDatesInBackground(final Context context, final String dataElementType, final String dataElementName,
            final AsyncCallback callback) {
      this.context = context;
      this.callback = callback;

      this.url = this.getURL(dataElementType, dataElementName);

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

   private String getURL(final String dataElementType, final String dataElementName) {
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

      url = PreferenceUtil.getBaseWSURL(this.context) + elementType + "/" + dataElementName + "/dates";

      return url;
   }
}