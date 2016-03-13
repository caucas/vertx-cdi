package cc.caucas.vertx.verticle.entity;

/**
 * @author Georgy Davityan.
 */
public class User {

    public static final String FIND_ALL = "select * from users";
    public static final String FIND_BY_ID = "select * from users where login='%s'";

    private String login;
    private String name;
    private String surname;
    private Integer age;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
