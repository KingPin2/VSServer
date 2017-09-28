package main.functions;

import main.objects.Group;
import main.objects.Message;
import main.objects.User;
import main.rmiinterface.NotifyUpdate;
import main.rmiinterface.UpdateType;

import java.util.Map;

/**
 * Created by Dominik on 26.09.2017.
 */
public class NotifyThread implements Runnable {

    public enum NotifyType {
        USER, GROUP, MESSAGE
    }

    Log log;
    NotifyType type;
    Map.Entry<String, NotifyUpdate> ent;
    NotifyCallback cb;
    UpdateType uType;
    Object obj;


    /**
     * Initiate log thread
     *
     * @param type
     */
    public NotifyThread(Log log, NotifyType type, Map.Entry<String, NotifyUpdate> ent, NotifyCallback cb, UpdateType uType, Object obj) {
        this.type = type;
        this.ent = ent;
        this.cb = cb;
        this.uType = uType;
        this.obj = obj;
        this.log = log;
    }

    /**
     * Run log thread
     */
    @Override
    public void run() {
        try {
            if (type == NotifyType.MESSAGE) {
                ent.getValue().onUpdateMessage((Message) obj,uType);
            } else if (type == NotifyType.GROUP) {
                ent.getValue().onUpdateGroup((Group) obj,uType);
            } else if (type == NotifyType.USER) {
                ent.getValue().onUpdateUser((User) obj,uType);
            }
        } catch (Exception e) {
            log.addErrorToLog("notifyThread: " + ent.getKey() + "; " + type.toString() + "; "+ e.toString());
            cb.notifyRemoved(ent.getKey());
        }
    }
}
