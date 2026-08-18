package com.utils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Appointment implements Serializable{

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
        this.appointmentType = appointmentType;
    }

    public void setAppointmentTime(LocalDateTime newAppointmentTime) {
        this.appointmentTime = newAppointmentTime;
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
