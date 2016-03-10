package cc.caucas.vertx.verticle.dao.jdbc;

import cc.caucas.vertx.verticle.dao.UserDao;
import cc.caucas.vertx.verticle.entity.User;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.vertx.core.json.Json;
import io.vertx.rxjava.ext.jdbc.JDBCClient;
import rx.Observable;
import rx.subjects.PublishSubject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

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
                .subscribe(connection -> connection.callObservable("select * from users")
                                .subscribe(result -> subject.onNext(result.getRows().toString()),
                                        subject::onError,
                                        subject::onCompleted),
                        subject::onError);
        return subject;
    }

    @Override
    public Observable<List<User>> all() {
        return this.allRaw().map(this::listFromJson);
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
