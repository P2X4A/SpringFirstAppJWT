package co.edu.unbosque.springfirstapp.dto;

/**
 * Chiste devuelto por JokeAPI. Cada campo se llama igual que su clave en el JSON, así Gson lo
 * asigna automáticamente.
 */
public class JokeAPIDTO {
	private Boolean error;
	private String category;
	private String type;
	private String setup;
	private String delivery;
	// En el JSON, "flags" es un objeto dentro del objeto principal. Por eso aquí es un campo de
	// tipo FlagsDTO (composición: el chiste "tiene" flags) y se llama igual que la clave.
	private FlagsDTO flags;
	private Boolean safe;
	private String lang;
	private int id;

	public JokeAPIDTO(Boolean error, String category, String type, String setup, String delivery, FlagsDTO flags,
			Boolean safe, String lang, int id) {
		super();
		this.error = error;
		this.category = category;
		this.type = type;
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

	@Override
	public String toString() {
		return "JokeAPIDTO [error=" + error + ", category=" + category + ", type=" + type + ", setup=" + setup
				+ ", delivery=" + delivery + ", flags=" + flags + ", safe=" + safe + ", lang=" + lang + ", id=" + id
				+ "]";
	}

}
