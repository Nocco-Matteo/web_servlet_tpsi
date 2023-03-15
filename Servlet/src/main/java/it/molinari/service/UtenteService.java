package it.molinari.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import it.molinari.dao.Dao;
import it.molinari.model.Utente;

public class UtenteService 
{
	//inserisci un utente nel db
	public Utente inserisci_utente(String nome, String cognome, String email, String password) throws Exception
	{
		Utente utente = new Utente(nome,cognome,email,password);
		Dao dao = new Dao();
		return dao.inserisci_utente(utente);
	}
	//prova accesso al db
	public Utente prova_accesso(String email, String password) throws Exception
	{
		Utente utente = new Utente(email,password);
		Dao dao = new Dao();
		return dao.prova_accesso(utente);
	}
	
	public Utente modifica_account(String nome, String cognome, String email, String password) throws Exception
	{
		Utente utente = new Utente(nome,cognome,email,password);
		Dao dao = new Dao();
		return dao.modifica_account(utente);
	}
	
	public int elimina_account(String email) throws Exception
	{
		Dao dao = new Dao();
		return dao.elimina_account(email);
	}
}
