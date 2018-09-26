var slideIndex = 0;
showSlides();

function showSlides() {
    var i;
    var slides = document.getElementsByClassName("mySlides");
    var dots = document.getElementsByClassName("dot");
    for (i = 0; i < slides.length; i++) {
        slides[i].style.display = "none";
    }
    slideIndex++;
    if (slideIndex > slides.length) {slideIndex = 1}
    for (i = 0; i < dots.length; i++) {
        dots[i].className = dots[i].className.replace(" active", "");
    }
    slides[slideIndex-1].style.display = "block";
    dots[slideIndex-1].className += " active";
    setTimeout(showSlides, 3000); // Change image every 2 seconds
}

function validateFeedback(){
    if(frm.name.value == ""){
        alert("Enter your name");
        frm.name.focus();
        return false;
    }
    if((frm.name.value).length > 20){
        alert("Too long name");
        frm.name.focus();
        return false;
    }

    if (frm.email.value == 0) {
        alert("Enter your email");
        frm.email.focus();
        return false;
    }
    var regexp = /^([a-z0-9.-]+)@([a-z0-9.-]+).([a-z.]{2,6})$/;
    if (!regexp.test(frm.email.value)) {
        alert("Wrong email");
        frm.email.focus();
        return false;
    }

    if(frm.message.value == ""){
        alert("Enter message");
        frm.message.focus();
        return false;
    }
    if((frm.message.value).length < 20){
        alert("Too short message");
        frm.message.focus();
        return false;
    }
    return true;
}