package co.edu.unbosque.springfirstapp.service;

import java.io.IOException;

import java.net.URI;

import java.net.http.HttpClient;

import java.net.http.HttpRequest;

import java.net.http.HttpResponse;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import co.edu.unbosque.springfirstapp.dto.FactDTO;

/**
 * Clase de ejemplo para hacer peticiones HTTP a una API externa (es decir, a un servidor que no
 * es nuestro) desde Java. No forma parte de la API REST del proyecto: se ejecuta por separado
 * con su propio método {@code main}.
 *
 * <p>Todos los métodos siguen la misma idea:
 * <ol>
 *   <li>Armar la petición: URL, método HTTP (GET, POST...), encabezados y, si aplica, un cuerpo JSON.</li>
 *   <li>Enviarla con el {@code HttpClient} y esperar la respuesta.</li>
 *   <li>Leer el código de estado (200 = OK, 404 = no encontrado...) y el cuerpo, que es texto JSON.</li>
 *   <li>Opcional: convertir ese JSON a un objeto Java con Gson.</li>
 * </ol>
 *
 * <p>Los métodos son {@code static}: se usan directamente, por ejemplo
 * {@code ExternalHTTPRequestHandler.doGet(url)}, sin crear un objeto.
 * El {@code try/catch} que se repite en cada método se explica en
 * {@link #doGetAndConvertToDtoList(String)}.
 */
public class ExternalHTTPRequestHandler {

	// El HttpClient es el "navegador" de Java: el que envía las peticiones.
	// Se crea una sola vez (static final) y se reutiliza en todos los métodos, porque se puede
	// compartir sin problema y crearlo en cada petición sería más lento.
	// - version(HTTP_2): intenta usar HTTP/2 y, si el servidor no lo soporta, usa HTTP/1.1.
	// - connectTimeout: si no logra conectarse en 10 segundos, se rinde y lanza un error.
	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
			.connectTimeout(Duration.ofSeconds(10)).build();

