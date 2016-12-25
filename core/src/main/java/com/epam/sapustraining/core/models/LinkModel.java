package com.epam.sapustraining.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

/**
 * Created by Uladzimir_Stsiatsko on 3/25/2016.
 */

@Model(adaptables = Resource.class)
public class LinkModel {

    private String url;
    private String name;
    private String cssClass;

    public LinkModel(){}
    public LinkModel(String name, String url, String cssClass){
        this.name = name;
        this.url = url;
        this.cssClass = cssClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }


}
