package com.skts.ourmemory.model;

public interface IUser {
    String getName();

    String getPassWd();

    int checkUserValidity(String name, String passWd);
}
