package main.functions;

import main.objects.Group;
import main.objects.Message;
import main.objects.User;
import main.rmiinterface.NotifyUpdate;
import main.rmiinterface.UpdateType;

import java.util.Map;

/**
 * Notify thread
 *
 * @author Dominik Bergum, 3603490
 */
public class NotifyThread implements Runnable {

    public enum NotifyType {
        USER, GROUP, MESSAGE
    }

    private Log log;
    private NotifyType type;
    private Map.Entry<String, NotifyUpdate> ent;
    private NotifyCallback cb;
    private UpdateType uType;
    private Object obj;


    /**
     * Initiate log thread
     *
     * @param type  NotifyType
     * @param cb    NotifyCallback
     * @param ent   NotifyUpdate
     * @param log   Log
     * @param obj   Object
     * @param uType UpdateType
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
                ent.getValue().onUpdateMessage((Message) obj, uType);
            } else if (type == NotifyType.GROUP) {
                ent.getValue().onUpdateGroup((Group) obj, uType);
            } else if (type == NotifyType.USER) {
                ent.getValue().onUpdateUser((User) obj, uType);
            }
        } catch (Exception e) {
            log.addErrorToLog("notifyThread: " + ent.getKey() + "; " + type.toString() + "; " + e.toString());
            cb.notifyRemoved(ent.getKey());
        }
    }
}
