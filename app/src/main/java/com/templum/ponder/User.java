package com.templum.ponder;

public class User {
    public String username, email,
                  gender, grade,
                  firstName, lastName;


    public User() {

    }

    public User(String username, String email,
                String gender, String grade,
                String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.grade = grade;
        this.firstName = firstName;
        this.lastName = lastName;

    }

}

