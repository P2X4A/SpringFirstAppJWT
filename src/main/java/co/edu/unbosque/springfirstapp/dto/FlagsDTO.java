package co.edu.unbosque.springfirstapp.dto;

/**
 * Objeto "flags" que viene anidado dentro del chiste de JokeAPI. Indica si el chiste pertenece a
 * categorías sensibles. Cada campo se llama igual que su clave en el JSON.
 */
public class FlagsDTO {
	private Boolean nsfw;
	private Boolean religious;
	private Boolean political;
	private Boolean racist;
	private Boolean sexist;
	private Boolean explicit;

	public FlagsDTO(Boolean nsfw, Boolean religious, Boolean political, Boolean racist, Boolean sexist,
			Boolean explicit) {
		super();
		this.nsfw = nsfw;
		this.religious = religious;
		this.political = political;
		this.racist = racist;
		this.sexist = sexist;
		this.explicit = explicit;
	}

	public FlagsDTO() {
		super();
	}

	public Boolean getNsfw() {
		return nsfw;
	}

	public void setNsfw(Boolean nsfw) {
		this.nsfw = nsfw;
	}

	public Boolean getReligious() {
		return religious;
	}

	public void setReligious(Boolean religious) {
		this.religious = religious;
	}

	public Boolean getPolitical() {
		return political;
	}

	public void setPolitical(Boolean political) {
		this.political = political;
	}

	public Boolean getRacist() {
		return racist;
	}

	public void setRacist(Boolean racist) {
		this.racist = racist;
	}

	public Boolean getSexist() {
		return sexist;
	}

	public void setSexist(Boolean sexist) {
		this.sexist = sexist;
	}

	public Boolean getExplicit() {
		return explicit;
	}

	public void setExplicit(Boolean explicit) {
		this.explicit = explicit;
	}

	@Override
	public String toString() {
		return "FlagsDTO [nsfw=" + nsfw + ", religious=" + religious + ", political=" + political + ", racist="
				+ racist + ", sexist=" + sexist + ", explicit=" + explicit + "]";
	}

}
