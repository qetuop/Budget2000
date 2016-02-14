/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget2000;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

        
/**
 *
 * @author brian
 */
public class MainApp extends Application {

    private BudgetData budgetData = new BudgetData();
    Stage mPrimaryStage;
    MainAppViewController mvc;

    @Override
    public void start(Stage primaryStage) throws Exception {

        //hardcodedSetup();
        Parent root;
        Scene scene;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("MainAppView.fxml"));
        root = loader.load();

        // enable all children to get this class (and thus the userData)
        mvc = loader.getController();
        mvc.setBudgetData(budgetData);
        mvc.setMainApp(this);

        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Budget 2000");
        mPrimaryStage = primaryStage;
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void hardcodedSetup() {
        System.out.println("Budget::hardcodedSetup()");
        System.out.println("--------USER---------");

        UserDbAdapter mUserDbAdapter = new UserDbAdapter();
        mUserDbAdapter.createConnection();
        mUserDbAdapter.createDatabase();

        User user = new User("Bob", "Smith");
        int userId = mUserDbAdapter.createUser(user);
        user.setId(userId);
        System.out.println("userId = " + userId);

        user.setFirstName("Sarah2");
        mUserDbAdapter.update(user);

        User user2 = new User("Joe", "Mama");
        int userId2 = mUserDbAdapter.createUser(user2);
        System.out.println("userId2 = " + userId2);

        //mUserDbAdapter.delete(userId2);
        ArrayList<User> users = mUserDbAdapter.getUsers();
        for (User u : users) {
            System.out.println("First Name: " + u.getFirstName());
            System.out.println("Last Name:  " + u.getLastName());
        }

        System.out.println("--------USER---------");
    }

    public Boolean load() {
        Boolean rv = false;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
//        fileChooser.setInitialDirectory( new File(System.getProperty("user.home")) ); 
        fileChooser.setInitialDirectory(new File("."));

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Budget Save File", "*.db"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        File file = fileChooser.showOpenDialog(mPrimaryStage);
        if (file != null) {
            System.out.println("file " + file.getName());
            

            // backup current DB
            File currentDB = new File(AbstractDbAdapter.DATABASE_NAME);
            if (currentDB.exists()) {
                currentDB.renameTo(new File(AbstractDbAdapter.DATABASE_NAME + ".bak"));
            }
            
            currentDB = new File(AbstractDbAdapter.DATABASE_NAME);
            
            try {
                // copy new DB to correct name
                Files.copy(file.toPath(), currentDB.toPath(), REPLACE_EXISTING);
            } catch (IOException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            AbstractDbAdapter dbAdapter = new AbstractDbAdapter();
            dbAdapter.close();
            dbAdapter.createConnection();

            mvc.setBudgetData(new BudgetData());
        }
        return rv;
    }

    public void save() {
        System.out.println("SAVING");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
//        fileChooser.setInitialDirectory( new File(System.getProperty("user.home")) ); 
        fileChooser.setInitialDirectory(new File("."));

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("sqlite File", "*.db"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        File file = fileChooser.showSaveDialog(mPrimaryStage);
        
//        if (file != null) {
//            System.out.println("w?hat do i do: " +file.getName());
//            File currentDB = new File(AbstractDbAdapter.DATABASE_NAME);
//            
//            try {
//                // copy new DB to correct name
//                Files.copy(file.toPath(), currentDB.toPath(), REPLACE_EXISTING);
//            } catch (IOException ex) {
//                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }

        //String fileName = "test.ser";
    }
//
//    private void saveFile(File file) {
//        FileOutputStream fos;
//
//        try {
//            fos = new FileOutputStream(file);
//            BufferedOutputStream bos = new BufferedOutputStream(fos);
//            ObjectOutputStream oos = new ObjectOutputStream(bos);
//            oos.writeObject(budgetData);
//            oos.close();
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(MainAppViewController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(MainAppViewController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

} // MainApp
