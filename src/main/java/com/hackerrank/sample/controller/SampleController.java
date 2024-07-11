package com.hackerrank.sample.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.hackerrank.sample.dto.FilteredProducts;
import com.hackerrank.sample.dto.SortedProducts;

@RestController
public class SampleController {

	
	   final String uri = "https://jsonmock.hackerrank.com/api/inventory";
	   RestTemplate restTemplate = new RestTemplate();
	   String result = restTemplate.getForObject(uri, String.class);			
	   JSONObject root = new JSONObject(result);
	   
	   JSONArray data = root.getJSONArray("data");
	   
	   
		
		@CrossOrigin
		@GetMapping("/filter/price/{initial_price}/{final_price}")  
		private ResponseEntity< ArrayList<FilteredProducts> > filtered_books(@PathVariable("initial_price") int init_price , @PathVariable("final_price") int final_price)   
		{  
			
			try {
				

					ArrayList<FilteredProducts> books = new ArrayList<FilteredProducts>();
					for (int i=0; i< data.length();i++){
						int price = data.getJSONObject(i).getInt("price");
						if(price <= init_price && price >= final_price){
							FilteredProducts filteredProducts = new FilteredProducts(
									data.getJSONObject(i).getString("barcode")
							);
							books.add(filteredProducts);

						}

					}

					if(books.isEmpty()){
						return new ResponseEntity<ArrayList<FilteredProducts>>(HttpStatus.BAD_REQUEST);
					}
			
				    return new ResponseEntity<ArrayList<FilteredProducts>>(books, HttpStatus.OK);

			   
			    
			}catch(Exception E)
				{
	   	System.out.println("Error encountered : "+E.getMessage());
	    return new ResponseEntity<ArrayList<FilteredProducts>>(HttpStatus.NOT_FOUND);
				}
			
		}  
		
		
		@CrossOrigin
		@GetMapping("/sort/price")  
		private ResponseEntity<SortedProducts[]> sorted_books()   
		{  
			
			try {
				
		         SortedProducts[] ans=new SortedProducts[data.length()];
//				 ArrayList <SortedProducts> ans = new ArrayList<>();

				for (int i=0; i< data.length();i++){
					SortedProducts sortedProducts = new SortedProducts(data.getJSONObject(i).getString("barcode"));
//					ans.add(sortedProducts);
					ans[i] = sortedProducts;

				 }
				Arrays.sort(ans,Comparator.comparingInt(product ->{
					JSONObject p = getProductByBarcode(product.getBarCode());
					return p.getInt("price");
				}));
//				ans.sort(Comparator.comparingInt(product ->{
//					JSONObject p = getProductByBarcode(product.getBarCode());
//					return p.getInt("price");
//				}));

			
		         
	
			    return new ResponseEntity<SortedProducts[]>(ans, HttpStatus.OK);
			    
			}catch(Exception E)
				{
	   	  System.out.println("Error encountered : "+E.getMessage());
	       return new ResponseEntity<SortedProducts[]>(HttpStatus.NOT_FOUND);
				}
			
		}

	private JSONObject getProductByBarcode(String barcode) {
		for (int i = 0; i < data.length(); i++) {
			JSONObject product = data.getJSONObject(i);
			if (product.getString("barcode").equals(barcode)) {
				return product;
			}
		}
		return null;
	}
		
		
	
}
