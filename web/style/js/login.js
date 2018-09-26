function hidePassword() {
    var x = document.getElementById("password");
    if (x.type === "password") {
        x.type = "text";
    } else {
        x.type = "password";
    }
}


function validateForm(){
    if(validateLogin()){
        if(validatePassword()){
            return true;
        }
    }
    return false;
}


function validateLogin() {
    if (frm.login.value == "") {
        alert("Enter login");
        frm.login.focus();
        return false;
    }

    if((frm.login.value).length < 4){
        alert("Login must be minimum 4 characters");
        frm.login.focus();
        return false;
    }
    if((frm.login.value).length > 16){
        alert("Login must be maximum 20 characters");
        frm.login.focus();
        return false;
    }

    var regexp = /^[a-zA-Z]+$/;
    if(!regexp.test(frm.login.value)){
        alert("Login must have only a latin alphabet");
        frm.login.focus();
        return false;
    }
    return true;
}


function validatePassword(){
    if(frm.password.value ==""){
        alert("Enter the password.");
        frm.password.focus();
        return false;
    }

    if((frm.password.value).length < 8){
        alert("Password should be minimum 8 characters");
        frm.password.focus();
        return false;
    }
    if((frm.password.value).length > 16){
        alert("Password should be maximum 16 characters");
        frm.password.focus();
        return false;
    }

    var regexp = /^[a-zA-Z0-9]+$/;
    if(!regexp.test(frm.password.value)){
        alert("Password must have only a latin alphabet and digits");
        frm.login.focus();
        return false;
    }
    if(frm.confirmPass.value == ""){
        alert("Enter the confirmation password.");
        frm.confirmPass.focus();
        return false;
    }

    if((frm.password.value).length != (frm.confirmPass.value).length){
        alert("Length of password doesn't coincide with confirm password length");
        frm.confirmPass.focus();
        return false;
    }
    if(frm.password.value != frm.confirmPass.value){
        alert("Password confirmation does not match.");
        frm.confirmPass.focus();
        return false;
    }

    return true;
}