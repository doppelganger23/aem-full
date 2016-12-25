package com.epam.sapustraining.core.models.event.clientside;

import com.epam.sapustraining.core.models.EventConfigModel;
import com.epam.sapustraining.core.services.EventParserService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Uladzimir_Stsiatsko on 4/8/2016.
 */
@Model(adaptables = Resource.class)
public class EventLayoutModel {

    private static final String SCRIPT_URL = "/apps/sapustraining/components/content/events/event-positioner-with-refresh/event-positioner-with-refresh";

    @Inject
    private EventParserService parseService;

    @Inject
    @Default(values = "")
    private String[] columnTypes;
    @Inject
    @Default(values = "Other Types")
    private String otherTypeColName;

    @Inject
    @Default(values = "")
    private String[] columnTopics;
    @Inject
    @Default(values = "Other Topics")
    private String otherTopicColName;

    private List<EventColumnModel> columnModels;

    public String getUrl(){
        return SCRIPT_URL;
    }

    ////COLUMNS-BY-TYPE METHODS:////

    public List<EventColumnModel> getColumnsByType(){
        setColumnModelsByType(Arrays.asList(columnTypes));
        return columnModels;
    }

    private void setColumnModelsByType(List<String> columnTypes){
        columnModels = new ArrayList<EventColumnModel>();
        List<EventModel> allEvents = parseService.getEvents(new EventConfigModel());

        for(String type : columnTypes){
            columnModels.add(new EventColumnModel(type, getEventGroupByType(allEvents, type)));
        }

        columnModels.add(new EventColumnModel(otherTypeColName, allEvents));//all remaining
    }

    private List<EventModel> getEventGroupByType(List<EventModel> allEvents, String type){

        List<EventModel> result = new ArrayList<EventModel>();
        for(EventModel event : allEvents){
            if (event.getType().equalsIgnoreCase(type)){
                result.add(event);
            }
        }
        allEvents.removeAll(result);//need this for "other" column calculation
        return result;
    }

    ////COLUMNS-BY-TOPIC METHODS:////

    public List<EventColumnModel> getColumnsByTopic(){
        setColumnModelsByTopic(Arrays.asList(columnTopics));
        return columnModels;
    }

    private void setColumnModelsByTopic(List<String> columnTopics){
        columnModels = new ArrayList<EventColumnModel>();
        List<EventModel> allEvents = parseService.getEvents(new EventConfigModel());

        for(String topic : columnTopics){
            columnModels.add(new EventColumnModel(topic, getEventGroupByTopic(allEvents, topic)));
        }
        columnModels.add(new EventColumnModel(otherTopicColName, allEvents));//all remaining
    }

    private List<EventModel> getEventGroupByTopic(List<EventModel> allEvents, String topic){

        List<EventModel> result = new ArrayList<EventModel>();
        for(EventModel event : allEvents){
            if (event.getTopic().equalsIgnoreCase(topic)){
                result.add(event);
            }
        }
        allEvents.removeAll(result);//need this for "other" column calculation
        return result;
    }

    //// JS JSON RENDERING ////

    public String getJson(){
        EventConfigModel cModel = new EventConfigModel();
        String jsonString = parseService.getServletResponse(cModel.getPath(), cModel.getLogin(), cModel.getPassword());
        return jsonString;
    }
}
