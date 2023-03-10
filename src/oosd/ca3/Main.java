package oosd.ca3;

import oosd.ca3.menus.Login;
import oosd.ca3.util.SQL;

import java.util.logging.Logger;

/*
*
* Created by Jack Foley
* Student ID: C00274246
* Object-Oriented Software Development
* Continuous Assessment 3
*
*/

public class Main {

    public static final Logger logger = Logger.getLogger("CA3");
    public static SQL sql;
    public static int userId; // Used to store the user ID of the currently logged-in user.

    public static void main(String[] args) {
        sql = new SQL(); // Used to create a database connection. The connection is done inside the SQL class constructor.
        new Login(); // Launches the login screen.
    }
}