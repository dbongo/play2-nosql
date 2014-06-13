package models;

import org.junit.Test;
import play.libs.F;
import play.test.TestBrowser;

import static org.fest.assertions.Assertions.*;
import static play.test.Helpers.*;
import static play.test.Helpers.HTMLUNIT;

public class RedisUserTest {

    @Test
    public void testSave() throws Exception {
        running(testServer(4444, fakeApplication(inMemoryDatabase())), HTMLUNIT, new F.Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                // GIVEN
                RedisUser user = new RedisUser();
                user.email = "toto@test.com";
                user.fullName = "Toto";
                user.password = "Password";
                user.userId = "toto";

                // WHEN
                user.save();

                // THEN
                User toto = RedisUser.findByUserId("toto");
                assertThat(toto).isNotNull();
                assertThat(toto.email).isEqualTo("toto@test.com");
                user.remove();
            }
        });
    }

    @Test
    public void testRemove() throws Exception {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new F.Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                // GIVEN
                RedisUser user = new RedisUser();
                user.email = "toto@test.com";
                user.fullName = "Toto";
                user.password = "Password";
                user.userId = "toto";
                user.save();

                // WHEN
                user.remove();

                // THEN
                assertThat(RedisUser.findByUserId("toto")).isNull();
            }
        });
    }

    @Test
    public void testFindByUserId() throws Exception {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new F.Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                // GIVEN
                RedisUser user = new RedisUser();
                user.email = "toto@test.com";
                user.fullName = "Toto";
                user.password = "Password";
                user.userId = "toto";
                user.save();

                // WHEN
                User foundUser = RedisUser.findByUserId("toto");

                // THEN
                assertThat(foundUser).isNotNull();
                assertThat(foundUser.email).isEqualTo(user.email);
                user.remove();
            }
        });
    }

    @Test
    public void testExists() throws Exception {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new F.Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                // GIVEN
                RedisUser user = new RedisUser();
                user.email = "toto@test.com";
                user.fullName = "Toto";
                user.password = "Password";
                user.userId = "toto";
                user.save();

                // WHEN
                boolean exists = RedisUser.exists(user.userId);

                // THEN
                user.remove();
                assertThat(exists).isTrue();
            }
        });
    }
}