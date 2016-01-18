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
        MainAppViewController mvc = loader.getController();
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
            //if ( (rv = loadFile(file)) == true )
            //    mvc.setBudgetData(budgetData);
        }
        return rv;
    }
//
//    private Boolean loadFile ( File file ){
//        FileInputStream fis;
//        
//        try {
//            fis = new FileInputStream(file);  
//            ObjectInputStream in = new ObjectInputStream(fis);
//            budgetData = (BudgetData) in.readObject();
//            budgetData.update();
//            in.close();
//        } catch (IOException i) {
//            i.printStackTrace();
//            return false;
//        } catch (ClassNotFoundException c) {
//            System.out.println("User class not found");
//            c.printStackTrace();
//            return false;
//        }
//
//        return true;
//    }
//    
    public void save() {
        System.out.println("SAVING");
//
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Save File");
////        fileChooser.setInitialDirectory( new File(System.getProperty("user.home")) ); 
//        fileChooser.setInitialDirectory(new File("C:\\Users\\Brian\\Documents\\NetBeansProjects\\Budget"));
//
//        fileChooser.getExtensionFilters().addAll(
//                new FileChooser.ExtensionFilter("Budget Save File", "*.bud"),
//                new FileChooser.ExtensionFilter("All Files", "*.*"));
//
//        File file = fileChooser.showSaveDialog(primaryStage);
//        if (file != null) {
//            saveFile(file);
//        }
//
//        //String fileName = "test.ser";
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