	/**
	 * Punto de entrada para probar la clase por separado (no lo usa Spring).
	 * Hace un GET a una API pública de datos curiosos y muestra el resultado.
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("//// GET ////");
		// URL de la API pública que devuelve un dato curioso al azar.
		String fact = "https://uselessfacts.jsph.pl/api/v2/facts/random";
		// System.out.println(doGetAndParse(fact));

		// Hace el GET y convierte el JSON de la respuesta en un objeto FactDTO.
		FactDTO resp = doGetAndConvertToDto(fact);
		System.out.println(resp.toString());

		// Ejemplos de POST, PUT y DELETE desactivados (comentados): quita los // para probarlos.
//		System.out.println("//// POST ////");
//		System.out.println(doPost("https://httpbin.org/post", "{\"name\":\"mkyong\",\"notes\":\"hello\"}"));
//		System.out.println("//// PUT ////");
//		System.out.println(doPut("https://httpbin.org/put", "{\"name\":\"mkyong\",\"notes\":\"hello\"}"));
//		System.out.println("//// DELETE ////");
//		System.out.println(doDelete("https://httpbin.org/delete"));
	}

	/**
	 * Hace un GET y devuelve la respuesta como una lista de {@link FactDTO}.
	 *
	 * <p>Ojo: la API devuelve un solo objeto, así que la lista que se arma tiene un único
	 * elemento. Si el JSON fuera realmente una lista ([...]) habría que usar {@code TypeToken}
	 * de Gson para convertirlo.
	 *
	 * @param url dirección completa de la API (por ejemplo, "https://...")
	 * @return lista con el dato recibido
	 */
	public static List<FactDTO> doGetAndConvertToDtoList(String url) {
		// 1. Armar la petición: método GET + dirección (URI) + encabezados.
		//    El encabezado Content-Type indica el formato del cuerpo que se envía. En un GET no hay
		//    cuerpo, así que aquí no hace falta (lo habitual sería "Accept: application/json").
		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url))
				.header("Content-Type", "application/json").build();

		// 2. Enviar la petición. "response" empieza en null y se llena cuando el envío funciona.
		HttpResponse<String> response = null;
		// try/catch: enviar puede fallar, así que se atrapan los errores posibles:
		//  - IOException: problema de conexión (sin internet, servidor caído, etc.).
		//  - InterruptedException: el hilo fue interrumpido mientras esperaba la respuesta.
		// Aquí solo se imprime el error. Ojo: si falla, "response" se queda en null y la línea que
		// lo usa más abajo lanzaría un NullPointerException.
		try {
			//SE GUARDA EL REQUEST Y EL BodyHandlers para manejarlo como cadena de caracteres
			response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 3. Leer el cuerpo de la respuesta: es un texto en formato JSON.
		String json = response.body();
		//SI LA CLASE DTO es igual al JSON, funciona como un DataMapper para convertir el JSON
		//A objeto
		//Si es una lista, el JSON pasa de Lista a una lista de Objetos
		List<FactDTO> list = Arrays.asList(new GsonBuilder().create().fromJson(json, FactDTO.class));

		return list;

	}

	/**
	 * Hace un GET y convierte la respuesta en un solo objeto {@link FactDTO}.
	 * Además guarda en el objeto el código de estado HTTP (200, 404, ...).
	 *
	 * @param url dirección completa de la API
	 * @return el dato recibido, ya convertido a objeto Java
	 */
	public static FactDTO doGetAndConvertToDto(String url) {
		// Misma petición GET de siempre: método + URL + encabezado.
		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url))
				.header("Content-Type", "application/json").build();

		HttpResponse<String> response = null;
		// Envío de la petición: mismo try/catch explicado en doGetAndConvertToDtoList.
		try {
			response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Cuerpo de la respuesta (texto JSON).
		String json = response.body();
		// Gson convierte el JSON en un FactDTO: cada campo del JSON se asigna al campo del DTO
		// que se llama igual.
		FactDTO fact = new GsonBuilder().create().fromJson(json, FactDTO.class);
		// El código de estado no viene en el JSON: se toma de la respuesta y se guarda en el DTO.
		fact.setStatuscode(response.statusCode());

		return fact;

	}

	/**
	 * Hace un GET y devuelve el JSON recibido como texto, con formato legible
	 * (saltos de línea y sangría). Además imprime el código de estado.
	 *
	 * @param url dirección completa de la API
	 * @return JSON recibido, con formato legible
	 */
	public static String doGetAndParse(String url) {
		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url))
				.header("Content-Type", "application/json").build();

		HttpResponse<String> response = null;
		try {
			response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Código de estado HTTP: 200 = todo bien, 404 = no encontrado, 500 = error del servidor.
		System.out.println("status code -> " + response.statusCode());

		// El JSON suele llegar todo en una sola línea, difícil de leer.
		String uglyJson = response.body();

		// Se devuelve con formato legible (ver prettyPrintUsingGson).
		return prettyPrintUsingGson(uglyJson);
	}

	/**
	 * Hace un GET enviando un encabezado extra con una clave de API ({@code X-Apikey}).
	 * Muchas APIs piden una clave para saber quién las usa, y se envía en un encabezado.
	 *
	 * <p>Ojo: la clave no debería estar escrita directamente en el código, porque queda visible
	 * para cualquiera que vea el repositorio. Lo recomendable es leerla de la configuración
	 * o de una variable de entorno.
	 *
	 * @param url dirección completa de la API
	 * @return JSON recibido, con formato legible
	 */
	public static String doGetWithHeaders(String url) {
		// setHeader agrega un encabezado extra: aquí, la clave de la API.
		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url))
				.header("Content-Type", "application/json")
				.setHeader("X-Apikey", "b022b08d83bbbfd856d2f1e6080a08cc93a24c38db6537639d2c0b5bd84c0701").build();

		HttpResponse<String> response = null;
		try {
			response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("status code -> " + response.statusCode());

		String uglyJson = response.body();
		return prettyPrintUsingGson(uglyJson);
	}

	/**
	 * Hace un GET sencillo y devuelve el JSON recibido con formato legible.
	 * Es el caso más básico: pedir información a una dirección y leer la respuesta.
	 *
	 * @param url dirección completa de la API
	 * @return JSON recibido, con formato legible
	 */
	public static String doGet(String url) {
		// Petición GET: método + URL + encabezado.
		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url))
				.header("Content-Type", "application/json").build();

		HttpResponse<String> response = null;
		// Envío de la petición: mismo try/catch explicado en doGetAndConvertToDtoList.
		try {
			response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("status code -> " + response.statusCode());

		String uglyJson = response.body();
		return prettyPrintUsingGson(uglyJson);
	}

	/**
	 * Hace un POST: envía datos al servidor (por ejemplo, para crear algo nuevo).
	 * Los datos van en el cuerpo de la petición, en formato JSON.
	 *
	 * @param url  dirección completa de la API
	 * @param json texto JSON que se enviará, por ejemplo {@code {"nombre":"Ana"}}
	 * @return JSON de respuesta, con formato legible
	 */
	public static String doPost(String url, String json) {
		// POST envía datos: BodyPublishers.ofString(json) pone el texto JSON como cuerpo.
		// Aquí sí hace falta Content-Type: le avisa al servidor de que el cuerpo es JSON.
		HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(json))
				.uri(URI.create(url)).header("Content-Type", "application/json").build();

		HttpResponse<String> response = null;
		try {
			response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Código de estado: 200 o 201 (creado) suelen indicar que todo salió bien.
		System.out.println("status code -> " + response.statusCode());

		return prettyPrintUsingGson(response.body());
	}

	/**
	 * Hace un PUT: envía datos al servidor para actualizar algo que ya existe.
	 * Funciona igual que {@link #doPost(String, String)}, pero con otro método HTTP.
	 *
	 * @param url  dirección completa de la API (normalmente incluye el id de lo que se actualiza)
	 * @param json texto JSON con los datos nuevos
	 * @return JSON de respuesta, con formato legible
	 */
	public static String doPut(String url, String json) {

		// PUT envía el JSON en el cuerpo, igual que POST, pero para actualizar un recurso.
		// User-Agent es un texto opcional que identifica a quien hace la petición.
		// add json header
		HttpRequest request = HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.ofString(json))
				.uri(URI.create(url)).setHeader("User-Agent", "Java 11 HttpClient Bot")
				.header("Content-Type", "application/json").build();

		HttpResponse<String> response = null;
		try {
			response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("status code -> " + response.statusCode());
		// Se muestra el cuerpo tal como llegó; abajo se devuelve ya con formato legible.
		System.out.println(response.body());
		return prettyPrintUsingGson(response.body());
	}

	/**
	 * Hace un DELETE: le pide al servidor que elimine un recurso.
	 *
	 * @param url dirección completa de lo que se quiere eliminar (normalmente incluye el id)
	 * @return JSON de respuesta, con formato legible
	 */
	public static String doDelete(String url) {

		// DELETE no lleva cuerpo: solo la dirección del recurso que se quiere borrar.
		HttpRequest request = HttpRequest.newBuilder().DELETE().uri(URI.create(url))
				.setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
				.header("Content-Type", "application/json").build();

		HttpResponse<String> response = null;
		try {
			response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("status code -> " + response.statusCode());

		return prettyPrintUsingGson(response.body());
	}

	/**
	 * Convierte un texto JSON "feo" (todo en una línea) en uno con saltos de línea y sangría,
	 * para que sea fácil de leer.
	 *
	 * <p>{@code JsonParser} lee el texto y lo convierte en un árbol de elementos JSON; después
	 * Gson lo vuelve a escribir con formato (setPrettyPrinting). Si el texto no es JSON
	 * (por ejemplo, una página HTML de error), normalmente lanza una excepción de Gson.
	 *
	 * @param uglyJson texto JSON tal como llegó del servidor
	 * @return el mismo JSON, con formato legible
	 */
	public static String prettyPrintUsingGson(String uglyJson) {
		Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
		JsonElement jsonElement = JsonParser.parseString(uglyJson);
		String prettyJsonString = gson.toJson(jsonElement);
		return prettyJsonString;
	}

}