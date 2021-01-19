package com.epam.incubation.service.reservationbooking.facade;

import javax.validation.Valid;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.epam.incubation.service.reservationbooking.requestmodel.InventoryRequestModel;
import com.epam.incubation.service.reservationbooking.responsemodel.InventoryDetailsResponseModel;

@FeignClient(name="hotel-info-service")
@RibbonClient(name="hotel-info-service")
public interface HotelInfoServiceProxy {

	@PostMapping("/hotelInfo/inventoryService")
	public InventoryDetailsResponseModel getInventoryDetails(@RequestBody InventoryRequestModel model);
	
	@PutMapping("/hotelInfo/inventoryService")
	public InventoryDetailsResponseModel updateInventory(@Valid @RequestBody InventoryRequestModel model);
}
