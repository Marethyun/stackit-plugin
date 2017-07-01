package org.stackit.network.pages;

import java.util.HashMap;

public class PageContent {

    private HashMap<String, Object> content;

    public PageContent(HashMap<String, Object> content) {
        this.content = content;
    }

    public HashMap<String, Object> get(){
        return content;
    }
}
