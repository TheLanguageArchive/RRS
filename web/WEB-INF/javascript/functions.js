<!-- 
functions.js
-->

<script type="text/javascript">
<!--

function checkEmail(email) {
    
    var firstchunk,indx,secondchunk
    
    if (email == "") {
        alert("Please make sure you have entered a valid email before submitting the info.")
        
        return false
    }
    
    //get the zero-based index of the "@" character
    
    indx = email.indexOf("@")
    
    //if the string does not contain an @ then return true
    
    if (indx == -1 ) {
        
        alert("Please make sure you have entered a valid email (with @-sign) before submitting the info.")
        
        return false
    }
    
    
    //if the first part of email is < 2 chars and second part < 7 chars
    //(arbitrary but workable criteria) then reject the input address
    
    firstchunk = email.substr(0,indx) //up to but not including the "@"
    
    
    //start at char following the "@" and include up to end of email addr
    
    secondchunk = email.substr(indx + 1)
    
    
    //if the part  following the "@" does not include a period "." then
    //also return false
    
    if ((firstchunk.length < 2 ) || (secondchunk.length < 6) ||
        (secondchunk.indexOf(".") == -1)){
    
    alert("Please make sure you have entered a valid email before submitting the info. ")
    
    return false
}


  //the email was okay; at least it had a @, more than 1 username chars,
  //more than 5 chars after the "@", and the substring after the "@"
  //contained a "." char

return true

}

function checkPassword (password) {
    
    if (password == "") {
        alert("You didn't enter a password.");
        return false;
    }    
    
    var illegalChars = /[\W_]/; // allow only letters and numbers
    if ((password.length < 6) || (password.length > 20)) {
        alert("The password should have a length of 6-20 characters.");
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



// -->
</script>