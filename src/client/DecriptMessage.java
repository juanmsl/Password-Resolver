package client;

import java.io.Serializable;

public class DecriptMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String MINUSCULAS = "abcdefghijklmnopqrstuvwxyz";
	public static final String MAYUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String NUMEROS = "0123456789";
	public static final String ESPECIALES = ",.;:-_!\"#$%&/()=?'\\°ø—Ò·ÈÌÛ˙¡…Õ”⁄¥";
	private String hash;
	private int characters;
	private String dictionary;
	private String password;
	
	public DecriptMessage(String hash, int characters, boolean minus, boolean mayus, boolean number, boolean special) {
		this.hash = hash;
		this.characters = characters;
		this.dictionary = "";
		this.password = "";
		this.dictionary += ((minus) ? DecriptMessage.MINUSCULAS : "");
		this.dictionary += ((mayus) ? DecriptMessage.MAYUSCULAS : "");
		this.dictionary += ((number) ? DecriptMessage.NUMEROS : "");
		this.dictionary += ((special) ? DecriptMessage.ESPECIALES : "");
		
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getHash() {
		return this.hash;
	}
	
	public int getCharacters() {
		return this.characters;
	}
	
	public String getDictionary() {
		return this.dictionary;
	}
	
	@Override
	public String toString() {
		return String.format("Hash: %s\nPassword contains %s characters\nDictionary: %s\nPassword: %s", this.hash, this.characters, this.dictionary, this.password);
	}
}
