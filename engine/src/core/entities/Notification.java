package core.entities;

import javafx.beans.property.SimpleBooleanProperty;

import java.util.HashMap;

public class Notification{



    private String title;
    private int date;
    private String content;



    public Notification(String title,int date,String content) {

        this.title = title;
        this.date = date;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public int getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }



    @Override
    public boolean equals(Object o) {

        if(o == null){
            return false;
        }

        int compareData=((Notification)o).getDate();
        String compareContent = ((Notification)o).getContent();
        String compareTitle = ((Notification)o).getTitle();

        return this.date == compareData &&
               this.content.equals(compareContent) &&
               this.title.equals(compareTitle);

    }
}
