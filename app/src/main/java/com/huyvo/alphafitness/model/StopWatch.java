package com.huyvo.alphafitness.model;

import java.util.ArrayList;
import java.util.List;

public class StopWatch {

    public final static int ONE_MINUTE  = 3000;// 60000;
    public final static int FIVE_MINUTE = 300000;
    public final static int TWO_MINUTE = 120000;




    public interface GetTime {
        long now();
    }


    private GetTime SystemTime = new GetTime() {
        @Override
        public long now() {	return System.currentTimeMillis(); }
    };

    public enum State { PAUSED, RUNNING }

    private GetTime m_time;
    private long m_startTime;
    private long m_stopTime;
    private long m_pauseOffset;
    private List<Long> m_laps = new ArrayList<>();
    private State m_state;

    public StopWatch() {
        m_time = SystemTime;
        reset();
    }
    public StopWatch(GetTime time) {
        m_time = time;
        reset();
    }

    public void start() {
        if ( m_state == State.PAUSED ) {
            m_pauseOffset = getElapsedTime();
            m_stopTime = 0;
            m_startTime = m_time.now();
            m_state = State.RUNNING;
        }
    }

    public void pause() {
        if ( m_state == State.RUNNING ) {
            m_stopTime = m_time.now();
            m_state = State.PAUSED;
        }
    }

    public void reset() {
        m_state = State.PAUSED;
        m_startTime 	= 0;
        m_stopTime 		= 0;
        m_pauseOffset 	= 0;
        m_laps.clear();
    }

    public void lap() {
        m_laps.add(getElapsedTime());
    }

    public long getElapsedTime() {
        if ( m_state == State.PAUSED ) {
            return (m_stopTime - m_startTime) + m_pauseOffset;
        } else {
            return (m_time.now() - m_startTime) + m_pauseOffset;
        }
    }

    public List<Long> getLaps() {
        return m_laps;
    }

    public boolean isRunning() {
        return (m_state == State.RUNNING);
    }

    public String getFormattedTime(){
        return formatTime(getElapsedTime());
    }

    public static String formatTime(long milliseconds){
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000*60)) % 60);
        int hours   = (int) ((milliseconds / (1000*60*60)) % 24);

        String theSeconds = String.valueOf(seconds);

        if(seconds < 10){
            theSeconds = "0"+theSeconds;
        }
        String theHours = String.valueOf(hours);

        if(hours<10){
            theHours = "0"+theHours;
        }

        String theMinutes = String.valueOf(minutes);

        if(minutes < 10){
            theMinutes = "0"+theMinutes;
        }


        return theHours+":"+theMinutes+":"+theSeconds;
    }


    public long getSeconds(){
        return (int) (getElapsedTime()/ 1000) % 60;
    }

    public long getMinutes(){
        return (int) ((getElapsedTime()/ (1000*60)) % 60);
    }

    public long getHours(){
        return (int) ((getElapsedTime() / (1000*60*60)) % 24);
    }
}