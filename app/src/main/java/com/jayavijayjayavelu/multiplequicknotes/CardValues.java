package com.jayavijayjayavelu.multiplequicknotes;

/**
 * Created by jayavijayjayavelu on 2/20/17.
 */

public class CardValues {
    private String notesName;
    private String sampleNotes;
    private String time1;
    private int intValue;
    public CardValues(String name, String sample, String time, int intValue) {
        notesName = name;
        sampleNotes = sample;
        time1=time;
        this.intValue = intValue;
    }

    public String getNotesName() {
        return notesName;
    }

    public String getSampleNotes() {
        return sampleNotes;
    }
    public String getTime1(){
        return time1;
    }

    public int getIntValue() {
        return intValue;
    }
}
