package org.stackit;

import org.stackit.api.StackItException;

public class Account {

    public static final String ID_SEPRATOR = ":";

    public final String username;
    public final String password;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Account(String serialized) {
        String[] split = serialized.split(ID_SEPRATOR);

        if (split.length != 2){
            throw new StackItException(String.format("Array with declaration '%s' is malformed", serialized));
        }

        username = split[0];
        password = split[1];
    }

    @Override
    public boolean equals(Object obj) {
        try {
            Account second = (Account) obj;

            return second.username.equals(this.username) && second.password.equals(this.password);
        } catch (ClassCastException ignored){
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.username.hashCode() + this.password.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s:%s", this.username, this.password.length() == 0 ? "(no password)" : this.password);
    }
}
