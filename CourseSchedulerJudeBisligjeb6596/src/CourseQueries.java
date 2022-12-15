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
public class CourseQueries {
    private static Connection connection;
    private static PreparedStatement getAllCourses;
    private static PreparedStatement addCourse;
    private static PreparedStatement getAllCourseCodes;
    private static PreparedStatement getCourseSeats;   
    private static PreparedStatement dropCourse;


    private static ResultSet resultSet;
    
    public static ArrayList<CourseEntry> getAllCourses(String semester)
    {
        connection = DBConnection.getConnection();
        ArrayList<CourseEntry> courses = new ArrayList<CourseEntry>();
        try
        {
            //Selects all courses in the database
            getAllCourses = connection.prepareStatement("select * from app.courseentry where semester = (?) order by coursecode");
            getAllCourses.setString(1, semester);
            resultSet = getAllCourses.executeQuery();
            
            //Adds all courses to the ArrayList
            while(resultSet.next())
            {
                courses.add(new CourseEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4)));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return courses;
        
    }
    
    public static void addCourse(CourseEntry course)
    {
        connection = DBConnection.getConnection();
        try
        {
            //Adds course into the database
            addCourse = connection.prepareStatement("insert into app.courseentry (semester, coursecode, description, seats) values (?, ?, ?, ?)");
            addCourse.setString(1, course.getSemester());
            addCourse.setString(2, course.getCourseCode());
            addCourse.setString(3, course.getCourseDescription());
            addCourse.setInt(4, course.getSeats());            
            addCourse.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
    
    public static ArrayList<String> getAllCourseCodes(String semester)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> courseCodes = new ArrayList<String>();
        try
        {
            //Gets all coursecodes in a semester
            getAllCourseCodes = connection.prepareStatement("select coursecode from app.courseentry where semester = (?) order by coursecode");
            getAllCourseCodes.setString(1, semester);
            resultSet = getAllCourseCodes.executeQuery();
            
            while(resultSet.next())
            {
                courseCodes.add(resultSet.getString(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return courseCodes;
        
    }
    
    public static int getCourseSeats(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        int courseSeats = 0;
        try
        {
            //Gets all course seats by semester and course
            getCourseSeats = connection.prepareStatement("select seats from app.courseentry where semester = ? and coursecode = ?");
            getCourseSeats.setString(1, semester);
            getCourseSeats.setString(2, courseCode);            
            resultSet = getCourseSeats.executeQuery();
            if (resultSet.next()){
                courseSeats = resultSet.getInt(1);                
            }
            
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return courseSeats;
        
    }
    
//    PART 2
    public static void dropCourse(String semester, String courseCode) {
        connection = DBConnection.getConnection();
        try
        {
            dropCourse = connection.prepareStatement("delete from app.courseentry where semester = (?) and coursecode = (?)");
            dropCourse.setString(1, semester);
            dropCourse.setString(2, courseCode);
                  
            dropCourse.executeUpdate();
            
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
}
