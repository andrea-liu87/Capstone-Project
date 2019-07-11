package com.andreasgift.petto.model;

import com.andreasgift.petto.data.PetContract;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

@SimpleSQLTable(table = PetContract.TABLE_NAME, provider = PetContract.PROVIDER_NAME)
public class Pet {

    @SimpleSQLColumn(PetContract.COLUMN_NAME)
    private
    String name;

    @SimpleSQLColumn(PetContract.COLUMN_AGE)
    private
    int age;

    @SimpleSQLColumn(PetContract.COLUMN_HUNGER)
    private
    int hunger;

    @SimpleSQLColumn(PetContract.COLUMN_HAPPINESS)
    private
    int happiness;

    @SimpleSQLColumn(PetContract.COLUMN_CLEANLINESS)
    private
    int cleanliness;

    public Pet() {
        this.name = "Choco";
        this.age = 0;
        this.hunger = 10;
        this.happiness = 10;
        this.cleanliness = 10;
    }

    public Pet(String name) {
        this.name = name;
        this.age = 0;
        this.hunger = 10;
        this.happiness = 10;
        this.cleanliness = 10;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHunger() {
        return hunger;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public int getHappiness() {
        return happiness;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }

    public int getCleanliness() {
        return cleanliness;
    }

    public void setCleanliness(int cleanliness) {
        this.cleanliness = cleanliness;
    }
}
