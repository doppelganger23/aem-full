package com.epam.sapustraining.core.models;

import com.epam.sapustraining.core.services.EventCollectorService;
import com.epam.sapustraining.core.util.NodePropertiesGetter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Uladzimir_Stsiatsko on 3/25/2016.
 */

@Model(adaptables = Resource.class)
public class LinkSectionModel {

    private Logger logger = LoggerFactory.getLogger(EventCollectorService.class);

    @Self
    private Resource resource;

    private List<LinkModel> links = new ArrayList<LinkModel>();

    private Node thisNode;

    private NodePropertiesGetter propGetter;

    @PostConstruct
    protected void init(){
        populateLinks();
    }

    private void populateLinks(){
        thisNode = resource.adaptTo(Node.class);

        try {
            Node linksNode = thisNode.getNode("links");
            NodeIterator iterator = linksNode.getNodes();

            while(iterator.hasNext()){
                Node node = iterator.nextNode();
                propGetter = new NodePropertiesGetter(node);
                LinkModel link = new LinkModel();

                link.setUrl(propGetter.getPropertyValue("url"));
                link.setName(propGetter.getPropertyValue("description"));
                link.setCssClass(propGetter.getPropertyValue("cssClass"));
                links.add(link);
            }
        } catch (RepositoryException e) {
            logger.error(Arrays.toString(e.getStackTrace()));
        }
    }

    public List<LinkModel> getLinks() {
        return links;
    }

    public boolean isEmpty(){
        return links.isEmpty();
    }
}
