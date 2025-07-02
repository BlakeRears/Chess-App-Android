package models;

import java.util.Comparator;

public class AlphaComparator implements Comparator<Recording> {
    public int compare(Recording first, Recording second) {
        int i = first.getName().toLowerCase().compareTo(second.getName().toLowerCase());
        return i;
    }
}