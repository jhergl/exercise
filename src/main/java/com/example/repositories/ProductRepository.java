package com.example.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Client;
import com.example.model.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long>{
	
	public Optional<Product> findByNameAndClient(String name, Client client);
	public List<Product> findByClient(Client client);
	
}

	