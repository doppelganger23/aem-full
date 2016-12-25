package com.epam.sapustraining.core.util;

import java.util.*;

/**
 * Created by Uladzimir_Stsiatsko on 4/29/2016.
 */
public class MainColumnsChoser {

    private int columnTypesQuantity = 3;

    public MainColumnsChoser() {
    }

    public MainColumnsChoser(int namedColumnQuantity) {
        this.columnTypesQuantity = namedColumnQuantity;
    }

    public List<String> mainColumns(List<String> list) {
        List<String> result = new ArrayList<String>();
        TreeMap<String, Integer> map = new TreeMap<String, Integer>();

        //counting entries
        for (String element : list) {
            if (map.containsKey(element)) {
                map.put(element, map.get(element) + 1);
            } else {
                map.put(element, 1);
            }
        }

        //repeat for each column except "other"
        for (int i = 0; i < columnTypesQuantity; i++) {
            if (!map.isEmpty()) {

                String leadElement = map.firstKey();
                int maxQuantity = map.get(leadElement);

                for (String key : map.keySet()) {
                    if (key.equalsIgnoreCase("other")) {
                        continue;
                    }
                    if (map.get(key) > maxQuantity) {
                        leadElement = key;
                        maxQuantity = map.get(key);
                    }
                }

                result.add(leadElement);
                map.remove(leadElement);
            }
        }

        return result;
    }

}
