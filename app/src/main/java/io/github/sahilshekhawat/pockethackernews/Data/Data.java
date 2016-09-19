package io.github.sahilshekhawat.pockethackernews.Data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sahil on 9/18/16.
 */
public class Data {
    public ArrayList<Long> topStories;
    public ArrayList<Long> newStories;
    public ArrayList<Long> bestStories;
    public ArrayList<Long> askStories;
    public ArrayList<Long> showStories;
    public ArrayList<Long> jobStories;

    public static HashMap<Long, Items> items;

    public Data() {
        items = new HashMap<>();
    }


}
