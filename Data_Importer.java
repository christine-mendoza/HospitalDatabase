import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.io.*;

public class Data_Importer {
	 
   private Connection connect() {
   
      Connection conn = null;
   	//SQLite connection string
      String url = "jdbc:sqlite:C:\\sqlite\\hospitalDB.sl3";
   
      try {
         conn = DriverManager.getConnection(url);
      	
      	//This will turn on foreign keys
         conn.createStatement().executeUpdate("PRAGMA foreign_keys = ON;");
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return conn;
   }

   public void insertPerson (String lastName, String firstName, String type) {
      String sql = "INSERT INTO person(lastName, firstName, type) "
         + "VALUES(?,?,?);";
   
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setString(1, lastName);
         ps.setString(2, firstName);
         ps.setString(3, type);
         ps.executeUpdate();
         ps.close();
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public void insertDoctor (String drName) {
      String sql = "INSERT INTO doctor(drName) VALUES(?);";
   
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setString(1, drName);
         ps.executeUpdate();
         ps.close();
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }


   public void insertPatient (String firstName, String lastName, String emergencyContact, String emergencyPhone,
   	 String primaryDr, int patientID) {
   	 
      String sql = "INSERT INTO patient(firstName, lastName, emergencyContact, emergencyPhone, primaryDr, patientID) "
         + "VALUES(?,?,?,?,?,?);";
   
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
      
         ps.setString(1, firstName);
         ps.setString(2, lastName);
         ps.setString(3, emergencyContact);
         ps.setString(4, emergencyPhone);
         ps.setString(5, primaryDr);	
         ps.setInt(6, patientID);
         ps.executeUpdate();
         ps.close();
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }


   public void insertRoom (int roomNum, String patient, String assignDate) {
      String sql = "INSERT INTO room(roomNum, patient, assignDate) VALUES(?,?,?);";
   
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setInt(1, roomNum);
         ps.setString(2, patient);
         ps.setString(3, assignDate);
         ps.executeUpdate();
         ps.close();
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }


   public void insertAdmitInstance(String patient, String admitDate, String admitDoctor, String releaseDate,
   	String currentDiagnosis, int roomNum, String insuranceComp, String insurancePolicyNum, int admitID, int diagnosisID) {
      String sql = "INSERT INTO admitInstance(patient, admitDate, admitDoctor, releaseDate, currentDiagnosis,"
         + "roomNum, insuranceComp, insurancePolicyNum, admitID, diagnosisID) VALUES(?,?,?,?,?,?,?,?,?,?);";
   
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setString(1, patient);
         ps.setString(2, admitDate);
         ps.setString(3,  admitDoctor);
         ps.setString(4, releaseDate);
         ps.setString(5, currentDiagnosis);
         ps.setInt(6, roomNum);
         ps.setString(7, insuranceComp);
         ps.setString(8, insurancePolicyNum);
         ps.setInt(9,  admitID);
         ps.setInt(10, diagnosisID);
         ps.executeUpdate();
         ps.close();
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public void insertTreatment(String patient, String orderedBy, String treatmentType, String treatmentName,
   	String treatmentDate, int admitInstance, String performedBy) {
      String sql = "INSERT INTO treatment(patient, orderedBy, treatmentType, treatmentName, treatmentDate, admitInstance,"
         + "performedBy) "
         + "VALUES(?,?,?,?,?,?, ?);";
   
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setString(1, patient);
         ps.setString(2, orderedBy);
         ps.setString(3,  treatmentType);
         ps.setString(4, treatmentName);
         ps.setString(5, treatmentDate);
         ps.setInt(6, admitInstance);
         ps.setString(7,performedBy);
         ps.executeUpdate();
         ps.close();
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public void insertDrToPatient(String patient, String doctor) {
      String sql = "INSERT INTO drToPatient(patient, doctor) VALUES(?,?);";
   
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setString(1, patient);
         ps.setString(2, doctor);
         ps.executeUpdate();
         ps.close();
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public void insertPopulateRooms(int room) {
      String sql = "INSERT INTO room(roomNum) VALUES(?);";
   
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setInt(1, room);
         ps.executeUpdate();
         ps.close();
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public int selectMaxPatientID() {
      String sql = "SELECT MAX(patientID) as patient FROM patient;";
      int count = 0;;
      try(Connection conn = this.connect();) {
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);
         if (!rs.next()) {
            return 1;
         }
         else {
            count = rs.getInt("patient") + 1;
         }
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return count;
   }

   public int selectMaxAdmitID() {
      String sql = "SELECT MAX(admitID) as admitInstance FROM admitInstance;";
      int count = 0;;
      try(Connection conn = this.connect();) {
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);
         if (!rs.next()) {
            return 1;
         }
         else {
            count = rs.getInt("admitInstance") + 1;
         }
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return count;
   }

   public int selectAdmitInstanceID(String lastName) {
   
      String sql = "SELECT admitID FROM admitInstance WHERE patient == ?";
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);	
         ps.setString(1, lastName);
         ResultSet rs = ps.executeQuery();
      
         return rs.getInt("admitID");
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return 0;
   }

   public String patientExistCheck(String lastName) {
      String sql = "SELECT lastName FROM patient WHERE lastName == ?";
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);	
         ps.setString(1, lastName);
         ResultSet rs = ps.executeQuery();
         if(rs.next()) {			
            return rs.getString("lastName");
         }
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return null;
   }
		
   public String doctorExistCheck(String lastName) {
      String sql = "SELECT drName FROM doctor WHERE drName == ?";
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);	
         ps.setString(1, lastName);
         ResultSet rs = ps.executeQuery();
         if(rs.next()) {			
            return rs.getString("drName");
         }
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return null;
   }	

   public void insertAdmitID(int admitInstance) {
      String sql = "INSERT INTO patient(admitInstance) VALUES(?);";
   
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setInt(1, admitInstance);
         ps.executeUpdate();
         ps.close();
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public void updatePatientAdmitID(int admitInstance, String patientName) {
      String sql = "UPDATE patient SET admitInstance = ? WHERE lastName = ?;";
   
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setInt(1, admitInstance);
         ps.setString(2, patientName);
         ps.executeUpdate();
         ps.close();
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public void updateRoom(int admitInstance, String patientName, int roomNum) {
      String sql = "UPDATE room SET admitInstance = ?, patient = ? WHERE roomNum = ?;";
   
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setInt(1, admitInstance);
         ps.setString(2, patientName);
         ps.setInt(3, roomNum);
         ps.executeUpdate();
         ps.close();
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public void updatePatient(int admitInstance, String patientName, int roomNum) {
      String sql = "UPDATE room SET admitInstance = ?, patient = ? WHERE roomNum = ?;";
   
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setInt(1, admitInstance);
         ps.setString(2, patientName);
         ps.setInt(3, roomNum);
         ps.executeUpdate();
         ps.close();
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public String roomExistCheck(int roomNum) {
      String sql = "SELECT patient FROM room WHERE roomNum == ?";
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);	
         ps.setInt(1, roomNum);
         ResultSet rs = ps.executeQuery();
         if(rs.next()) {			
            return rs.getString("patient");
         }
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return null;
   }

   public String diagnosisExistCheck(String diagnosis) {
      String sql = "SELECT currentDiagnosis FROM admitInstance WHERE currentDiagnosis == ?";
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);	
         ps.setString(1, diagnosis);
         ResultSet rs = ps.executeQuery();
         if(rs.next()) {			
            return rs.getString("currentDiagnosis");
         }
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return null;
   }

   public int diagnosisIDupdate(String diagnosis) {
      String sql = "SELECT diagnosisID FROM admitInstance WHERE currentDiagnosis == ?";
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);	
         ps.setString(1, diagnosis);
         ResultSet rs = ps.executeQuery();
         if(rs.next()) {			
            return rs.getInt("diagnosisID");
         }
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return 0;
   }

   public int admitIDforTreatment(String patient, String treatmentDate) {
      String sql = "SELECT admitID FROM admitInstance WHERE patient = ? AND admitDate <= ? AND releaseDate >= ?"
         + "OR patient = ? AND releaseDate IS NULL";
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);	
         ps.setString(1, patient);
         ps.setString(2, treatmentDate);
         ps.setString(3, treatmentDate);
         ps.setString(4, patient);
         ResultSet rs = ps.executeQuery();
         if(rs.next()) {			
            return rs.getInt("admitID");
         }
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return 0;
   }

   public String selectPersonType(String name) {
      String sql = "SELECT type FROM person WHERE lastName == ?";
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);	
         ps.setString(1, name);
         ResultSet rs = ps.executeQuery();
         if(rs.next()) {			
            return rs.getString("type");
         }
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return null;
   }


   public void insertadmitIDTreatment(int admitID) {
      String sql = "INSERT INTO patient(admitInstance) VALUES(?);";
   
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setInt(1, admitID);
         ps.executeUpdate();
         ps.close();
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public String selectPrimaryDr(String patient) {
      String sql = "SELECT admitDoctor FROM admitInstance WHERE patient == ?";
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);	
         ps.setString(1, patient);
         ResultSet rs = ps.executeQuery();
         if(rs.next()) {			
            return rs.getString("admitDoctor");
         }
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return null;
   }

   public int selectCurrentAdmitID(String name) {
      String sql = "SELECT admitInstance FROM patient WHERE lastName == ?;";
      int count = 0;;
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);	
         ps.setString(1, name);
         ResultSet rs = ps.executeQuery();
         if (!rs.next()) {
            return 1;
         }
         else {
            count = rs.getInt("admitInstance");
         }
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return count;
   }




   public void insertPatientId(int patientID, String emergencyContact, String emergencyPhone) {
      String sql = "INSERT INTO patient(patientID, emergencyContact, emergencyPhone) VALUES(?,?,?);";
   
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setInt(1, patientID);
         ps.setString(2, emergencyContact);
         ps.setString(3, emergencyPhone);
         ps.executeUpdate();
         ps.close();
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

/** QUERY STATEMENTS**/
   public void selectOccupiedRooms() {
      String sql = "SELECT a.roomNum as room, a.patient, b.admitDate FROM room a, admitInstance b"
         + " WHERE a.patient = b.patient AND a.patient IS NOT NULL GROUP BY a.roomNum;";
   
      try(Connection conn = this.connect();) {
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);
      
         String heading1 = "ROOM: ";
         String heading2 = "PATIENT: ";
         String heading3 = "ADMIT DATE: ";
         System.out.printf("%-15s %-20s %-20s %n", heading1, heading2, heading3);
      
         while(rs.next()) {
            int r = rs.getInt("room");
            String pat = rs.getString("patient");
            String ad = rs.getString("admitDate");
            System.out.printf("%-15s %-20s %-20s %n", r, pat, ad);
         }
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public void selectUnoccupiedRooms() {
      String sql = "SELECT roomNum as open_rooms FROM room WHERE patient IS NULL;";
   
      try(Connection conn = this.connect();) {
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);
      
         String heading1 = "OPEN ROOMS: ";
         System.out.printf("%-15s %n", heading1);
      
         while(rs.next()) {
            int r = rs.getInt("open_rooms");
            System.out.printf("%-15s %n", r);
         }
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public void selectAllRoomsWithPatientInfo() {
      String sql = "SELECT rm.roomNum as room, ad.patient, ad.admitDate FROM room AS rm"
         + " LEFT JOIN admitInstance AS ad ON rm.patient = ad.patient GROUP BY rm.roomNum;";
   
      try(Connection conn = this.connect();) {
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);
      
         String heading1 = "ROOM: ";
         String heading2 = "PATIENT: ";
         String heading3 = "ADMIT DATE: ";
         System.out.printf("%-15s %-20s %-20s %n", heading1, heading2, heading3);
      
         while(rs.next()) {
            int r = rs.getInt("room");
            String pat = rs.getString("patient");
            if (pat == null) {
               pat = "";
            }
            String ad = rs.getString("admitDate");
            if (ad == null) {
               ad = "";
            }
            System.out.printf("%-15s %-20s %-20s %n", r, pat, ad);
         
         }
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public void selectPatientsAndInfo() {
      String sql = "SELECT * FROM patient;";
   
      try(Connection conn = this.connect();) {
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);
      
         String heading1 = "PATIENT ID: ";
         String heading2 = "FIRST NAME: ";
         String heading3 = "LAST NAME: ";
         String heading4 = "PRIMARY DR: ";
         String heading5 = "LATEST ADMIT: ";
         String heading6 = "EMERGENCY CONTACT: ";
         String heading7 = "EMERGENCY CONTACT PHONE#: ";
         System.out.printf("%-15s %-20s %-20s %-20s %-15s %-30s %-20s %n", heading1, heading2, heading3, heading4, heading5, heading6, heading7);
      
         while(rs.next()) {
            int patID = rs.getInt("patientID");
            String fn = rs.getString("firstName");
            String ls = rs.getString("lastName");
            String pd = rs.getString("primaryDr");
            int ai = rs.getInt("admitInstance");
            String ec = rs.getString("emergencyContact");
            String ep = rs.getString("emergencyPhone");
            System.out.printf("%-15s %-20s %-20s %-20s %-15s %-30s %-20s %n", patID, fn, ls, pd, ai, ec, ep);
         }
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
   public void selectAdmittedPatients() {
      String sql = "SELECT a.patientID, b.patient FROM patient a, admitInstance b WHERE a.lastName = b.patient AND b.releaseDate IS NULL;";
   
      try(Connection conn = this.connect();) {
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);
      
         String heading1 = "PATIENT ID: ";
         String heading2 = "PATIENT NAME: ";
         System.out.printf("%-15s %-20s %n", heading1, heading2);
      
         while(rs.next()) {
         
            int patID = rs.getInt("patientID");
            String pat = rs.getString("patient");
            System.out.printf("%-15s %-20s %n", patID, pat);
         }
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public void selectPatientDischargeDateInRange(String date1, String date2) {
      String sql = "SELECT a.patientID, b.patient FROM patient a, admitInstance b WHERE a.lastName = b.patient"
         + " AND b.releaseDate > ? AND b.releaseDate < ? GROUP BY b.patient;";
   
      try(Connection conn = this.connect();) {
      
         PreparedStatement ps = conn.prepareStatement(sql);	
         ps.setString(1, date1);
         ps.setString(2, date2);
         ResultSet rs = ps.executeQuery();
      
         String heading1 = "PATIENT ID: ";
         String heading2 = "PATIENT NAME: ";
         System.out.printf("%-15s %-20s %n", heading1, heading2);
      
         while(rs.next()) {
         
            int patID = rs.getInt("patientID");
            String pat = rs.getString("patient");
            System.out.printf("%-15s %-20s %n", patID, pat);
         }
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public void selectPatientAdmitDateInRange(String date1, String date2) {
      String sql = "SELECT a.patientID, b.patient FROM patient a, admitInstance b WHERE a.lastName = b.patient"
         + " AND admitDate > ? AND admitDate < ? GROUP BY b.patient;";
   
      try(Connection conn = this.connect();) {
      
         PreparedStatement ps = conn.prepareStatement(sql);	
         ps.setString(1, date1);
         ps.setString(2, date2);
         ResultSet rs = ps.executeQuery();
      
         String heading1 = "PATIENT ID: ";
         String heading2 = "PATIENT NAME: ";
         System.out.printf("%-15s %-20s %n", heading1, heading2);
      
         while(rs.next()) {
            int patID = rs.getInt("patientID");
            String pat = rs.getString("patient");
            System.out.printf("%-15s %-20s %n", patID, pat);
         }
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public void selectPatientAdmissions(String patient) {
      String sql = "SELECT patient, admitID, currentDiagnosis as diagnosis, admitDate, admitDoctor, releaseDate"
         + " FROM admitInstance WHERE patient = ?;";
   
      try(Connection conn = this.connect();) {
      
         PreparedStatement ps = conn.prepareStatement(sql);	
         ps.setString(1, patient);
         ResultSet rs = ps.executeQuery();
      
         String heading1 = "PATIENT: ";
         String heading2 = "ADMIT ID: ";
         String heading3 = "DIAGNOSIS: ";
         String heading4 = "ADMIT DATE: ";
         String heading5 = "PRIMARY DR: ";
         String heading6 = "RELEASE DATE: ";
         System.out.printf("%-20s %-20s %-30s %-20s %-20s %-20s %n", heading1, heading2, heading3, heading4, heading5, heading6);
      
         while(rs.next()) {
         
            String pat = rs.getString("patient");
            int adID = rs.getInt("admitID");
            String d = rs.getString("Diagnosis");
            String ad = rs.getString("admitDate");
            String adr = rs.getString("admitDoctor");
            String rd = rs.getString("releaseDate");
            if (rd == null) {
               rd = "";
            }
            System.out.printf("%-20s %-20s %-30s %-20s %-20s %-20s %n", pat, adID, d, ad, adr, rd);
         
         }
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
   public void selectPatientTreatments(String patient) {
      String sql = "SELECT patient, treatmentName, admitInstance, treatmentID FROM treatment"
         + " WHERE patient = ? ORDER BY admitInstance DESC, treatmentID ASC;";
   
      try(Connection conn = this.connect();) {
      
         PreparedStatement ps = conn.prepareStatement(sql);	
         ps.setString(1, patient);
         ResultSet rs = ps.executeQuery();
         String heading1 = "PATIENT: ";
         String heading2 = "TREATMENT: ";
         String heading3 = "ADMIT ID: ";
         String heading4 = "TREATMENT ID: ";
         System.out.printf("%-20s %-20s %-20s %-20s %n", heading1, heading2, heading3, heading4);
      
         while(rs.next()) {
         
            String pat = rs.getString("patient");
            String tn = rs.getString("treatmentName");
            int adID = rs.getInt("admitInstance");
            int tID = rs.getInt("treatmentID");
            System.out.printf("%-20s %-20s %-20s %-20s %n", pat, tn, adID, tID);
         
         }
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
   public void selectPatientsAdmitted30DaysFromDischarge() {
      String sql = "SELECT a.patientID, b.patient, b.currentDiagnosis, b.admitDoctor FROM patient a, (SELECT admitInstance.patient, currentDiagnosis, admitDoctor"
         + " FROM admitInstance INNER JOIN (SELECT patient, MAX(JULIANDAY(releaseDate), '+ 30 days') AS maxval FROM admitInstance GROUP BY patient)"
         + " AS maxrel on admitInstance.patient = maxrel.patient WHERE admitInstance.releaseDate IS NULL AND maxval IS NOT NULL) b WHERE a.lastName = b.patient;";
   
      try(Connection conn = this.connect();) {
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);
      
         String heading1 = "PATIENT ID: ";
         String heading2 = "PATIENT: ";
         String heading3 = "DIAGNOSIS: ";
         String heading4 = "PRIMARY DR: ";
         System.out.printf("%-20s %-20s %-20s %-20s %n", heading1, heading2, heading3, heading4);
      
         while(rs.next()) {
         
            int patID = rs.getInt("patientID");
            String pat = rs.getString("patient");
            String d = rs.getString("currentDiagnosis");
            String ad = rs.getString("admitDoctor");
            System.out.printf("%-20s %-20s %-20s %-20s %n", patID, pat, d, ad);
         
         }
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public void selectAdmissionDetails() {
      String sql = "SELECT a.patient, COUNT(DISTINCT a.admitDate) as TotalAdmissions,  avg(JULIANDAY(a.releaseDate) - JULIANDAY(a.admitDate)) as avgDuration, MAX(JULIANDAY(a.admitDate) - JULIANDAY(b.priorAdmitDate)) as MaxSpan,"
         + " MIN(JULIANDAY(a.admitDate) - JULIANDAY(b.priorAdmitDate)) as MinSpan, avg(JULIANDAY(a.admitDate) - JULIANDAY(b.priorAdmitDate)) as AvgSpan"
         + " FROM admitInstance a, (select a.patient,a.admitDate, (select MAX(b.admitDate) from admitInstance"
         + " as b where b.admitDate < a.admitDate and a.patient = b.patient) as priorAdmitDate from admitInstance as a order by a.patient ASC, a.admitDate ASC) b"
         + " WHERE a.patient = b.patient GROUP BY a.patient;";
   
      try(Connection conn = this.connect();) {
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);
      
         String heading1 = "PATIENT: ";
         String heading2 = "TOTAL ADMISSIONS: ";
         String heading3 = "AVG DURATION: ";
         String heading4 = "MAX SPAN: ";
         String heading5 = "MIN SPAN: ";
         String heading6 = "AVG SPAN: ";
         System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %n", heading1, heading2, heading3, heading4, heading5,heading6);
      
         while(rs.next()) {
            String pat = rs.getString("patient");
            String ta = rs.getString("TotalAdmissions");
            if (ta == null) {
               ta = "";
            }
            String ad = rs.getString("avgDuration");
            if(ad == null) {
               ad = "";
            }
            String max = rs.getString("MaxSpan");
            if(max == null) {
               max = "";
            }
            String min = rs.getString("MinSpan");
            if(min == null) {
               min = "";
            }
            String as = rs.getString("AvgSpan");
            if(as == null) {
               as = "";
            }
         
            System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %n", pat,ta,ad,max,min,as);
         }
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public void selectDiagnosisInfo() {
      String sql = "SELECT diagnosisID, currentDiagnosis as diagnosis, COUNT(currentDiagnosis) as totalCount FROM admitInstance GROUP BY currentDiagnosis HAVING COUNT(currentDiagnosis > 4) ORDER BY currentDiagnosis DESC;";
   
      try(Connection conn = this.connect();) {
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);
         String heading1 = "DIAGNOSIS ID: ";
         String heading2 = "DIAGNOSIS: ";
         String heading3 = "TOTAL COUNT: ";
         System.out.printf("%-20s %-30s %-20s %n", heading1, heading2, heading3);
      
         while(rs.next()) {
            int dID = rs.getInt("diagnosisID");
            String d = rs.getString("diagnosis");
            String tc = rs.getString("totalCount");
            System.out.printf("%-20s %-30s %-20s %n", dID,d,tc);
         }
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
   public void selectTreatmentOnAdmittedPatients() {
      String sql = "SELECT a.treatmentID, a.treatmentName, COUNT(a.treatmentName) as totalCount FROM treatment a, admitInstance b WHERE a.patient = b.patient AND b.releaseDate IS NULL GROUP BY treatmentName ORDER BY treatmentName;";
   
      try(Connection conn = this.connect();) {
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);
         String heading1 = "TREATMENT ID: ";
         String heading2 = "TREATMENT: ";
         String heading3 = "TOTAL COUNT: ";
         System.out.printf("%-20s %-30s %-20s %n", heading1, heading2, heading3);
         while(rs.next()) {
            int tID = rs.getInt("treatmentID");
            String tn = rs.getString("treatmentName");
            String tc = rs.getString("totalCount");
            System.out.printf("%-20s %-30s %-20s %n", tID,tn,tc);
         
         }
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
   public void selectDiagnosisisOnHighAdmitPatients() {
      String sql = "SELECT currentDiagnosis as diagnosis FROM admitInstance GROUP BY currentDiagnosis HAVING COUNT(admitDate) > 5 ORDER BY currentDiagnosis DESC;";
   
      try(Connection conn = this.connect();) {
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);
         String heading1 = "DIAGNOSIS: ";
         System.out.printf("%-35s %n", heading1);
         while(rs.next()) {
            String d = rs.getString("diagnosis");
            System.out.printf("%-35s %n", d);
         }
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
   public void selectTreatmentPatientAndDoctor() {
      String sql = "SELECT treatmentName, patient, orderedBy FROM treatment;";
   
      try(Connection conn = this.connect();) {
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);
         String heading1 = "TREATMENT: ";
         String heading2 = "PATIENT: ";
         String heading3 = "ORDERED BY: ";
         System.out.printf("%-30s %-25s %-25s %n", heading1, heading2, heading3);
         while(rs.next()) {
            String tn = rs.getString("treatmentName");
            String pat = rs.getString("patient");
            String ob = rs.getString("orderedBy");
            System.out.printf("%-30s %-25s %-25s %n", tn, pat,ob);
         }
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public void selectEmployees() {
      String sql = "SELECT * FROM person WHERE type != 'P' ORDER BY lastName ASC, firstName ASC;";
   
      try(Connection conn = this.connect();) {
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);
         String heading1 = "FIRST NAME: ";
         String heading2 = "LAST NAME: ";
         String heading3 = "JOB TYPE: ";
         System.out.printf("%-20s %-20s %-20s %n", heading1, heading2, heading3);
         while(rs.next()) {
            String fn = rs.getString("firstName");
            String ln = rs.getString("lastName");
            String type = rs.getString("type");
            System.out.printf("%-20s %-20s %-20s %n", fn, ln,type);
         }
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public void selectPrimaryDoctorOfHighAdmitPatients() {
      String sql = "SELECT a.admitDoctor as Doctor FROM admitInstance a, (SELECT patient, admitDate, LAG(admitDate, 3,0)"
         + " OVER (PARTITION BY patient ORDER BY admitDate) as admitFour FROM admitInstance) b"
         + " WHERE a.patient = b.patient AND(a.admitDate - admitFour) <= 365 AND admitFour > 1 GROUP BY a.admitDoctor;";
   
      try(Connection conn = this.connect();) {
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);
         String heading1 = "DOCTOR: ";
         System.out.printf("%-20s %n", heading1);
         while(rs.next()) {
            String dr = rs.getString("Doctor");
            System.out.printf("%-20s %n", dr);
         }
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
   public void selectDiagnosesForDoctor(String doctor) {
      String sql = "SELECT admitDoctor, currentDiagnosis as diagnosis, COUNT(currentDiagnosis) as totalCount FROM admitInstance WHERE admitDoctor = ? GROUP BY currentDiagnosis ORDER BY admitDate DESC;";
   
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);	
         ps.setString(1, doctor);
         ResultSet rs = ps.executeQuery();
      
         String heading1 = "DOCTOR: ";
         String heading2 = "DIAGNOSIS: ";
         String heading3 = "TOTAL COUNT: ";
         System.out.printf("%-20s %-35s %-20s %n", heading1, heading2, heading3);
         while(rs.next()) {
            String ad = rs.getString("admitDoctor");
            String d = rs.getString("diagnosis");
            String tc = rs.getString("totalCount");
            System.out.printf("%-20s %-35s %-20s %n", ad, d,tc);
         }
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
   public void selectTreatmentsOrderedByDoctor(String doctor) {
      String sql = "SELECT orderedBy, treatmentName, COUNT(treatmentName) as totalCount FROM treatment WHERE orderedBy = ? GROUP BY treatmentName ORDER BY treatmentDate DESC;";
   
      try(Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);	
         ps.setString(1, doctor);
         ResultSet rs = ps.executeQuery();
      
         String heading1 = "ORDERED BY: ";
         String heading2 = "TREATMENT: ";
         String heading3 = "TOTAL COUNT: ";
         System.out.printf("%-20s %-25s %-20s %n", heading1, heading2, heading3);
         while(rs.next()) {
            String ob = rs.getString("orderedBy");
            String tn = rs.getString("treatmentName");
            String tc = rs.getString("totalCount");
            System.out.printf("%-20s %-25s %-20s %n", ob, tn,tc);
         }
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public void selectEmployeesInvolvedInTreatment() {
      String sql = "SELECT DISTINCT a.patient, b.orderedBy, b.performedBy FROM admitInstance a, treatment b WHERE a.patient = b.patient AND a.releaseDate IS NULL;";
   
      try(Connection conn = this.connect();) {
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);
         String heading1 = "PATIENT: ";
         String heading2 = "ORDERED BY: ";
         String heading3 = "PERFORMED BY: ";
         System.out.printf("%-20s %-20s %-20s %n", heading1, heading2, heading3);
         while(rs.next()) {
            String pat = rs.getString("patient");
            String ob = rs.getString("orderedBy");
            String pb = rs.getString("performedBy");
            System.out.printf("%-20s %-20s %-20s %n", pat, ob,pb);
         }
      
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

//add 0's to month/date/hour if they come in XXXX-X-X X:XX
   public String dateFormatter(String date) {
      if (date.substring(5,7).contains("-")) {
         String buffer = date.substring(5,6);
         date = date.substring(0,4) + "-0" + buffer + date.substring(6);
      }
      if (date.substring(8,10).contains(" ")) {
         String buffer = date.substring(8,9);
         date = date.substring(0,7) + "-0" + buffer + date.substring(9);
      }
      if (date.substring(10,13).contains(":")) {
         String buffer = date.substring(11,12);
         date = date.substring(0,11) + "0" + buffer + date.substring(12);
      }
      return date;
   }


   public static void personData() {
   
      Data_Importer ex = new Data_Importer();
      Scanner scan = new Scanner(System.in);
   
      System.out.println("Please enter patient file path to import data:");
      String filepath = scan.nextLine();
   
      try {
         File file = new File(filepath);
         FileReader fr = new FileReader(file);
         BufferedReader br = new BufferedReader(fr);
         String line = "";
         String[] input;
      
         String type, firstName, lastName, emergencyContact, emergencyPhone, insurancePolicyNum,
            insuranceComp, primaryDr, initialDiagnosis;
         int room;
         int patientID, admitID;
         int diagnosisID = 0;
      
      
      
         while((line = br.readLine()) != null) {
            input = line.split(",");
            for (int i = 0; i < input.length; i++) {
               input[i] = input[i].trim();
            }
            if (input[0].equals("D")) {
               type = "D";
               firstName = input[1];
               firstName = firstName.substring(0,1).toUpperCase() + firstName.substring(1).toLowerCase();
               lastName = input[2];
               lastName = lastName.substring(0,1).toUpperCase() + lastName.substring(1).toLowerCase();
            
               ex.insertPerson(lastName, firstName, type);
               ex.insertDoctor(lastName);
            }
            
            else if (input[0].equals("A") || input[0].contentEquals("N") || input[0].contentEquals("T")) {
               type = input[0];
               firstName = input[1];
               firstName = firstName.substring(0,1).toUpperCase() + firstName.substring(1).toLowerCase();
               lastName = input[2];
               lastName = lastName.substring(0,1).toUpperCase() + lastName.substring(1).toLowerCase();
            
               ex.insertPerson(lastName, firstName, type);
            }
            
            else if (input[0].equals("P")) {
               type = "P";
               firstName = input[1];
               firstName = firstName.substring(0,1).toUpperCase() + firstName.substring(1).toLowerCase();
               lastName = input[2];
               lastName = lastName.substring(0,1).toUpperCase() + lastName.substring(1).toLowerCase();
               room = Integer.parseInt(input[3]);
               emergencyContact = input[4];
               emergencyPhone = input[5];                      
               insurancePolicyNum = input[6];
               insuranceComp = input[7];
               primaryDr = input[8];
               primaryDr = primaryDr.substring(0,1).toUpperCase() + primaryDr.substring(1).toLowerCase();
               initialDiagnosis = input[9];
               initialDiagnosis = initialDiagnosis.substring(0,1).toUpperCase() + initialDiagnosis.substring(1).toLowerCase();
               String admitDate = ex.dateFormatter(input[10]);
            
            
               String releaseDate = null;
               if (input.length == 12) {
                  releaseDate = ex.dateFormatter(input[11]);
               }
               if (ex.diagnosisExistCheck(initialDiagnosis) == null) {
                  diagnosisID++;
               }
               else {
                  diagnosisID = ex.diagnosisIDupdate(initialDiagnosis);
               }
               if(ex.patientExistCheck(lastName) != null) {
                  if (ex.roomExistCheck(room) != null) {
                  
                  }
                  else {
                     admitID = ex.selectMaxAdmitID();
                     ex.insertAdmitInstance(lastName, admitDate, primaryDr, releaseDate, initialDiagnosis, room, insuranceComp, insurancePolicyNum, admitID,
                        diagnosisID);
                     ex.updatePatientAdmitID(admitID, lastName);
                     if (releaseDate == null && ex.roomExistCheck(room) == null) {
                        ex.updateRoom(admitID, lastName, room);
                     }
                  
                  }
               }
               else {		
                  patientID = ex.selectMaxPatientID();
                  admitID = ex.selectMaxAdmitID();
               
                  ex.insertPerson(lastName, firstName, type);
               
                  ex.insertPatient(firstName, lastName, emergencyContact, emergencyPhone, primaryDr,patientID);
                  ex.insertAdmitInstance(lastName, admitDate, primaryDr, releaseDate, initialDiagnosis,room,
                     insuranceComp,insurancePolicyNum, admitID, diagnosisID);
               //update patient to include admitID
                  ex.updatePatientAdmitID(admitID, lastName);
               //update room & add admitInstance
                  if (releaseDate == null) {
                     ex.updateRoom(admitID, lastName, room);
                  
                  }
               }
            
            }
         }
         System.out.println("Data Import Complete.\n");
      } catch (IOException e) {
         e.printStackTrace();
      }
   
   }

   public static void addDoctorData() {
   	
      Data_Importer ex = new Data_Importer();
      Scanner scan = new Scanner(System.in);
   
      System.out.println("Please enter additional doctor file path to import data: ");
      String filepath = scan.nextLine();
   
      try {
         File file = new File(filepath);
         FileReader fr = new FileReader(file);
         BufferedReader br = new BufferedReader(fr);
         String line = "";
         String[] input;
         String secondaryDr,patient;
      
      
         while((line = br.readLine()) != null) {
            input = line.split(",");
         //clear out spaces
            for (int i = 0; i < input.length; i++) {
               input[i] = input[i].trim();
            }
            secondaryDr = input[0];
            secondaryDr = secondaryDr.substring(0,1).toUpperCase() + secondaryDr.substring(1).toLowerCase();
            patient = input[1];
            patient = patient.substring(0,1).toUpperCase() + patient.substring(1).toLowerCase();
            if(ex.patientExistCheck(patient) != null && ex.doctorExistCheck(secondaryDr) != null) {
               ex.insertDrToPatient(secondaryDr,patient);
            }
         }
         System.out.println("Data Import Complete.\n");
      }catch (IOException e) {
         e.printStackTrace();
      }
   
   }

   public static void treatmentData() {
   
      Data_Importer ex = new Data_Importer();
      Scanner scan = new Scanner(System.in);
   
      System.out.println("Please enter treatment file path to import data: ");
      String filepath = scan.nextLine();
   
      try {
         File file = new File(filepath);
         FileReader fr = new FileReader(file);
         BufferedReader br = new BufferedReader(fr);
         String line = "";
         String[] input;
         String patient, orderedBy, treatmentType, treatmentName, treatmentDate, performedBy;
         int admitID;
      
         while((line = br.readLine()) != null) {
            input = line.split(",");
         //clear out spaces
            for (int i = 0; i < input.length; i++) {
               input[i] = input[i].trim();
            }
            performedBy = input[0];
            performedBy = performedBy.substring(0,1).toUpperCase() + performedBy.substring(1).toLowerCase();
            patient = input[1];
            patient = patient.substring(0,1).toUpperCase() + patient.substring(1).toLowerCase();
            treatmentType = input[2];
            treatmentType.toUpperCase();
            treatmentName = input[3];
            treatmentName = treatmentName.substring(0,1).toUpperCase() + treatmentName.substring(1).toLowerCase();
            treatmentDate = ex.dateFormatter(input[4]);
         
            admitID = ex.admitIDforTreatment(patient, treatmentDate);
         
            if (ex.patientExistCheck(patient) != null) {
            
               if (ex.selectPersonType(performedBy).equals("D") ) {
                  orderedBy = performedBy;
                  ex.insertTreatment(patient, orderedBy, treatmentType, treatmentName, treatmentDate, admitID, performedBy);
               
               }
            
               if (ex.selectPersonType(performedBy).equals("N") || ex.selectPersonType(performedBy).equals("T")) {
                  orderedBy = ex.selectPrimaryDr(patient);
                  ex.insertTreatment(patient, orderedBy, treatmentType, treatmentName, treatmentDate, admitID, performedBy);
               
               }
            }
         
         }
         System.out.println("Data Import Complete.\n");
      }catch (IOException e) {
         e.printStackTrace();
      }
   }

   public static void main(String[] args) throws Exception {
      String check = "";
      while(check != "0") {
      
      
         Data_Importer ex = new Data_Importer();
      
         System.out.print("Menu Options: \nP = Import Person Data File\nD = "
            + "Import Additional Doctor File\nT = Import Treatment Data File\nS = Selection Menu\n0 = EXIT"
            + "\nPlease enter letter selection: ");
      
         Scanner scan = new Scanner(System.in);
         String fileType = scan.nextLine();
         if (fileType.contentEquals("0")) {
            break;
         }
         if (fileType.toUpperCase().equals("P")) {
            Data_Importer.personData();
         }
      
         if (fileType.toUpperCase().equals("D")) {
            Data_Importer.addDoctorData();
         }
      
         if (fileType.toUpperCase().equals("T")) {
            Data_Importer.treatmentData();
         }
      
         if (fileType.toUpperCase().equals("S")) {
            boolean back = false;
            while (back == false) {
               System.out.println("Selection Menu: ");
               System.out.println("R = Room Utilization Selection Options");
               System.out.println("P = Patient Information Selection Options");
               System.out.println("D = Diagnosis and Treatment Information Selection Options");
               System.out.println("E = Employee Information Selection Options");
               System.out.println("0 = EXIT");
               System.out.print("Please enter letter selection: ");
               String userSelect = scan.nextLine();
               if (userSelect.contentEquals("0")) {
                  break;
               }
               if (userSelect.toUpperCase().contentEquals("R")) {
                  System.out.println("1 = occupied rooms, associated patients, and the patient admit date");
                  System.out.println("2 = unoccupied rooms");
                  System.out.println("3 = all rooms, patient name, and admit date");
                  System.out.println("B = back to selection menu");
                  System.out.print("Please enter number selection: ");
                  String input = scan.nextLine();
                  if (input.contentEquals("0")) {
                     break;
                  }
                  if(input.contentEquals("1")) {
                     ex.selectOccupiedRooms();
                  }
                  if(input.contentEquals("2")) {
                     ex.selectUnoccupiedRooms();
                  }
               //this one printing nulls
                  if(input.contentEquals("3")) {
                     ex.selectAllRoomsWithPatientInfo();
                  }
               }
               if (userSelect.toUpperCase().contentEquals("P")) {
                  System.out.println("1 = all patients with information");
                  System.out.println("2 = patients currently admitted");
                  System.out.println("3 = patients who were discharged in a given date range");
                  System.out.println("4 = patients who were admitted in a given date range");
                  System.out.println("5 = check patient admissions and diagnosis");
                  System.out.println("6 = check patient treatments");
                  System.out.println("7 = patients admitted within 30 days of last discharge date");
                  System.out.println("8 = get admission details");
                  System.out.println("B = back to selection menu");
                  System.out.print("Please enter number selection: ");
                  String input = scan.nextLine();
                  if (input.contentEquals("0")) {
                     break;
                  }
                  if(input.contentEquals("1")) {
                     ex.selectPatientsAndInfo();
                  }
                  if(input.contentEquals("2")) {
                     ex.selectAdmittedPatients();
                  }
                  if (input.contentEquals("3")) {
                     System.out.println("**Please note the dates must be in the format XXXX-XX-XX XX:XX to calculate correctly***");
                     System.out.print("Please enter start date: ");
                     String dateInput1 = scan.nextLine();
                     dateInput1 = ex.dateFormatter(dateInput1);
                  
                     System.out.print("Please enter end date: ");
                     String dateInput2 = scan.nextLine();
                     dateInput2 = ex.dateFormatter(dateInput2);
                  
                     ex.selectPatientDischargeDateInRange(dateInput1, dateInput2);
                  }
               
                  if (input.contentEquals("4")) {
                     System.out.println("**Please note the dates must be in the format XXXX-XX-XX XX:XX to calculate correctly***");
                     System.out.print("Please enter start date: ");
                     String dateInput1 = scan.nextLine();
                     dateInput1 = ex.dateFormatter(dateInput1);
                  
                     System.out.print("Please enter end date: ");
                     String dateInput2 = scan.nextLine();
                     dateInput2 = ex.dateFormatter(dateInput2);
                  
                     ex.selectPatientAdmitDateInRange(dateInput1, dateInput2);
                  }
                  if(input.contentEquals("5")) {
                     System.out.print("Please enter patient name: ");
                     String name = scan.nextLine();
                     name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
                     ex.selectPatientAdmissions(name);
                  }
               
                  if(input.contentEquals("6")) {
                     System.out.print("Please enter patient name: ");
                     String name = scan.nextLine();
                     name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
                     ex.selectPatientTreatments(name);
                  }
                  if(input.contentEquals("7")) {
                     ex.selectPatientsAdmitted30DaysFromDischarge();
                  }
               //not formatted correctly - need to clear null values
                  if(input.contentEquals("8")) {
                     ex.selectAdmissionDetails();
                  }
               
               
               }
               if (userSelect.toUpperCase().contentEquals("D")) {
                  System.out.println("1 = all diagnosis given");
                  System.out.println("2 = diagnosis information");
                  System.out.println("3 = all treatments performed on admitted patients");
                  System.out.println("4 = diagnosis associated with high admit paitents");
                  System.out.println("5 = patient and doctor name for a given treatment");
                  System.out.println("B = back to selection menu");
                  System.out.print("Please enter number selection: ");
               
                  String input = scan.nextLine();
                  if (input.contentEquals("0")) {
                     break;
                  }
               
                  if(input.contentEquals("1")) {
                     ex.selectDiagnosisInfo();
                  }
                  if(input.contentEquals("2")) {
                     ex.selectDiagnosisInfo();
                  }
                  if(input.contentEquals("3")) {
                     ex.selectTreatmentOnAdmittedPatients();
                  }
                  if(input.contentEquals("4")) {
                     ex.selectDiagnosisisOnHighAdmitPatients();
                  }
                  if(input.contentEquals("5")) {
                     ex.selectTreatmentPatientAndDoctor();
                  }
               }
            
               if (userSelect.toUpperCase().contentEquals("E")) {
                  System.out.println("1 = employee list");
                  System.out.println("2 = primary doctors of patients with high admission rates");
                  System.out.println("3 = all associated diagnoses for a given doctor");
                  System.out.println("4 = treatments a given doctor ordered");
                  System.out.println("5 = list of employees involved in treatments");
                  System.out.print("Please enter number selection: ");
               
                  String input = scan.nextLine();
                  if (input.contentEquals("0")) {
                     break;
                  }
               
                  if(input.contentEquals("1")) {
                     ex.selectEmployees();
                  }
                  if(input.contentEquals("2")) {
                     ex.selectPrimaryDoctorOfHighAdmitPatients();
                  }
                  if(input.contentEquals("3")) {
                     System.out.print("Please enter the name of doctor: ");
                     String name = scan.nextLine();
                     name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
                     ex.selectDiagnosesForDoctor(name);
                  }
                  if(input.contentEquals("4")) {
                     System.out.print("Please enter the name of doctor: ");
                     String name = scan.nextLine();
                     name.toLowerCase();
                     name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
                     ex.selectTreatmentsOrderedByDoctor(name);
                  }
                  if(input.contentEquals("5")) {
                     ex.selectEmployeesInvolvedInTreatment();
                  }
               }
            
            
            
            }	
         
         
         
         
         
         
         }
      }
   }
}


