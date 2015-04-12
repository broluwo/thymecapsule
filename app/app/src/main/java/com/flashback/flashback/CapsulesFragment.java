package com.flashback.flashback;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.IconTextView;
import android.widget.ListView;
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

public class CapsulesFragment extends Fragment {
    private ArrayList<Capsule> capsules;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        capsules = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_capsules, container, false);
        ListView listView = (ListView) view.findViewById(R.id.capsulesList);
        listView.setAdapter(new CapsuleAdapter(getActivity(), capsules));
        return view;
    }

    private class Capsule {
        private String uploader;
        private boolean pictureContent;
        private double lat, lon;

    }

    private class CapsuleAdapter extends ArrayAdapter<Capsule> {
        private Context context;
        private ArrayList<Capsule> capsules;

        public CapsuleAdapter(Context context, ArrayList<Capsule> capsules) {
            super(context, R.layout.capsule_row, capsules);
            this.context = context;
            this.capsules = capsules;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.capsule_row, parent, false);
            TextView location = (TextView) rowView.findViewById(R.id.location);
            IconTextView contentType = (IconTextView) rowView.findViewById(R.id.contentType);

            location.setText(capsules.get(position).lat + ", " + capsules.get(position).lon);

            if (capsules.get(position).pictureContent) {
                contentType.setText("{fa-camera}");
            } else {
                contentType.setText("{fa-video-camera}");
            }

            return rowView;
        }
    }

    private class GetCapsulesTask extends AsyncTask<String, Void, List<Capsule>> {
        @Override
        protected List<Capsule> doInBackground(String... users) {
            if (users[0] != null) {
                try {
                    URL url = new URL("http://bitcmp.ngrok.com/cap");
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
}
