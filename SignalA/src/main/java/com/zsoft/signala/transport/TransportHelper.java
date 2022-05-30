package com.zsoft.signala.transport;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zsoft.signala.ConnectionBase;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class TransportHelper {
    private static final String TAG = "TransportHelper";

    public static ProcessResult ProcessResponse(ConnectionBase connection, JSONObject response) {
        ProcessResult result = new ProcessResult();

        if (connection == null || response == null) {
            return result;
        }

        String newMessageId = null;
        JSONArray messagesArray = null;
        String groupsToken = null;
        //JSONObject transportData = null;
        String info = null;

        result.timedOut = response.getIntValue("T") == 1;
        result.disconnected = response.getIntValue("D") == 1;
        result.initialized = response.getIntValue("S") == 1;
        newMessageId = response.getString("C");
        messagesArray = response.getJSONArray("M");
        groupsToken = response.getString("G");
        info = response.getString("I");

        if (info != null) {
            connection.setMessage(response);
            return result;
        }

        if (result.disconnected) {
            return result;
        }

        if (groupsToken != null) {
            connection.setGroupsToken(groupsToken);
            result.initialized = true;
        }

        if (messagesArray != null) {
            connection.setMessageId(newMessageId);
            for (int i = 0; i < messagesArray.size(); i++)
                connection.setMessage(messagesArray.getJSONObject(i));
            // ToDo: Initialize the connection?
            //if(result.initialized)
            //connection.onInitialized();
        }

        return result;
    }


    public static String GetNegotiationQueryString(ConnectionBase connection, String connectionData) {
        if (connection == null) {
            throw new IllegalArgumentException("connection");
        }

        String qs = "?clientProtocol=";
        qs += connection.getProtocolVersion();

        if (connectionData != null) {
            try {
                qs += "&connectionData=" + URLEncoder.encode(connectionData, "utf-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "Unsupported message encoding error, when encoding connectionData.");
            }
        }

        return qs;
    }


    public static String GetSendQueryString(ConnectionBase connection, String connectionData, String transport) {
        if (connection == null) {
            throw new IllegalArgumentException("connection");
        }

        String qs = "?transport=";
        qs += transport;
        try {
            qs += "&connectionToken=" + URLEncoder.encode(connection.getConnectionToken(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Unsupported message encoding error, when encoding connectionToken.");
        }

        if (connection.getGroupsToken() != null && connection.getGroupsToken().length() > 0) {
            try {
                qs += "&groupsToken=" + URLEncoder.encode(connection.getGroupsToken(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "Unsupported message encoding error, when encoding groupsToken.");
            }
        }

        if (connectionData != null) {
            try {
                qs += "&connectionData=" + URLEncoder.encode(connectionData, "utf-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "Unsupported message encoding error, when encoding connectionData.");
            }
        }

        if (connection.getQueryString() != null && connection.getQueryString().length() > 0) {
            qs += "&" + connection.getQueryString();
//            try {
//                qs += "&" + URLEncoder.encode(connection.getQueryString(), "utf-8");
//            } catch (UnsupportedEncodingException e) {
//                Log.e(TAG, "Unsupported message encoding error, when encoding querystring.");
//            }
        }

        return qs;
    }


    public static String GetReceiveQueryString(ConnectionBase connection, String data, String transport) {
        if (connection == null) {
            throw new IllegalArgumentException("connection");
        }
        if (transport == null) {
            throw new IllegalArgumentException("transport");
        }


        // ?transport={0}&connectionToken={1}&messageId={2}&groupsToken={3}&connectionData={4}{5}
        String qs = "?transport=";
        qs += transport;
        try {
            qs += "&connectionToken=" + URLEncoder.encode(connection.getConnectionToken(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Unsupported message encoding error, when encoding connectionToken.");
        }
        if (connection.getMessageId() != null) {
            try {
                qs += "&messageId=" + URLEncoder.encode(connection.getMessageId(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "Unsupported message encoding error, when encoding messageid.");
            }
        }

        if (connection.getGroupsToken() != null && connection.getGroupsToken().length() > 0) {
            try {
                qs += "&groupsToken=" + URLEncoder.encode(connection.getGroupsToken(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "Unsupported message encoding error, when encoding groupsToken.");
            }
        }

        if (data != null) {
            try {
                qs += "&connectionData=" + URLEncoder.encode(data, "utf-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "Unsupported message encoding error, when encoding connectionData.");
            }
        }

        if (connection.getQueryString() != null && connection.getQueryString().length() > 0) {
//            try {
//				qs += "&" + URLEncoder.encode(connection.getQueryString(), "utf-8");
//			} catch (UnsupportedEncodingException e) {
//				Log.e(TAG, "Unsupported message encoding error, when encoding querystring.");
//			}
            qs += "&" + connection.getQueryString();

        }

        return qs;
    }


    public static ArrayList<String> ToArrayList(JSONArray jsonArray) {
        ArrayList<String> list = null;
        if (jsonArray != null) {
            int len = jsonArray.size();
            list = new ArrayList<String>(len);
            for (int i = 0; i < len; i++) {
                Object o = jsonArray.get(i);
                if (o != null)
                    list.add(o.toString());
            }
        }

        return list;
    }

    public static String AppendCustomQueryString(ConnectionBase connection, String baseUrl) {
        if (connection == null) {
            throw new IllegalArgumentException("connection");
        }

        if (baseUrl == null) {
            baseUrl = "";
        }

        String appender = "",
                customQuery = connection.getQueryString(),
                qs = "";

        if (customQuery != null && customQuery.length() > 0) {
            char firstChar = customQuery.charAt(0);

            // If the custom query string already starts with an ampersand or question mark
            // then we don't have to use any appender, it can be empty.
            if (firstChar != '?' && firstChar != '&') {
                appender = "?";

                if (baseUrl.contains(appender)) {
                    appender = "&";
                }
            }

            qs += appender + customQuery;
        }
        return qs;
    }

}
