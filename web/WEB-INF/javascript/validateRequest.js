<!-- 
validateRequest.js
-->

<script type="text/javascript">
<!-- 

     requiredFields = new Array( "paramUserOldUserName",
                                 "paramUserOldPassword",
                                 "paramRequestResearchProject",
                                 "paramRequestPublicationAim");
                                  
     requiredText   = new Array(  "User ID",
                                  "Password",
                                  "Research Project",
                                  "Publication Aim");
                                  
                     

    function validateForm ( form )
    {

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