package com.axioma.datamanager.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.axioma.datamanager.util.PreferenceUtil;
import com.axioma.datamanager.util.RestClientUtil;
import com.axioma.datamanager.util.URLUtil;

/**
 * @author rkannappan
 */
public class GetDataElementDatesInBackground extends AsyncTask<Void, Void, String> {
   private final Context context;
   private final AsyncCallback callback;
   private final String url;

   private ProgressDialog dialog;
   private final String dataElementName;

   public GetDataElementDatesInBackground(final Context context, final String dataElementType, final String dataElementName,
            final AsyncCallback callback) {
      this.context = context;
      this.callback = callback;

      this.dataElementName = dataElementName;

      this.url = this.getURL(dataElementType, dataElementName);

      System.out.println(url);
   }

   @Override
   protected void onPreExecute() {
      this.dialog = new ProgressDialog(this.context);
      this.dialog.setMessage("Getting dates for " + this.dataElementName + " ...");
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

   private String getURL(final String dataElementType, final String dataElementName) {
      String elementType = URLUtil.getURLElementType(dataElementType);

      String url = PreferenceUtil.getBaseWSURL(this.context) + elementType + "/" + dataElementName + "/dates";

      return url;
   }
}