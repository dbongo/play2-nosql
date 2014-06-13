package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import models.RedisToken;
import models.RedisUser;
import models.Token;
import models.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Date;
import java.util.UUID;

/**
 * Redis controller
 *
 * @author SQLI
 */
public class RedisController extends Controller {

    // POST /redis/users/signup
    public static Result signup() {
        Result result = badRequest();
        try {
            User user = Json.fromJson(request().body().asJson(), RedisUser.class);
            user.save();
            result = ok();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

    // POST /redis/users/login
    public static Result login() {
        Result result = badRequest();
        JsonNode bodyAsJSON = request().body().asJson();
        JsonNode login = bodyAsJSON.get("login");
        JsonNode password = bodyAsJSON.get("password");
        if (login != null && password != null) {
            User user = RedisUser.findByUserId(login.asText());
            if (user == null) {
                result = notFound();
            } else if (password.asText().equals(user.password)) {
                Token token = new RedisToken();
                token.token = UUID.randomUUID().toString();
                token.userId = user.userId;
                token.created = new Date();
                token.save();
                result = ok(Json.toJson(ImmutableMap.of("token", token.token)));
            }
        }
        return result;
    }

    // PUT /redis/users/password
    public static Result changePassword() {
        Result result = badRequest();
        JsonNode bodyAsJSON = request().body().asJson();
        JsonNode token = bodyAsJSON.get("token");
        JsonNode oldPassword = bodyAsJSON.get("oldPassword");
        JsonNode newPassword = bodyAsJSON.get("newPassword");
        if (token != null && oldPassword != null && newPassword != null) {
            Token foundToken = RedisToken.findByToken(token.asText());
            if (foundToken != null) {
                User user = RedisUser.findByUserId(foundToken.userId);
                if (user != null && oldPassword.asText().equals(user.password)) {
                    user.password = newPassword.asText();
                    user.save();
                    result = ok();
                }
            }
        }
        return result;
    }

    // PUT /redis/users
    public static Result update() {
        Result result = badRequest();
        JsonNode bodyAsJSON = request().body().asJson();
        JsonNode token = bodyAsJSON.get("token");
        JsonNode user = bodyAsJSON.get("user");
        if (token != null && user != null) {
            Token foundToken = RedisToken.findByToken(token.asText());
            if (foundToken != null && user.get("userId") != null) {
                // FIXME ce n'est pas un update mais un save
                User userToUpdate = new RedisUser();
                userToUpdate.email = user.get("email").asText();
                userToUpdate.fullName = user.get("fullName").asText();
                userToUpdate.password = user.get("password").asText();
                userToUpdate.save();
                result = ok();
            }
        }
        return result;
    }

    // DELETE /redis/users/${token}
    // @Cached(key = "redisDeletion")
    public static Result delete(String token) {
        Result result = badRequest();
        if (token != null) {
            Token foundToken = RedisToken.findByToken(token);
            if (foundToken != null) {
                User foundUser = RedisUser.findByUserId(foundToken.userId);
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
