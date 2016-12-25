package com.epam.sapustraining.core.services.impl;

import com.epam.sapustraining.core.models.event.clientside.EventModel;
import com.epam.sapustraining.core.services.EventCollectorService;
import com.epam.sapustraining.core.util.NodePropertiesGetter;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Uladzimir_Stsiatsko on 4/13/2016.
 */

@Component(immediate = true)
@Service
public class EventCollectorServiceImpl implements EventCollectorService {

    private static final String EVENT_RESOURCE_TYPE = "sapustraining/components/content/events/event";
    private static final String RESOURCE_TYPE_PROP_NAME = "sling:resourceType";

    @Override
    public List<EventModel> getObjectsFromNode(Node eventsParentNode) throws RepositoryException {
        List<EventModel> eventModels = new ArrayList<EventModel>();

        NodeIterator iterator = eventsParentNode.getNodes();
        while (iterator.hasNext()){
            Node node = iterator.nextNode();
            NodePropertiesGetter getter = new NodePropertiesGetter(node);

            if (getter.getPropertyValue(RESOURCE_TYPE_PROP_NAME).equals(EVENT_RESOURCE_TYPE)) {
                EventModel event = new EventModel();

                event.setType(getter.getPropertyValue("eventType"));
                event.setTopic(getter.getPropertyValue("eventTopic"));
                event.setDate(getter.getPropertyValue("eventDate"));
                event.setTitleText(getter.getPropertyValue("eventTitleText"));
                event.setTitleLink(getter.getPropertyValue("eventTitleLink"));
                event.setDescription(getter.getPropertyValue("eventDescription"));

                eventModels.add(event);
            }
        }

        return eventModels;
    }

}
