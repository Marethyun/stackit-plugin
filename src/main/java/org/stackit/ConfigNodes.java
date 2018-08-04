package org.stackit;

import java.util.ArrayList;

public enum ConfigNodes {
    ENABLED("StackIt.enabled"),
    BUNDLES("StackIt.bundles"),
    NOTIFY_OPS("StackIt.notify-ops"),

    SSL_ENABLED("StackIt.ssl.enabled"),
    SSL_KEYSTORE_PATH("StackIt.ssl.keystore.path"),
    SSL_KEYSTORE_PASSPHRASE("StackIt.ssl.keystore.passphrase"),
    SSL_TRUST_STORE_PATH("StackIt.ssl.truststore.path"),
    SSL_TRUST_STORE_PASSPHRASE("StackIt.ssl.truststore.passphrase"),

    API_PORT("StackIt.api.port"),
    API_SESSION_EXPIRE("StackIt.api.sessionexpire"),
    API_ACCOUNTS("StackIt.api.accounts");

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
