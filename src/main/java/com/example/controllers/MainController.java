package com.example.controllers;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.services.MainService;
import com.example.util.Utils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "User Rest Controller", description = "REST API for User")
@RestController
@RequestMapping("api")
public class MainController {
	
	private static final String PRODUCT_EMPTY = "El producto no puede ser vacío";
	private static final String CLIENT_EMPTY = "El cliente no puede ser vacío";
	@Autowired
	MainService mainService;

	@ApiOperation(value = "insertProduct", notes = "Inserta un producto a un cliente")
	@PostMapping(value="insertProduct")
	public String insertProduct(@RequestParam(value = "clientName") String clientName,
			@RequestParam(value = "productName") String productName) {
		if(checkPropertyNull(clientName)) {
			return CLIENT_EMPTY;
		}
		if(checkPropertyNull(productName)) {
			return PRODUCT_EMPTY;
		}
		
		try {
			mainService.insertProduct(Utils.adaptString(clientName), Utils.adaptString(productName));
			return "Inserción correcta del producto " + productName;
		}catch(Exception e) {
			return e.getMessage();
		}
		
	}
	
	@ApiOperation(value = "getPossibleAssociatedProducts", notes = "Devuelve una lista de objetos que posiblemente sean equivalentes al producto de un cliente. "
			+ "Tiene un parámetro opcional preccisionPercentage que por defecto es 70. El método busca los productos de otros clientes que tengan un rango de "
			+ "similitud en palabras superior al % indicado")
	@GetMapping(value = "getPossibleAssociatedProducts")
	public String getPossibleAssociatedProducts	(@RequestParam(value = "clientName") String clientName,
			@RequestParam(value = "productName") String productName,
			@RequestParam(value = "preccisionPercentage",required = false) Integer preccisionPercentage) {
		if(checkPropertyNull(clientName)) {
			return CLIENT_EMPTY;
		}
		if(checkPropertyNull(productName)) {
			return PRODUCT_EMPTY;
		}
		
		int percentageAccept = 70;
		if(preccisionPercentage != null) {
			percentageAccept = preccisionPercentage.intValue();
			if(preccisionPercentage<0 || preccisionPercentage>100) {
				return "La precisión debe estar comprendida entre 0 y 100";
			}
		}
		
		try {
			return mainService.getPossibleAssociatedProductsByPercentage(Utils.adaptString(clientName), Utils.adaptString(productName), percentageAccept).stream().map(
					prod->"PRODUCTO: " +prod.getName() + " CLIENTE :" + prod.getClient().getName())
					.collect( Collectors.joining( "\n" ) )
            		.toString();
			
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	@ApiOperation(value = "getAssociatedProductsByWords", notes = "Devuelve una lista de objetos que posiblemente sean equivalentes al producto de un cliente. "
			+ "Tiene un parámetro opcional numberWords que por defecto es 3. El método busca los productos de otros clientes que tengan un número de palabras "
			+ " superior al indicado")
	@GetMapping(value = "getAssociatedProductsByWords")
	public String getAssociatedProductsByWords(@RequestParam(value = "clientName") String clientName,
			@RequestParam(value = "productName") String productName,
			@RequestParam(value = "numberWords",required = false) Integer numberWords) {
		if(checkPropertyNull(clientName)) {
			return CLIENT_EMPTY;
		}
		if(checkPropertyNull(productName)) {
			return PRODUCT_EMPTY;
		}
		
		int numerWordsAcceptable = 3;
		if(numberWords != null) {
			numerWordsAcceptable = numberWords.intValue();
			if(numerWordsAcceptable<0 ) {
				return "El número de palabras debe ser mayor que 0";
			}
		}
		
		try {
			return mainService.getPossibleAssociatedProductsByWords(Utils.adaptString(clientName), Utils.adaptString(productName), numerWordsAcceptable).stream().map(
					prod->"PRODUCTO: " +prod.getName() + " CLIENTE :" + prod.getClient().getName())
					.collect( Collectors.joining( "\n" ) )
            		.toString();
			
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	@ApiOperation(value = "associateProducts", notes = "Asocia dos productos pasando como parámetros las dos claves (cliente, producto)")
	@PostMapping(value="associateProducts")
	public String associateProducts(
			@RequestParam(value = "clientName1") String clientName1,@RequestParam(value = "productName1") String productName1,
			@RequestParam(value = "clientName2") String clientName2,@RequestParam(value = "productName2") String productName2) {
		if(checkPropertyNull(clientName1)) {
			return CLIENT_EMPTY;
		}
		if(checkPropertyNull(productName1)) {
			return PRODUCT_EMPTY;
		}
		
		if(checkPropertyNull(clientName2)) {
			return CLIENT_EMPTY;
		}
		if(checkPropertyNull(productName2)) {
			return PRODUCT_EMPTY;
		}
		
		try {
			return mainService.associateProducts(clientName1, productName1,clientName2, productName2);
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	@ApiOperation(value = "getProducts", notes = "Devuelve los productos de un cliente")
	@GetMapping(value = "getProducts")
	public String getProducts(@RequestParam(value = "clientName") String clientName) {
		if(checkPropertyNull(clientName)) {
			return CLIENT_EMPTY;
		}
		
		try {
			return mainService.getProducts(Utils.adaptString(clientName)).stream().map(
					prod->prod.getName())
					.collect( Collectors.joining( "\n" ) )
            		.toString();
			
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	@ApiOperation(value = "getClients", notes = "Devuelve los clientes almacenados")
	@GetMapping(value = "getClients")
	public String getClients() {
		try {
			return mainService.getClients().stream().map(
					client->client.getName())
					.collect( Collectors.joining( "\n" ) )
            		.toString();
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	@ApiOperation(value = "getAssociatedProducts", notes = "Devuelve los productos asociados al producto de un cliente")
	@GetMapping(value = "getAssociatedProducts")
	public String getAssociatedProducts(@RequestParam(value = "clientName") String clientName,
			@RequestParam(value = "productName") String productName) {
		if(checkPropertyNull(clientName)) {
			return CLIENT_EMPTY;
		}
		if(checkPropertyNull(productName)) {
			return PRODUCT_EMPTY;
		}
		
		try {
			return mainService.getAssociatedProducts(Utils.adaptString(clientName), Utils.adaptString(productName)).stream().map(
					prod->"PRODUCTO: " +prod.getName() + " CLIENTE :" + prod.getClient().getName())
					.collect( Collectors.joining( "\n" ) )
            		.toString();
			
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	
	private boolean checkPropertyNull(String property) {
		if(property==null || property=="") {
			return true;
		}
		return false;
	}
}
