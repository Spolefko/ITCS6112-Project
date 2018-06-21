/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3;

import java.sql.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;

/**
 *
 * @author Shane
 */
public class UserData extends Observable{
    
    private int userId = 1;
    private Connection conn = null;
    private static final String DB_NAME = "healthDb";
    private ArrayList<Doctor> dr = new ArrayList<>(0);
    //private ArrayList<Prescription> pres = new ArrayList<>(0);
    //private ArrayList<Measurement> measure = new ArrayList<>(0);
    //private ArrayList<Appointment> appt = new ArrayList<>(0);
    
    
    /**
     * Constructor
     * 
     * @param userName
     * @param password 
     */
    public UserData(String userName, String password)
    {        
        try{
        conn = DriverManager.getConnection("jdbc:derby:" +
               DB_NAME + ";user=" + userName + ";password=" + password);
        }
        catch (SQLException e) {
            exceptionPrint(e);
        }        
    }
    
    /**
     * Used to disconnect database at end
     */
    public void disconnectDb(){
        // need XJ015 exception to confirm successful shutdown. 
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } 
        catch (SQLException se) {
            if (se.getSQLState().equals("XJ015")) 
                System.out.println("Database shut down normally");
            else{                 
                System.out.println("Database did not shut down normally");
                exceptionPrint(se);
            }
        }
    }
    
    /**
     * 
     * @param se 
     */
    private static void exceptionPrint(SQLException se){
        System.out.println(se.getSQLState());
        System.out.println(se.getErrorCode());
        System.out.println(se.getMessage()); 
    }
    
    
   /**
    * Debating about bothering with the objects for each item 
    * or to just use db as sole source of data
    */
    public void initializeData(){
        String query = "SELECT id, name, address, office, specialty " +
                "FROM doctors" + 
                "WHERE userId = 1";
        
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                dr.add(new Doctor(rs.getInt("id"), rs.getString("name"),
                    rs.getString("address"), rs.getString("office"), 
                    rs.getString("specialty")));
            }
        }catch (SQLException se){
            exceptionPrint(se);
        }
    }
    
    /**
     * Returns all doctors associated with user ID
     * 
     * @return ArrayList<String>
     */
    public ArrayList<String> getDoctor(){

        ArrayList<String> doctorInfo = new ArrayList<>(0);
        String query = "SELECT id, name, address, office, specialty " +
                "FROM doctors " + 
                "WHERE userId = " + userId;
        
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                doctorInfo.add(String.valueOf(rs.getInt("id")) + ";" +
                    rs.getString("name") + ";" +
                    rs.getString("address") + ";" +
                    rs.getString("office") + ";" +
                    rs.getString("specialty"));
            }
        }catch (SQLException se){
            exceptionPrint(se);
        }
            
        return doctorInfo;
    }
    
    /**
     * Returns all prescriptions associated with user ID
     * 
     * @return ArrayList<String>
     */
    public ArrayList<String> getPrescription(){

        ArrayList<String> prescriptionInfo = new ArrayList<>(0);
        String query = "SELECT id, name, dosage, schedule " +
                "FROM prescriptions " + 
                "WHERE userId = " + userId;
        
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                prescriptionInfo.add(String.valueOf(rs.getInt("id")) + ";" +
                    rs.getString("name") + ";" +
                    rs.getString("dosage") + ";" +
                    rs.getString("schedule"));
            }
        }catch (SQLException se){
            exceptionPrint(se);
        }
            
        return prescriptionInfo;
    }
    
    /**
     * Returns all tracking measurements associated with user ID
     * 
     * @return ArrayList<String>
     */
    public ArrayList<String> getTracking(){

        ArrayList<String> trackingInfo = new ArrayList<>(0);
        String query = "SELECT id, type, measurement, date, time " +
                "FROM measurements " + 
                "WHERE userId = " + userId;
        
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                trackingInfo.add(String.valueOf(rs.getInt("id")) + ";" +
                    rs.getString("type") + ";" +
                    rs.getString("measurement") + ";" +
                    rs.getString("date") + ";" +
                    rs.getString("time"));
            }
        }catch (SQLException se){
            exceptionPrint(se);
        }
            
        return trackingInfo;
    }
    
    /**
     * Returns all appointments associated with user ID
     * 
     * @return ArrayList<String>
     */
    public ArrayList<String> getAppointment(){

        ArrayList<String> appointmentInfo = new ArrayList<>(0);
        String query = "SELECT id, date, time, doctorName, address " +
                "FROM appointments " + 
                "WHERE userId = " + userId;
        
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                appointmentInfo.add(String.valueOf(rs.getInt("id")) + ";" +
                    rs.getString("date") + ";" +
                    rs.getString("time") + ";" +
                    rs.getString("doctorName") + ";" +
                    rs.getString("address"));
            }
        }catch (SQLException se){
            exceptionPrint(se);
        }
            
        return appointmentInfo;
    }
    
    /**
     * Adds new doctor to user
     * 
     * @param name
     * @param address
     * @param office
     * @param specialty 
     */
    public void addDoctor(String name, String address, String office, String specialty){
        int id = 0;
        Statement stmt;
        
        //get next ID number available
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT (id) AS count_id FROM doctors");
            
            while (rs.next()){
                id = rs.getInt("count_id");
                id++;
            }
        }
        catch (SQLException se){
            exceptionPrint(se);
        }
        
        //add new doctor to DB
        String insert = "INSERT INTO doctors VALUES(" + 
                userId + ", " + id + ", '" + 
                name + "', '" + address + 
                "', '" + office + "', '" + 
                specialty + "')";
        
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(insert); 
        }catch (SQLException se){
            exceptionPrint(se);
        }
        
        setChanged();
        notifyObservers();
    }
    
    /**
     * Add new prescription to user
     * 
     * @param name
     * @param dosage
     * @param schedule 
     */
    public void addPrescription(String name, String dosage, String schedule){
        int id = 0;
        Statement stmt;
        
        //get next ID number available
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT (id) AS count_id FROM prescriptions");
            
            while (rs.next()){
                id = rs.getInt("count_id");
                id++;
            }
        }
        catch (SQLException se){
            exceptionPrint(se);
        }
        
        //add new prescription to DB
        String insert = "INSERT INTO prescriptions VALUES(" + 
                userId + ", " + id + ", '" + name + "', '" + dosage + "', '" + schedule + "')";
        
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(insert); 
        }catch (SQLException se){
            exceptionPrint(se);
        }
        
        setChanged();
        notifyObservers();
    }
    
    /**
     * add new health tracking measurement to user
     * 
     * @param type
     * @param measurement
     * @param date
     * @param time 
     */
    public void addTracking(String type, String measurement, String date, String time){
        int id = 0;
        Statement stmt;
        
        //get next ID number available
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT (id) AS count_id FROM measurements");
            
            while (rs.next()){
                id = rs.getInt("count_id");
                id++;
            }
        }
        catch (SQLException se){
            exceptionPrint(se);
        }
        
        //add new measurement to DB
        String insert = "INSERT INTO measurements VALUES(" + 
                userId + ", " + id + ", '" + type + "', '" + measurement + "', '" + date + "', '" + time + "')";
        
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(insert); 
        }catch (SQLException se){
            exceptionPrint(se);
        }
        
        setChanged();
        notifyObservers();
    }
    
    /**
     * add new appointment to user
     * 
     * @param date
     * @param time
     * @param doctorName
     * @param address 
     */
    public void addAppointment(String date, String time, String doctorName, String address){
        int id = 0;
        Statement stmt;
        
        //get next ID number available
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT (id) AS count_id FROM appointments");
            
            while (rs.next()){
                id = rs.getInt("count_id");
                id++;
            }
        }
        catch (SQLException se){
            exceptionPrint(se);
        }
        
        //add new appointment to DB
        String insert = "INSERT INTO appointments VALUES(" + 
                userId + ", " + id + ", '" + date + "', '" + time + "', '" + doctorName + "', '" + address + "')";
        
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(insert); 
        }catch (SQLException se){
            exceptionPrint(se);
        }
        
        setChanged();
        notifyObservers();
    }

