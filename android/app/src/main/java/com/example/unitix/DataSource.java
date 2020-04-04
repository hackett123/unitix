package com.example.unitix;


import java.net.*;
import java.util.Scanner;
import android.os.AsyncTask;
import org.json.*;
import android.util.Log;


public class DataSource {

    private String host;
    private int port;

    public DataSource() {
        // use Node Express defaults
        host = "http://10.0.2.2";
        port = 3000;
    }


    public User getUser(String email) {

        try {
            String urlString = host + ":" + port + "/find_user?email=" + email;
            URL url = new URL(urlString);
            AsyncTask<URL, String, JSONObject> task =
                    new AccessWebJSONTask();
            task.execute(url);
            JSONObject jo = task.get();
            return new User(jo.getJSONObject("user"));

        } catch (Exception e) {
            Log.e("Yash","getUser exception " + e);
            return null;
        }
    }

    // for testing purposes
    public Event[] getAllEvents() {
        Event[] events = new Event[0];
        try {
            URL url = new URL(host + ":" + port + "/list_events_with_shows");
            AsyncTask<URL, String, JSONObject> task =
                    new AccessWebJSONTask();
            task.execute(url);
            JSONObject jo = task.get();

            events = Event.createEventList(jo.getJSONArray("events"));
        } catch (Exception e) {
            Log.e("NOAH","exception: " + e);
            // pass
        }
        Log.e("NOAH","about to return events, got" + events.length);
        return events;
    }


    // reads all contents from scanner
//    public static String exhaust(Scanner in) {
//        String total = "";
//        while (in.hasNext()) {
//            String line = in.nextLine();
//            total += line;
//        }
//        return total;
//    }

    public Event getEventByID(String eventID) {
        try {
            // TODO: make url based on variables !
            String urlString = host + ":" + port + "/find_event_with_shows?eventID=" + eventID;

            URL url = new URL(urlString);
            AsyncTask<URL, String, JSONObject> task =
                    new AccessWebJSONTask();
            task.execute(url);
            JSONObject jo = task.get();
            return new Event(jo.getJSONObject("event"));

        } catch (Exception e) {
            Log.e("NOAH","getEventByID exception " + e);
            return null;
        }
    }

    public boolean purchaseTicket(String email, String showID) {
        try {
            AccessWebJSONPutTask task = new AccessWebJSONPutTask();
            JSONObject jo = new JSONObject();
            jo.put("email", email);
            jo.put("showID", showID);
            String url = host + ":" + port + "/purchase_ticket";
            AccessWebJSONPutTask.Req req = new AccessWebJSONPutTask.Req(url, jo);
            task.execute(req);
            JSONObject res = task.get();
            Log.e("NOAH","res is " + res);
            if (res.getString("status").equals("success")) {
                return true;
            }
        } catch (Exception e) {
            Log.e("NOAH","failed to purchase");
        }
        return false;
    }


    public boolean createUser(String email, String password, String firstName, String lastName, String phone) {
        try {
            //haven't changed this - need to for create user
            //need to figure out URL

//            String urlString = host + ":" + port + "/create_user?email=" + email;
//            urlString += "&password=" + password;
//
//            if (firstName != null && firstName.length() > 0) {
//                urlString += "&first_name=" + firstName;
//            }
//            if (lastName != null && lastName.length() > 0) {
//                urlString += "&last_name=" + lastName;
//            }
//
//            if (phone != null && phone.length() > 0) {
//                urlString += "&phone=" + phone;
//            }

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("email", email);
            jsonParam.put("password", password);
            jsonParam.put("first_name", firstName);
            jsonParam.put("last_name", lastName);
            jsonParam.put("phone", phone);
            String urlString = "http://10.0.2.2:3000/create_user";

            AccessWebJSONPutTask.Req req = new AccessWebJSONPutTask.Req(urlString, jsonParam);

            AccessWebJSONPutTask task = new AccessWebJSONPutTask();
            task.execute(req);

            return true;

        } catch (Exception e) {
            Log.e("Yash","createUser exception " + e);
            return false;
        }
    }

}