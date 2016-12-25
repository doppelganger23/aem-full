package com.epam.sapustraining.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Uladzimir_Stsiatsko on 5/6/2016.
 */
@Model(adaptables = Resource.class)
public class DateModel {

    private Logger logger = LoggerFactory.getLogger(DateModel.class);

    @Inject
    private String articleDate;

    private SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yy");
    private SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd, YYYY");

    public String getDate() {
        if(articleDate == null){
            return "date not specified";
        }

        String result = "date parse error";
        try {
            Date date = inputFormat.parse(articleDate);
            result = outputFormat.format(date);
        } catch (ParseException e) {
           logger.error(e.getMessage());
        }

        return result;
    }

}
