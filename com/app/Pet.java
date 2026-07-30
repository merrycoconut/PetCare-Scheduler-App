package com.app;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.InputMismatchException;

class Pet {

    private int id = 0;
    private String name;
    private String species;
    private int age;
    private String ownerName;
    private String contactInfo;
    private LocalDate registrationDate;
    private Appointment[] listOfAppointment;

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getSpecies() {
        return this.species;
    }

    public int getAge() {
        return this.age;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public String getContactInfo() {
        return this.contactInfo;
    }

    public LocalDate getRegistrationDate() {
        return this.registrationDate;
    }

    public Appointment[] getListOfAppointment() {
        return this.listOfAppointment;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setAge(int age) {
        if (age > 0) {
            this.age = age;
        }
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setListOfAppointment(Appointment[] listOfAppointment) {
        this.listOfAppointment = listOfAppointment;
    }

}

class Appointment {

    private String appointmentType;
    private LocalDateTime appointmentTime;
    private String notes;

    public String getAppointmentType() {
        return this.appointmentType;
    }

    public LocalDateTime getAppointmentTime() {
        return this.appointmentTime;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setAppointmentType(String appointmentType) {

        try {
            if (!"visit".equals(appointmentType) && !"vaccination".equals(appointmentType) && !"grooming".equals(appointmentType)) {
                throw new InputMismatchException("Invalid appointment type.");
            }
            this.appointmentType = appointmentType;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
