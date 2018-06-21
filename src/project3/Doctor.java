package project3;

import java.sql.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Shane
 */
public class Doctor {
    private int id;
    private String name;
    private String address;
    private String office;
    private String specialty;
    
    public Doctor (int id, String name, String address, String office, String specialty){
        this.id = id;
        this.name = name;
        this.address = address;
        this.office = office;
        this.specialty = specialty;
    }
    
    public void addDr(){
        
    }
    
    
    public String getInfo(){
        String info;
         
        info = id + "," + name + "," + address + "," + office + "," + specialty;
        
        return info;
    }
}
