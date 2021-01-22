package com.skts.ourmemory.model;

public class UserModel implements IUser {
    String name;
    String passWd;

    public UserModel(String name, String passWd) {
        this.name = name;
        this.passWd = passWd;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPassWd() {
        return passWd;
    }

    @Override
    public int checkUserValidity(String name, String passWd) {
        if (name == null || passWd == null || !name.equals(getName()) || !passWd.equals(getPassWd())) {
            return -1;
        }
        return 0;
    }
}
