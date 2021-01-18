package com.epam.incubation.service.reservationbooking.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.epam.incubation.service.reservationbooking.exception.RestTemplateResponseErrorHandler;
import com.epam.incubation.service.reservationbooking.requestmodel.InventoryRequestModel;
import com.epam.incubation.service.reservationbooking.responsemodel.InventoryDetailsResponseModel;

@Service
public class HotelConsumerService {

	private RestTemplate restTemplate;
	@Value("${hotel.inventory.service}")
	private String inventoryServiceUrl;

	@Autowired
	public HotelConsumerService(RestTemplateBuilder restTemplateBuilder) {
		 restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
	}

	public ResponseEntity<InventoryDetailsResponseModel> getInventoryDetails(InventoryRequestModel inventoryRequestModel) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<InventoryRequestModel> entity = new HttpEntity<>(inventoryRequestModel, headers);
		return restTemplate.exchange(inventoryServiceUrl, HttpMethod.POST, entity, InventoryDetailsResponseModel.class);
	}
	
	public ResponseEntity<InventoryDetailsResponseModel> updateInventoryDetails(InventoryRequestModel inventoryRequestModel){
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<InventoryRequestModel> entity = new HttpEntity<>(inventoryRequestModel, headers);
		return restTemplate.exchange(inventoryServiceUrl, HttpMethod.PUT, entity, InventoryDetailsResponseModel.class);
	}
}
