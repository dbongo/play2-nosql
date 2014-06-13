package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import uk.co.panaxiom.playjongo.PlayJongo;

/**
 * User model for MongoDB
 *
 * @author SQLI
 */
public class MongoDBUser extends User {

    @JsonProperty("_id")
    public ObjectId id;

    public static MongoCollection users() {
        return PlayJongo.getCollection("users");
    }

    public void save() {
        if (findByUserId(userId) == null) {
            users().save(this);
        } else {
            users().update(String.format("{userId: '%s'}", userId)).with(this);
        }
    }

    public void remove() {
        users().remove(this.id);
    }

    public static User findByUserId(String userId) {
        return users().findOne("{userId: #}", userId).as(MongoDBUser.class);
    }

    public static boolean exists(String userId) {
        return users().count("{userId: #}", userId) > 0;
    }
}
