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



