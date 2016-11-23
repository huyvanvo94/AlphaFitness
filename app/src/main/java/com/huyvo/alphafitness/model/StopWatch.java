package com.huyvo.alphafitness.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StopWatch {

    public interface GetTime {
        long now();
    }


    private GetTime SystemTime = new GetTime() {
        @Override
        public long now() {	return System.currentTimeMillis(); }
    };

    public enum State { PAUSED, RUNNING };

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

        long millis = getElapsedTime();

        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
}