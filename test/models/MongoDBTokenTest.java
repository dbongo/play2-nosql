package models;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import play.libs.F;
import play.test.TestBrowser;

import java.util.Date;

import static org.fest.assertions.Assertions.*;
import static play.test.Helpers.*;
import static play.test.Helpers.HTMLUNIT;

public class MongoDBTokenTest {

    @Test
    public void testSave() throws Exception {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new F.Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                // GIVEN
                MongoDBToken token = new MongoDBToken();
                token.token = "0000";
                token.userId = "toto";
                token.created = new Date();

                // WHEN
                token.save();

                // THEN
                assertThat(MongoDBToken.findByToken("0000")).isNotNull();
                token.remove();
            }
        });
    }

    @Test
    public void testRemove() throws Exception {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new F.Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                // GIVEN
                MongoDBToken token = new MongoDBToken();
                token.token = "0000";
                token.userId = "toto";
                token.created = new Date();
                token.save();

                // WHEN
                token.remove();

                // THEN
                assertThat(MongoDBToken.findByToken("0000")).isNull();
            }
        });
    }

    @Test
    public void testFindByToken() throws Exception {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new F.Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                // GIVEN
                MongoDBToken token = new MongoDBToken();
                token.token = "0000";
                token.userId = "toto";
                token.created = new Date();
                token.save();

                // WHEN
                MongoDBToken foundToken = MongoDBToken.findByToken("0000");

                // THEN
                assertThat(foundToken).isNotNull();
                token.remove();

                // GIVEN
                token.created = DateUtils.addDays(new Date(), -2);
                token.save();

                // WHEN
                foundToken = MongoDBToken.findByToken("0000");

                // THEN
                token.remove();
                assertThat(foundToken).isNull();
            }
        });
    }
}