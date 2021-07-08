package com.tosattosimonepio;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class SocketInstance{

    private WebSocket webSocket;
    private String SERVER_PATH = "http://192.168.1.101:8080";

    public SocketInstance() {
        initiateSocketConnection();
    }

    private void initiateSocketConnection() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());
        Log.d("WebSocketStatus",webSocket.toString());
    }

    private class SocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            Log.d("WebSocketStatus","onOpenExecuted");
            webSocket.send("ciao dal client!");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            Log.d("WebSocketStatus","onMessageFired");
        }
    }
}
