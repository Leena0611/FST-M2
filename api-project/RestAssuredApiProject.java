package liveProject;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import io.restassured.response.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.specification.RequestSpecification;
public class RestAssuredApiProject {
	
	String sshKey =  "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCMmMgodoswj2ni1568K6h5oIIUW+cnDD2QIBa6Dw4MDbBEHEZ4ENlJQoV8+ztG00h6pBmLqV+1hQMdNERMLaLzJJ33/lIU0A8scOhUmI0qLWnvjzzdUiyilJcU2228bZtLqCeu+aleGL2hcEe2YMzp+JNxNK8FeSq8oatnwoZBbBA9tuMznQyFB+/nRxRA0YhEdVrc06OpJ2DMQpTieIweZeVslEdbB/TDT517fbdqYC4PWr4a6LPkai9tJL34P80vYXn1QAv8HufmqTZuLbp8zGPUkE6ZNNz8wYSOD3BEht3I8mG+RTHHqcybOtC2yIX/D+SxeblvfjtJBGrIvo6z";
			
	int sshId;	
	
	RequestSpecification rs;
	
	@BeforeClass
	public void SetUp() {
		rs = new RequestSpecBuilder()
			.setBaseUri("https://api.github.com")
			.addHeader("Authorization", "token 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")
			.build();
	}
	
	@Test(priority = 1)
	public void post() {
		Map<String, String> reqBody = new HashMap<>();
		reqBody.put("title", "testKey");
		reqBody.put("key", sshKey );
		
		Response res = given().spec(rs).body(reqBody)
				.when().post("/user/keys");
		
		System.out.println(res.getBody().asPrettyString());
		sshId=res.then().extract().path("id");
		res.then().statusCode(201).body("key", equalTo(sshKey));
		
		
	}

	@Test(priority = 2)
	public void get() {
		Response res = given().spec(rs).pathParam("keyId", sshId)
				.when().get("/user/keys/{keyId}");
		System.out.println(res.getBody().asPrettyString());
		//sshId=res.then().extract().path("id");
		res.then().statusCode(200).body("key", equalTo(sshKey));
		
	}
	
	@Test(priority = 3)
	public void delete() {
		Response res = given().spec(rs).pathParam("keyId", sshId)
				.when().delete("/user/keys/{keyId}");
		res.then().statusCode(204);
		
	}
	
	
}
