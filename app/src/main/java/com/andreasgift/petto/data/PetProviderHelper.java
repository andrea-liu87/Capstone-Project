package com.andreasgift.petto.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.andreasgift.petto.model.Pet;
import com.andreasgift.petto.model.PetTable;

/*************************************************************
 * This class is helper class for PetProvider content provider
 * The available method in this class :
 * createPet    : Register pet into PetTable
 * deleteAllPet : Clear PetTable, delete all the content inside
 * getPet       : Retrieve Pet from PetTable
 * updateAttributes : Add +1 to Pet Attributes
 * reduceAttributes : Reduce Pet attributes -1
 ************************************************************/
public class PetProviderHelper {
    private static final String TAG = "PetProviderHelper";



    /**
     * This method is helper method to register new pet in Pet Content Provider
     * @param resolver ContentResolver for the intended Content Provider
     * @param pet      Pet that will be registered into Pet Content Provider table
     * @return          If successful, uri will be returned (not null)
     */
    public static Uri createPet (ContentResolver resolver, Pet pet) {
        Uri uri;
        if (getPet(resolver) == null) {
            uri = resolver.insert(PetTable.CONTENT_URI, PetTable.getContentValues(pet, false));
            if (uri != null) {
                Log.d(TAG, "Pet is added");
            }
        } else {
            uri = null;
        }
        return uri;
    }



    /**
     * This method will clear all the content inside PetTable
     * @param resolver  ContentResolver for the intended Content Provider
     * @return          + return indicate the PetTable has been successfully clear
     */
    public static int deleteAllPet (ContentResolver resolver) {
        int result = resolver.delete(PetTable.CONTENT_URI, null, null);
        if (result > 0) {
            Log.d(TAG, "Pet is deleted");
        }
        return result;
    }



    /**
     * Retrieve Pet from PetTable. In this application, PetTable only have one Pet.
     * @param resolver  ContentResolver for the intended Content Provider
     * @return          + number successfully retrieved
     */
    public static Pet getPet (ContentResolver resolver) {
        Cursor cursor = resolver.query(PetTable.CONTENT_URI,
                null,
                null,
               null,
                null);

        if (cursor != null && cursor.moveToFirst()){
            Log.d(TAG,"Pet is successfully retrieved");
            return PetTable.getRow(cursor,true);
        }
        else {return null;}
    }



    /**
     * Update pet attributes +1
     * @param resolver      ContentResolver for the intended Content Provider
     * @param attributes    0 == Hunger; 1 == Cleanliness; 2 == Happiness
     * @return              0 == no  updateNextPosition; 1 == updateNextPosition success
     */
    public static int updateAttributes (ContentResolver resolver, int attributes) {
        ContentValues updatedValues = new ContentValues();
        int currentValue;
        String key;

        switch (attributes){
            case PetContract.HUNGER_IND :
                currentValue = getPet(resolver).getHunger();
                key = PetTable.FIELD_COL_HUNGER;
                break;
            case PetContract.CLEANLINESS_IND :
                currentValue = getPet(resolver).getCleanliness();
                key = PetTable.FIELD_COL_CLEANLINESS;
                break;
            case PetContract.HAPPINESS_IND :
                currentValue = getPet(resolver).getHappiness();
                key = PetTable.FIELD_COL_HAPPINESS;
                break;
                default: return -1;
        }

        int returnCursor;
        if (currentValue < 10) {
            updatedValues.put(key, currentValue+1);
            returnCursor = resolver.update(
                    PetTable.CONTENT_URI,
                    updatedValues,
                    PetTable.FIELD_COL_NAME + "=?",
                    new String[]{"Choco"}
            );
        } else {
            returnCursor = 0; //No updateNextPosition since value already max
            }
        return returnCursor;
    }



    /**
     * Reduce pet attributes -1
     * @param resolver      ContentResolver for the intended Content Provider
     * @param attributes    0 == Hunger; 1 == Cleanliness; 2 == Happiness
     * @return              0 == no updateNextPosition; 1 == updateNextPosition success
     */
    public static int reduceAttributes (ContentResolver resolver, int attributes) {
        ContentValues updatedValues = new ContentValues();
        int currentValue;
        String key;

        switch (attributes){
            case PetContract.HUNGER_IND :
                currentValue = getPet(resolver).getHunger();
                key = PetTable.FIELD_COL_HUNGER;
                break;
            case PetContract.CLEANLINESS_IND :
                currentValue = getPet(resolver).getCleanliness();
                key = PetTable.FIELD_COL_CLEANLINESS;
                break;
            case PetContract.HAPPINESS_IND :
                currentValue = getPet(resolver).getHappiness();
                key = PetTable.FIELD_COL_HAPPINESS;
                break;
            default: return -1;
        }

        int returnCursor;
        if (currentValue > 0) {
            updatedValues.put(key, currentValue-2);
            returnCursor = resolver.update(
                    PetTable.CONTENT_URI,
                    updatedValues,
                    PetTable.FIELD_COL_NAME + "=?",
                    new String[]{"Choco"}
            );
        } else {
            returnCursor = 0; //No updateNextPosition since value already 0
        }
        return returnCursor;
    }
}