const stringToBase64 = (str) => //da stringa a base64
{
    //byte[] bytes = str.getBytes();
    //byte[] encoded = Base64.getEncoder().encode(bytes);
    return new String(encoded);
}
const base64ToString = (base64) => //da base64 a stringa
{
    //byte[] decoded = Base64.getDecoder().decode(base64);
    return new String(decoded);
}

const is_logged_in = (nome) =>
{
    if(!nome) 
        return false;
        return true;
}

var dati = [
    { id: 1, nome: "Mario", cognome: "Rossi", eta: 30 },
    { id: 2, nome: "Luigi", cognome: "Verdi", eta: 35 },
    { id: 3, nome: "Anna", cognome: "Bianchi", eta: 25 }
  ];

  function popolaTabella(data) {
    var table = $("#tabella");
    table.empty();
    var headerRow = $("<tr></tr>");
    headerRow.append("<th>ID</th>");
    headerRow.append("<th>Nome</th>");
    headerRow.append("<th>Cognome</th>");
    headerRow.append("<th>Età</th>");
    table.append(headerRow);

    for (var i = 0; i < data.length; i++) {
      var row = $("<tr></tr>");
      row.append("<td>" + data[i].id + "</td>");
      row.append("<td>" + data[i].nome + "</td>");
      row.append("<td>" + data[i].cognome + "</td>");
      row.append("<td>" + data[i].eta + "</td>");
      table.append(row);
    }
  }

  popolaTabella(dati);