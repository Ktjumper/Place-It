/**
 * Created by CSE 110 Team 12.
 * User: Team 12 - Kristiyan Dzhamalov
 * 				 - Richard Tran
 * 				 - Kenneth Tran
 * 				 - Monica Cheung
 * 				 - Heather Lee
 * 				 - Allen Lin
 * Date: 3/15/14
 */
package place_its;
 
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
  
public class ConnectionDetector {
  
    private Context mContext;
  
    public ConnectionDetector(Context context) {
        this.mContext = context;
    }
  
    /**
     * Checking for all possible internet providers
     * **/
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null) {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null)
                  for (int i = 0; i < info.length; i++)
                      if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                          return true;
                      }
  
          }
          return false;
    }
}