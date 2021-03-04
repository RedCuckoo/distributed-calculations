package com.momotenko.lab3.task1.entity;

import lombok.Getter;

import java.util.Objects;
import java.util.StringTokenizer;

@Getter
public class Record {
    private String familyName;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public Record(String familyName, String firstName, String lastName, String phoneNumber) {
        this.familyName = familyName.replaceAll("\\P{Print}","");
        this.firstName = firstName.replaceAll("\\P{Print}","");
        this.lastName = lastName.replaceAll("\\P{Print}","");
        this.phoneNumber = phoneNumber.replaceAll("\\P{Print}","");
    }

    public Record(String toParse){
        StringTokenizer stringTokenizer = new StringTokenizer(toParse);

        assert(stringTokenizer.countTokens() == 4);

        this.familyName = stringTokenizer.nextToken().replaceAll("\\P{Print}","");
        this.firstName = stringTokenizer.nextToken().replaceAll("\\P{Print}","");
        this.lastName = stringTokenizer.nextToken().replaceAll("\\P{Print}","");
        this.phoneNumber = stringTokenizer.nextToken().replaceAll("\\P{Print}","");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return Objects.equals(familyName, record.familyName) && Objects.equals(firstName, record.firstName) && Objects.equals(lastName, record.lastName) && Objects.equals(phoneNumber, record.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(familyName, firstName, lastName, phoneNumber);
    }

    @Override
    public String toString() {
        return familyName + " " + firstName + " " + lastName + " " + phoneNumber;
    }
}
