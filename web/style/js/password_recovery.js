Notification.requestPermission(newMessage);

function showMessage() {
    var mailNotification = new Notification("Андрей Чернышёв", {
        tag: "ache-mail",
        body: "Привет, высылаю материалы по проекту...",
        icon: "http://habrastorage.org/storage2/cf9/50b/e87/cf950be87f8c34e63c07217a009f1d17.jpg"
    });
}


function validateEmail() {
    if (frm.email.value == 0) {
        alert("Enter your email");
        frm.email.focus();
        return false;
    }
    var regexp = /^([a-z0-9.-]+)@([a-z0-9.-]+).([a-z.]{2,6})$/;
    if (!regexp.test(frm.email.value)) {
        alert("Wrong email.\n Example: example@example.com");
        frm.email.focus();
        return false;
    }
    return true;
}

function validateKey(){
    if(frm2.key.value == ""){
        alert("Enter your key");
        frm2.key.focus();
        return false;
    }
    return true;
}