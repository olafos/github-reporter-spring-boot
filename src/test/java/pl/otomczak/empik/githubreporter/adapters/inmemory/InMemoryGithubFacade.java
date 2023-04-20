package pl.otomczak.empik.githubreporter.adapters.inmemory;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import pl.otomczak.empik.githubreporter.core.model.User;
import pl.otomczak.empik.githubreporter.core.ports.GithubFacade;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryGithubFacade implements GithubFacade {

    public final static String LOGIN_FOR_RATE_LIMIT_ERROR = "zach";

    private final ConcurrentMap<String, User> loginToUser = new ConcurrentHashMap<>();

    public void register(User user) {
        this.loginToUser.putIfAbsent(user.getLogin(), user);
    }

    @Override
    public User getUser(String login) {
        return getUserByLogin(login);
    }

    private User getUserByLogin(String login) {
        if (LOGIN_FOR_RATE_LIMIT_ERROR.equalsIgnoreCase(login))
            throw new HttpClientErrorException(HttpStatusCode.valueOf(403));
        final User user = this.loginToUser.get(login);
        if (user == null)
            throw new HttpClientErrorException(HttpStatusCode.valueOf(404));
        return user;
    }
}
