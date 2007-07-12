<script type="text/javascript">
    

    function CheckEmail(email) {

    var firstchunk,indx,secondchunk

    if (email == "") {
        alert("Please make sure you have entered a valid email before submitting the info.")

        return false
    }

    //get the zero-based index of the "@" character
    indx = email.indexOf("@")

    //if the string does not contain an @ then then return true
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
    // contained a "." char

    return true

    }//CheckEmail

    


     requiredFields = new Array( "paramRequestResearchProject",
                                  "paramRequestPublicationAim");
                                  
     requiredText   = new Array(  "Research Project",
                                  "Publication Aim");
                                  
     requiredFieldsUser = new Array("paramUserNewFirstName",
                                    "paramUserNewLastName",
                                    "paramUserNewEmail");  
                                    
     requiredFieldsUserText = new Array("First name",
                                        "Last name",
                                        "Email");       
                     

    function validateForm ( form )
    {

    if (document.resource_request_form.paramUserOldUserName.value == '') {
       
       for ( var i = 0; i < requiredFieldsUser.length; i++ ) {
          element = requiredFieldsUser[i];
          element_text = requiredFieldsUserText[i];
          if ( form[element].value == "" ) {
             alert( "Please enter a value for '" + element_text + "'." );
             form[element].focus();
             return false;
          }
       }

       if (! CheckEmail(form["paramUserNewEmail"].value)) {
          form["paramUserNewEmail"].focus();
          return false;
       }   
 
    } else {
       if (document.resource_request_form.paramUserOldPassword.value == '') {
         alert( "Please enter a value for '" + "Password" + "'." );
         document.resource_request_form.paramUserOldPassword.focus();
         return false;
       }
    }
    
    for ( var i = 0; i < requiredFields.length; i++ ) {
        element = requiredFields[i];
        element_text = requiredText[i];
        if ( form[element].value == "" ) {
           alert( "Please enter a value for '" + element_text + "'." );
           form[element].focus();
           return false;
        }
     }
    
     if (document.resource_request_form.paramRequestFromDateDay.options[document.resource_request_form.paramRequestFromDateDay.selectedIndex].value == 'Choose') {
        alert( "Please enter a value for '" + "From Date Day" + "'." );
        document.resource_request_form.paramRequestFromDateDay.focus();
        return false;
     }
                                        
     if (document.resource_request_form.paramRequestFromDateMonth.options[document.resource_request_form.paramRequestFromDateMonth.selectedIndex].value == 'Choose') {
        alert( "Please enter a value for '" + "From Date Month" + "'." );
        return false;
     }
     
     if (document.resource_request_form.paramRequestFromDateYear.options[document.resource_request_form.paramRequestFromDateYear.selectedIndex].value == 'Choose') {
        alert( "Please enter a value for '" + "From Date Year" + "'." );
        return false;
     }
     
     if (document.resource_request_form.paramRequestToDateDay.options[document.resource_request_form.paramRequestToDateDay.selectedIndex].value == 'Choose') {
        alert( "Please enter a value for '" + "To Date Day" + "'." );
        return false;
     }
     
     if (document.resource_request_form.paramRequestToDateMonth.options[document.resource_request_form.paramRequestToDateMonth.selectedIndex].value == 'Choose') {
        alert( "Please enter a value for '" + "To Date Month" + "'." );
        return false;
     }
                                         
     if (document.resource_request_form.paramRequestToDateYear.options[document.resource_request_form.paramRequestToDateYear.selectedIndex].value == 'Choose') {
        alert( "Please enter a value for '" + "To Date Year" + "'." );
        return false;
     }
    
    
    
    return true;
    }

    // -->
</script>