package pl.otomczak.empik.githubreporter.core.ports;

import pl.otomczak.empik.githubreporter.core.model.User;

/**
 * Port to GitHub REST API.
 */
public interface GithubFacade {

    User getUser(String login);
}
