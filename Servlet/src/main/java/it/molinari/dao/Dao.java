package it.molinari.dao;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.molinari.model.Utente;

public class Dao 
{
	//query
	private final static String 
		query_accesso="select nome,cognome,password,email from utente where email=? and password=?", //accesso
		query_registra="insert into utente(nome,cognome,email,password) values(?,?,?,?)", //registra
		query_modifica="update utente set nome=?,cognome=?,password=? where email = ?", //modifica
		query_elimina="delete from utente where email=?",//elimina
		query_get_utenti="select nome,cognome,email from utente"; 
	private String driver;//driver per mysql 
	private String url,usern,pass; //credenziali db 
	private Connection conn; //connessione al db
	
	//costruttore
	public Dao() throws Exception
	{
		log("Istanzio dao");
		//dati per connessione al db
		this.url = "jdbc:mysql://localhost:3306/servlet";
		this.usern = "root";
		this.pass = "root";
		
		//connessione al db
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		conn = get_connessione();
		log("connessione fatta");
	}
	
	//inserisci un nuovo utente
	public Utente inserisci_utente(Utente utente) throws Exception
	{
		log("Sono nel dao inserisci");
		PreparedStatement prep = this.getConn().prepareStatement(query_registra);//prepared statement
		
		//prepared statement
		prep.setString(1,stringToBase64(utente.get_nome()));
	    prep.setString(2,stringToBase64(utente.get_cognome()));
	    prep.setString(3,stringToBase64(utente.get_email()));
	    prep.setString(4,stringToBase64(utente.get_password()));
	    
	    log("Eseguo query...");
		prep.executeUpdate();//invio query
		log("Query eseguita");
		
		//chiusura connessione
		this.getConn().close();
		
		return utente;
	}
	
	public List<Utente> get_utenti() throws Exception
	{
		log("Sono nel dao get_utenti");
		PreparedStatement prep = this.getConn().prepareStatement(query_get_utenti);
		List<Utente> utenti = new ArrayList<>();
		ResultSet risposta; //risposta del db
		Utente utente;
		
		log("Eseguo query...");
		risposta = prep.executeQuery();//invio query
		log("Query eseguita");
		
		while(risposta.next())
		{
			utente = new Utente(
					base64ToString(risposta.getString("nome")),
					base64ToString(risposta.getString("cognome")),
					base64ToString(risposta.getString("email")));
		
			utenti.add(utente);
		}
		
		return utenti;
	}
	//prova ad accedere ad un account
	public Utente prova_accesso(Utente utente) throws Exception
	{
		log("Sono nel dao accedi");
		String nome, cognome; //ulteriori dati utente
		Boolean controllo = false; //controllo se l'utente esiste
		ResultSet risposta; //risposta del db
		
		//prepared statement
		PreparedStatement prep = this.getConn().prepareStatement(query_accesso);
		prep.setString(1, stringToBase64(utente.get_email()));
	    prep.setString(2, stringToBase64(utente.get_password()));
	    
	    log("prima dell'esecuzione della query");
	    risposta = prep.executeQuery(); //invio query
	    log("Query eseguita");
	    
	    log(risposta);
		controllo = risposta.next(); //devo andare alla prossima
		
		if(!controllo) //se l'accesso fallisce
			throw new NoSuchElementException("empty");
		nome = risposta.getString("nome");
		cognome = risposta.getString("cognome");
		
		utente.set_nome(base64ToString(nome));
		utente.set_cognome(base64ToString(cognome));
		//chiusura connessione
		this.getConn().close();

		return utente;
	}

	//modifica credenziali account
	public Utente modifica_account(Utente utente) throws Exception
	{
		log("Sono nel dao modifica");
		log(utente.get_email());
		PreparedStatement prep = this.getConn().prepareStatement(query_modifica);
		int risposta;
		
		prep.setString(1,stringToBase64(utente.get_nome()));
	    prep.setString(2,stringToBase64(utente.get_cognome()));
	    prep.setString(3,stringToBase64(utente.get_email()));
	    prep.setString(4,stringToBase64(utente.get_password()));
	    
	    log("Eseguo query...");
	    risposta = prep.executeUpdate();//invio query
		log("Query eseguita");
		
		//chiusura connessione
		this.getConn().close();
		
		return utente;
	}
	
	public int elimina_account(String email) throws Exception
	{
		log("Sono nel dao elimina");
		log(email);
		PreparedStatement prep = this.getConn().prepareStatement(query_elimina);
		int risposta;
		
		prep.setString(1,stringToBase64(email));
		
		log("Eseguo query...");
	    risposta = prep.executeUpdate();//invio query
	    log(risposta);
		log("Query eseguita");
		
		return risposta;
	}
	public static String stringToBase64(String str) //da stringa a base64
	{
	    byte[] bytes = str.getBytes();
	    byte[] encoded = Base64.getEncoder().encode(bytes);
	    return new String(encoded);
	}
	public static String base64ToString(String base64) //da base64 a stringa
	{
	    byte[] decoded = Base64.getDecoder().decode(base64);
	    return new String(decoded);
	}
	//connessione al database
	private Connection get_connessione() throws SQLException 
	{
		Connection conn = null;
		conn = DriverManager.getConnection(this.url, this.usern, this.pass);
		return conn;
	}
	//scrive in console un valore
	public <generico> void log(generico stringa)
	{
		System.out.println(stringa);
	}
	
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsern() {
		return usern;
	}

	public void setUsern(String usern) {
		this.usern = usern;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}
}
