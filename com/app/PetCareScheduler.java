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
import java.util.stream.Collectors;

public class PetCareScheduler {

    private static Map<String, Pet> pets = new HashMap<>();
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

            try {
                switch (userInput) {
                    case "1":
                        registerPet();
                        break;
                    case "2":
                        scheduleAppointment();
                        break;
                    case "3":
                        displayData();
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
            } catch (AssertionError e) {
                System.err.println("Error: " + e.getMessage());
            }

        }

        scanner.close();
    }

    public static void registerPet() {
        Pet newPet;
        while (true) {
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
                newPet = new Pet(petName, petSpecies, petOwnerName, petContactInfo);
                newPet.setAge(petAge);
                pets.put(petName, newPet);
                break;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        System.out.println("Pet registered! ");
        System.out.println(newPet);
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
        Appointment newAppointment;

        // Create a new apppointment
        while (true) {
            try {
                System.out.println("Please enter the type of your appointment from the following:\n Visit\n Vaccination\n Grooming ");
                String appointmentType = scanner.nextLine();
                if (!"Visit".equals(appointmentType) && !"Vaccination".equals(appointmentType) && !"Grooming".equals(appointmentType)) {
                    throw new InputMismatchException("Invalid appointment type.");
                }

                System.out.println("Please enter the type of your appointment time with the MM-dd-yyyy HH:mm:ss format. ");
                String appointmentTime = scanner.nextLine();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
                LocalDateTime fAppointmentTime = LocalDateTime.parse(appointmentTime, formatter);

                System.out.println("Please enter the note of your appointment. If nothing to add, please enter 0");
                String appointmentNote = scanner.nextLine();

                // Create a new appointment
                newAppointment = new Appointment(appointmentType, fAppointmentTime, appointmentNote);

                // Add the new appointment to the pet's appointment list
                targetPet.addNewAppointment(newAppointment);

                break;
            } catch (InputMismatchException e) {
                System.err.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

        System.out.println("Appointment scheduled! ");
        System.out.println(newAppointment);
    }

    public static void displayData() {
        if (pets.isEmpty()) {
            System.out.println("There is no pet yet. Register first. ");
            return;
        }

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
                Pet searchingPet = pets.get(petName);

                if (searchingPet == null) {
                    System.out.println("Can not find this pet. ");
                    return;
                }

                ArrayList<Appointment> appointments = searchingPet.getListOfAppointments();
                appointments.forEach(appointment -> {
                    System.out.println(appointment);
                });
                break;
            case "3":
                // View upcoming appointments for all pets
                ArrayList<Appointment> upcomingAppointmentsForAllPets = new ArrayList<>();

                for (Pet pet : pets.values()) {
                    if (pet.getListOfAppointments().isEmpty()) {
                        continue;
                    }

                    System.out.println(pet);

                    // Get all the upcoming appointments on this pet
                    LocalDateTime currentTime = LocalDateTime.now();

                    ArrayList<Appointment> upcomingAppointments = pet.getListOfAppointments().stream()
                            .filter(appointment -> appointment.getAppointmentTime().isAfter(currentTime))
                            .collect(Collectors.toCollection(ArrayList::new));

                    upcomingAppointmentsForAllPets.addAll(upcomingAppointments);
                }

                if (upcomingAppointmentsForAllPets.isEmpty()) {
                    System.out.println("This is no upcoming appointments. ");
                } else {
                    upcomingAppointmentsForAllPets.forEach(appointment -> {
                        System.out.println(appointment);
                    });
                }

                break;
            case "4":
                // - Past appointment history for each pet
                for (Pet pet : pets.values()) {
                    System.out.println("Pet Name: " + pet);

                    if (pet.getListOfAppointments().isEmpty()) {
                        System.out.println(pet + "has no past appointment. ");
                        continue;
                    }

                    LocalDateTime currentTime = LocalDateTime.now();

                    ArrayList<Appointment> pastAppointments = pet.getListOfAppointments().stream()
                            .filter(appointment -> appointment.getAppointmentTime().isBefore(currentTime))
                            .collect(Collectors.toCollection(ArrayList::new));

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

        System.out.println("Please enter the report you want to generate for: ");
        System.out.println("1. Upcoming appointments in the next week. ");
        System.out.println("2. Overdue appointments for a vet visit in the last 6 months. ");

        String userInput = scanner.nextLine();

        switch (userInput) {
            case "1":
                pets.values().forEach(pet -> {
                    System.out.println(pet.getName() + "'s upcoming appointments in the next week: ");
                    pet.getNextWeekAppointments().forEach(appointment -> {
                        System.out.println(appointment);
                    });
                });
                break;

            case "2":
                pets.values().forEach(pet -> {
                    System.out.println(pet.getName() + "'s overdue appointments for a vet visit in the last 6 months: ");
                    pet.getOverdueVetAppointments().forEach(appointment -> {
                        System.out.println(appointment);
                    });

                });
                break;
        }

        System.out.println("Report generated! ");

    }

    public static void savePetsToFile() {
        try {
            ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream("petSchedule.ser"));
            obj.writeObject(pets);
            obj.close();
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }

        System.out.println("Data saved! ");
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
