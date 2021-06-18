package seth.mastery.models;

import org.springframework.stereotype.Component;


public class Guest {

    // guest_id,first_name,last_name,email,phone,state
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String state;

    public Guest(){};
    public Guest(int id, String firstName, String lastName, String email, String phone, String state) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.state = state;
    }

    public Guest(String firstName, String lastName, String email, String phone, String state) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.state = state;
    }

    public Guest(Guest guest1) {
        this.id = guest1.getId();
        this.firstName = guest1.getFirstName();
        this.lastName = guest1.getLastName();
        this.email = guest1.getEmail();
        this.phone = guest1.getPhone();
        this.state = guest1.getState();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
