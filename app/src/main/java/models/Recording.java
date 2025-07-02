package models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class Recording {

    private String name;
    private String date;

    public Recording(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public void setName(String str) {
        name = str;
    }

    public void setDate(String str) {
        date = str;
    }

    public String toString() {
        return name + "     " + date;
    }

    public static void sortByDate(ArrayList<Recording> list) {
        Comparator<Recording> comparator = new Comparator<Recording>() {
            @Override
            public int compare(Recording o1, Recording o2) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date1 = dateFormat.parse(o1.getDate());
                    Date date2 = dateFormat.parse(o2.getDate());
                    return date1.compareTo(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        };
        list.sort(comparator);
    }
}
