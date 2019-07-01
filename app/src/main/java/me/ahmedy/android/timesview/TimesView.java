package me.ahmedy.android.timesview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.List;

import me.ahmedy.android.timesview.data.TimePeriod;


public class TimesView extends View {
    private int clockBackgroundColor;
    private int clockTimeTextColor = Color.BLACK;
    private int am_pmTextColor = 0xFF8F8F8F;

    private List<TimePeriod> times;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int selectedEvent = -1;

    Rect rect = new Rect();
    RectF leftRect = new RectF();
    RectF rightRect = new RectF();

    public TimesView(Context context) {
        super(context);
        init(null, 0);
    }

    public TimesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TimesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TimesView, defStyle, 0);

        clockBackgroundColor = getResources().getColor(R.color.colorPrimary, null);

        clockBackgroundColor = a.getColor(R.styleable.TimesView_clockBackgroundColor, clockBackgroundColor);
        clockTimeTextColor = a.getColor(R.styleable.TimesView_clockTimeTextColor, clockTimeTextColor);
        am_pmTextColor = a.getColor(R.styleable.TimesView_AMPMTextColor, am_pmTextColor);

        a.recycle();

    }

    private static final int clockPadding = 20;
    private static final int distanceBetweenClocks = 30;
    int offsetAM = 0;
    int offsetPM = 0;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float r = Math.max(getHeight(), getWidth()) / 4.5f - clockPadding;

        paint.setColor(clockBackgroundColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getWidth()/2f-r-distanceBetweenClocks, getHeight()/2f, r+clockPadding, paint);
        canvas.drawCircle(getWidth()/2f+r+distanceBetweenClocks, getHeight()/2f, r+clockPadding, paint);

        leftRect.set(Math.round((getWidth()/2f-r-distanceBetweenClocks)-(r+clockPadding)), Math.round((getHeight()/2f)-(r+clockPadding)), Math.round((getWidth()/2f-r-distanceBetweenClocks)+(r+clockPadding)), Math.round((getHeight()/2f)+(r+clockPadding)));
        rightRect.set(Math.round((getWidth()/2f+r+distanceBetweenClocks)-(r+clockPadding)), Math.round((getHeight()/2f)-(r+clockPadding)), Math.round((getWidth()/2f+r+distanceBetweenClocks)+(r+clockPadding)), Math.round((getHeight()/2f)+(r+clockPadding)));

        if(times != null && times.size() > 0){
            offsetAM = 10;
            offsetPM = 10;
            for(int i = 0; i < times.size(); i++) {
                TimePeriod time = times.get(i);
                paint.setColor(time.getDrawColor());
                if(selectedEvent != -1 && i != selectedEvent){
                    paint.setAlpha(100);
                } else paint.setAlpha(255);
                Calendar c = time.getStartTime();
                boolean isAM = c.get(Calendar.HOUR_OF_DAY) < 12;
                int startHour = c.get(Calendar.HOUR_OF_DAY);
                int startMin = c.get(Calendar.MINUTE);
                float start = 270 + ((startHour % 12)*30) + startMin/2f;
                c = time.getEndTime();
                float end ;
                boolean hasSecondArc = false;
                if(isAM == c.get(Calendar.HOUR_OF_DAY) < 12){
                    end = (((c.get(Calendar.HOUR_OF_DAY) - startHour))*30) + (c.get(Calendar.MINUTE)-startMin)/2f;
                } else {
                    end = ((12 - (startHour + (startMin!=0? 0:1)))*30) + (60-startMin)/2f;
                    Log.d("end angle", end+"");
                    hasSecondArc = true;
                }
                int currentColor = paint.getColor();
                paint.setStyle(Paint.Style.FILL);
                canvas.drawArc((isAM? leftRect : rightRect), start, end, true, paint);
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.GRAY);
                paint.setStrokeWidth(1);
                canvas.drawArc((isAM? leftRect : rightRect), start, end, true, paint);
                if(hasSecondArc){
                    end = (((c.get(Calendar.HOUR_OF_DAY) - 12))*30) + c.get(Calendar.MINUTE)/2f;
                    paint.setColor(currentColor);
                    if(selectedEvent != -1 && i != selectedEvent){
                        paint.setAlpha(100);
                    } else paint.setAlpha(255);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawArc( rightRect, 270, end, true, paint);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setColor(Color.GRAY);
                    paint.setStrokeWidth(1);
                    canvas.drawArc(rightRect, 270, end, true, paint);
                }
                if(isAM) {
                    leftRect.set(Math.round((getWidth() / 2f - r - distanceBetweenClocks) - (r + clockPadding - offsetAM)), Math.round((getHeight() / 2f) - (r + clockPadding - offsetAM)), Math.round((getWidth() / 2f - r - distanceBetweenClocks) + (r + clockPadding - offsetAM)), Math.round((getHeight() / 2f) + (r + clockPadding - offsetAM)));
                    offsetAM +=10;
                }
                if(!isAM || hasSecondArc){
                    rightRect.set(Math.round((getWidth()/2f+r+distanceBetweenClocks)-(r+clockPadding- offsetPM)), Math.round((getHeight()/2f)-(r+clockPadding- offsetPM)), Math.round((getWidth()/2f+r+distanceBetweenClocks)+(r+clockPadding- offsetPM)), Math.round((getHeight()/2f)+(r+clockPadding- offsetPM)));
                    offsetPM +=10;
                }
            }
        }
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(clockTimeTextColor);
        paint.setTextSize(18);
        int[] numbers = {1,2,3,4,5,6,7,8,9,10,11,12};
        for (int number : numbers) {
            String tmp = String.valueOf(number);
            paint.getTextBounds(tmp, 0, tmp.length(), rect);
            double angle = Math.PI / 6 * (number - 3);
            int x = (int) (getWidth()/2f-r-distanceBetweenClocks + Math.cos(angle) * r - rect.width() / 2);
            int y = (int) (getHeight()/2f + Math.sin(angle) * r + rect.height() / 2f);
            canvas.drawText(tmp, x, y, paint);
            x = (int) (getWidth()/2f+r+distanceBetweenClocks + Math.cos(angle) * r - rect.width() / 2f);
            canvas.drawText(tmp, x, y, paint);
        }

        paint.setColor(am_pmTextColor);
        paint.setTextSize(24);
        canvas.drawText("AM", getWidth()/2f-r-distanceBetweenClocks-19, getHeight()/2f - r - 40, paint);
        canvas.drawText("PM", getWidth()/2f+r+distanceBetweenClocks-19, getHeight()/2f - r - 40, paint);
    }

    public List<TimePeriod> getTimePeriods() {
        return times;
    }

    public void setTimePeriods(List<TimePeriod> timePeriods) {
        this.times = timePeriods;
        clearSelection();
    }

    public int getSelectedTimePeriod(){return this.selectedEvent;}

    public void selectTimePeriod(int index){
        if(index >= times.size() || index < 0) throw new ArrayIndexOutOfBoundsException("There is no time index: " + index);
        this.selectedEvent = index;
        this.invalidate();
    }

    public void clearSelection(){
        this.selectedEvent = -1;
        this.invalidate();
    }

    public int getClockBackgroundColor() {
        return clockBackgroundColor;
    }

    public void setClockBackgroundColor(int clockBackgroundColor) {
        this.clockBackgroundColor = clockBackgroundColor;
        this.invalidate();
    }

    public int getClockTimeTextColor() {
        return clockTimeTextColor;
    }

    public void setClockTimeTextColor(int clockTimeTextColor) {
        this.clockTimeTextColor = clockTimeTextColor;
        this.invalidate();
    }

    public int getAm_pmTextColor() {
        return am_pmTextColor;
    }

    public void setAm_pmTextColor(int am_pmTextColor) {
        this.am_pmTextColor = am_pmTextColor;
        this.invalidate();
    }
}
