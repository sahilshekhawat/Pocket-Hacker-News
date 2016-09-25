package io.github.sahilshekhawat.pockethackernews.Utils;

import java.util.Date;

import io.github.sahilshekhawat.pockethackernews.Data.Data;
import io.github.sahilshekhawat.pockethackernews.Data.Items;

/**
 * Created by sahil on 9/25/16.
 */

public class ConvertTime {

    public ConvertTime() {
    }

    public static String toHumanReadable(Long time){
        Date postTime = new Date(time*1000);
        Date currTime = new Date();
        long diff = currTime.getTime() - postTime.getTime();
        long diffSeconds = (diff / 1000) % 60;
        long diffMinutes = (diff / (60 * 1000)) % 60;
        long diffHours = (diff / (60 * 60 * 1000)) % 24;
        int diffInDays = (int) ((currTime.getTime() - postTime.getTime()) / (1000 * 60 * 60 * 24));
        String assignedTime = "";
        if (diffInDays > 1) {
            assignedTime = Long.toString(diffInDays) + " days ago";
        } else if (diffInDays == 1) {
            assignedTime = (Long.toString(diffInDays) + " day and " +
                    Long.toString(diffHours) + " hours ago");
        }else if (diffHours > 1) {
            assignedTime = Long.toString(diffHours) + " hours ago";
        } else if (diffHours == 1) {
            assignedTime = (Long.toString(diffHours) + " hour and " +
                    Long.toString(diffMinutes) + " minutes ago");
        } else if (diffMinutes > 1) {
            assignedTime = Long.toString(diffMinutes) + " minutes ago";
        } else if (diffMinutes == 1) {
            assignedTime = Long.toString(diffMinutes) + " minute ago";
        }
        else{
            assignedTime = "seconds ago";
        }
        return assignedTime;
    }


    public static String toSmallHumanReadable(Long time){
        Date postTime = new Date(time*1000);
        Date currTime = new Date();
        long diff = currTime.getTime() - postTime.getTime();
        long diffSeconds = (diff / 1000) % 60;
        long diffMinutes = (diff / (60 * 1000)) % 60;
        long diffHours = (diff / (60 * 60 * 1000)) % 24;
        int diffInDays = (int) ((currTime.getTime() - postTime.getTime()) / (1000 * 60 * 60 * 24));
        String assignedTime = "";
        if (diffInDays > 1) {
            assignedTime = Long.toString(diffInDays) + " d";
        } else if (diffInDays == 1) {
            assignedTime = (Long.toString(diffInDays) + " d " +
                    Long.toString(diffHours) + " h");
        }else if (diffHours > 1) {
            assignedTime = Long.toString(diffHours) + " h";
        } else if (diffHours == 1) {
            assignedTime = (Long.toString(diffHours) + " h " +
                    Long.toString(diffMinutes) + " m");
        } else if (diffMinutes > 1) {
            assignedTime = Long.toString(diffMinutes) + " m";
        } else if (diffMinutes == 1) {
            assignedTime = Long.toString(diffMinutes) + " m";
        }
        else{
            assignedTime = "secs";
        }
        return assignedTime;
    }
}
