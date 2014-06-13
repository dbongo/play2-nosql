package models;

import org.junit.Test;
import play.libs.F;
import play.test.TestBrowser;

import java.util.Date;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

public class RedisTokenTest {


    @Test
    public void testSave() throws Exception {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new F.Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                // GIVEN
                RedisToken token = new RedisToken();
                token.token = "0000";
                token.userId = "toto";
                token.created = new Date();

                // WHEN
                token.save();

                // THEN
                assertThat(RedisToken.findByToken("0000")).isNotNull();
                token.remove();
            }
        });
    }

    @Test
    public void testRemove() throws Exception {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new F.Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                // GIVEN
                RedisToken token = new RedisToken();
                token.token = "0000";
                token.userId = "toto";
                token.created = new Date();
                token.save();

                // WHEN
                token.remove();

                // THEN
                assertThat(RedisToken.findByToken("0000")).isNull();
            }
        });
    }

    @Test
    public void testFindByToken() throws Exception {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new F.Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                // GIVEN
                RedisToken token = new RedisToken();
                token.token = "0000";
                token.userId = "toto";
                token.created = new Date();
                token.save();

                // WHEN
                Token foundToken = RedisToken.findByToken("0000");

                // THEN
                assertThat(foundToken).isNotNull();
                token.remove();
            }
        });
    }
}