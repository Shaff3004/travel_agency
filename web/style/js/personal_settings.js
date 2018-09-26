var dialog = document.querySelector('dialog');
document.querySelector('#show').onclick = function () {
    dialog.showModal(); // открыть модальное диалоговое окно
};

document.querySelector('#close').onclick = function () {
    dialog.close(); // закрыть диалоговое окно
};


function openAvatar() {

    var dialog2 = document.querySelector('#dialog2');
    document.querySelector('#show2').onclick = function () {
        dialog2.showModal();
    };

}

function clickUpload() {
    document.querySelector('#file').click();
    if (confirm("You want to change avatar?")) {
        clickSubmit();
    }
}

function clickSubmit() {
    document.querySelector('#submit').click();
}

function validateCard() {
    if (validateNumber()) {

        if (confirm("Are you sure?")) {
            return true;
        }

    }
    return false;
}

function validateNumber() {
    if (frm.credit_number.value == "") {
        alert("Enter credit card number");
        frm.credit_number.focus();
        return false;
    }
    var regexp = /^[0-9]+$/;
    if (!regexp.test(frm.credit_number.value)) {
        alert("Only digits");
        frm.credit_number.focus();
        return false;
    }
    if ((frm.credit_number.value).length < 15) {
        alert("You entered less than 16 digits");
        frm.credit_number.focus();
        return false;
    }
    if ((frm.credit_number.value).length > 16) {
        alert("Credit card number cannot be longer than 16 symbols");
        frm.credit_number.focus();
        return false;
    }
    if (frm.money.value == "") {
        alert("Enter money");
        frm.money.focus();
        return false;
    }
    if (frm.money.value <= 0) {
        alert("Operation impossible");
        frm.money.focus();
        return false;
    }

    if (frm.money.value > 30000) {
        alert("The amount of one-time payment cannot be more than 30000");
        frm.money.focus();
        return false;
    }
    var regexp2 = /^[0-9]+$/;
    if (!regexp2.test(frm.money.value)) {
        alert("Only digits");
        frm.money.focus();
        return false;
    }

    return true;

}

function validateMoney() {
    if (frm.money.value == "") {
        alert("Enter money");
        frm.money.focus();
        return false;
    }
    if (frm.money.value < 0) {
        alert("Operation impossible");
        frm.money.focus();
        return false;
    }

    if (frm.money.value > 30000) {
        alert("The amount of one-time payment cannot be more than 30000");
        frm.money.focus();
        return false;
    }
    var regexp2 = /^[0-9]+$/;
    if (!regexp2.test(frm.money.value)) {
        alert("Only digits");
        frm.money.focus();
        return false;
    }
    return true;

}