import org.springframework.cloud.contract.spec.Contract

Contract.make {
   request {
       method 'GET'
       urlPath('/reservationservice/guestreservationshistory/1')
   }
   response {
       status OK()
        body("""{
  			"data": {
    			"reservations": [
      						{
        						"reservationId": 1,
                				"guestId": 1
               				}
               	]
        	}
        	}""")
       headers {
            contentType(applicationJson())
        }
   }
}