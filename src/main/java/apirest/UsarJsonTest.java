package apirest;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;

public class UsarJsonTest {
		@Test
		public void deveriaVerificarPrimeiraNivel() {
			RestAssured.given()
			.when()
				.get("https://restapi.wcaquino.me/users/1")
			.then()
				.statusCode(200)
				.body("id", Matchers.is(1))
				.and()
				.body("name", Matchers.containsString("Silva"))
				.and()
				.body("age", Matchers.greaterThan(18))
				.and()
				.body("age", Matchers.is(30));
		}
		@Test
		public void deveVerificarPrimeiroNivelOutrasFormas() {
			Response response = RestAssured.request(Method.GET, "https://restapi.wcaquino.me/users/1");
			
			// Path
			// System.out.println("O ID é: "+response.path("id")+".");
			
			Assert.assertEquals(1, response.path("id"));
			Assert.assertEquals(1, response.path("%s", "id"));
			
			//JSON Path
			
			JsonPath jpath = new JsonPath(response.asString());
			Assert.assertEquals(1, jpath.getInt("id"));
			
			// FROM
			
			int id = JsonPath.from(response.asString()).getInt("id");
			Assert.assertEquals(1, id);
		}
		
		@Test
		public void deveVerificarSegundoNivel() {
			RestAssured.given()
			.when()
				.get("https://restapi.wcaquino.me/users/2")
			.then()
				.statusCode(200)
				.body("endereco.rua", Matchers.is("Rua dos bobos"));
		}
		
		@Test
		public void deveVerificarLista() {
			RestAssured.given()
			.when()
				.get("https://restapi.wcaquino.me/users/3")
			.then()
				.statusCode(200)
				.body("name", Matchers.containsString("Ana"))
				.body("filhos", Matchers.hasSize(2))
				.body("filhos[0].name", Matchers.is("Zezinho"))
				.body("filhos[1].name", Matchers.is("Luizinho"))
				.body("filhos.name", Matchers.hasItem("Luizinho"))
				.body("filhos.name", Matchers.hasItems("Luizinho", "Luizinho", "Luizinho"))
				;
			
		}
		@Test
		public void deveRetornarErroUsuarioInexistente() {
			RestAssured.given()
			.when()
				.get("https://restapi.wcaquino.me/users/4")
			.then()
				.statusCode(404)
				.body("error", Matchers.is("Usuário inexistente"))
				;
		}
		@Test
		public void deveVerificarListaRaiz() {
			RestAssured.given()
			.when()
				.get("https://restapi.wcaquino.me/users")
			.then()
				.statusCode(200)
				.body("name", Matchers.hasItems("João da Silva", "Maria Joaquina", "Ana Júlia"))
				.body("$", Matchers.hasSize(3)) // Cifrão é uma convenção - Poderia ser apenas ""
				.body("age[1]", Matchers.is(25)) // Idade na posição 2
				.body("filhos.name", Matchers.hasItem(Arrays.asList("Zezinho", "Luizinho"))) // Procura a lista completa
				.body("salary", Matchers.contains(1234.5678f, 2500, null)) // Contains apenas aceita com todos valores e a ordem correta
				.body("filhos[2].name[0]", Matchers.is("Zezinho"))
				.body("salary[0]", Matchers.is(1234.5678f))
				;
		}
		
		@Test
		public void devoFazerVerificacoesAvancadas() {
			RestAssured.given()
			.when()
				.get("https://restapi.wcaquino.me/users")
			.then()
				.statusCode(200)
				.body("age.findAll{it <= 25}.size()", Matchers.is(2)) // Filtra todas as idades menor ou igual a 25 anos 
				.body("age.findAll{it <= 25 && it > 20}.size()", Matchers.is(1)) // Filtra todas as idades menor ou igual a 25 e maior que 20 anos
				.body("findAll{it.age <= 25 && it.age > 20}.name", Matchers.hasItem("Maria Joaquina")) // Retorna o nome do individuo que tem a idade menor ou igual a 25 e maior do que 20 anos
				.body("findAll{it.age <= 25}[0].name", Matchers.is("Maria Joaquina")) // Por padrão, ele vai retornar o primeiro que atende aos parâmetros estabelecidos. Se for, necessário, deverá especificar pela [POSIÇÃO]
				.body("findAll{it.age <= 25}[-1].name", Matchers.is("Ana Júlia"))
				.body("find{it.age <= 25}.name", Matchers.is("Maria Joaquina")) // Mesmo padrão do abordado acima
				.body("findAll{it.name.contains('n')}.name", Matchers.hasItems("Maria Joaquina", "Ana Júlia")) // Filtra todos os elementos cujo o nome contém a letra n
				.body("findAll{it.name.length() > 10}[0].name", Matchers.is("João da Silva")) // Retorna o nome que tem tamanho maior que 10
				.body("name.collect{it.toUpperCase()}", Matchers.hasItem("MARIA JOAQUINA"))
				.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}", Matchers.hasItem("MARIA JOAQUINA"))
				.body("age.collect{it * 2}", Matchers.hasItems(60, 50, 40)) // Multiplica todas as idades por 2 e valida se as alterações foram feitas	
				.body("id.max()", Matchers.is(3)) // Retorna o número máximo de IDs
				.body("salary.min()", Matchers.is(1234.5678f))
				.body("salary.findAll{it != null}.sum()", Matchers.is(Matchers.closeTo(3734.5678f, 0.001)))
				;
		}
		@Test
		public void devoUnirJsonPathComJava() {
			RestAssured.given()
			.when()
				.get("https://restapi.wcaquino.me/users")
			.then()
				.statusCode(200);
		}
}