/**
 * Updates all of a doctor's information
 * 
 * @param id
 * @param name
 * @param address
 * @param office
 * @param specialty 
 */    
    public void updateDoctor(int id, String name, String address, String office, String specialty) {
        //update a specific doctor to DB
        String update = "UPDATE doctors " + 
                "SET name = '" + name + "', " +
                "address = '" + address + "', " +
                "office = '" + office + "', " +
                "specialty = '" + specialty + "' " +
                "WHERE id = " + id;        
        try (Statement stmt = conn.createStatement()){
            stmt.executeUpdate(update); 
        }catch (SQLException se){
            exceptionPrint(se);
        }       
        
        setChanged();
        notifyObservers();        
    }
    
    /**
     * Updates all information for a prescription
     * @param id
     * @param name
     * @param dosage
     * @param schedule 
     */
    public void updatePrescription(int id, String name, String dosage, String schedule) {
        //update a specific doctor to DB
        String update = "UPDATE prescriptions " + 
                "SET name = '" + name + "', " +
                "dosage = '" + dosage + "', " +
                "schedule = '" + schedule + "', " +
                "WHERE id = " + id;        
        try (Statement stmt = conn.createStatement()){
            stmt.executeUpdate(update); 
        }catch (SQLException se){
            exceptionPrint(se);
        }       
        
        setChanged();
        notifyObservers();
    }
        
    /**
     * Updates all information for an appointment
     * @param id
     * @param date
     * @param time
     * @param doctorName
     * @param address 
     */
    public void updateAppointment(int id, String date, String time, String doctorName, String address) {
        //update a specific doctor to DB
        String update = "UPDATE doctors " + 
                "SET date = '" + date + "', " +
                "time = '" + time + "', " +
                "doctorName = '" + doctorName + "', " +
                "address = '" + address + "' " +
                "WHERE id = " + id;        
        try (Statement stmt = conn.createStatement()){
            stmt.executeUpdate(update); 
        }catch (SQLException se){
            exceptionPrint(se);
        }       
        
        setChanged();
        notifyObservers();
    }
            
    /**
     * Updates all information for a tracking measurement
     * @param id
     * @param type
     * @param measurement
     * @param date
     * @param time 
     */
    public void updateTracking(int id, String type, String measurement, String date, String time) {
        //update a specific doctor to DB
        String update = "UPDATE doctors " + 
                "SET type = '" + type + "', " +
                "measurement = '" + measurement + "', " +
                "date = '" + date + "', " +
                "time = '" + time + "' " +
                "WHERE id = " + id;        
        try (Statement stmt = conn.createStatement()){
            stmt.executeUpdate(update); 
        }catch (SQLException se){
            exceptionPrint(se);
        }       
        
        setChanged();
        notifyObservers();
    }
    
    /**
     * Needs to be used to create the database and set up
     * authentication. Plan to deploy with database already
     * set up. Needs to be followed by a restart and creating
     * the tables.
     * 
     * @throws SQLException 
     */
    public static void initializeDb() throws SQLException{
        Connection conn;        
        conn = DriverManager.getConnection("jdbc:derby:" + DB_NAME + ";create=true");
               
        try (Statement stmt = conn.createStatement()) {
            // Setting requireAuthentication
            stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" +
                    "'derby.connection.requireAuthentication', 'true')");
            
            // Setting authentication scheme to Derby
            stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" +
                    "'derby.authentication.provider', 'BUILTIN')");
            
            // Create users
            stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" +
                    "'derby.user.admin', 'password')");
            stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" +
                    "'derby.user.user1', 'password')");
            
            // Set default connection mode to no access
            stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" +
                    "'derby.database.defaultConnectionMode', 'noAccess')");
            
            // Define read-write users
            stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" +
                    "'derby.database.fullAccessUsers', 'admin,user1')");
            
            // change to true when deployed
            stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" +
                    "'derby.database.propertiesOnly', 'false')");
        }
        catch (SQLException se) {
            exceptionPrint(se);
        }
        
        //close connection
        conn.close();      
    }
    
    /**
     * Needs to be run if database is created to set up tables.
     * Database should be deployed with this already made
     * 
     * @throws SQLException 
     */
    private void createTables() throws SQLException{
        String createString = "CREATE TABLE users " +
                "(userId INTEGER not NULL, " + "userName VARCHAR(15) not NULL, " +
                "PRIMARY KEY (userId))";
        
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
        }
        catch (SQLException se){
            exceptionPrint(se);
        }
        finally {
            if (stmt!=null)
                stmt.close();
        }
        
        createString = "CREATE TABLE doctors " +
                "(userId INTEGER NOT NULL, " + "id INTEGER NOT NULL, " +
                "name VARCHAR(50), " + "address VARCHAR(255), " +
                "office VARCHAR(50), " + "specialty VARCHAR(50), " +
                "PRIMARY KEY (id))";
        
        stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
        }
        catch (SQLException se){
            exceptionPrint(se);
        }
        finally {
            if (stmt!=null)
                stmt.close();
        }
        
        createString = "CREATE TABLE prescriptions " +
                "(userId INTEGER NOT NULL, " + "id INTEGER NOT NULL, " +
                "name VARCHAR(50), " + "dosage VARCHAR(50), " +
                "schedule VARCHAR(200), " + "PRIMARY KEY (id))";
        
        stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
        }
        catch (SQLException se){
            exceptionPrint(se);
        }
        finally {
            if (stmt!=null)
                stmt.close();
        }
                
        createString = "CREATE TABLE measurements " +
                "(userId INTEGER NOT NULL, " + "id INTEGER NOT NULL, " +
                "type VARCHAR(50), " + "measurement VARCHAR(50), " +
                "date VARCHAR(50), " + "time VARCHAR(50), " +
                "PRIMARY KEY (id))";
        
        stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
        }
        catch (SQLException se){
            exceptionPrint(se);
        }
        finally {
            if (stmt!=null)
                stmt.close();
        }
        
        createString = "CREATE TABLE appointments " +
                "(userId INTEGER NOT NULL, " + "id INTEGER NOT NULL, " +
                "date VARCHAR(50), " + "time VARCHAR(50), " +
                "doctorName VARCHAR(50), " + "address VARCHAR(50), " +
                "PRIMARY KEY (id))";
        
        stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
        }
        catch (SQLException se){
            exceptionPrint(se);
        }
        finally {
            if (stmt!=null)
                stmt.close();
        }
        
    }
    
    /**
     * Would be used if we wanted additional users
     * 
     * @param userName
     * @throws SQLException 
     */
    public void addUser(String userName) throws SQLException{
        int id = 0;
        Statement stmt;
        
        //get next ID number available
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT (userId) AS count_id FROM users");
            
            while (rs.next()){
                id = rs.getInt("count_id");
                id++;
            }
        }
        catch (SQLException se){
            exceptionPrint(se);
        }
        
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(
            "INSERT INTO users " +
            "VALUES (" + id + ", '" + userName + "')");
        }
        catch (SQLException se){
            exceptionPrint(se);
        }         
    }
    
    /**
     * Used to get username and ID. Probably going to delete. 
     * Mostly was for testing
     */
    public void getUser(){
        
        try{
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users");
        
        
        while(rs.next()){
        int id = rs.getInt("userId");
        String user = rs.getString("userName");
        
        System.out.println(id + user);
            }
        }
        catch(SQLException se){
            exceptionPrint(se);
        }     
    }
}




