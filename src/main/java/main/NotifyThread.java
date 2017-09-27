package main;

import main.rmiinterface.NotifyUpdate;

import java.util.Map;

/**
 * Created by Dominik on 26.09.2017.
 */
public class NotifyThread implements Runnable {

    enum NotifyType {
        USER, GROUP, MESSAGE
    }

    NotifyType type;
    Map.Entry<String, NotifyUpdate> ent;
    NotifyCallback cb;


    /**
     * Initiate log thread
     *
     * @param type
     */
    public NotifyThread(NotifyType type, Map.Entry<String, NotifyUpdate> ent, NotifyCallback cb) {
        this.type = type;
        this.ent = ent;
        this.cb = cb;
    }

    /**
     * Run log thread
     */
    @Override
    public void run() {
        try {
            if (type == NotifyType.MESSAGE) {
                ent.getValue().onUpdateMessage();
            } else if (type == NotifyType.GROUP) {
                ent.getValue().onUpdateGroup();
            } else if (type == NotifyType.USER) {
                ent.getValue().onUpdateUser();
            }
        } catch (Exception e) {
            cb.notifyRemoved(ent.getKey());
        }
    }
}
