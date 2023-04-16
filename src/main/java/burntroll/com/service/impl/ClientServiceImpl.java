package burntroll.com.service.impl;

import burntroll.com.model.Address;
import burntroll.com.model.AddressRepository;
import burntroll.com.model.Client;
import burntroll.com.model.ClientRepository;
import burntroll.com.service.ClientService;
import burntroll.com.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
    // Singleton: Inject Spring components with @Autowired.
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ViaCepService viaCepService;

    // Strategy: implement the methods defined in the interface.
    // Facade: Abstract integrations with subsystems, providing a simple interface.
    @Override
    public Iterable<Client> searchAll() {
        // Search all clients
        return clientRepository.findAll();
    }

    @Override
    public Client searchById(Long id) {
        // Search client by ID
        Optional<Client> client = clientRepository.findById(id);
        return client.get();
    }

    @Override
    public void insert(Client client) {
        saveClientWithCep(client);
    }

    @Override
    public void update(Long id, Client client) {
        // Search Client by ID, if exists
        Optional<Client> clientBd = clientRepository.findById(id);
        if (clientBd.isPresent()) {
            saveClientWithCep(client);
        }
    }

    @Override
    public void delete(Long id) {
        // Delete Client by ID
        clientRepository.deleteById(id);
    }

    private void saveClientWithCep(Client client) {
        // Verify if the client`s address already exists (by cep)
        String cep = client.getAddress().getCep();
        Address address = addressRepository.findById(cep).orElseGet(() -> {
            // If not exists, integrates with ViaCEP and persists the return
            Address newAddress = viaCepService.searchZipCode(cep);
            addressRepository.save(newAddress);
            return newAddress;
        });
        client.setAddress(address);
        // Insert Client, bind the address.
        clientRepository.save(client);
    }

}
