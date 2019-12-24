package com.team9889.ftc2019.test.local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by Eric on 12/16/2019.
 */

public class ListTest {
    private Map a = new HashMap();
    private Map[] aMap;
    private ArrayList aL = new ArrayList<Integer>();

    public void Start(){
        a.put("hi", 1);

        if (!a.containsKey("hi")){
            a.put("hi", "test");
        }

//        aMap[0]

        String cord = "1, 2, 3";

        aL.add(cord.split(","));
    }
}