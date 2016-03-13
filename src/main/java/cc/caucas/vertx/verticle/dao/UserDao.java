package cc.caucas.vertx.verticle.dao;

import cc.caucas.vertx.verticle.entity.User;
import io.vertx.core.json.JsonObject;
import rx.Observable;

import java.util.List;

/**
 * @author Georgy Davityan.
 */
public interface UserDao {

    Observable<String> allRaw();
    Observable<List<User>> all();
    Observable<String> getRaw(String login);
    Observable<User> get(String login);

}
