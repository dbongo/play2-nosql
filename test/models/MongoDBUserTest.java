package models;

import org.junit.Test;
import play.libs.F;
import play.test.TestBrowser;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

public class MongoDBUserTest {

    @Test
    public void testSave() throws Exception {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new F.Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                // GIVEN
                MongoDBUser user = new MongoDBUser();
                user.email = "toto@test.com";
                user.fullName = "Toto";
                user.password = "Password";
                user.userId = "toto";

                // WHEN
                user.save();

                // THEN
                assertThat(MongoDBUser.findByUserId("toto")).isNotNull();
                assertThat(MongoDBUser.findByUserId("toto").email).isEqualTo("toto@test.com");
                user.remove();
            }
        });
    }

    @Test
    public void testRemove() throws Exception {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new F.Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                // GIVEN
                MongoDBUser user = new MongoDBUser();
                user.email = "toto@test.com";
                user.fullName = "Toto";
                user.password = "Password";
                user.userId = "toto";
                user.save();

                // WHEN
                user.remove();

                // THEN
                assertThat(MongoDBUser.findByUserId("toto")).isNull();
            }
        });
    }

    @Test
    public void testFindByUserId() throws Exception {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new F.Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                // GIVEN
                MongoDBUser user = new MongoDBUser();
                user.email = "toto@test.com";
                user.fullName = "Toto";
                user.password = "Password";
                user.userId = "toto";
                user.save();

                // WHEN
                User foundUser = MongoDBUser.findByUserId("toto");

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
                MongoDBUser user = new MongoDBUser();
                user.email = "toto@test.com";
                user.fullName = "Toto";
                user.password = "Password";
                user.userId = "toto";
                user.save();

                // WHEN
                boolean exists = MongoDBUser.exists(user.userId);

                // THEN
                user.remove();
                assertThat(exists).isTrue();
            }
        });
    }
}