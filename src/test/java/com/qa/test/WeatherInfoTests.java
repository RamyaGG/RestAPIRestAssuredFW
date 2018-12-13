package com.qa.test;

import org.apache.poi.hpsf.Property;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.util.TestBase;
import com.qa.util.TestUtil;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class WeatherInfoTests extends TestBase {
	
	@BeforeMethod
	public void setUp() {
		TestBase.init();
	}
	
	
	@DataProvider
	public Object[][] getdata() {
		Object testData[][] = TestUtil.getTestData(TestUtil.WeatherSheetName);
		return testData;
	}
	
	@Test(dataProvider="getdata")
	public void getWeatherDetailsWithCorrectCityNameTest(String city, String HTTPMethod,String humidity	, String temperature,
			String weatherDescription,String windSpeed,String windDirectionDegree){
		
		//1. define the base URL
		//http://restapi.demoqa.com/utilities/weather/city
		RestAssured.baseURI = prop.getProperty("serviceurl");
		
		//2. Define the http request:
		RequestSpecification httpRequest = RestAssured.given();
		
		//3. Make Request/Execute the request
		Response response = httpRequest.request(Method.GET,"/"+city);
		
		//4. Get the Response Body
		String responseBody = response.getBody().asString();
		System.out.println("Response Body is "+responseBody);
		
		//Validate city name or Validate the key or value
		Assert.assertEquals(responseBody.contains(city), true);
		
		//5. Get the Status Code and Validate it
		int statuscode = response.getStatusCode();
		System.out.println("The status code is :"+statuscode);
		
		//6. Validate it
		Assert.assertEquals(statuscode, TestUtil.RESPONSE_CODE_200);
		
		System.out.println("The status line is "+response.getStatusLine());
		
		//7. Get the headers
		Headers headers = response.getHeaders();
		System.out.println(headers);
		
		String contentType = response.getHeader("Content-Type");
		System.out.println("The value of Content Type is: "+contentType);
		
		String contentLength = response.getHeader("Content-Length");
		System.out.println("The value of Content Length is: "+contentLength);
		
		//Get the key/node value by using JSONPath
		JsonPath jsonPathValue = response.jsonPath();
		String cityVal = jsonPathValue.get("City");
		System.out.println("The value of City is: "+cityVal);
		Assert.assertEquals(cityVal, city);

		String temp = jsonPathValue.get("Temperature");
		System.out.println("The value of Temperature is: "+temp);
		Assert.assertEquals(temp, temperature);
		
		String humidityVal = jsonPathValue.get("Humidity");
		System.out.println("The value of Humidity is: "+humidityVal);
		
		String weatherDescriptionVal = jsonPathValue.get("WeatherDescription");
		System.out.println("The value of WeatherDescription is: "+weatherDescriptionVal);
		
		String windSpeedVal = jsonPathValue.get("WindSpeed");
		System.out.println("The value of WindSpeed is: "+windSpeedVal);
		
		String windDirectionDegreeVal = jsonPathValue.get("WindDirectionDegree");
		System.out.println("The value of WindDirectionDegree is: "+windDirectionDegreeVal);

		
		
	}

}
