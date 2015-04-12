package com.flashback.flashback;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.IconTextView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.undercouch.bson4jackson.BsonFactory;
import de.undercouch.bson4jackson.BsonGenerator;

public class FriendsFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        return view;
    }

    private class Friend {
        private String user;
        private boolean request;
    }

    private class CapsuleAdapter extends ArrayAdapter<Friend> {
        private Context context;
        private ArrayList<Friend> friends;

        public CapsuleAdapter(Context context, ArrayList<Friend> friends) {
            super(context, R.layout.capsule_row, friends);
            this.context = context;
            this.friends = friends;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.capsule_row, parent, false);
            TextView friendName = (TextView) rowView.findViewById(R.id.friendName);

            friendName.setText(friends.get(position).user);

            if (friends.get(position).request) {
                View acceptBox = rowView.findViewById(R.id.acceptBox);
                View declineBox = rowView.findViewById(R.id.declineBox);

                acceptBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new FriendRequestTask(true).execute(friends.get(position).user);
                    }
                });

                declineBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new FriendRequestTask(false).execute(friends.get(position).user);
                    }
                });
            }

            return rowView;
        }
    }

    private class GetFriendsTask extends AsyncTask<String, Void, List<Friend>> {
        @Override
        protected List<Friend> doInBackground(String... users) {
            if (users[0] != null) {
                try {
                    URL url = new URL("http://bitcmp.ngrok.com/friends");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);

                    OutputStreamWriter outputStream = new OutputStreamWriter(connection.getOutputStream());

                    BsonFactory factory = new BsonFactory();
                    BsonGenerator gen = factory.createGenerator(outputStream);

                    gen.writeStartObject();

                    gen.writeFieldName("user");
                    gen.writeString(users[0]);

                    gen.writeEndObject();

                    gen.close();
                    outputStream.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "", response = "";

                    while ((line = reader.readLine()) != null) {
                        response += line;
                    }

                    JSONObject jsonObject = new JSONObject(response);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    private class FriendRequestTask extends AsyncTask<String, Void, String> {
        private boolean accepted;

        public FriendRequestTask(boolean accepted) {
            this.accepted = accepted;
        }

        @Override
        protected String doInBackground(String... users) {

            return null;
        }

        @Override
        public void onPostExecute(String result) {

        }
    }
}
