package com.epam.incubation.service.reservationbooking.facade;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.epam.incubation.service.reservationbooking.requestmodel.InventoryRequestModel;
import com.epam.incubation.service.reservationbooking.responsemodel.InventoryDetailsResponseModel;

@FeignClient(name="gatewayserver")
public interface HotelInfoServiceProxy {

	@PostMapping("/hotelservice/hotelInfo/inventoryService")
	public InventoryDetailsResponseModel getInventoryDetails(@RequestBody InventoryRequestModel model);

	@PutMapping("/hotelservice/hotelInfo/inventoryService")
	public InventoryDetailsResponseModel updateInventory(@Valid @RequestBody InventoryRequestModel model);
}
