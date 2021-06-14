# DON'T WRECK MAH HOUSE 

## Overview

* Application for making arrangements for a Guest to stay with a Host in a Location for a certain duration
* Application will be run by an Administrator
* Major features to be implemented:
  1. View existing Reservations for a host.
  2. Create a Reservation for a guest with a host.
  3. Edit existing Reservations.
  4. Cancel a future Reservation.
* This will be an MVC application with data files for Hosts, Guests, and Reservations
    * Data, Domain, and UI layers will be established as well as a package for the Models
    
* Summary of plan:
    * Set up project via GitHub
    * Add data files for Guests, Hosts, and Reservations to a new directory in root
    * Add Spring and JUnit dependencies to pom.xml
    * Set up package structure for project and initialize files needed
      * **Models** - need models for Administrator, Guest, Host, and Reservation for sure, and maybe Location?
      * **Data** - HostFileRepository, GuestFileRepository, ReservationFileRepository
        * These will all implement their respective interfaces
        * Connect the repositories with their respective data files
        * Reservation will need Hosts and Guests as dependencies
      * **Domain** - HostService, GuestService, and ReservationService, as well as a general Result class will all live here
      * **UI** - just need the View and Controller class
    * Once files are all initialized, I'll go ahead and do the Spring DI and run the Controller in App
    * Start by fleshing out models
    * Then start implementing features
        * Go in order of what I think is most doable/relevant, then go from there
            * e.g. Start with Viewing Reservations for a Host, writing and testing methods needed all the way up to View/Controller and testing the UI
    * Once all MVP features implemented, tested, and debugged, start moving into Stretch Goals
    

## Tasks

### I. Initialization
* I have already connected this project to the repo and added Spring and JUnit in the pom.xml file
* Still need to create a config.xml file in resources for injecting dependencies
  * Estimated Time: 15 minutes
* Create packages for Data, Domain, UI, and Model layers 
    * Within Data, create interfaces for Host, Guest, and Reservation
        * I'll do my best with the methods at first - I might just start with the first feature I plan on implementing and deduce what methods will be needed and add those to the interfaces
        * Have FileRepositories that implement the respective interfaces
    * Inside Domain, have Service files for Host, Guest, and Reservation, as well as a Result class
    * Establish Controller (starting by defining run() and runMenuLoop() methods) and View
    * Estimated time: 30 minutes
* **Models** (rough idea, may be subject to some change)
    1. Administrator
        * Fields: not entirely sure about this or if Administrator even needs to be a model...?
    2. Guest
        * Fields: int id, String firstName, String lastName, String email, String phone, String state
        * Methods: Getters and setters for all fields and a constructor
    3. Host
        * Fields: String id, String lastName, String email, String phone, String address, String city, String state, int postalCode, BigDecimal standardRate, BigDecimal weekendRate 
        * Methods: Getters and setters for all fields and a constructor
    4. Reservation
        * Fields: int id, LocalDate startDate, LocalDate endDate, Host host, Guest guest, BigDecimal totalCost, String filePath (which will be the reservations directory)
        * Methods: Getters and setters, a constructor for filePath
    * Estimated Time: 40 minutes
* Spring DI
  * Estimated Time: 15 minutes
    
#### Total Time for Initialization: 1 hour 40 minutes

### II. View Existing Reservations Feature

* Start by creating a mainMenu() method in View, capturing an int input from Administrator and passing that into a corresponding method in Controller
  * Borrow the readInt, readString, and readRequiredString methods from previous projects as well
    Estimated Time: 15 minutes
* Next, go back down to ReservationFileRepository and write out a findAll method
    * This will take in a host id and gather all the reservations from the database with that id
        * Basically will read from the .csv file that matches the passed in id, deserialize fields, and add them to an array
        * Can recycle serialize and deserialize methods from previous projects
    * Test this method to make sure it's working before moving onto ReservationService
    * Estimated Time: 60 minutes
* Flesh out Result and Response classes in Domain
    * Will closely follow the format from the Sustainable Foraging assessment
    * Estimated Time: 15 minutes
* Write the findAll method inside ReservationService which will take in a host id and return a list
    * Test this method as well
    * Estimated Time: 15 minutes
* Move on to Controller -> create a viewReservations() method and declare it in the switch statement within runMenuLoop()
    * will need to write a few helper methods inside View to place within this method
        * displayHeader in View - should be very similar to previous displayHeader methods
        * helper method displayReservations in View that takes in a list of Reservations and prints them in a desirable format to the console
            * This will use a stream to sort the results meaningfully, probably alphabetically by last name
        * Borrow the enterToContinue() method from Sustainable Foraging
    * Estimated Time: 20 minutes
* Lastly, need a method for capturing the host id input from Administrator and passing it into Controller
    * Not entirely sure how I will accomplish this but the plan as of now is having the user enter the email of the Host, then passing this to a corresponding method in Controller
        * Controller method will take email, search through all the hosts, find a match, then return the id
        * This id will then get passed into ReservationService to get a list of the reservations which will then be printed
    * Estimated Time: 60 minutes

#### Total Time: 3 hours 5 minutes

### III. Create Reservation for a Guest with a Host
* Need method for prompting user for a Host (which we can recycle from above) and a Guest
    * **Stretch Goal**: eventually I might try and ask the user if they want to create a Host or Guest, or choose from the list
    * This will be in View
    * Pass both of the emails onto other methods
      * Recycle above method to retrieve a list of reservations for that Host, including the dates of each reservation
      * Need a getGuest method that will take in an email and return the Guest with the matching email
    
    Estimated Time: 30 minutes
  
* Prompt user for start date and end date
    * Will need a helper method in View for this, converting the strings the user passes in to LocalDates
    * Need to pass these dates into a ReservationService method that will validate that this reservation does not overlap with any others
        * Thus will need a validate Reservation method or two with this
    
    Estimated Time: 40 minutes
  
* Go back into ReservationFileRepository and create and add() method that takes in a Reservation and returns a Reservation
    * Will need a method for generating an id for this new reservation
    * Add this reservation to the list of reservations for this host (using the findAll method to retrieve them)
    * Need a writeAll method to then serialize the fields for the reservation and write them to that host's file
    * Test this method as well to make sure it works
    
    Estimated Time: 60 minutes
    
* Move on to ReservationService and write out the add method within there
    * Need a validate method or two as mentioned previously
    * Test method thoroughly with positive and negative cases
    
    Estimated Time: 60 minutes
  
* Connect all the dots between UI/Domain/Data inside the Controller class
    * Write the addReservation method inside Controller and add it to the switch statement in the menu loop
    
    Estimated Time: 30 minutes
  
#### Total Time: 3 hours 40 minutes

### IV. Edit Existing Reservations

