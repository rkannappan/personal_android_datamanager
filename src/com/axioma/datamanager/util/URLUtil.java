package com.axioma.datamanager.util;

import com.axioma.datamanager.DataElementsActivity;

/**
 * @author rkannappan
 */
public class URLUtil {

   public static String getURLElementType(final String dataElementType) {
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
      } else if (dataElementType.equals(DataElementsActivity.COUNTRY_ATTRIBUTES)) {
         elementType = "countryAttributes";
      } else if (dataElementType.equals(DataElementsActivity.FACTOR_ATTRIBUTES)) {
         elementType = "factorAttributes";
      } else if (dataElementType.equals(DataElementsActivity.FACTOR_RISK_MODELS)) {
         elementType = "factorRiskModels";
      } else if (dataElementType.equals(DataElementsActivity.CLASSIFICATION_SCHEMES)) {
         elementType = "classificationSchemes";
      } else if (dataElementType.equals(DataElementsActivity.CLASSIFICATIONS)) {
         elementType = "classifications";
      }

      return elementType;
   }
}
