package io.github.sahilshekhawat.pockethackernews.Data;

import java.util.ArrayList;
import java.util.HashMap;

public class Data {
    public static ArrayList<Long> topStories = new ArrayList<>();
    public static  ArrayList<Long> newStories = new ArrayList<>();
    public static  ArrayList<Long> bestStories = new ArrayList<>();
    public static  ArrayList<Long> askStories = new ArrayList<>();
    public static  ArrayList<Long> showStories = new ArrayList<>();
    public static  ArrayList<Long> jobStories = new ArrayList<>();

    public static ArrayList<Items> topStoryItems = new ArrayList<>();
    public static ArrayList<Items> newStoryItems = new ArrayList<>();
    public static ArrayList<Items> bestStoryItems = new ArrayList<>();
    public static ArrayList<Items> askStoryItems = new ArrayList<>();
    public static ArrayList<Items> showStoryItems = new ArrayList<>();
    public static ArrayList<Items> jobStoryItems = new ArrayList<>();

    public static ArrayList<Items> comments = new ArrayList<>();

    public static HashMap<Long, Items> items = new HashMap<>();


    public static void addItem(String storyType, int position, Items item){
        if(storyType.equals(StoryType.TOPSTORIES)){
            topStoryItems.add(position, item);
        }
        if(storyType.equals(StoryType.NEWSTORIES)){
            newStoryItems.add(position, item);
        }
        if(storyType.equals(StoryType.BESTSTORIES)){
            bestStoryItems.add(position, item);
        }
        if(storyType.equals(StoryType.ASKSTORIES)){
            askStoryItems.add(position, item);
        }
        if(storyType.equals(StoryType.SHOWSTORIES)){
            showStoryItems.add(position, item);
        }
        if(storyType.equals(StoryType.JOBSTORIES)){
            jobStoryItems.add(position, item);
        }
    }

    public static void addItem(String storyType, Items item){
        if(storyType.equals(StoryType.TOPSTORIES)){
            topStoryItems.add(item);
        }
        if(storyType.equals(StoryType.NEWSTORIES)){
            newStoryItems.add(item);
        }
        if(storyType.equals(StoryType.BESTSTORIES)){
            bestStoryItems.add(item);
        }
        if(storyType.equals(StoryType.ASKSTORIES)){
            askStoryItems.add(item);
        }
        if(storyType.equals(StoryType.SHOWSTORIES)){
            showStoryItems.add(item);
        }
        if(storyType.equals(StoryType.JOBSTORIES)){
            jobStoryItems.add(item);
        }
    }

    public static void setItem(String storyType, int position, Items item){
        if(storyType.equals(StoryType.TOPSTORIES)){
            topStoryItems.set(position, item);
        }
        if(storyType.equals(StoryType.NEWSTORIES)){
            newStoryItems.set(position, item);
        }
        if(storyType.equals(StoryType.BESTSTORIES)){
            bestStoryItems.set(position, item);
        }
        if(storyType.equals(StoryType.ASKSTORIES)){
            askStoryItems.set(position, item);
        }
        if(storyType.equals(StoryType.SHOWSTORIES)){
            showStoryItems.set(position, item);
        }
        if(storyType.equals(StoryType.JOBSTORIES)){
            jobStoryItems.set(position, item);
        }
    }

    public static Items getItem(String storyType, int position){
        if(storyType.equals(StoryType.TOPSTORIES)){
            return topStoryItems.get(position);
        }
        if(storyType.equals(StoryType.NEWSTORIES)){
            return newStoryItems.get(position);
        }
        if(storyType.equals(StoryType.BESTSTORIES)){
            return bestStoryItems.get(position);
        }
        if(storyType.equals(StoryType.ASKSTORIES)){
            return askStoryItems.get(position);
        }
        if(storyType.equals(StoryType.SHOWSTORIES)){
            return showStoryItems.get(position);
        }
        if(storyType.equals(StoryType.JOBSTORIES)){
            return jobStoryItems.get(position);
        }
        return null;
    }



    public static ArrayList<Items> getAllItems(String storyType){
        if(storyType.equals(StoryType.TOPSTORIES)){
            return topStoryItems;
        }
        if(storyType.equals(StoryType.NEWSTORIES)){
            return newStoryItems;
        }
        if(storyType.equals(StoryType.BESTSTORIES)){
            return bestStoryItems;
        }
        if(storyType.equals(StoryType.ASKSTORIES)){
            return askStoryItems;
        }
        if(storyType.equals(StoryType.SHOWSTORIES)){
            return showStoryItems;
        }
        if(storyType.equals(StoryType.JOBSTORIES)){
            return jobStoryItems;
        }
        return null;
    }

    public static ArrayList<Long> getAllStories(String storyType){
        if(storyType.equals(StoryType.TOPSTORIES)){
            return topStories;
        }
        if(storyType.equals(StoryType.NEWSTORIES)){
            return newStories;
        }
        if(storyType.equals(StoryType.BESTSTORIES)){
            return bestStories;
        }
        if(storyType.equals(StoryType.ASKSTORIES)){
            return askStories;
        }
        if(storyType.equals(StoryType.SHOWSTORIES)){
            return showStories;
        }
        if(storyType.equals(StoryType.JOBSTORIES)){
            return jobStories;
        }
        return null;
    }

    public static void setAllItems(String storyType, ArrayList<Items> newItems){
        if(storyType.equals(StoryType.TOPSTORIES)){
            topStoryItems = newItems;
        }
        if(storyType.equals(StoryType.NEWSTORIES)){
            newStoryItems = newItems;
        }
        if(storyType.equals(StoryType.BESTSTORIES)){
            bestStoryItems = newItems;
        }
        if(storyType.equals(StoryType.ASKSTORIES)){
            askStoryItems = newItems;
        }
        if(storyType.equals(StoryType.SHOWSTORIES)){
            showStoryItems = newItems;
        }
        if(storyType.equals(StoryType.JOBSTORIES)){
            jobStoryItems = newItems;
        }
    }




    public static int itemContains(String storyType, Long id){
        ArrayList<Items> dataSet = new ArrayList<>();
        if(storyType.equals(StoryType.TOPSTORIES)){
            dataSet = topStoryItems;
        }
        if(storyType.equals(StoryType.NEWSTORIES)){
            dataSet = newStoryItems;
        }
        if(storyType.equals(StoryType.BESTSTORIES)){
            dataSet = bestStoryItems;
        }
        if(storyType.equals(StoryType.ASKSTORIES)){
            dataSet = askStoryItems;
        }
        if(storyType.equals(StoryType.SHOWSTORIES)){
            dataSet = showStoryItems;
        }
        if(storyType.equals(StoryType.JOBSTORIES)){
            dataSet = jobStoryItems;
        }
        for(int i=0; i<dataSet.size(); i++){
            if(id.equals(dataSet.get(i).getId())){
                return i;
            }
        }
        return -1;
    }

    public static void removeItem(String storyType, int position){
        if(storyType.equals(StoryType.TOPSTORIES)){
            topStoryItems.remove(position);
        }
        if(storyType.equals(StoryType.NEWSTORIES)){
            newStoryItems.remove(position);
        }
        if(storyType.equals(StoryType.BESTSTORIES)){
            bestStoryItems.remove(position);
        }
        if(storyType.equals(StoryType.ASKSTORIES)){
            askStoryItems.remove(position);
        }
        if(storyType.equals(StoryType.SHOWSTORIES)){
            showStoryItems.remove(position);
        }
        if(storyType.equals(StoryType.JOBSTORIES)){
            jobStoryItems.remove(position);
        }
    }


}
