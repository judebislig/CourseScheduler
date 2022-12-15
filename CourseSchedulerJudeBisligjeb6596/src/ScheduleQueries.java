
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Timestamp;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author judeb
 */
public class ScheduleQueries {
    private static Connection connection;
    private static PreparedStatement addScheduleEntry;
    private static PreparedStatement getScheduleByStudent;
    private static PreparedStatement getScheduledStudentCount;
    private static PreparedStatement getScheduledByCourse;
    private static PreparedStatement getWaitlistedbyCourse;
    private static PreparedStatement dropStudentByCourse;
    private static PreparedStatement dropScheduleByCourse;
    private static PreparedStatement updateEntry;

    private static ResultSet resultSet;
    
    public static void addScheduleEntry(ScheduleEntry entry)
    {
        connection = DBConnection.getConnection();
        try
        {            
            //Add the schedule into the database
            addScheduleEntry = connection.prepareStatement("insert into app.scheduleentry (semester, studentid, coursecode, status, timestamp) values (?, ?, ?, ?, ?)");
            addScheduleEntry.setString(1, entry.getSemester());
            addScheduleEntry.setString(2, entry.getStudentID());
            addScheduleEntry.setString(3, entry.getCourseCode());
            addScheduleEntry.setString(4, entry.getStatus()); 
            addScheduleEntry.setTimestamp(5, entry.getTimestamp());            
            addScheduleEntry.executeUpdate();
            
               
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
    
    public static ArrayList<ScheduleEntry> getScheduleByStudent(String semester, String studentID)
    {
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> schedule = new ArrayList<ScheduleEntry>();
        try
        {
            getScheduleByStudent = connection.prepareStatement("select * from app.scheduleentry where semester = ? and studentid = ?");
            getScheduleByStudent.setString(1, semester);
            getScheduleByStudent.setString(2, studentID);            
            resultSet = getScheduleByStudent.executeQuery();
            
            while(resultSet.next())
            {
                ScheduleEntry entry = new ScheduleEntry(semester, resultSet.getString(3), studentID, resultSet.getString(4), resultSet.getTimestamp(5));
                schedule.add(entry);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return schedule;
        
    }
    
    public static int getScheduledStudentCount(String currentSemester, String courseCode)
    {
        connection = DBConnection.getConnection();
        int studentCount = 0;
        try
        {
            getScheduledStudentCount = connection.prepareStatement("select studentid from app.scheduleentry where semester = (?) and coursecode = (?)");
            getScheduledStudentCount.setString(1, currentSemester);
            getScheduledStudentCount.setString(2, courseCode);   
            
            resultSet = getScheduledStudentCount.executeQuery();
            //Checks how many students there are 
            while(resultSet.next())
            {
                studentCount++;
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return studentCount;
    }    
    
    public static ArrayList<ScheduleEntry> getScheduledStudentsByCourse(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> retList = new ArrayList<ScheduleEntry>();
        try
        {
            getScheduledByCourse = connection.prepareStatement("select studentid, timestamp from app.scheduleentry where semester = ? and courseCode = ? and status = ?");
            getScheduledByCourse.setString(1, semester);
            getScheduledByCourse.setString(2, courseCode);
            getScheduledByCourse.setString(3, "Scheduled");
            resultSet = getScheduledByCourse.executeQuery();
            
            while(resultSet.next())
            {
                retList.add(new ScheduleEntry(semester, courseCode, resultSet.getString(1), "Scheduled", resultSet.getTimestamp(2)));
            }
            
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return retList;
    }

    public static ArrayList<ScheduleEntry> getWaitlistedStudentsByCourse(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> retList = new ArrayList<ScheduleEntry>();
        try
        {
            getWaitlistedbyCourse = connection.prepareStatement("select studentid, timestamp from app.scheduleentry where semester = ? and courseCode = ? and status = ?");
            getWaitlistedbyCourse.setString(1, semester);
            getWaitlistedbyCourse.setString(2, courseCode);
            getWaitlistedbyCourse.setString(3, "Waitlisted");
            resultSet = getWaitlistedbyCourse.executeQuery();
            
            while(resultSet.next())
            {
                retList.add(new ScheduleEntry(semester, courseCode, resultSet.getString(1), "Waitlisted", resultSet.getTimestamp(2)));
            }
            
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return retList;
    }

    public static void dropStudentScheduleByCourse(String semester, String studentID, String courseCode)
    {
        connection = DBConnection.getConnection();
        try
        {
            dropStudentByCourse = connection.prepareStatement("delete from app.scheduleentry where semester = ? and studentid = ? and coursecode = ?");
            
            dropStudentByCourse.setString(1, semester);
            dropStudentByCourse.setString(2, studentID);
            dropStudentByCourse.setString(3, courseCode);
            dropStudentByCourse.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }

    public static void dropScheduleByCourse(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        try
        {
            dropScheduleByCourse = connection.prepareStatement("delete from app.scheduleentry where semester = ? and coursecode = ?");
            dropScheduleByCourse.setString(1, semester);
            dropScheduleByCourse.setString(2, courseCode);
            dropScheduleByCourse.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }

    public static void updateScheduleEntry(String semester, ScheduleEntry entry)
    {
        connection = DBConnection.getConnection();
        try
        {
            updateEntry = connection.prepareStatement("update app.scheduleentry set status = 'Scheduled' where semester = ? and studentid = ? and coursecode = ?");
            updateEntry.setString(1, semester);
            updateEntry.setString(2, entry.getStudentID());
            updateEntry.setString(3, entry.getCourseCode());
            updateEntry.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
}
