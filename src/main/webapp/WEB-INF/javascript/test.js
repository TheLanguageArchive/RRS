function checkPassword (password) {
    
    if (password == "") {
        alert("You didn't enter a password.");
        return false;
    }    
    
    var illegalChars = /[\W_]/; // allow only letters and numbers
    if ((password.length < 6) || (password.length > 8)) {
        alert("The password should have a length of 6-8 characters.");
        return false;
    } 
    else if (illegalChars.test(password)) {
        alert("The password contains illegal characters. Use only letters and numbers.");
        return false;
    }    
    else if (!(password.search(/[0-9]+/) > -1)) {
        alert("The password must contain at least one numeral.");
        return false;
    }   
    else if (!(password.search(/[A-Za-z]+/) > -1)) {
        alert("The password must contain at least one letter.");
        return false;
    }   
    
    return true;
}