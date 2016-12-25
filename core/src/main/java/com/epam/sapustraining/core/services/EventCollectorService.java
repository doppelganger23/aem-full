package com.epam.sapustraining.core.services;

import com.epam.sapustraining.core.models.event.clientside.EventModel;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.List;

/**
 * Created by Uladzimir_Stsiatsko on 4/13/2016.
 */
public interface EventCollectorService {

    List<EventModel> getObjectsFromNode(Node eventsParentNode) throws RepositoryException;

}
