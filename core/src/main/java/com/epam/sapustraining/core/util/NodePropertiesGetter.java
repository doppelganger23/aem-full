package com.epam.sapustraining.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.Arrays;

/**
 * Created by Uladzimir_Stsiatsko on 4/18/2016.
 */
public class NodePropertiesGetter {

    private Logger logger = LoggerFactory.getLogger(NodePropertiesGetter.class);

    private Node node;

    public NodePropertiesGetter(Node node){
        this.node = node;
    }

    public String getPropertyValue(String name){
        try {
            return node.getProperty(name).getString();
        } catch (RepositoryException e) {
            logger.error(Arrays.toString(e.getStackTrace()));
            return "";
        }
    }


}
