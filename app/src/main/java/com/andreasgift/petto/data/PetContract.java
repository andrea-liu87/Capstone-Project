package com.andreasgift.petto.data;

public class PetContract {

    public final static String PROVIDER_NAME = "PetProvider";
    public final static String TABLE_NAME = "pet";
    public final static String DATABASE_NAME = "pet.db";

    public final static String AUTHORITY = "pet_provider.authority";

    //database column name
    public final static String COLUMN_NAME = "col_name";
    public final static String COLUMN_AGE = "col_age";
    public final static String COLUMN_HUNGER = "col_hunger";
    public final static String COLUMN_HAPPINESS = "col_happiness";
    public final static String COLUMN_CLEANLINESS = "col_cleanliness";

    public final static String ATTRIBUTES_KEY = "atributes_key_indicator";
    public final static int HUNGER_IND = 0;
    public final static int CLEANLINESS_IND = 1;
    public final static int HAPPINESS_IND = 2;
}
