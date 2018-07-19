package org.stackit;

import java.util.ArrayList;

public enum ConfigNodes {
    ENABLED("StackIt.enabled"),
    SSL_ENABLED("StackIt.ssl.enabled"),
    SSL_KEYSTORE_PATH("StackIt.ssl.keystore"),
    SSL_KEYSTORE_PASSPHRASE("StackIt.ssl.passphrase"),
    API_PORT("StackIt.api.port"),
    API_SESSION_EXPIRE("StackIt.api.sessionexpire"),
    API_ACCOUNTS("StackIt.api.accounts"),
    DATA_STORING_MODE("StackIt.storemode"),

    DATABASE_HOST("StackIt.database.host"),
    DATABASE_PORT("StackIt.database.port"),
    DATABASE_USERNAME("StackIt.database.username"),
    DATABASE_PASSWORD("StackIt.database.password"),
    DATABASE_NAME("StackIt.database.name"),

    BUNDLES("StackIt.bundles");

    private String node;

    ConfigNodes(String node) {
        this.node = node;
    }

    public String getNode() {
        return node;
    }

    @Override
    public String toString() {
        return node;
    }

    public static String[] toNodeArray(){
        ArrayList<String> nodes = new ArrayList<>();

        for (ConfigNodes configNodes : values()) {
            nodes.add(configNodes.getNode());
        }

        return nodes.toArray(new String[]{});
    }
}
