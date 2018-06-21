/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3;

import java.util.ArrayList;

/**
 *
 * @author Shane
 */
public class Project3 {

    
    public static void main(String[] args) {
        ArrayList<String> print = new ArrayList(0);
        
        //user1 is only set up user
        UserData user = new UserData("admin","password");
        
        /* Used to test adding information
        user.addDoctor("John Smith", "1105 Driver Ln, Charlotte, NC 28567", "CMC", "Primary Care");
        user.addPrescription("amoxicillin", "500mg", "Every 8 hours");
        user.addTracking("Blood Pressure", "120/80", "06/18/2018", "6:20PM");
        user.addAppointment("07/20/2018", "2:30PM", "Dr. Smith", "1105 Driver Ln");
        */
        
        print = user.getDoctor();
        
        for (int i = 0;i<print.size();i++){
            System.out.println(print.get(i));
        }
        
        print = user.getPrescription();
        
        for (int i = 0;i<print.size();i++){
            System.out.println(print.get(i));
        }
        
        print = user.getTracking();
        
        for (int i = 0;i<print.size();i++){
            System.out.println(print.get(i));
        }
        
        print = user.getAppointment();
        
        for (int i = 0;i<print.size();i++){
            System.out.println(print.get(i));
        }
            
        //user.updateDoctor(2, "Jane Doe", "12 Sample Ave, Charlotte, NC", "Atrium", "Endocrinology");
        
        
        user.disconnectDb();
    }
    
}
