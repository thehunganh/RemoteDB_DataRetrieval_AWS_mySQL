package sample;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.UUID;

public class Controller implements Initializable
{
    @FXML
    JFXButton over21;
    @FXML
    JFXButton cisMajor;
    @FXML
    JFXButton gpa3Plus;
    @FXML
    JFXButton createBtn;
    @FXML
    JFXButton dropBtn;
    @FXML
    JFXButton displayBtn;
    @FXML
    JFXListView studentListView;
    @FXML
    Label msgLabel;

    final String AWS_URL = "jdbc:mysql://hap.cstctc64fqwk.us-east-1.rds.amazonaws.com:3306/hw2?user=admin&password=11223344";


    private void createTable(String url)
    {
        try
        {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            try
            {
                stmt.execute("CREATE TABLE Student ("+
                        "StuID VARCHAR(50)," +
                        "StuName CHAR(50)," +
                        "StuAge INT," +
                        "StuMajor CHAR(50)," +
                        "StuGPA FLOAT(10,2));");
            }
            catch (Exception ex)
            {
                msgLabel.setText("TABLE ALREADY EXISTS, NOT CREATED");
            }

            String[] nameArr = {"Hebe Cochran", "Jake Woodward", "Mahima Combs", "Vicky Gonzalez", "Shelby Villegas", "Emir Humphries", "Niamh Franklin", "Safiya Morris", "Lori Lowery", "Clara Keenan"};
            String[] majorArr = {"CIS", "CS", "MIS", "Biotech", "Biology", "CIS", "CS", "MIS", "Biotech", "Biology"};
            double[] gpaArr = {2.7, 2.8, 2.9, 3.0, 3.1, 3.2, 3.3, 3.4, 3.5, 3.6};

            for(int i=0; i<10; i++){

                UUID id = UUID.randomUUID();
                Student stuNew = new Student();
                stuNew.stuID = id;
                stuNew.stuName = nameArr[i];
                stuNew.stuAge = 19 +i;
                stuNew.stuMajor = majorArr[i];
                stuNew.stuGPA = gpaArr[i];

                String sql = "INSERT INTO Student VALUES" +
                        "('"+stuNew.stuID+"', '"+stuNew.stuName+"', '"+stuNew.stuAge+"', '"+stuNew.stuMajor+"', '"+stuNew.stuGPA+"');";
                stmt.executeUpdate(sql);
            }

            msgLabel.setText("TABLE CREATED & FILLED");

            stmt.close();
            conn.close();

        }
        catch (Exception ex)
        {
            String msg = ex.getMessage();
            msgLabel.setText("TABLE NOT CREATED");
            System.out.println(msg);
        }
    }

    private void deleteTable(String url)
    {
        try
        {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute("DROP TABLE Student");
            studentListView.getItems().clear();
            stmt.close();
            conn.close();
            msgLabel.setText("TABLE DROPPED");
        }
        catch (Exception ex)
        {
            String msg = ex.getMessage();
            msgLabel.setText("TABLE NOT DROPPED");
            System.out.println(msg);
        }
    }

    private void displayTable(String url)
    {
        try
        {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            String sqlStatement = "SELECT StuID, StuName, StuAge, StuMajor, StuGPA FROM Student";
            queryResult(stmt, sqlStatement);
            msgLabel.setText("TABLE DISPLAYED");

            stmt.close();
            conn.close();
        }
        catch (Exception ex)
        {
            String msg = ex.getMessage();
            msgLabel.setText("TABLE NOT DISPLAYED");
            System.out.println(msg);
        }
    }

    private void filter1(String url)
    {
        try
        {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            String cisQuery = "SELECT * FROM Student WHERE StuMajor = 'CIS';";
            queryResult(stmt, cisQuery);
            msgLabel.setText("CIS STUDENTS FILTERED");
            stmt.close();
            conn.close();
        }
        catch (Exception ex)
        {
            String msg = ex.getMessage();
            msgLabel.setText("NOT FILTERED");
            System.out.println(msg);
        }
    }

    private void filter2(String url)
    {
        try
        {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            String ageQuery = "SELECT * FROM Student WHERE StuAge > 21;";
            queryResult(stmt, ageQuery);
            msgLabel.setText("21+ YEARS OLD STUDENTS FILTERED");

            stmt.close();
            conn.close();
        }
        catch (Exception ex)
        {
            String msg = ex.getMessage();
            msgLabel.setText("NOT FILTERED");
            System.out.println(msg);
        }
    }

    private void filter3(String url)
    {
        try
        {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            String gpaQuery = "SELECT * FROM Student WHERE StuGPA > 3.0;";
            queryResult(stmt, gpaQuery);
            msgLabel.setText("3.0+ GPA STUDENTS FILTERED");

            stmt.close();
            conn.close();
        }
        catch (Exception ex)
        {
            String msg = ex.getMessage();
            msgLabel.setText("NOT FILTERED");
            System.out.println(msg);
        }
    }

    public void queryResult(Statement stmt, String sqlStatement) throws SQLException {
        ResultSet result = stmt.executeQuery(sqlStatement);
        ObservableList<Student> studentDBList = FXCollections.observableArrayList();
        while (result.next()) {
            Student student = new Student();
            student.stuName = result.getString("StuName");
            student.stuID = UUID.fromString(result.getString("StuID"));
            student.stuAge = result.getInt("StuAge");
            student.stuMajor = result.getString("StuMajor");
            student.stuGPA = result.getDouble("StuGPA");
            studentDBList.add(student);
        }
        studentListView.setItems(studentDBList);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        createBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) {
                createTable(AWS_URL);
            }
        });

        dropBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) {
                deleteTable(AWS_URL);
            }
        });

        displayBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) {
                displayTable(AWS_URL);
            }
        });


        cisMajor.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) {
                filter1(AWS_URL);
            }
        });

        over21.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) {
                filter2(AWS_URL);
            }
        });

        gpa3Plus.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) {
                filter3(AWS_URL);
            }
        });
    }
}
