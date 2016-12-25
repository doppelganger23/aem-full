package com.epam.sapustraining.core.models;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.apache.sling.models.annotations.Model;

import java.util.Map;

/**
 * Created by Uladzimir_Stsiatsko on 4/8/2016.
 */
@Component(
        name = "EventConfigModel",
        metatype = true,
        label = "Event Config",
        description = "Configuration for servlet access and authorisation",
        immediate = true,
        configurationFactory = true
)
@Model(adaptables = Resource.class)
@Properties({
        @Property(name = "full.get.request.path", value = "http://localhost:4502/bin/getEvents", description = "Absolute URL on which servlet is responding"),
        @Property(name = "login", value = "admin", description = "AEM Login"),
        @Property(name = "password", value = "admin", description = "AEM Password")
})
public class EventConfigModel {

    private static final String REQUEST_PATH_PROPERTY_NAME = "full.get.request.path";
    private static final String LOGIN_PROPERTY_NAME = "login";
    private static final String PASSWORD_PROPERTY_NAME = "password";
    private static final String ADMIN_DEFAULT_CREDENTIAL = "admin";
    private static final String SERVLET_PATH = "http://localhost:4502/bin/getEvents";

    private String path = SERVLET_PATH;
    private String login = ADMIN_DEFAULT_CREDENTIAL;
    private String password = ADMIN_DEFAULT_CREDENTIAL;

    @Activate
    protected void activate(final Map<String, Object> config){
        this.path = PropertiesUtil.toString(config.get(REQUEST_PATH_PROPERTY_NAME), SERVLET_PATH);
        this.login = PropertiesUtil.toString(config.get(LOGIN_PROPERTY_NAME), ADMIN_DEFAULT_CREDENTIAL);
        this.password = PropertiesUtil.toString(config.get(PASSWORD_PROPERTY_NAME), ADMIN_DEFAULT_CREDENTIAL);
    }

    public String getPath() {
        return path;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

}
