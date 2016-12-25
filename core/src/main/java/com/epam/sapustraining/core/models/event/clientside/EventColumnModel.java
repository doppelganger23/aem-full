package com.epam.sapustraining.core.models.event.clientside;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Uladzimir_Stsiatsko on 4/8/2016.
 */
@Model(adaptables = Resource.class)
public class EventColumnModel {

    private String columnName = "column";

    private List<EventModel> eventModels = new ArrayList<EventModel>();

    public EventColumnModel(){}
    public EventColumnModel(String columnName, List<EventModel> eventModels){
        this.columnName = columnName;
        this.eventModels = eventModels;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public List<EventModel> getEventModels() {
        return eventModels;
    }

    public void setEventModels(List<EventModel> eventModels) {
        this.eventModels = eventModels;
    }
}
