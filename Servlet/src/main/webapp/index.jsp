<%@page import="java.net.http.HttpRequest"%>
<%@ page language="java" contentType="text/html; charset=US-ASCII" pageEncoding="US-ASCII"%>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.Base64.*" %>
<!DOCTYPE html>
<html>
<head>
	<title>Titolo Pagina</title>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style">
</head>
<body>
<%!
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
	
	public static Boolean is_logged_in(String nome)
	{
		if(nome == null) return false;
		else return true;
	}
	public static String pulisci_attributo()
	{
		return null;
	}
%>

<%
	Boolean accesso = false; //controllo se l'utente ha fatto l'accesso
	String nome_sess = "", //dati utente
			cognome_sess ="",
			email_sess = "",
			password_sess="";
	String pagina = ""; //pagine dinamiche
	String tipo_errore="";//tipo di errore (piu` specifico di classe_errore)
	String classe_errore=""; //classe dell'errore (piu` generale di tipo_errore)
	
	if(session.getAttribute("nome")!= null) //controllo se l'utente ha fatto l'accesso
	{
		nome_sess = session.getAttribute("nome").toString(); //prendi attributo nome della sessione
		nome_sess = nome_sess.substring(0, 1).toUpperCase() + nome_sess.substring(1); //primo carattere maiuscolo

		cognome_sess = session.getAttribute("cognome").toString(); //prendi attributo cognome della sessione
		cognome_sess = cognome_sess.substring(0, 1).toUpperCase() + cognome_sess.substring(1); //primo carattere maiuscolo
		
		email_sess = session.getAttribute("email").toString(); //prendi attributo email della sessione
		email_sess = email_sess.toLowerCase(); //tutto minuscolo

		password_sess = session.getAttribute("password").toString(); //prendi attributo password della sessione
		
		accesso = is_logged_in(nome_sess); //controlla se ha fatto l'accesso
	}
	
	
	
	if(request.getParameter("page")!= null) //cambia pagina in base al parametro get
		pagina = request.getParameter("page"); //impostazione della pagina richiesta
	
		
	if(request.getParameter("error")!= null) //se viene rilevato un errore in una richiesta
	{
		String [] errore = base64ToString(request.getParameter("error")).split(","); //divisione del parametro errore
		//errore[0]=classe_errore; errore[1]=tipo_specifico_errore; errore[2]=pagina_di_arrivo
		classe_errore = errore[0];
		//out.print(classe_errore);
		switch(classe_errore)
		{
			case "sql": //errore sql
				pagina = stringToBase64(errore[2]); //ritorno alla pagina di errore
				tipo_errore = errore[1]; //tipo dell'errore dettagliato
				//out.print("errore: "+tipo_errore); 
			break;
			case "regex": //errore regex
				pagina = stringToBase64(errore[2]); //ritorno alla pagina di errore
				tipo_errore = errore[1]; //tipo dell'errore dettagliato 
				//out.print("errore: "+tipo_errore); 
			break;
			case "empty": //accesso fallito
				pagina = stringToBase64(errore[2]); //ritorno alla pagina di errore
				tipo_errore = errore[1]; //tipo dell'errore dettagliato 
				//out.print("errore: "+tipo_errore); 
			break;
			default:
		}
	}
	//out.print(accesso+"\n");
	
	
	
	//out.print(tipo_errore+"\n");
	//out.print(pagina+"\n");
	pagina = base64ToString(pagina); //conversione parametro get con la pagina
	//out.print(pagina);
