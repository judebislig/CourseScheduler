/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author acv
 */
public class StudentQueries {
    private static Connection connection;
    private static PreparedStatement addStudent;
    private static PreparedStatement getAllStudents; 
    private static PreparedStatement dropStudent; 
    private static ResultSet resultSet;
    
    public static void addStudent(StudentEntry entry)
    {
        connection = DBConnection.getConnection();
        try
        {
            //Inserts new student into the database
            addStudent = connection.prepareStatement("insert into app.studententry (studentid, firstname, lastname) values (?, ?, ?)");
            addStudent.setString(1, entry.getStudentID());
            addStudent.setString(2, entry.getFirstName());
            addStudent.setString(3, entry.getLastName());
            addStudent.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
    
    public static ArrayList<StudentEntry> getAllStudents()
    {
        connection = DBConnection.getConnection();
        ArrayList<StudentEntry> students = new ArrayList<StudentEntry>();
        try
        {
            //Gets all students from the database
            getAllStudents = connection.prepareStatement("select * from app.studententry order by studentid");
            resultSet = getAllStudents.executeQuery();
            
            //Adds student into the ArrayList
            while(resultSet.next())
            {
                students.add(new StudentEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return students;
        
    }
    
    
    public static StudentEntry getStudent(String studentID)
    {
        connection = DBConnection.getConnection();
        try
        {
            getAllStudents = connection.prepareStatement("select studentid, firstname, lastname from app.studententry where studentid = ?");
            getAllStudents.setString(1, studentID);
            resultSet = getAllStudents.executeQuery();
            
            resultSet.next();
            return new StudentEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
            return null;
        }
    }
    public static void dropStudent(String studentID)
    {
        connection = DBConnection.getConnection();
        try
        {
            getAllStudents = connection.prepareStatement("delete from app.studententry where studentid = ?");
            getAllStudents.setString(1, studentID);
            getAllStudents.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
}
