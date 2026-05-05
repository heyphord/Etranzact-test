package com.gmfs.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    public int id;
    public String name;
    public String username;
    public String email;
    public Address address;
    public String phone;
    public String website;
    public Company company;


    public static class Address{
        public String street;
        public String suite;
        public String city;
        public String zipcode;
        public Geo geo;
    }

    public static class Company{
        public String name;
        public String catchPhrase;
        public String bs;
    }

    public static class Geo{
        public String lat;
        public String lng;
    }

//    public static class Root{
//        public int id;
//        public String name;
//        public String username;
//        public String email;
//        public Address address;
//        public String phone;
//        public String website;
//        public Company company;
//    }


}