%>
<!-- Testa Navbar -->
<div class="foto_bg" style="background-image:url('${pageContext.request.contextPath}/background');">
	<nav class="navbar navbar-expand-lg navbar-light ">
	
		<a class="navbar-brand mt-2 logo" href="${pageContext.request.contextPath}">
			<img class="" width="75px" height="75px" src="${pageContext.request.contextPath}/logo" alt="Logo"> 
			<p><b>SIG</b>LOG</p>
		</a>
		
		<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		
		<div class="collapse navbar-collapse justify-content-end" id="navbarNav">
			<%if(accesso){ %>
			<ul class="navbar-nav">
				<li class="nav-item">
					<%=email_sess%>
				</li>
			</ul>
			<ul class="navbar-nav">
			<%if(email_sess.equals("admin@admin")) {%>
				<li class="nav-item">
					<a class="nav-link" href="http://localhost:8080/Servlet/utenti">UTENTI</a>
				</li>
				<%}%>
				<li class="nav-item">
					<a class="nav-link" href="http://localhost:8080/Servlet/index.jsp?page=<%=stringToBase64("profilo")%>">PROFILO</a>
				</li>
			</ul>
			<%} else{ %>
			<ul class="navbar-nav r-0">
				<li class="nav-item">
					<a class="nav-link" href="http://localhost:8080/Servlet/index.jsp?page=<%=stringToBase64("accedi")%>">ACCEDI </a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="http://localhost:8080/Servlet/index.jsp?page=<%=stringToBase64("registra")%>">REGISTRATI</a>
				</li>
			</ul>
			<%} %>
		</div>
	</nav>
	<!-- Corpo Pagina -->
	<div class="container-fluid corpo" >
		<div class="row">
			<div class="col"></div> <!-- col per spazio -->
			<div class="centrale col-md-5 pt-2" style="background-image:url('${pageContext.request.contextPath}/centrale">
				<%if(pagina.equals("registra") && (!accesso || (tipo_errore !=""))){%>
					<!-- BLOCCO REGISTRAZIONE -->
					<form method="POST" action="http://localhost:8080/Servlet/registra" >
						<p><b>REGISTRAZIONE</b></p>
						<hr>
						<%switch(classe_errore){
							case "sql":%>
						<div class="alert alert-danger alert-dismissible fade show d-flex justify-content-center" role="alert">
						    EMAIL' GIA REGISTRATA!
	      				</div>
						<%break;
							case "regex":%>
								<div class="alert alert-danger alert-dismissible fade show d-flex justify-content-center" role="alert">
							    ERRORE NELL'INSERIMENTO DEL PARAMETRO: <%=tipo_errore.toUpperCase()%>
		      				</div>
						<%break;} %>
						<div class="form-group">
							<label for="nome">Nome:</label>
							<input type="text" class="form-control" id="nome" name="nome" required>
						</div>
						<div class="form-group">
							<label for="cognome">Cognome:</label>
							<input type="text" class="form-control" id="cognome" name="cognome" required>
						</div>
						<div class="form-group">
							<label for="email">Email:</label>
							<input type="email" class="form-control" id="email" name="email" required>
						</div>
						<div class="form-group">
							<label for="password">Password:</label>
							<input type="password" class="form-control" id="password" name="password" placeholder="almeno 4 caratteri, una lettera maiuscola ed una minuscola" required>
						</div>
						<div class="bottoni">
							<button type="submit" class="btn btn-primary button_a">Registrati</button>
						</div>
						<hr>
					</form>
					<div class="bottoni">
						<div class="d-flex flex-column align-items-center">
							<p>Hai gia' un account?</p>
							<button type="button" class="btn btn-primary button_a" ><a href="http://localhost:8080/Servlet/index.jsp?page=<%=stringToBase64("accedi")%>">Accedi</a></button>
						</div>
					</div>
				<%} else if(pagina.equals("accedi") && !accesso){ %>
					<!-- BLOCCO ACCEDI -->
					<form method="POST" action="http://localhost:8080/Servlet/accedi">
						<p><b>Accesso</b></p>
						<hr>
						<%switch(classe_errore){
							case "empty":%>
						<div class="alert alert-danger alert-dismissible fade show d-flex justify-content-center" role="alert">
						    ACCOUNT NON TROVATO
	      				</div>
						<%break;} %>
						<div class="form-group">
							<label for="email">Email:</label>
							<input type="email" class="form-control" id="email" name="email" required>
						</div>
						<div class="form-group">
							<label for="password">Password:</label>
							<input type="password" class="form-control" id="password" name="password" required>
						</div>
						<div class="bottoni">
							<button type="submit" class="btn btn-primary">Accedi</button>
						</div>
						<hr>
						</form>
					<div class="bottoni">
						<div class="d-flex flex-column align-items-center">
							<p>Non hai ancora un account?</p>
							<button type="button" class="btn btn-primary button_a" ><a href="http://localhost:8080/Servlet/index.jsp?page=<%=stringToBase64("registra")%>">Registrati</a></button>
						</div>
					</div>
				<% }else if(pagina.equals("profilo") && accesso){%>
			        <!-- BLOCCO GESTIONE PROFILO -->
			        <form method="POST" action="http://localhost:8080/Servlet/modifica">
			        	<p><b>Modifica Profilo Utente</b></p>
			        	<hr>
			            <div class="form-group">
			                <label for="nome">Nome:</label>
			                <input type="text" class="form-control" id="nome" name="nome" value="<%=nome_sess%>" required>
			            </div>
			            <div class="form-group">
			                <label for="cognome">Cognome:</label>
			                <input type="text" class="form-control" id="cognome" name="cognome" value="<%=cognome_sess%>" required>
			            </div>
			            <div class="form-group">
			                <label for="password">Password:</label>
			                <input type="password" class="form-control" id="password" name="password" value="<%=password_sess%>" required>
			            </div>
			            <div class="form-group" hidden>
							<label for="email">Email:</label>
							<input type="email" class="form-control" id="email" name="email" value="<%=email_sess%>" required>
						</div>
			            <div class="bottoni">
							<button type="submit" class="btn btn-primary">Salva modifiche</button>
						</div>
			        </form>
			        <div class="bottoni btn-group dropup">
						<div class="spazio"></div>
						<div class="dropdown-menu dropdown-menu-right" aria-labelledby="conferma_elimina">
							<p class="conferma">SEI SICURO?</p>
							<a class="dropdown-item" href="${pageContext.request.contextPath}/elimina">Elimina account</a>
						</div>
						<button type="button" class="btn btn-danger button_a" data-toggle="dropdown" aria-controls="conferma_elimina" aria-expanded="false" aria-label="Toggle navigation">
							<a href="#">Elimina</a>
						</button>
					</div>
				<% }else if(pagina.equals("entrato") || accesso){%>
					<!-- BLOCCO DI BENTORNATO/BENVENUTO -->
				    <div class="col mx-auto text-center">
				      <h1>Ciao <%=nome_sess%>!</h1>
				      <p class="lead">Grazie per aver effettuato l'accesso a SIGLOG.</p>
				      <p class="lead">
				      	Siamo entusiasti di averti tra i nostri utenti <br> 
				      	e siamo qui per aiutarti a sfruttare al massimo Siglog.<br>
				      	Ti auguriamo una buona permanenza e speriamo di rendere la tua esperienza con noi unica ed emozionante.<br>
				      	<br>	
						Con il tuo nuovo account, potrai accedere a contenuti personalizzati e servizi esclusivi.<br>
						Ti invitiamo ad esplorare Siglog<br>
						e a trovare tutte le funzionalita` che si adattano meglio alle tue esigenze.<br>
						Inizia subito ad usare il tuo account per accedere ai nostri servizi personalizzati.<br>
						<br>
						Se hai bisogno di aiuto o hai domande sul nostro sito o sui servizi che offriamo,<br>
						non esitare a contattarci.<br>
						Il nostro team di supporto e` a tua disposizione per rispondere a tutte le tue domande<br>
						e aiutarti in ogni modo possibile.<br>
						<br>
						Grazie ancora per aver scelto di unirti a noi.<br>
						Siamo impazienti di vedere cosa puoi fare con il tuo account<br>
						e di averti tra i nostri utenti piu' attivi ed entusiasti.<br>
						Buon divertimento!</p>
				      <button type="button" class="btn btn-primary button_a" ><a href="http://localhost:8080/Servlet/esci">Esci</a></button>
				    </div>
				<%} else{%>
					<!-- BLOCCO HOME -->
					<div class="col mb-4 mx-auto text-center">
						<div class="mt-4 d-flex flex-row justify-content-center">
							<h1>Benvenuto in SIGLOG</h1>
					      	<img width="30px" height="30px" src="${pageContext.request.contextPath}/logo" alt="Logo">
					    </div>
					    <br>
				      <p class="lead">Ciao e benvenuto nella nostra piattaforma</p>
				      <p class="lead">
				      	Immagina di poter accedere a contenuti personalizzati e servizi esclusivi,<br>
				      	tutti al tuo servizio 24 ore al giorno. <br>
				      	Con Siglog, tutto questo e` possibile!<br>  
				      	<br>
				      	Inizia il tuo viaggio iscrivendoti.<br>
				      	Fornisci il tuo nome e l'indirizzo email per cominciare e scegli una password sicura,<br>
				      	cosi` potrai accedere facilmente ogni volta che vuoi.<br>
				      	<br>
						Una volta completata la registrazione, potrai finalmente accedere a Siglog.<br>
						Qui, potrai sfruttare appieno tutte le nostre funzionalita` personalizzate e le informazioni in esclusiva.<br>
						Immagina di poter gustare la bellezza delle nostre funzionalita`,<br>
						magari ascoltando una playlist personalizzata mentre sei seduto in un ambiente confortevole a casa.<br>
						O magari potresti usare la tua app preferita mentre sei immerso in una vasca da bagno calda e rilassante.<br>
						La scelta e` tua.<br>
						<br>
						E se deciderai che Siglog non fa al caso tuo, non c'e` problema: <br>
						puoi facilmente eliminare il tuo account seguendo le istruzioni sul sito. <br>
						Tuttavia, siamo certi che una volta provato, non potrai piu` farne a meno. <br>
						<br>
						Ti invitiamo a unirti a noi per un'esperienza unica e indimenticabile, <br>
						che non vedrai l'ora di condividere con i tuoi amici. Ti aspettiamo!</p>
				      <button type="button" class="btn btn-primary button_a" ><a href="http://localhost:8080/Servlet/index.jsp?page=<%=stringToBase64("registra")%>">Registrati</a></button>
				    </div>
				<%} %>
				
			</div>
			<div class="col"></div> <!-- col per spazio -->
		</div>
	</div>
	<!-- Footbar -->
</div>
<footer class="footer bg-light mt-auto py-3" style="background-image:url('${pageContext.request.contextPath}/nav');">
	<div class="container text-center">
		<span class="text-muted">&copy; 2023 SigLog. Tutti i diritti riservati.</span>
	</div>
</footer>

<!-- js Bootstrap -->
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>

</body>
</html>

