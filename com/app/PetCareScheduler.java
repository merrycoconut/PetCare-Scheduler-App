package com.app;

import com.utils.Appointment;
import com.utils.Pet;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class PetCareScheduler {

    private static Map<String, Pet> pets = new HashMap<>();
    private static ArrayList<Appointment> upcomingAppointments = new ArrayList<>();
    private static ArrayList<Appointment> pastAppointments= new ArrayList<>();

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean isRunning = true;

        loadHouseholdsFromFile();

        while (isRunning) {
            System.out.println("Welcome to the Pet Care Scheduler!");
            System.out.println("Please select an option from the menu below:");
            System.out.println("1. Register your pets");
            System.out.println("2. Schedule an appointment");
            System.out.println("3. View records");
            System.out.println("4. Generate reports");
            System.out.println("5. Save and Exit");

            String userInput = scanner.nextLine();

            switch (userInput) {
                case "1":
                    registerPet();
                    break;
                case "2":
                    scheduleAppointment();
                    break;
                case "3":
                    viewRecords();
                    break;
                case "4":
                    generateReports();
                    break;
                case "5":
                    savePetsToFile();
                    isRunning = false;
                    System.out.println("Data saved. Goodbye!");
                    break;
                default:
                    throw new AssertionError();
            }

        }

        scanner.close();
    }

    public static void registerPet() {
        System.out.println("Please enter the name of your pet: ");
        String petName = scanner.nextLine().trim().toUpperCase();

        System.out.println("Please enter the species of your pet: ");
        String petSpecies = scanner.nextLine();

        System.out.println("Please enter the age of your pet: ");
        int petAge = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Please enter the name of the pet owner: ");
        String petOwnerName = scanner.nextLine().trim();

        System.out.println("Please enter the contact information of the pet owner: ");
        String petContactInfo = scanner.nextLine().trim();

        try {
            Pet newPet = new Pet(petName, petSpecies, petOwnerName, petContactInfo);
            newPet.setAge(petAge);
            pets.put(petName, newPet);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void scheduleAppointment() {
        System.out.println("Please enter the pet name if you already register your pet. If not, please enter 0 and register your pet first. ");
        String petName = scanner.nextLine().toUpperCase();

        // Handle edge cases: pet is not in map.
        if (petName.equals("0")) {
            return;
        }

        if (pets.get(petName) == null) {
            System.out.println("Can not find the pet name. Please check. ");
            return;
        }

        // The pet is in map, get the target pet
        Pet targetPet = pets.get(petName);

        // Create a new apppointment
        while (true) {
            try {
                Appointment newAppointment = new Appointment();

                System.out.println("Please enter the type of your appointment from the following:\n Visit\n Vaccination\n Grooming ");
                String appointmentType = scanner.nextLine();
                if (!"Visit".equals(appointmentType) && !"Vaccination".equals(appointmentType) && !"Grooming".equals(appointmentType)) {
                    throw new InputMismatchException("Invalid appointment type.");
                }
                newAppointment.setAppointmentType(appointmentType);

                System.out.println("Please enter the type of your appointment time with the MM-dd-yyyy HH:mm:ss format. ");
                String appointmentTime = scanner.nextLine();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
                LocalDateTime fAppointmentTime = LocalDateTime.parse(appointmentTime, formatter);
                newAppointment.setAppointmentTime(fAppointmentTime);

                System.out.println("Please enter the note of your appointment. If nothing to add, please enter 0");
                String appointmentNote = scanner.nextLine();
                newAppointment.setAppointmentNotes(appointmentNote);

                // Add the new appointment to the pet's appointment list
                targetPet.addNewAppointment(newAppointment);

                // Add the new appointment to the list of appointmens
                upcomingAppointments.add(newAppointment);
                break;
            } catch (InputMismatchException e) {
                System.err.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

    }

    public static void viewRecords() {
        // Show the following information based on the option chosen: 
        System.out.println("Please enter the record you want to view:\n 1. View all pets. \n 2. View all Appointment of a specific pet\n 3. Upcoming appointments for all pets\n 4. Past appointment history for each pet");
        String userInput = scanner.nextLine();

        switch (userInput) {
            case "1":
                // View all registered pets 
                pets.forEach((key, value) -> {
                    System.out.println(value);
                });
                break;
            case "2":
                // View all appointments for a specific pet 
                System.out.println("Please enter the name of the pet your want to view: ");
                String petName = scanner.nextLine().toUpperCase();
                ArrayList<Appointment> appointments = pets.get(petName).getListOfAppointment();
                appointments.forEach(appointment -> {
                    System.out.println(appointment);
                });
                break;
            case "3":
                // View upcoming appointments for all pets 
                if (upcomingAppointments.isEmpty()) {
                    System.out.println("This is no upcoming appointments. ");
                } else {
                    upcomingAppointments.forEach(appointment -> {
                        System.out.println(appointment);
                    });
                }
                break;
            case "4":
                // - Past appointment history for each pet
                if (pastAppointments.isEmpty()) {
                    System.out.println("There is no past appointment. ");
                } else {
                    pastAppointments.forEach(appointment -> {
                        System.out.println(appointment);
                    });
                }
                break;
        }

    }

    public static void generateReports() {
        if (pets.isEmpty()) {
            System.out.println("There is no pet yet. Register first. ");
            return;
        }

        // Produce simple reports including: 
        // - Pets with upcoming appointments in the next week 
        // - Pets overdue for a vet visit (For example: No vet visit in the last 6 months)
        pets.values().forEach(pet -> {
            System.out.println(pet);

            // - Pets with upcoming appointments in the next week 
            pet.getUpcomingAppointmentsNextWeek().forEach(appointment -> {
                System.out.println(appointment);
            });

            // - Pets overdue for a vet visit (For example: No vet visit in the last 6 months)
            pet.getOverdueVetAppointments().forEach(appointment -> {
                System.out.println(appointment);
            });

        });

    }

    public static void savePetsToFile() {
        try {
            ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream("petSchedule.ser"));
            obj.writeObject(pets);
            obj.close();
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadHouseholdsFromFile() {
        // Use a try-with-resources block to automatically close the input stream
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("petSchedule.ser"))) {
            pets = (Map<String, Pet>) in.readObject();
            System.out.println("Pet schedule data loaded.");
        } catch (FileNotFoundException e) {
            System.out.println("No saved data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
}
