package com.utils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Appointment implements Serializable {

    private String appointmentType;
    private LocalDateTime appointmentTime;
    private String appointmentNotes;

    public Appointment(String appointmentType, LocalDateTime appointmentTime, String appointmentNotes) {
        this.appointmentType = appointmentType;
        this.appointmentTime = appointmentTime;
        this.appointmentNotes = appointmentNotes;
    }

    public String getAppointmentType() {
        return this.appointmentType;
    }

    public LocalDateTime getAppointmentTime() {
        return this.appointmentTime;
    }

    public String getappointmentNotes() {
        return this.appointmentNotes;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public void setAppointmentNotes(String appointmentNotes) {
        if (!appointmentNotes.equals("0")) {
            this.appointmentNotes = appointmentNotes;
        } else {
            this.appointmentNotes = "-";
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        String stringDateTime = this.appointmentTime.format(formatter);

        return "Appointment Type: " + this.appointmentType
                + "\nAppointment Time: " + stringDateTime
                + "\nNotes: " + this.appointmentNotes;
    }
}
