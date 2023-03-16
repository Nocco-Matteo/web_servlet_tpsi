package it.molinari.service;

import java.util.List;

import it.molinari.dao.Dao;
import it.molinari.model.Utente;

public class Api_rest {
	
	public List<Utente> get_utenti() throws Exception
	{
		Dao dao = new Dao();
		return dao.get_utenti();
	}
	public int elimina_account(String email) throws Exception
	{
		Dao dao = new Dao();
		return dao.elimina_account(email);
	}
	public void modifica_account(Utente utente) throws Exception
	{
		Dao dao = new Dao();
		dao.api_modifica_account(utente);
	}
}
