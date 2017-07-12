package org.stackit.network.pages;

import org.stackit.network.Page;
import org.stackit.network.StatusMessage;
import org.stackit.network.StatusType;

public class DebugPage extends Page {

    @Override
    public void handle() {
        status(StatusMessage.SUCCESS, StatusType.SUCCESS);
    }
}
