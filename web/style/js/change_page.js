function validateForm() {
    if (validate()) {
        if (confirm("Are you sure?")) {
            return true;
        }
    }
    return false;
}


function validate() {
    if (frm.type.value == 0) {
        alert("Choose tour type");
        frm.type.focus();
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
    if (frm.price.value < 0){
        alert("Incorrect price");
        frm.price.focus();
        return false;
    }

    if(frm.price.value > 100000){
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

    if (frm.persons.value == 0) {
        alert("Choose peoples");
        frm.persons.focus();
        return false;
    }


    if (frm.date.value == 0) {
        alert("Choose date");
        frm.date.focus();
        return false;
    }
    return true;
}