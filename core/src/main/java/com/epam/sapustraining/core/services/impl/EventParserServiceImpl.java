package com.epam.sapustraining.core.services.impl;

import com.epam.sapustraining.core.models.EventConfigModel;
import com.epam.sapustraining.core.models.event.clientside.EventModel;
import com.epam.sapustraining.core.services.EventParserService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.util.Base64;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Uladzimir_Stsiatsko on 4/7/2016.
 */
@Component(immediate = true)
@Service
public class EventParserServiceImpl implements EventParserService {

    private Logger logger = LoggerFactory.getLogger(EventParserService.class);

    @Override
    public List<EventModel> getEvents(EventConfigModel config){
        return getEvents(config.getPath(), config.getLogin(), config.getPassword());
    }

    private List<EventModel> getEvents(String path, String login, String password){
        List<EventModel> result = new ArrayList<EventModel>();
        try {
            result = parseJsonForEvents(getServletResponse(path, login, password));
        } catch (JSONException e) {
            logger.error(Arrays.toString(e.getStackTrace()));
        }
        return result;
    }

    //made public for js json rendering
    @Override
    public String getServletResponse(String path, String login, String password) {
        HttpURLConnection connection = null;
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            String authKey = login + ":" + password;
            String encodedPassword = Base64.encode(authKey);
            connection.setRequestProperty("Authorization", "Basic " + encodedPassword);

            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }

            reader.close();
        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()));
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return builder.toString();
    }

    private List<EventModel> parseJsonForEvents(String jsonString) throws JSONException {
        List<EventModel> events = new ArrayList<EventModel>();
        JSONObject jsonListObject = new JSONObject(jsonString);
        JSONArray jsonEventArray = jsonListObject.optJSONArray("events");

        for (int i = 0; i < jsonEventArray.length(); i++) {
            JSONObject jsonEventObject = jsonEventArray.getJSONObject(i);
            EventModel event = new EventModel();

            event.setType(jsonEventObject.getString("type"));
            event.setTopic(jsonEventObject.getString("topic"));
            event.setDate(jsonEventObject.getString("date"));
            event.setTitleText(jsonEventObject.getString("titleText"));
            event.setTitleLink(jsonEventObject.getString("titleLink"));
            event.setDescription(jsonEventObject.getString("description"));

            events.add(event);
        }
        return events;
    }

}
