package com.jd.bdp.hydra.dubbomonitor;

import com.jd.bdp.hydra.Span;
import com.lmax.disruptor.EventFactory;

/**
 * User: yfliuyu
 * Date: 13-3-19
 * Time: 上午10:26
 */
public class LogEvent {
    private Span value;

    public Span getValue() {
        return value;
    }

    public void setValue(Span value) {
        this.value = value;
    }



    public final static EventFactory<LogEvent> EVENT_FACTORY = new EventFactory<LogEvent>() {
        @Override
        public LogEvent newInstance() {
            return new LogEvent();
        }
    };
}
