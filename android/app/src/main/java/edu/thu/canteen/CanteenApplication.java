package edu.thu.canteen;

import android.app.Application;
import edu.thu.canteen.data.network.NetworkClient;

public class CanteenApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetworkClient.init(this);
    }
}
