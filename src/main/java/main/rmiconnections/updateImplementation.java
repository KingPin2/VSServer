package main.rmiconnections;

import main.rmiinterface.NotifyUpdate;

import java.io.Serializable;

public class updateImplementation implements NotifyUpdate , Serializable{
    @Override
    public void onUserUpdated() {
        System.out.println("onUserUpdate");
    }

    @Override
    public void onMessageUpdated() {
        System.out.println("onMessageUpdate");
    }

    @Override
    public void onGroupUpdated() {
        System.out.println("onGroupUpdate");
    }
}
