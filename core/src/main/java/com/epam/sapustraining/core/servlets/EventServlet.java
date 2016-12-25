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

    //working on this
    @Reference
    private ResourceResolverFactory factory;
    private static final String SERVICE_ACCOUNT_IDENTIFIER = "my-system-user";

    ////METHODS////

    @Activate
    protected void activate(final Map<String, Object> config) {
        logger.info("ACTIVATION OF EVENT SERVLET");
        this.adminPagePath = PropertiesUtil.toString(config.get(ADMIN_PAGE_PATH_PROPERTY_NAME), null);
        this.eventsParentNodeSubpath = PropertiesUtil.toString(config.get(EVENTS_PARENT_NODE_SUBPATH_PROPERTY_NAME), null);
    }

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException {

        //////TODO: change following 2 lines to use Service Resource Resolver ("my-system-user" with no password)
        //final Resource resource = request.getResourceResolver().getResource(adminPagePath + JcrConstants.JCR_CONTENT + eventsParentNodeSubpath);
        //Node eventsParentNode = resource.adaptTo(Node.class);
        /////////
        Node eventsParentNode = this.getIt();
        //////////////

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

    //working on it
    //TODO: fix errors and use abowe
    private Node getIt(){
        Map<String, Object> param = new HashMap<String, Object>();
        ResourceResolver resourceResolver = null;
        Resource resource = null;
        Node node = null;
        param.put(ResourceResolverFactory.SUBSERVICE,"testService");
        try {
            //resourceResolver = factory.getServiceResourceResolver(param);
            resourceResolver = factory.getAdministrativeResourceResolver(param);//works with that line

            resource = resourceResolver.getResource(adminPagePath + JcrConstants.JCR_CONTENT + eventsParentNodeSubpath);
            node = resource.adaptTo(Node.class);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return node;
    }

    //working on it
    //TODO: fix errors and use abowe
    private Node getEventsNode() {
        ResourceResolver serviceResolver = null;
        Node node = null;
        try {
            Map<String, Object> authInfo = new HashMap<String, Object>();
            authInfo.put(ResourceResolverFactory.USER,"my-system-user");
            serviceResolver = factory.getServiceResourceResolver(authInfo);

            if(serviceResolver != null) {
                // Do some work w your service resource resolver
                Resource resource = serviceResolver.getResource(adminPagePath + JcrConstants.JCR_CONTENT + eventsParentNodeSubpath);
                node = resource.adaptTo(Node.class);
            } else {
                logger.error("Could not obtain a CRX User for the Service: " + SERVICE_ACCOUNT_IDENTIFIER);
            }
        } catch (LoginException e){
            logger.error(e.getMessage());
        }
        return node;
    }

    //working on it
    //TODO: fix errors and use abowe
    private Node getNode(){
        Resource res = null;
        Map<String, Object> param = new HashMap<String, Object>();
        param.put(ResourceResolverFactory.SUBSERVICE, "readService");
        ResourceResolver resolver = null;
        try {
            resolver = factory.getServiceResourceResolver(param);
            logger.info(resolver.getUserID());
            res = resolver.getResource(adminPagePath + JcrConstants.JCR_CONTENT + eventsParentNodeSubpath);
            ValueMap readMap = res.getValueMap();
            logger.info(readMap.get("jcr:primaryType", ""));
            ModifiableValueMap modMap = res.adaptTo(ModifiableValueMap.class);
            if(modMap != null){
                modMap.put("my-system-user", "");
                resolver.commit();
                logger.info("Successfully saved");
            }
        } catch (LoginException e) {
            logger.error("LoginException",e);
        } catch (PersistenceException e) {
            logger.error("LoginException",e);
        }finally{
            if(resolver != null && resolver.isLive()){
                resolver.close();
            }
        }
        return res.adaptTo(Node.class);
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
