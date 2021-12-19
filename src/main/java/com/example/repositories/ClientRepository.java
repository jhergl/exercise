package com.example.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Client;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long>{
	public Optional<Client> findByName(String name);
}
