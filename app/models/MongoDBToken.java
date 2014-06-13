package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.time.DateUtils;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.util.Date;

/**
 * Token model for MongoDB
 *
 * @author vdubois
 */
public class MongoDBToken extends Token {

    @JsonProperty("_id")
    public ObjectId id;

    public static MongoCollection tokens() {
        return PlayJongo.getCollection("tokens");
    }

    public void save() {
        tokens().save(this);
    }

    public void remove() {
        tokens().remove(this.id);
    }

    public static MongoDBToken findByToken(String token) {
        Date oneDayFromNowInPast = DateUtils.addDays(new Date(), -1);
        return tokens().findOne("{token: #, created: {$gte : #}}", token, oneDayFromNowInPast).as(MongoDBToken.class);
    }
}
