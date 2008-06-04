<!-- 
validateRegis.js
requires functions.js
-->

<script type="text/javascript">
<!--

requiredFieldsUser = new Array("paramUserNewUserName",
    "paramUserNewFirstName",
    "paramUserNewLastName",
    "paramUserNewEmail",
    "paramUserNewOrganization",
    "paramUserNewPassword_1",
    "paramUserNewPassword_2");  

requiredFieldsUserText = new Array("User ID",
    "First name",
    "Last name",
    "Email",
    "Organisation",
    "Password",
    "Password (verification)");       


function validateForm ( form )
{  
    for ( var i = 0; i < requiredFieldsUser.length; i++ ) {
        element = requiredFieldsUser[i];
        element_text = requiredFieldsUserText[i];
        if ( form[element].value == "" ) {
            alert( "Please enter a value for '" + element_text + "'." );
            form[element].focus();
            return false;
        }
    }     
    
    //if (document.resource_request_registration_form.paramUserNewPassword_1.value != document.resource_request_registration_form.paramUserNewPassword_2.value) {
    if (form["paramUserNewPassword_1"].value != form["paramUserNewPassword_2"].value) {
        alert( "Password verification error!");
        form["paramUserNewPassword_1"].focus();
        //document.resource_request_registration_form.paramUserNewPassword_1.focus();
        return false;
    }
    
    if (! checkEmail(form["paramUserNewEmail"].value)) {
        form["paramUserNewEmail"].focus();
        return false;
    }   
    
    if (! checkPassword(form["paramUserNewPassword_1"].value)) {
        form["paramUserNewPassword_1"].focus();
        return false;
    }   
    
    
    
    return true;
}
// -->
</script>