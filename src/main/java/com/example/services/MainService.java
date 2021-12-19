package com.example.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.example.model.Client;
import com.example.model.Product;
import com.example.repositories.ClientRepository;
import com.example.repositories.ProductRepository;
import com.example.util.Utils;
import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

import lombok.extern.slf4j.Slf4j;

@Service
public class MainService {

	private static final String FOR_CLIENT = " para el cliente ";

	private static final String PRODUCT_NOT_FOUND = "No se ha encontrado el producto ";

	private static final String CLIENT_NOT_FOUND = "No se ha encontrado el cliente ";

	@Autowired
	ClientRepository clientRepository;

	@Autowired
	ProductRepository productRepository;

	public Product insertProduct(String clientName, String productName) throws Exception {

		Client client = clientRepository.findByName(clientName)
				.orElseThrow(() -> new Exception(CLIENT_NOT_FOUND));
		
		Optional<Product> opProd =  productRepository.findByNameAndClient(productName, client);
		if(opProd.isPresent()) {
			throw new Exception("Ya existe el producto asociado al cliente");
		}
		try {
			Product product = new Product(null, Utils.adaptString(productName), client, null);
			productRepository.save(product);
			return product;
			
		} catch (Exception e) {
			throw new Exception("Error al insertar producto-> " + productName);
		} 
	}

	public List<Product> getPossibleAssociatedProductsByPercentage(String clientName, String productName, int percentageAccept) throws Exception {
		List<Product> resultList = new ArrayList<Product>();

		Client client = clientRepository.findByName(clientName)
				.orElseThrow(() -> new Exception(CLIENT_NOT_FOUND));

		Product product = productRepository.findByNameAndClient(productName, client)
				.orElseThrow(() -> new Exception(PRODUCT_NOT_FOUND+ FOR_CLIENT + clientName));

		List<Product> checkList = (List<Product>) productRepository.findAll();
		checkList.stream().forEach(product2->{
			if(product2.getClient().getId() != product.getClient().getId() 
					&& checkProductsByPercentage(product.getName(), product2.getName(), percentageAccept)){
				resultList.add(product2);
			}
		});

		return resultList;
	}
	
	public List<Product> getPossibleAssociatedProductsByWords(String clientName, String productName, int numberWords) throws Exception {
		List<Product> resultList = new ArrayList<Product>();

		Client client = clientRepository.findByName(clientName)
				.orElseThrow(() -> new Exception(CLIENT_NOT_FOUND));

		Product product = productRepository.findByNameAndClient(productName, client)
				.orElseThrow(() -> new Exception(PRODUCT_NOT_FOUND+ FOR_CLIENT + clientName));

		List<Product> checkList = (List<Product>) productRepository.findAll();
		checkList.stream().forEach(product2->{
			if(product2.getClient().getId() != product.getClient().getId() 
					&& checkProductsByWords(product.getName(), product2.getName(), numberWords)){
				resultList.add(product2);
			}
		});

		return resultList;
	}

	private boolean checkProductsByPercentage(String prod1, String prod2, int percentageAccept) {
		
		int counter = 0;
		String [] prod1Array = prod1.split(" ");
		String [] prod2Array = prod2.split(" ");
		for(int i = 0; i<prod1Array.length; i++) {
			for(int j = 0; j<prod2Array.length; j++) {
				if(prod1Array[i].equals(prod2Array[j])) {
					counter++;
					break;
				}
			}
		}
		
		if((double)counter/prod1Array.length*100>=percentageAccept) {
			return true;
		}else {
			return false;
		}
	}
	
	private boolean checkProductsByWords(String prod1, String prod2, int numberWords) {
		int counter = 0;
		String [] prod1Array = prod1.split(" ");
		String [] prod2Array = prod2.split(" ");
		for(int i = 0; i<prod1Array.length; i++) {
			for(int j = 0; j<prod2Array.length; j++) {
				if(prod1Array[i].equals(prod2Array[j])) {
					counter++;
					break;
				}
			}
			if(counter>=numberWords) {
				return true;
			}
		}
		return false;
	}
	
	
	public String associateProducts(String clientName1, String productName1,
			String clientName2, String productName2) throws Exception {

		Client client1 = clientRepository.findByName(clientName1)
				.orElseThrow(() -> new Exception(CLIENT_NOT_FOUND + clientName1));

		Product product1 = productRepository.findByNameAndClient(productName1, client1)
				.orElseThrow(() -> new Exception(PRODUCT_NOT_FOUND + productName1  + FOR_CLIENT + clientName1));
		
		Client client2 = clientRepository.findByName(clientName2)
				.orElseThrow(() -> new Exception(CLIENT_NOT_FOUND + clientName2));

		Product product2 = productRepository.findByNameAndClient(productName2, client2)
				.orElseThrow(() -> new Exception(PRODUCT_NOT_FOUND + productName1 + FOR_CLIENT + clientName2));
		
		if(client1.getId()==client2.getId()) {
			throw new Exception("No se pueden asociar productos del mismo cliente");
		}
		
		if(checkIfProductsAlreadyAssociated(product1, product2)) {
			throw new Exception("Los productos ya están relacionados");
		}
		
		if(product1.getProducts()==null) {
			product1.setProducts(new ArrayList<Product>());
		}
		if(product2.getProducts()==null) {
			product2.setProducts(new ArrayList<Product>());
		}
		product1.getProducts().add(product2);
		product2.getProducts().add(product1);		
		productRepository.save(product1);
		productRepository.save(product2);
		
		return "Asociación realizada correctamente";
	}
	
	public boolean checkIfProductsAlreadyAssociated (Product prod1, Product prod2) {
		if(prod1.getProducts()==null || prod1.getProducts().size()==0) {
			//The product has any association yet
			return false;
		}
		if(prod1.getProducts().contains(prod2)){
			//The products are associated
			return true;
		} else {
			//They are not associated
			return false;
		}
	}
	
	public List<Product> getProducts(String clientName) throws Exception{
		Client client = clientRepository.findByName(clientName)
				.orElseThrow(() -> new Exception(CLIENT_NOT_FOUND + clientName));
		
		return productRepository.findByClient(client);
		
	}
	
	public List<Client> getClients() throws Exception{
		return (List<Client>) clientRepository.findAll();
	}
	
	public List<Product> getAssociatedProducts(String clientName, String productName) throws Exception{
		Client client = clientRepository.findByName(clientName)
				.orElseThrow(() -> new Exception(CLIENT_NOT_FOUND + clientName));

		Product product = productRepository.findByNameAndClient(productName, client)
				.orElseThrow(() -> new Exception(PRODUCT_NOT_FOUND + productName + FOR_CLIENT + clientName));
		
		return product.getProducts();
	}

}
