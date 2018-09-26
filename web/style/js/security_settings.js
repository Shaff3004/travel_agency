function validateEmail(){
    if (frm.email.value == 0) {
        alert("Enter new email");
        frm.email.focus();
        return false;
    }
    var regexp = /^([a-z0-9.-]+)@([a-z0-9.-]+).([a-z.]{2,6})$/;
    if (!regexp.test(frm.email.value)) {
        alert("Wrong email");
        frm.email.focus();
        return false;
    }
    if(confirm("Are you sure?")){
    return true;
    }
}

function validatePassword(){
    if (frm2.oldPass.value == "") {
        alert("Enter your password.");
        frm2.oldPass.focus();
        return false;
    }

    if ((frm2.oldPass.value).length < 8) {
        alert("Incorrect password");
        frm2.oldPass.focus();
        return false;
    }
    if ((frm2.oldPass.value).length > 16) {
        alert("Incorrect password");
        frm2.oldPass.focus();
        return false;
    }

    var regexp = /^[a-zA-Z0-9]+$/;
    if (!regexp.test(frm2.oldPass.value)) {
        alert("Password has only a latin alphabet and digits");
        frm2.oldPass.focus();
        return false;
    }
    if (frm2.newPass.value == "") {
        alert("Enter new password.");
        frm2.newPass.focus();
        return false;
    }
    if ((frm2.newPass.value).length < 8) {
        alert("Incorrect password");
        frm2.newPass.focus();
        return false;
    }
    if ((frm2.newPass.value).length > 16) {
        alert("Incorrect password");
        frm2.newPass.focus();
        return false;
    }
    if (!regexp.test(frm2.newPass.value)) {
        alert("Password must has only a latin alphabet and digits");
        frm2.newPass.focus();
        return false;
    }


    if(frm2.confirmPass.value == ""){
        alert("Enter confirm password");
        frm2.confirmPass.focus();
        return false;
    }
    if ((frm2.newPass.value).length != (frm2.confirmPass.value).length) {
        alert("Length of password doesn't coincide with confirm password length");
        frm2.confirmPass.focus();
        return false;
    }
    if (frm2.newPass.value != frm2.confirmPass.value) {
        alert("Password confirmation does not match.");
        frm2.confirmPass.focus();
        return false;
    }
    if(confirm("Are you sure?")){
        return true;
    }

}