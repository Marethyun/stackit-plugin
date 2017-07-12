package org.stackit.network.pages;

import org.stackit.network.Authenticate;
import org.stackit.network.Page;
import org.stackit.network.StatusMessage;
import org.stackit.network.StatusType;

import java.util.HashMap;

public class DictionaryPage extends Page implements Authenticate {

    @Override
    public void handle() {
        HashMap<String, Object> statusList = new HashMap<>();

        for (StatusType st : StatusType.values()) {
            statusList.put(serializeMessage(st.toString()), st.toString());
        }

        getContent().put("dictionary", statusList);
        status(StatusMessage.SUCCESS, StatusType.SUCCESS);
    }

    private String serializeMessage(String message){
        return message.replace(' ', '_').toLowerCase();
    }
}
