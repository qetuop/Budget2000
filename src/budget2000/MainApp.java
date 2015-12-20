/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget2000;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        hardcodedSetup();

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

        user.setFirstName("Sarah");
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

} // MainApp
