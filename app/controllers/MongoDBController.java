package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import models.MongoDBToken;
import models.MongoDBUser;
import models.Token;
import models.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Date;
import java.util.UUID;

/**
 * MongoDB controller
 *
 * @author vdubois
 */
public class MongoDBController extends Controller {

    // POST /mongodb/users/signup
    public static Result signup() {
        Result result = badRequest();
        try {
            User user = Json.fromJson(request().body().asJson(), MongoDBUser.class);
            user.save();
            result = ok();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

    // POST /mongodb/users/login
    public static Result login() {
        Result result = badRequest();
        JsonNode bodyAsJSON = request().body().asJson();
        JsonNode login = bodyAsJSON.get("login");
        JsonNode password = bodyAsJSON.get("password");
        if (login != null && password != null) {
            User user = MongoDBUser.findByUserId(login.asText());
            if (user == null) {
                result = notFound();
            } else if (password.asText().equals(user.password)) {
                Token token = new MongoDBToken();
                token.token = UUID.randomUUID().toString();
                token.userId = user.userId;
                token.created = new Date();
                token.save();
                result = ok(Json.toJson(ImmutableMap.of("token", token.token)));
            }
        }
        return result;
    }

    // PUT /mongodb/users/password
    public static Result changePassword() {
        Result result = badRequest();
        JsonNode bodyAsJSON = request().body().asJson();
        JsonNode token = bodyAsJSON.get("token");
        JsonNode oldPassword = bodyAsJSON.get("oldPassword");
        JsonNode newPassword = bodyAsJSON.get("newPassword");
        if (token != null && oldPassword != null && newPassword != null) {
            Token foundToken = MongoDBToken.findByToken(token.asText());
            if (foundToken != null) {
                User user = MongoDBUser.findByUserId(foundToken.userId);
                if (user != null && oldPassword.asText().equals(user.password)) {
                    user.password = newPassword.asText();
                    user.save();
                    result = ok();
                }
            }
        }
        return result;
    }

    // PUT /users
    public static Result update() {
        Result result = badRequest();
        JsonNode bodyAsJSON = request().body().asJson();
        JsonNode token = bodyAsJSON.get("token");
        JsonNode user = bodyAsJSON.get("user");
        if (token != null && user != null) {
            Token foundToken = MongoDBToken.findByToken(token.asText());
            if (foundToken != null && user.get("userId") != null) {
                User userToUpdate = MongoDBUser.findByUserId(foundToken.userId);
                userToUpdate.email = user.get("email").asText();
                userToUpdate.fullName = user.get("fullName").asText();
                userToUpdate.save();
                result = ok();
            }
        }
        return result;
    }

    // DELETE /mongodb/users/${token}
    // @Cached(key = "mongodbDeletion")
    public static Result delete(String token) {
        Result result = badRequest();
        if (token != null) {
            Token foundToken = MongoDBToken.findByToken(token);
            if (foundToken != null) {
                User foundUser = MongoDBUser.findByUserId(foundToken.userId);
                if (foundUser != null) {
                    foundToken.remove();
                    foundUser.remove();
                    result = ok();
                }
            }
        }
        return result;
    }
}
