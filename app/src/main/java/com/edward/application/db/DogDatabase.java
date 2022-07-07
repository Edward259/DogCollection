package com.edward.application.db;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = DogDatabase.NAME, version = DogDatabase.VERSION)
public class DogDatabase {
    public static final int VERSION = 1;
    public static final String NAME = "DogDatabase";
}