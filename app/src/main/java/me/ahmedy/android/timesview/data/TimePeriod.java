package me.ahmedy.android.timesview.data;

import android.graphics.Color;

import java.util.Calendar;

public class TimePeriod {

    private Calendar startTime;
    private Calendar endTime;
    private int drawColor;

    private static final int DEFAULT_COLOR = Color.WHITE;

    public TimePeriod(Calendar startTime, Calendar endTime)  throws EndTimeBeforeStartTimeException {
        this(startTime, endTime, DEFAULT_COLOR);
    }

    public TimePeriod(Calendar startTime, Calendar endTime, int drawColor) throws EndTimeBeforeStartTimeException {
        if(startTime.after(endTime)) throw new EndTimeBeforeStartTimeException();
        this.startTime = startTime;
        this.endTime = endTime;
        this.drawColor = drawColor;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public int getDrawColor() {
        return drawColor;
    }

    public void setDrawColor(int drawColor) {
        this.drawColor = drawColor;
    }

    class EndTimeBeforeStartTimeException extends IllegalArgumentException {
        public EndTimeBeforeStartTimeException() {
            super("End time can not be before Start time.");
        }
    }
}
