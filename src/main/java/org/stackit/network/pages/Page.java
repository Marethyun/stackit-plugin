package org.stackit.network.pages;

import org.stackit.network.StatusType;
import spark.Request;
import spark.Response;

import java.util.HashMap;

public abstract class Page {

    private PageContent response;
    protected PageContent content;

    public void setContent(HashMap<String, Object> responseContent){
        this.response = new PageContent(responseContent);
        HashMap<String, Object> futureContent = new HashMap<>();

        this.content = new PageContent(futureContent);

        this.getResponseContent().get().put("content", futureContent);
    }

    public PageContent getResponseContent() {
        return response;
    }

    public boolean haveNullContent(){
        return this.getContent().get().isEmpty();
    }

    public void removeContent(){
        this.getResponseContent().get().remove("content");
    }

    public PageContent getContent(){
        return this.content;
    }

    public void setAPIState(StatusType type){
        this.getResponseContent().get().put("status", type);
    }

    public void setMessage(String message){
        this.getResponseContent().get().put("message", message);
    }

    public void addErrorMessage(String message){
        HashMap<String, Object> errorMessages;
        if (!this.getResponseContent().get().containsKey("error")){
            this.getResponseContent().get().put("error", new HashMap<String, Object>());
        }
        errorMessages = (HashMap<String, Object>) this.getResponseContent().get().get("error");
        errorMessages.put(serializeError(message), message);
    }

    private String serializeError(String error){
        return error.replace(' ', '_').toLowerCase();
    }

    abstract public void handle(Request request, Response response) throws Exception;
}
