package com.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Pet {

    private int id = 0;
    private String name;
    private String species;
    private int age;
    private String ownerName;
    private String contactInfo;
    private LocalDateTime registrationDate;
    private ArrayList<Appointment> listOfAppointments;

    public Pet(String petName, String petSpecies, String petOwnerName, String petContactInfo) {
        id += 1;
        name = petName;
        species = petSpecies;
        ownerName = petOwnerName;
        contactInfo = petContactInfo;
        registrationDate = LocalDateTime.now();
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

    public ArrayList<Appointment> getListOfAppointment() {
        return this.listOfAppointments;
    }

    public ArrayList<Appointment> getUpcomingAppointmentsNextWeek() {
        ArrayList<Appointment> allAppointments = this.listOfAppointments;

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime nextWeek = currentTime.plusDays(7);

        ArrayList<Appointment> upComingAppointments = allAppointments.stream()
                .filter(appointment -> appointment.getAppointmentTime().isBefore(nextWeek))
                .collect(Collectors.toCollection(ArrayList::new));

        return upComingAppointments;
    }

    public ArrayList<Appointment> getOverdueVetAppointments() {
        ArrayList<Appointment> allAppointments = this.listOfAppointments;

        ArrayList<Appointment> overdueAppointments = allAppointments.stream()
                .filter(appointment -> appointment.getAppointmentTime().isAfter(LocalDateTime.now()))
                .collect(Collectors.toCollection(ArrayList::new));

        return overdueAppointments;
    }

    public void setAge(int age) {
        if (age > 0) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("Age is invalid.");
        }
    }

    public void addNewAppointment(Appointment newAppointment) {
        listOfAppointments.add(newAppointment);
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
