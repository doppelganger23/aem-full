package com.epam.sapustraining.core.services;

import com.epam.sapustraining.core.models.EventConfigModel;
import com.epam.sapustraining.core.models.event.clientside.EventModel;

import java.util.List;

/**
 * Created by Uladzimir_Stsiatsko on 4/7/2016.
 */
public interface EventParserService {

    List<EventModel> getEvents(EventConfigModel config);

    String getServletResponse(String path, String login, String password);
}
