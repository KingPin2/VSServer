package main.rmiinterface;

/**
 * Created by Dominik on 26.09.2017.
 */
public interface NotifyUpdate {
    void onUserUpdated();
    void onMessageUpdated();
    void onGroupUpdated();
}
