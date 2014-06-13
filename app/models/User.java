package models;

/**
 * User model
 *
 * @author SQLI
 */
public abstract class User {

    /** user id */
    public String userId;

    /** user email */
    public String email;

    /** user name */
    public String fullName;

    /** user password */
    public String password;


    public abstract void save();

    public abstract void remove();
}
