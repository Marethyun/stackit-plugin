package org.stackit.network;

import spark.Request;
import spark.Response;

import java.util.HashMap;

public abstract class Page {

    private HashMap<String, Object> responseC;
    protected HashMap<String, Object> content;

    private StatusType apiState;
    private StatusMessage apiMessage;

    private Request request;
    private Response response;

    public void setContent(HashMap<String, Object> responseContent){
        this.responseC = responseContent;

        this.content = new HashMap<>();

        this.getResponseContent().put("content", this.content);
    }

    public HashMap<String, Object> getResponseContent() {
        return responseC;
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

    private void setAPIStatus(StatusMessage message, StatusType type){
        this.apiState = type;
        this.apiMessage = message;

        this.getResponseContent().put("status", type.toString());
    }

    @SuppressWarnings("unchecked")
    public void status(StatusMessage message, StatusType type){
        response.status(type.getCode());
        setAPIStatus(message, type);
        if (!type.equals(StatusType.SUCCESS)) {
            HashMap<String, Object> errorMessages;
            if (!this.getResponseContent().containsKey("error")) {
                this.getResponseContent().put("error", new HashMap<String, Object>());
            }
            errorMessages = (HashMap<String, Object>) this.getResponseContent().get("error");
            errorMessages.put(serializeError(message.toString()), message.toString());
        } else {
            this.getResponseContent().put("message", message.toString());
        }
    }

    private String serializeError(String error){
        return error.replace(' ', '_').toLowerCase();
    }

    public void setRequest(Request r){
        this.request = r;
    }

    public void setResponse(Response r){
        this.response = r;
    }

    protected Request getRequest() {
        return this.request;
    }

    protected Response getResponse() {
        return this.response;
    }

    public StatusType getAPIState(){
        return this.apiState;
    }

    public StatusMessage getApiMessage(){
        return this.apiMessage;
    }

    public abstract void handle();
}
