package GUIs;

import db_objs.User;

import javax.swing.*;

public abstract class BaseFrame extends JFrame {
    //store user information
    protected User user;

    public BaseFrame(String title){
        initialize(title);
    }

    public BaseFrame(String title, User user){
        //initialize user
        this.user = user;
        initialize(title);
    }

    private void initialize(String title){
        //instantiate JFrame properties and add a title to the bar
        setTitle(title);

        //set size
        setSize(420, 600);

        //terminate the program when the GUI is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //set layout
        setLayout(null);

        //prevent GUI from being resized
        setResizable(false);

        //launch the GUI at the center of the screen
        setLocationRelativeTo(null);

        //call subclass' addGUIComponents()
        addGUIComponents();

    }

    //defined by subclass
    protected abstract void addGUIComponents();
}
