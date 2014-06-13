package models;

import java.util.Date;

/**
 * Token model
 *
 * @author vdubois
 */
public abstract class Token {

    /** token */
    public String token;

    /** user id */
    public String userId;

    /** created date */
    public Date created;

    public abstract void save();

    public abstract void remove();
}
