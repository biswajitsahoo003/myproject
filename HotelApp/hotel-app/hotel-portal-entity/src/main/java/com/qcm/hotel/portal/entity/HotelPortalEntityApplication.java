package com.qcm.hotel.portal.entity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//@EntityScan("com.qcm.hotel.portal.entites")
@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class HotelPortalEntityApplication 
{
	public static void main(String[] args) {
		SpringApplication.run(HotelPortalEntityApplication.class, args);
	}

}
