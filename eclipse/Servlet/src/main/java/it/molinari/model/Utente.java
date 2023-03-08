package it.molinari.model;

public class Utente 
{
	private String  nome,
					cognome,
					email,
					password;
	
	//costruttore
	public Utente()
	{
		this.nome=null;
		this.cognome=null;
		this.email=null;
		this.password=null;
		
	}
	//costruttore
	public Utente(String nome, String cognome,String email, String password)
	{
		this.nome = nome;
		this.cognome= cognome;
		this.email=email;
		this.password=password;
	}
	//costruttore
	public Utente(String email, String password)
	{
		this.nome = null;
		this.cognome= null;
		this.email=email;
		this.password=password;
	}
	public String get_nome() {
		return nome;
	}
	public void set_nome(String nome) {
		this.nome = nome;
	}
	public String get_cognome() {
		return cognome;
	}
	public void set_cognome(String cognome) {
		this.cognome = cognome;
	}
	public String get_email() {
		return email;
	}
	public void set_email(String email) {
		this.email = email;
	}
	public String get_password() {
		return password;
	}
	public void set_password(String password) {
		this.password = password;
	}
}
