package apirest;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;

public class OlaMundoTest {
	
	@Test
	public void testarOlaMundo() {
		Response response = RestAssured.request(Method.GET, "https://restapi.wcaquino.me/ola");
	
		//System.out.println(response.getBody().asString());
		//System.out.println(response.getStatusCode());
		
		/* No JUNIT, existem dois tipos de "BUGS": o errors e failures.
		 * O errors � uma inconsist�ncia da aplica��o (geralmente o script automatizado), n�o permitindo que chegue at� as valida��es de cen�rio.
		 * O failure pode ocorrer quando h� alguma valida��o, por exemplo, entre valores onde est�o divergentes.
		 */
		
		
		response.then().statusCode(200);
		
		// O assertTrue pode ser utilizado com um ou dois par�metros. A premissa � que, quando h� um par�mtro deve ser o boolean.
		
		Assert.assertTrue("", response.getBody().asString().equals("Ola Mundo!"));
		Assert.assertTrue("O status deveria ser 200", response.getStatusCode() == 200);
		Assert.assertEquals(200, response.getStatusCode());
		// Primeiro par�metro � o esperado / Segundo � o obtido
	
	}
	
	@Test
	public void devoConhecerOutrasFormasRestAssured() {
		// get("https://restapi.wcaquino.me/ola").then().statusCode(200);
		
		// Given When Then - Gherkin
		
		given() // Pr� condi��es
		.when() // A��o 
			.get("https://restapi.wcaquino.me/ola")
		.then() // Assertivas
			.statusCode(200);
	}
	
	@Test
	public void devoConhecerMatcherHamcrest() {
		Assert.assertThat("Maria", Matchers.is("Maria")); // Compara��o de nomes
		Assert.assertThat(128, Matchers.is(128)); // Compara��o de n�meros
		Assert.assertThat(128, Matchers.isA(Integer.class)); // Valida��o se � um n�mero inteiro
		Assert.assertThat(128d, Matchers.isA(Double.class));  // Valida��o se � um n�mero Double
		Assert.assertThat(128,  Matchers.greaterThan(120)); // Maior que
		
		List<Integer> impares = Arrays.asList(1, 3, 5, 7, 9);
		assertThat(impares, Matchers.hasSize(5));
		// assertThat(impares, Matchers.contains(1, 2, 3));
		assertThat(impares, Matchers.containsInAnyOrder(1, 9, 7, 5, 3));
		assertThat(impares, Matchers.hasItem(1));
		assertThat(impares, Matchers.hasItem(3));
		
		assertThat("Maria", Matchers.is(Matchers.not("Jo�o")));
		assertThat("Maria", Matchers.anyOf(Matchers.is("Maria"), Matchers.is("Pedro")));
		assertThat("Joaquina", Matchers.allOf(Matchers.startsWith("Joa"), Matchers.endsWith("ina"), Matchers.containsString("qui")));
		
	}
	
	@Test
	public void devoValidarBody() {
		given()
		.when()
			.get("https://restapi.wcaquino.me/ola")
		.then()
			.statusCode(200)
			.body(Matchers.is("Ola Mundo!"))
			.and()
			.body(Matchers.containsString("Mundo"))
			.and()
			.body(Matchers.is(Matchers.not(Matchers.nullValue())));
	}
}
