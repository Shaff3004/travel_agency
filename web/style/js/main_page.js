function resend() {
    document.second.secondInput.value = document.first.firstInput.value;
}

function openLink(evt, linkName) {
    var i, x, tablinks;
    x = document.getElementsByClassName("myLink");
    for (i = 0; i < x.length; i++) {
        x[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablink");
    for (i = 0; i < x.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" w3-red", "");
    }
    document.getElementById(linkName).style.display = "block";
    evt.currentTarget.className += " w3-red";
}


//click on the last button on reload
if (sessionStorage.getItem("button_number") == null) {
    document.getElementsByClassName("tablink")[0].click();
} else {
    document.getElementsByClassName("tablink")[sessionStorage.getItem("button_number")].click();
}

//function that remember pressed button on reload
function rememberButton(number) {
    sessionStorage.setItem("button_number", number);
}

function rememberFieldSet(number) {
    sessionStorage.setItem("fieldSet_number", number);
}

if (sessionStorage.getItem("fieldSet_number") != null) {
    document.getElementsByClassName("spoiler")[sessionStorage.getItem("fieldSet_number")].click();
}


//clear local storage before logout
function clearStorage() {
    sessionStorage.removeItem("button_number");
    sessionStorage.removeItem("fieldSet_number");
}


function validateAddTourForm() {
    if (frm2.country.value == 0) {
        alert("Choose country");
        frm2.country.focus();
        return false;
    }
    if (frm2.city.value == 0) {
        alert("Choose city or region");
        frm2.city.focus();
        return false;
    }
    if (frm2.type.value == 0) {
        alert("Choose tour type");
        frm2.type.focus();
        return false;
    }
    if (frm2.hotel.value == 0) {
        alert("Choose hotel type");
        frm2.hotel.focus();
        return false;
    }
    if (frm2.persons.value == 0) {
        alert("Choose number of persons");
        frm2.persons.focus();
        return false;
    }
    if (frm2.date.value == 0) {
        alert("Choose date");
        frm2.date.focus();
        return false;
    }
    if (frm2.price.value == 0) {
        alert("Enter price");
        frm2.price.focus();
        return false;
    }
    if (frm2.price.value <= 0) {
        alert("Price cannot be less than zero");
        frm2.price.focus();
        return false;
    }
    if (frm2.price.value > 100000) {
        alert("Price cannot be more than 100000");
        frm2.price.focus();
        return false;
    }
    var regexp = /^\d+$/;
    if (!regexp.test(frm2.price.value)) {
        alert("Incorrect price");
        frm2.price.focus();
        return false;
    }
    if (confirm("Are you sure?")) {
        return true;
    }
    return false;
}

function validateSearchForm() {


    if (frm.type.value == 0) {
        alert("Choose tour type");
        frm.type.focus();
        return false;
    }

    if (frm.peoples.value == 0) {
        alert("Choose peoples");
        frm.peoples.focus();
        return false;
    }
    if (frm.hotel.value == 0) {
        alert("Choose hotel type");
        frm.hotel.focus();
        return false;
    }


    if (frm.price.value == 0) {
        alert("Enter price");
        frm.price.focus();
        return false;
    }
    if (frm.price.value < 0) {
        alert("Incorrect price");
        frm.price.focus();
        return false;
    }
    if (frm.price.value > 100000) {
        alert("Incorrect price");
        frm.price.focus();
        return false;
    }
    var regexp = /^\d+$/;
    if (!regexp.test(frm.price.value)) {
        alert("Incorrect price");
        frm.price.focus();
        return false;
    }
    return true;
}


function confirmAction() {
    confirm("Are you sure?");
}

function updateTextInput(val) {
    document.getElementById('textInput').value = val;
}


function throwTour(tour) {
    sessionStorage.setItem("tour", tour);

}

function showMessage() {
    var mailNotification = new Notification("Johnny Sins", {
        sound: "default",
        tag: "ache-mail",
        body: "Привет, высылаю материалы по проекту...",
        icon: "style/img/sins.jpg"
    });


}

document.addEventListener('DOMContentLoaded', function () {
    if (!Notification) {
        alert('Desktop notifications not available in your browser. Try Chromium.');
        return;
    }

    if (Notification.permission !== "granted")
        Notification.requestPermission();
});

function notifyMessage() {

    if (Notification.permission !== "granted")
        Notification.requestPermission();
    else {
        var notification = new Notification('Best Tour', {
            icon: 'style/img/notify.jpg',
            body: "Operation successful"
        });

        setTimeout(notification.close().bind(notification), 5000);
    }
}

function blockEmail() {
    var x = document.getElementById('email');
    if (!x.disabled) {
        x.disabled = true;
    } else {
        x.disabled = false;
    }
}

function validateMessage() {
    var x = document.getElementById('email');
    if (!x.disabled) {
        if (form.email.value == 0) {
            alert("Enter email");
            form.email.focus();
            return false;
        }
        var regexp = /^([a-z0-9.-]+)@([a-z0-9.-]+).([a-z.]{2,6})$/;
        if (!regexp.test(form.email.value)) {
            alert("Wrong email");
            form.email.focus();
            return false;
        }
    }

    if (form.message.value == "") {
        alert("Enter message");
        form.message.focus();
        return false;
    }
    if ((form.message.value).length < 20) {
        alert("Too short message");
        form.message.focus();
        return false;
    }
    if (confirm("Are you sure?")) {
        return true;
    } else {
        return false;
    }
}


function getSnackbar() {
    // Get the snackbar DIV
    var x = document.getElementById("snackbar");

    // Add the "show" class to DIV
    x.className = "show";

    // After 3 seconds, remove the show class from DIV
    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 10000);
}


$(document).ready(function()
    {
        $(document.getElementsByClassName("table")).tablesorter();
    }
);

