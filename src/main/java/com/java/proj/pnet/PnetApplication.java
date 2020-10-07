package com.java.proj.pnet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import org.springframework.beans.factory.annotation.*;
import org.springframework.core.*;
import org.springframework.http.*;
import java.util.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.*;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class PnetApplication extends SpringBootServletInitializer{

	@Autowired    
	private CountryRepository countryRepository;
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(PnetApplication.class);
        app.setDefaultProperties(Collections
          .singletonMap("server.port", "8084"));
        app.run(args);
	}


	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean

	// Consume external API
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {	 

			HttpHeaders headers = new HttpHeaders();
			headers.set("x-rapidapi-host", "restcountries-v1.p.rapidapi.com");
			headers.set("x-rapidapi-key", "34cce90b94mshb79f9d2db4398d8p182f8djsnd9a6777a1371");
			HttpEntity entity = new HttpEntity(headers);

			HttpEntity<List <Country>> response = restTemplate.exchange(
				"https://restcountries-v1.p.rapidapi.com/subregion/South-Eastern Asia", 
				HttpMethod.GET, entity, new ParameterizedTypeReference<List <Country>> () {} );

			List <Country> countryList = response.getBody();
			//Save data.

			 
			countryRepository.saveAll(countryList);   ;
			 
			
		};
	}



}
