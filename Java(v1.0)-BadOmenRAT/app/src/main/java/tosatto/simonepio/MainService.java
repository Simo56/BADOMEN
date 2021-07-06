package tosatto.simonepio;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class MainService extends Service {

    private static Context contextOfApplication;

    //questo viene chiamato la prima volta da startService(Intent)
    @Override
    public void onCreate() {
        super.onCreate();
    }

    //questo viene chiamato dopo il startService(Intent) se il servizio è gia attivo
    @Override
    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
    {
        contextOfApplication = this;
        ConnectionManager.startAsync(this);
        return Service.START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

}
