package org.stackit.network;

import org.stackit.Logger;
import org.stackit.config.StackItConfiguration;
import org.stackit.database.DatabaseManager;
import org.stackit.database.entities.Token;

import java.util.List;

abstract public class TokenManager {
    public static boolean isTokenValid(String token){
        List<Token> tokens = DatabaseManager.getTokens().getAll();
        if (tokens != null){
            for (Token t : tokens) {
                if (t.getValue().equals(token)){
                    Logger.info("A");
                    return verify(t.getTime());
                }
            }
        }
        return false;
    }

    public static boolean isTokenValid(Token token){
        return verify(token.getTime());
    }

    private static boolean verify(long timestamp){
        return timestamp + StackItConfiguration.getTokensExpiration() >= System.currentTimeMillis();
    }
}
