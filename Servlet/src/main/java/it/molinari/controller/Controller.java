package it.molinari.controller;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.molinari.model.Utente;
import it.molinari.service.Api_rest;
import it.molinari.service.UtenteService;

public  class  Controller extends HttpServlet
{
	private Utente utente; //utente
	private UtenteService servizio; //servizi per l'utente
	private Api_rest api; //servizi api rest
	private RequestDispatcher dispatcher; //dispatcher
	private String pagina=""; //pagina richiesta
	
	//gestione richieste get
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try 
		{
			//System.out.println(dtf.format(now));  
			HttpSession session = request.getSession();
			String file; //path del file da inviare
			String pagina = dividi_uri(request.getRequestURI());//pagina richiesta
			
			log(pagina);
			switch(pagina)
			{
				case "style": //ritorna lo stile dell'index
					response.setContentType("text/css");
					file = "/css/style.css";
					get_file(response,file);
				break;
				case "style_js": //ritorna lo stile dell'index
					response.setContentType("text/css");
					file = "/css/style_js.css";
					get_file(response,file);
				break;
				case "script": //ritorna lo stile dell'index
					response.setContentType("text/javascript");
					file = "/script/script.js";
					get_file(response,file);
				break;
				case "logo": //ritorna il logo della pagina
					response.setContentType("image/png");
					file = "/img/logo.png";
					get_file(response,file);
				break;
				case "background": //ritorna il background della pagina
					response.setContentType("image/png");
					file = "/img/bg.png";
					get_file(response,file);
				break;
				case "nav":
					response.setContentType("image/png");
					file = "/img/nav.png";
					get_file(response,file);
				break;
				case "centrale":
					response.setContentType("image/png");
					file = "/img/col_bg.png";
					get_file(response,file);
				break;
				case "elimina": elimina(request,response,session);
				break;
				case "esci": esci(response,session); 
				break;
				case "utenti": response.sendRedirect("http://localhost:8080/Servlet/ajax.html");
				break;
				case "get_utenti": get_utenti(request,response);
				break;
				default: 
					RequestDispatcher dispatch = request.getRequestDispatcher("index.jsp");
					dispatch.forward(request, response);
			}
			
		} 
		catch (Exception e){e.printStackTrace();}
	}	
	//gestione richieste post
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try 
		{
			HttpSession session = request.getSession();
			servizio = new UtenteService();//servizio per eseguire l'operazione richiesta dall'utente
			pagina = dividi_uri(request.getRequestURI());//pagina richiesta
			
			switch(pagina)
			{
				case "registra": registra(request,response,session); //esegue la registrazione di un utente
				break;
				case "accedi": accedi(request,response,session); //prova l'accesso ad un account
				break;
				case "modifica": modifica(request,response,session); //modifica il nome,cognome e/o la password di un account
				break;
				default: throw new IllegalArgumentException("Errore");
			}
		} 
		catch(NoSuchElementException e) //empty result dalla query accedi
		{
			dispatcher = request.getRequestDispatcher("index.jsp?page="
					+stringToBase64("accedi")
					+"&error="
					+stringToBase64("empty,account,"+pagina));
	        dispatcher.forward(request, response);
		}
		catch(SQLIntegrityConstraintViolationException e) //errore di unique email
		{
			dispatcher = request.getRequestDispatcher("index.jsp?page="
					+stringToBase64("registra")
					+"&error="
					+stringToBase64("sql,constraint,"+pagina));
	        dispatcher.forward(request, response);
		}
		catch(IllegalArgumentException e)  //errore di controllo regex
		{
			dispatcher = request.getRequestDispatcher("index.jsp?page="
					+stringToBase64("registra")
					+"&error="
					+stringToBase64("regex,"+e.getMessage()+","+pagina)); //e.getMessage() = parametro errato
	        dispatcher.forward(request, response);
		}
		catch(Exception e){e.printStackTrace();}
	}
	//gestione richieste delete
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try 
		{
			HttpSession session = request.getSession();
			servizio = new UtenteService();//servizio per eseguire l'operazione richiesta dall'utente
			pagina = dividi_uri(request.getRequestURI());//pagina richiesta
			
			switch(pagina)
			{
				case "delete_utente": delete_utente(request,response, session); //esegue la registrazione di un utente
				break;
				default: throw new IllegalArgumentException("Errore");
			}
		} 
		catch(Exception e){e.printStackTrace();}
	}
	//gestione richieste put
	protected void doPut(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
	{
		try 
		{
			log("Sono nel put");
			HttpSession session = request.getSession();
			servizio = new UtenteService();//servizio per eseguire l'operazione richiesta dall'utente
			pagina = dividi_uri(request.getRequestURI());//pagina richiesta
			
			log("Pagina: "+pagina);
			switch(pagina)
			{
				case "put_utente": put(request, response);
					
				break;
				default: throw new IllegalArgumentException("Errore");
			}
		} 
		catch(Exception e){e.printStackTrace();}
	}
	
	//api modifica utente
	private void put(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = leggi_corpo_request(request);
		System.out.println("parametro: "+json);
		Gson gson = new Gson();
		Utente utente = new Utente();
		utente = gson.fromJson(json, Utente.class);
		System.out.println(utente.get_email());
		api = new Api_rest();
		
		api.modifica_account(utente);
		
		response.setContentType("application/json");
		response.getWriter().write("{\"success\": true}");
	}
	//legge il corpo della request e ritorna una stringa con i campi
	public String leggi_corpo_request(HttpServletRequest request) throws Exception
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
	}
	//esce dall'account corrente
	private void esci(HttpServletResponse response, HttpSession session) throws Exception {
		session.invalidate();
		response.sendRedirect("index.jsp");
	}
	//api elenco utenti
	public void get_utenti(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log("get utenti");
		//utente = 
		api = new Api_rest();
		
		List<Utente> lista = api.get_utenti();
		
		Gson gson = new Gson();
		String json = gson.toJson(lista);
		
	    response.setContentType("application/json");
	    response.getWriter().write(json);
	}
	//api elimina utente
	public void delete_utente(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
	{ 
		log("elimina");
		log("parametro: "+request.getAttribute("email"));
		api = new Api_rest();
		//avvio servizio di elimina account
		int risposta = api.elimina_account(request.getParameter("email"));
		log("prima di set attribute");
		//impostazione parametri della sessione
		session.invalidate();
        
        dispatcher = request.getRequestDispatcher("ajax.html");
        dispatcher.forward(request, response);
	}
	//esegue la registrazione di un utente e rimanda alla pagina di benvenuto
	public void registra(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
	{
		log("registra");
		
		controllo(request,response);
		
		utente = servizio.inserisci_utente(
				request.getParameter("nome"), 
				request.getParameter("cognome"),
				request.getParameter("email"), 
				request.getParameter("password"));
		
		log("prima di set attribute");
		//impostazione parametri della sessione
		imposta_session(session);
		log("prima di dispatcher");
        dispatcher = request.getRequestDispatcher("index.jsp?page="
				+stringToBase64("entrato"));
        dispatcher.forward(request, response); 
	}
	//prova l'accesso ad un account 
	public void accedi(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
	{
		log("accedi");
		utente = servizio.prova_accesso(
				request.getParameter("email"), 
				request.getParameter("password"));//risultato query

		//impostazione parametri della sessione
		imposta_session(session);
        
        dispatcher = request.getRequestDispatcher("index.jsp?page="+stringToBase64("entrato"));
        dispatcher.forward(request, response);
	}
	//prova la modifica di un account
	public void modifica(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
	{
		log("modifica");
		//avvio servizio di modifica account
		utente = servizio.modifica_account(
				request.getParameter("nome"), 
				request.getParameter("cognome"),
				request.getParameter("email"), 
				request.getParameter("password"));
		log("prima di set attribute");
		//impostazione parametri della sessione
        imposta_session(session);
        
        dispatcher = request.getRequestDispatcher("index.jsp?page="+stringToBase64("modificato"));
        dispatcher.forward(request, response);

	}
	//elimina un account
	public void elimina(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
	{
		log("elimina");
		//avvio servizio di elimina account
		int risposta = servizio.elimina_account(session.getAttribute("email").toString());
		log("prima di set attribute");
		//impostazione parametri della sessione
		session.invalidate();
        
        dispatcher = request.getRequestDispatcher("index.jsp");
        dispatcher.forward(request, response);
	}
	//imposta la sessione con l'utente globale
	public void imposta_session(HttpSession session) 
	{
		session.setAttribute("nome", utente.get_nome());
        session.setAttribute("cognome", utente.get_cognome());
        session.setAttribute("email", utente.get_email());
        session.setAttribute("password", utente.get_password());
	}
	//controllo RegEx
	public void controllo(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		if(pagina=="modifica") return;
		
		Pattern reg = Pattern.compile("^[a-zA-Z]+$");
		Matcher matcher = reg.matcher(request.getParameter("nome"));
		if(!matcher.matches() ) throw new IllegalArgumentException("nome");
		log("riuscita");
		
		reg = Pattern.compile("^[a-zA-Z]+$");
		matcher = reg.matcher(request.getParameter("cognome"));
		if(!matcher.matches()) throw new IllegalArgumentException("cognome");
		log("riuscita");
		
		reg = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
		matcher = reg.matcher(request.getParameter("email"));
		if(!matcher.matches()&& pagina!="modifica") throw new IllegalArgumentException("email");
		log("riuscita");
		
		reg = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])[a-zA-Z\\d]{4,}$");
		matcher = reg.matcher(request.getParameter("password"));
		
		
		if(!matcher.matches()) throw new IllegalArgumentException("password");
		log("riuscita");
	}	
	//invia il file richiesto
	protected void get_file(HttpServletResponse response, String path) throws Exception //invia un file del path dichiarato
	{
		InputStream in = getServletContext().getResourceAsStream(path);
		OutputStream out = response.getOutputStream();
		byte[ ] buffer = new byte[1024]; //buffer per leggere
		int letti;
		while((letti = in.read(buffer)) != -1) //legge fino alla fine
			out.write(buffer,0,letti);
		
	    in.close();
	    out.flush();
	}
	//divide l'uri e ritorna l'ultima parte
	public String dividi_uri(String uri)
	{
		System.out.println("divido "+uri);
		String[] splitted = uri.split("/");
		
		if(splitted[2].equals("api_rest")) //se è una richiesta api
		{
			System.out.println(splitted[2]+" c'è "+splitted[3]);
			return splitted[3]; //ritorna la 4 cella
		}
			return splitted[2]; //
	}
	//prende una stringa e ritorna il base64
	public static String stringToBase64(String str)
	{
	    byte[] bytes = str.getBytes();
	    byte[] encoded = Base64.getEncoder().encode(bytes);
	    return new String(encoded);
	}
	//prende un base64 e ritorna la stringa
	public static String base64ToString(String base64) 
	{
	    byte[] decoded = Base64.getDecoder().decode(base64);
	    return new String(decoded);
	}
}


