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
}
