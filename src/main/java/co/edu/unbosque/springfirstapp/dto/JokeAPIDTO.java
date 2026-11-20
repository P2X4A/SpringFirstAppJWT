package co.edu.unbosque.springfirstapp.dto;

 //
 // Chiste devuelto por JokeAPI. Cada campo se llama igual que su clave en el JSON, así Gson lo
 // asigna automáticamente.
 
public class JokeAPIDTO {
	private Boolean error;
	private String category;
	private String type;
	// Solo viene cuando type = "single": el chiste completo en una sola frase.
	private String joke;
	private String setup;
	private String delivery;
	// En el JSON, "flags" es un objeto dentro del objeto principal. Por eso aquí es un campo de
	// tipo FlagsDTO (composición: el chiste "tiene" flags) y se llama igual que la clave.
	private FlagsDTO flags;
	private Boolean safe;
	private String lang;
	private int id;

	public JokeAPIDTO(Boolean error, String category, String type, String joke, String setup, String delivery,
			FlagsDTO flags, Boolean safe, String lang, int id) {
		super();
		this.error = error;
		this.category = category;
		this.type = type;
		this.joke = joke;
		this.setup = setup;
		this.delivery = delivery;
		this.flags = flags;
		this.safe = safe;
		this.lang = lang;
		this.id = id;
	}

	public JokeAPIDTO() {
		super();
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getJoke() {
		return joke;
	}

	public void setJoke(String joke) {
		this.joke = joke;
	}

	public String getSetup() {
		return setup;
	}

	public void setSetup(String setup) {
		this.setup = setup;
	}

	public String getDelivery() {
		return delivery;
	}

	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}

	public FlagsDTO getFlags() {
		return flags;
	}

	public void setFlags(FlagsDTO flags) {
		this.flags = flags;
	}

	public Boolean getSafe() {
		return safe;
	}

	public void setSafe(Boolean safe) {
		this.safe = safe;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// Imprime solo el chiste: si es "single" usa joke; si es "twopart", setup y delivery en dos líneas.
	// Si la API respondió con error, lo avisa en vez de imprimir campos vacíos (null).
	@Override
	public String toString() {
		if (Boolean.TRUE.equals(error)) {
			return "No se pudo obtener el chiste: la API respondió con error.";
		}
		if ("single".equals(type)) {
			return joke;
		}
		return setup + "\n" + delivery;
	}

}
