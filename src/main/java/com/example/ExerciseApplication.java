package com.example;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.model.Client;
import com.example.model.Product;
import com.example.repositories.ClientRepository;
import com.example.repositories.ProductRepository;
import com.example.util.Utils;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@Slf4j
@SpringBootApplication
@EnableSwagger2
public class ExerciseApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ExerciseApplication.class, args);
	}
	
	@Autowired
	ClientRepository clientRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	
	@Override
    public void run(String... args) throws Exception {
		productRepository.deleteAll();
		clientRepository.deleteAll();
		clientRepository.save(new Client(null,Utils.adaptString("Mediamarkt")));
		clientRepository.save(new Client(null, Utils.adaptString("PcComponentes")));
		clientRepository.save(new Client(null, Utils.adaptString("Worten")));
		clientRepository.save(new Client(null, Utils.adaptString("Fnac")));
		clientRepository.save(new Client(null, Utils.adaptString("Eroski")));
		clientRepository.save(new Client(null, Utils.adaptString("Carrefour")));
		clientRepository.save(new Client(null, Utils.adaptString("Cliente1")));
		clientRepository.save(new Client(null, Utils.adaptString("Cliente2")));
		clientRepository.save(new Client(null, Utils.adaptString("Cliente3")));
		clientRepository.save(new Client(null, Utils.adaptString("Cliente4")));
		List<Client> clients = (List<Client>) clientRepository.findAll();
		clients.stream().forEach(client-> log.info("Cliente: "+ client.getName()));
        
		productRepository.save(new Product(null,Utils.adaptString("Monitor DELL UltraSharp 24\""),clients.get(0), null));
		productRepository.save(new Product(null,Utils.adaptString("Pantalla DELL UltraSharp 24\""),clients.get(1), null));
		productRepository.save(new Product(null,Utils.adaptString("producto precisión,asd"),clients.get(2), null));
		productRepository.save(new Product(null,Utils.adaptString("producto-dsa/asd"),clients.get(3), null));
		productRepository.save(new Product(null,Utils.adaptString("Pantalla DELL UltraSharp 24\" LCD"),clients.get(4), null));
		List<Product> products = (List<Product>) productRepository.findAll();
		products.stream().forEach(product-> log.info("Producto: "+ product.getName()+"  Cliente: "+ product.getClient().getName()));
		
        log.info("Application Started !!");
        
		
    }

}
