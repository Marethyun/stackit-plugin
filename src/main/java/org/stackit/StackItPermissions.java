package org.stackit;

public enum StackItPermissions {
    STACKIT_COMMAND("stackit.command");

    private String permission;

    StackItPermissions(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
