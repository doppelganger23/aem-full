package com.epam.sapustraining.core.servlets;

import com.day.cq.commons.jcr.JcrConstants;
import com.epam.sapustraining.core.models.event.clientside.EventModel;
import com.epam.sapustraining.core.services.EventCollectorService;
import com.epam.sapustraining.core.util.MainColumnsChoser;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import java.io.IOException;
import java.util.*;

/**
 * Created by Uladzimir_Stsiatsko on 4/5/2016.
 */
@SuppressWarnings("serial")
@SlingServlet(
        name = "com.epam.sapustraining.core.servlets.EventServlet",
        metatype = true,
        label = "Event Servlet",
        description = "Servlet which parses events and returns JSON string",
        paths = "/bin/getEvents"
)
public class EventServlet extends SlingAllMethodsServlet {

    private Logger logger = LoggerFactory.getLogger(EventServlet.class);

    @Reference
    private EventCollectorService eventCollectorService;

    @Property(
            label = "Admin Page URL",
            description = "Relative to '/apps' path to admin event page, surrounded with slashes",
            value = "/content/sapustraining/admin-event/")
    private static final String ADMIN_PAGE_PATH_PROPERTY_NAME = "admin.page.path";
    private String adminPagePath;

    @Property(
            label = "Events Parent Node Subpath",
            description = "Subpath to event-holding node under admin event page, begins with a slash ",
            value = "/par")
    private static final String EVENTS_PARENT_NODE_SUBPATH_PROPERTY_NAME = "parent.node.subpath";
    private String eventsParentNodeSubpath;

    @Reference
    private ResourceResolverFactory factory;
    private static final String SERVICE_ACCOUNT_IDENTIFIER = "my-system-user";

    @Activate
    protected void activate(final Map<String, Object> config) {
        logger.info("ACTIVATION OF EVENT SERVLET");
        this.adminPagePath = PropertiesUtil.toString(config.get(ADMIN_PAGE_PATH_PROPERTY_NAME), null);
        this.eventsParentNodeSubpath = PropertiesUtil.toString(config.get(EVENTS_PARENT_NODE_SUBPATH_PROPERTY_NAME), null);
    }

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException {

        final Resource resource = request.getResourceResolver().getResource(adminPagePath + JcrConstants.JCR_CONTENT + eventsParentNodeSubpath);
        Node eventsParentNode = resource.adaptTo(Node.class);

        String jsonEventsString = null;
        List<EventModel> eventModels = null;
        try {
            eventModels = eventCollectorService.getObjectsFromNode(eventsParentNode);
            jsonEventsString = convertEventsToJson(eventModels);
        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()));
        }
        response.getWriter().write(jsonEventsString);
    }

    private String convertEventsToJson(List<EventModel> eventModels) throws JSONException {
        JSONObject listObject = new JSONObject();

        //populating json with events
        listObject.put("events", new ArrayList());
        Iterator<EventModel> iterator = eventModels.iterator();

        List<String> typeList = new ArrayList<String>();
        List<String> topicList = new ArrayList<String>();

        while (iterator.hasNext()) {
            EventModel eventModel = iterator.next();

            JSONObject jsonObject = new JSONObject();

            typeList.add(eventModel.getType());
            topicList.add(eventModel.getTopic());

            jsonObject.put("type", eventModel.getType());
            jsonObject.put("topic", eventModel.getTopic());
            jsonObject.put("date", eventModel.getDate());
            jsonObject.put("titleText", eventModel.getTitleText());
            jsonObject.put("titleLink", eventModel.getTitleLink());
            jsonObject.put("description", eventModel.getDescription());

            listObject.accumulate("events", jsonObject);
        }

        //configuring main columns
        MainColumnsChoser choser = new MainColumnsChoser();
        listObject.put("types", choser.mainColumns(typeList));
        listObject.put("topics", choser.mainColumns(topicList));

        return listObject.toString();
    }

}
