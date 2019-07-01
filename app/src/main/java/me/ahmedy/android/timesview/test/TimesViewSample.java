package me.ahmedy.android.timesview.test;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.ahmedy.android.timesview.R;
import me.ahmedy.android.timesview.TimesView;
import me.ahmedy.android.timesview.data.TimePeriod;

public class TimesViewSample extends Activity {

    private TimesView timesView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_times_view);

        timesView = findViewById(R.id.sample_times_view);
        List<TimePeriod> timePeriods = new ArrayList<>();
        Calendar startTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        endTime.set(Calendar.HOUR_OF_DAY, 2);
        timePeriods.add(new TimePeriod(startTime, endTime, Color.LTGRAY));
        for(int i = 0; i < 6; i+=2){
            startTime = Calendar.getInstance();
            endTime = Calendar.getInstance();
            startTime.add(Calendar.HOUR, i);
            endTime.add(Calendar.HOUR, i+3);
            timePeriods.add(new TimePeriod(startTime, endTime, i==2? Color.LTGRAY : Color.WHITE));
        }
        timesView.setTimePeriods(timePeriods);
    }

    public void select(View view){
        switch (view.getId()){
            case R.id.select_0:
                if(timesView.getSelectedTimePeriod() == 0) timesView.clearSelection();
                else timesView.selectTimePeriod(0);
                break;
            case R.id.select_1:
                if(timesView.getSelectedTimePeriod() == 1) timesView.clearSelection();
                else timesView.selectTimePeriod(1);
                break;
            case R.id.select_2:
                if(timesView.getSelectedTimePeriod() == 2) timesView.clearSelection();
                else timesView.selectTimePeriod(2);
                break;
            case R.id.select_3:
                if(timesView.getSelectedTimePeriod() == 3) timesView.clearSelection();
                else timesView.selectTimePeriod(3);
                break;
        }
    }

}
