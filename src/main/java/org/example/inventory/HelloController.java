package org.example.inventory;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableRow;

public class HelloController implements Initializable{
    @FXML
    private Label label;
    @FXML
    private TextField productNamme;
    @FXML
    private TextField productNumber;
    @FXML
    private TextField courseStock;
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, String> IDColmn;
    @FXML
    private TableColumn<Product, String> NameColmn;
    @FXML
    private TableColumn<Product, String> MobileColmn;
    @FXML
    private TableColumn<Product, String> CourseColmn;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    void AddProduct(ActionEvent event) {

        String stname,mobile,course;
        stname = productNamme.getText();
        mobile = productNumber.getText();
        course = courseStock.getText();
        try
        {
            pst = con.prepareStatement("insert into registation(name,mobile,course)values(?,?,?)");
            pst.setString(1, stname);
            pst.setString(2, mobile);
            pst.setString(3, course);
            pst.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Product Registation");

            alert.setHeaderText("Product Registation");
            alert.setContentText("Record Addedddd!");
            alert.showAndWait();
            productTable();

            productNamme.setText("");
            productNumber.setText("");
            courseStock.setText("");
            productNamme.requestFocus();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void productTable()
    {
        Connect();
        ObservableList<Product> products = FXCollections.observableArrayList();
        try
        {
            pst = con.prepareStatement("select id,name,mobile,course from registation");
            ResultSet rs = pst.executeQuery();
            {
                while (rs.next())
                {
                    Product st = new Product();
                    st.setId(rs.getString("id"));
                    st.setName(rs.getString("name"));
                    st.setMobile(rs.getString("mobile"));
                    st.setCourse(rs.getString("course"));
                    products.add(st);
                }
            }
            productTable.setItems(products);
            IDColmn.setCellValueFactory(f -> f.getValue().idProperty());
            NameColmn.setCellValueFactory(f -> f.getValue().nameProperty());
            MobileColmn.setCellValueFactory(f -> f.getValue().mobileProperty());
            CourseColmn.setCellValueFactory(f -> f.getValue().courseProperty());


        }

        catch (SQLException ex)
        {
            Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, ex);
        }
        productTable.setRowFactory( tv -> {
            TableRow<Product> myRow = new TableRow<>();
            myRow.setOnMouseClicked (event ->
            {
                if (event.getClickCount() == 1 && (!myRow.isEmpty()))
                {
                    myIndex =  productTable.getSelectionModel().getSelectedIndex();

                    id = Integer.parseInt(String.valueOf(productTable.getItems().get(myIndex).getId()));
                    productNamme.setText(productTable.getItems().get(myIndex).getName());
                    productNumber.setText(productTable.getItems().get(myIndex).getMobile());
                    courseStock.setText(productTable.getItems().get(myIndex).getCourse());



                }
            });
            return myRow;
        });


    }
    @FXML
    void Delete(ActionEvent event) {
        myIndex = productTable.getSelectionModel().getSelectedIndex();

        id = Integer.parseInt(String.valueOf(productTable.getItems().get(myIndex).getId()));

        try
        {
            pst = con.prepareStatement("delete from registation where id = ? ");
            pst.setInt(1, id);
            pst.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Student Registationn");

            alert.setHeaderText("Student Registation");
            alert.setContentText("Deletedd!");
            alert.showAndWait();
            productTable();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    void Update(ActionEvent event) {

        String stname,mobile,course;

        myIndex = productTable.getSelectionModel().getSelectedIndex();

        id = Integer.parseInt(String.valueOf(productTable.getItems().get(myIndex).getId()));

        stname = productNamme.getText();
        mobile = productNumber.getText();
        course = courseStock.getText();
        try
        {
            pst = con.prepareStatement("update registation set name = ?,mobile = ? ,course = ? where id = ? ");
            pst.setString(1, stname);
            pst.setString(2, mobile);
            pst.setString(3, course);
            pst.setInt(4, id);
            pst.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Student Registationn");

            alert.setHeaderText("Student Registation");
            alert.setContentText("Updateddd!");
            alert.showAndWait();
            productTable();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    Connection con;
    PreparedStatement pst;
    int myIndex;
    int id;



    public void Connect()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/inventor","root","");
        } catch (ClassNotFoundException ex) {

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connect();
        productTable();
    }

}