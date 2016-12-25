package com.epam.sapustraining.core.models;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Uladzimir_Stsiatsko on 3/30/2016.
 */

@Model(adaptables = SlingHttpServletRequest.class)
public class NavElementsModel {

    private static final String HOME_PAGE_NAME = "home";

    @Inject
    private Page currentPage;

    private List<Page> navPages = new ArrayList<Page>();

    private Page findParent(String parentName){
        Page thisPage = currentPage;

        while(thisPage.getParent() != null){
            if(thisPage.getName().equalsIgnoreCase(parentName)){
                break;
            }
            thisPage = thisPage.getParent();
        }
        return thisPage;
    }

    private void populateNavPages(Page parentPage){
        navPages.add(parentPage);

        Iterator<Page> iterator = parentPage.listChildren();
        while (iterator.hasNext()){
            Page child = iterator.next();
            navPages.add(child);
        }
    }

    public List<Page> getNavPages() {
        populateNavPages(findParent(HOME_PAGE_NAME));
        return navPages;
    }

}
