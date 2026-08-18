package com.utils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Pet implements Serializable {
    private static int idCounter = 1;

    private int id;
    private String name;
    private String species;
    private int age;
    private String ownerName;
    private String contactInfo;
    private LocalDateTime registrationDate;
    private ArrayList<Appointment> listOfAppointments;

    public Pet(String petName, String petSpecies, String petOwnerName, String petContactInfo) {
        id = idCounter++;
        name = petName;
        species = petSpecies;
        ownerName = petOwnerName;
        contactInfo = petContactInfo;
        registrationDate = LocalDateTime.now();
        listOfAppointments = new ArrayList<>();
    }

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

    public LocalDateTime getRegistrationDate() {
        return this.registrationDate;
    }

    public ArrayList<Appointment> getListOfAppointments() {
        return this.listOfAppointments;
    }

    public ArrayList<Appointment> getNextWeekAppointments() {
        ArrayList<Appointment> appointments = this.listOfAppointments;

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime nextWeek = currentTime.plusDays(7);

        ArrayList<Appointment> nextWeekAppointments = appointments.stream()
                .filter(appointment -> appointment.getAppointmentTime().isAfter(currentTime) && appointment.getAppointmentTime().isBefore(nextWeek))
                .collect(Collectors.toCollection(ArrayList::new));

        return nextWeekAppointments;
    }

    public ArrayList<Appointment> getOverdueVetAppointments() {
        ArrayList<Appointment> appointments = this.listOfAppointments;

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime pastSixMonths = currentTime.minusMonths(6);

        ArrayList<Appointment> overdueAppointments = appointments.stream()
                .filter(appointment
                        -> appointment.getAppointmentTime().isBefore(currentTime)
                && appointment.getAppointmentTime().isAfter(pastSixMonths)
                )
                .collect(Collectors.toCollection(ArrayList::new));

        return overdueAppointments;
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

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setListOfAppointments(ArrayList<Appointment> listOfAppointments) {
        this.listOfAppointments = listOfAppointments;
    }

    public void setAge(int age) {
        if (age > 0) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("Age is invalid.");
        }
    }

    public void addNewAppointment(Appointment newAppointment) {
        this.listOfAppointments.add(newAppointment);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String stringDate = this.registrationDate.format(formatter);

        return "Pet name: " + this.name
                + "\nSpecies: " + this.species
                + "\nAge: " + this.age
                + "\nOwner name: " + this.ownerName
                + "\nContact Information: " + this.contactInfo
                + "\nRegistration Date: " + stringDate;
    }
}
