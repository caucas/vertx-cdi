package cc.caucas.vertx.verticle.dao.jdbc;

import cc.caucas.vertx.verticle.dao.UserDao;
import cc.caucas.vertx.verticle.entity.User;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.vertx.core.json.Json;
import io.vertx.ext.sql.ResultSet;
import io.vertx.rxjava.ext.jdbc.JDBCClient;
import io.vertx.rxjava.ext.sql.SQLConnection;
import rx.Observable;
import rx.subjects.PublishSubject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

import static cc.caucas.vertx.verticle.entity.User.FIND_ALL;
import static cc.caucas.vertx.verticle.entity.User.FIND_BY_ID;

/**
 * @author Georgy Davityan.
 */
@ApplicationScoped
public class UserDaoJdbc implements UserDao {

    @Inject
    private JDBCClient jdbc;

    @Override
    public Observable<String> allRaw() {
        PublishSubject<String> subject = PublishSubject.create();
        jdbc.getConnectionObservable()
                .flatMap(connection -> executeQuery(connection, FIND_ALL))
                .flatMap(result -> Observable.just(result.getRows().toString()))
                .subscribe(subject);
        return subject;
    }

    @Override
    public Observable<List<User>> all() {
        return this.allRaw().map(this::listFromJson);
    }

    @Override
    public Observable<String> getRaw(String login) {
        PublishSubject<String> subject = PublishSubject.create();
        jdbc.getConnectionObservable()
                .flatMap(connection -> executeQuery(connection, String.format(FIND_BY_ID, login)))
                .flatMap(result -> result.getRows().isEmpty() ? Observable.empty() :
                        Observable.just(result.getRows().iterator().next().toString()))
                .subscribe(subject);
        return subject;
    }

    @Override
    public Observable<User> get(String login) {
        return this.getRaw(login).map(this::fromJson);
    }

    private Observable<ResultSet> executeQuery(SQLConnection connection, String query) {
        return Observable.using(
                () -> connection,
                resource -> resource.callObservable(query),
                resource -> resource.close());
    }

    private User fromJson(String json) {
        return Json.decodeValue(json, User.class);
    }

    private List<User> listFromJson(String json) {
        TypeFactory factory = Json.mapper.getTypeFactory();
        List<User> users;
        try {
            users = Json.mapper.readValue(json, factory.constructType(List.class, User.class));
        } catch (IOException e) {
            throw new RuntimeException("Can't deserialize users.", e);
        }
        return users;
    }

}
