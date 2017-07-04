package org.stackit.network.pages;

import org.stackit.network.StatusType;
import spark.Request;
import spark.Response;

import java.util.HashMap;

public abstract class Page {

    private HashMap<String, Object> response;
    protected HashMap<String, Object> content;

    public void setContent(HashMap<String, Object> responseContent){
        this.response = responseContent;

        this.content = new HashMap<>();

        this.getResponseContent().put("content", this.content);
    }

    public HashMap<String, Object> getResponseContent() {
        return response;
    }

    public boolean haveNullContent(){
        return this.getContent().isEmpty();
    }

    public void removeContent(){
        this.getResponseContent().remove("content");
    }

    public HashMap<String, Object> getContent(){
        return this.content;
    }

    private void setAPIState(StatusType type){
        this.getResponseContent().put("status", type);
    }

    public void success(String message){
        setAPIState(StatusType.SUCCESS);
        this.getResponseContent().put("message", message);
    }

    public void error(String message){
        setAPIState(StatusType.ERROR);
        HashMap<String, Object> errorMessages;
        if (!this.getResponseContent().containsKey("error")){
            this.getResponseContent().put("error", new HashMap<String, Object>());
        }
        errorMessages = (HashMap<String, Object>) this.getResponseContent().get("error");
        errorMessages.put(serializeError(message), message);
    }

    private String serializeError(String error){
        return error.replace(' ', '_').toLowerCase();
    }

    abstract public void handle(Request request, Response response) throws Exception;
}
