package burntroll.com.service;

import burntroll.com.model.Client;

/**
 * Interface that defines the <b>Strategy</b> pattern in the client domain.
 * Thus, if necessary, we can have multiple implementations of this same interface.
 *
 * @author Burntroll
 */

public interface ClientService {

    Iterable<Client> searchAll();

    Client searchById(Long id);

    void insert(Client client);

    void update(Long id, Client client);

    void delete(Long id);
}
