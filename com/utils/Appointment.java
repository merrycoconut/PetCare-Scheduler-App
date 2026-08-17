package com.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;

public class Appointment {

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
            if (!"Visit".equals(appointmentType) && !"Vaccination".equals(appointmentType) && !"Grooming".equals(appointmentType)) {
                throw new InputMismatchException("Invalid appointment type.");
            }
            this.appointmentType = appointmentType;
        } catch (InputMismatchException e) {
            System.err.println("Error: " + e.getMessage());
        }

    }

    public void setAppointmentTime(String newAppointmentTime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
            this.appointmentTime = LocalDateTime.parse(newAppointmentTime, formatter);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }

    public void setAppointmentNotes(String notes) {
        if (!notes.equals("0")) {
            this.notes = notes;
        } else {
            this.notes = "-";
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        String stringDateTime = this.appointmentTime.format(formatter);

        return "Appointment Type: " + this.appointmentType
                + "\nAppointment Time: " + stringDateTime
                + "\nNotes: " + this.notes;
    }
}
