package liveProject;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
	//Headers Object
	Map<String, String> headers = new HashMap<>();
	
	//Resource path
	String resourcePath = "/api/users";
	
	//generate contract 
	@Pact(consumer="UserConsumer",provider="UserProvider")
	public RequestResponsePact createPact(PactDslWithProvider builder) {
		//add headers
		headers.put("Content-Type", "application/json");
		
		//create json body for req and response
		DslPart requestResponseBody = new PactDslJsonBody()
				.numberType("id",666)
				.stringType("firstName", "Leena")
				.stringValue("lastName", "Kedar")
				.stringValue("email", "email@example.com");
		
		//write fragment to pact
		return builder.given("A request to create a user")
				.uponReceiving("A request to create a user")
				.method("post")
				.headers(headers)
				.path(resourcePath)
				.body(requestResponseBody)
				.willRespondWith()
				.status(201)
				.body(requestResponseBody)
				.toPact();
	
		}
	
	@Test
	@PactTestFor(providerName="UserProvider", port="8686")
	public void consumerTest() {
		//baseURI
		String requestURI = "http://localhost:8686"+resourcePath;
		
		//Request Body
		Map<String, Object> reqBody = new HashMap<>();
		reqBody.put("id",666);
		reqBody.put("firstName", "Leena");
		reqBody.put("lastName", "Kedar");
		reqBody.put("email", "email@example.com");
		
		//Generate response
		given().headers(headers).body(reqBody).log().all().
		when().post(requestURI).
		then().statusCode(201).log().all();
		
	}
}
