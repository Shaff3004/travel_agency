<%--
  Created by IntelliJ IDEA.
  User: glite
  Date: 03.02.2018
  Time: 16:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<html>
<head>
    <title>About Us</title>
    <link rel="stylesheet" type="text/css" href="../../../style/css/w3.css">
    <link rel="stylesheet" type="text/css" href="../../../style/css/about_us.css">
</head>
<body>
<div class="w3-bar w3-white w3-border-bottom w3-xlarge">
    <form action="controller" method="post">
        <a class="w3-bar-item w3-button w3-text-red w3-hover-red" href="controller?command=go_home"><fmt:message
                key="personalSettings.home"/></a>
        <input type="hidden" name="command" value="logout">
        <input class="w3-bar-item w3-button w3-right w3-hover-red w3-text-grey" type="submit"
               name="<fmt:message key="navBar.logout"/>"
               value="<fmt:message key="navBar.logout"/>" onclick="clearStorage()">
    </form>
</div>

<header>

    <!-- First Grid -->
    <div class="w3-row-padding w3-padding-64 w3-container">
        <div class="w3-content">
            <div class="w3-twothird">
                <h1>Best tour</h1>
                <h5 class="w3-padding-32"><fmt:message key="aboutUs.info1"/></h5>

                <p class="w3-text-grey"><fmt:message key="aboutUs.info19"/>
                </p>
            </div>

            <div class="w3-third w3-center">
                <i class="fa fa-anchor w3-padding-64 w3-text-red"></i>
            </div>
        </div>
    </div>
    <div class="slideshow-container">

        <div class="mySlides fade">
            <div class="numbertext">1 / 7</div>
            <img src="../../../style/img/open_wolrd.jpg" style="width:100%">
            <div class="text"><fmt:message key="aboutUs.info3"/></div>
        </div>

        <div class="mySlides fade">
            <div class="numbertext">2 / 7</div>
            <img src="../../../style/img/img_mountains.jpg" style="width:100%">
            <div class="text"><fmt:message key="aboutUs.info2"/></div>
        </div>

        <div class="mySlides fade">
            <div class="numbertext">3 / 7</div>
            <img src="../../../style/img/egypt.jpg" style="width:100%">
            <div class="text"><fmt:message key="aboutUs.info5"/></div>
        </div>

        <div class="mySlides fade">
            <div class="numbertext">4 / 7</div>
            <img src="../../../style/img/maldives-island.jpg" style="width:100%">
            <div class="text"><fmt:message key="aboutUs.info4"/></div>
        </div>

        <div class="mySlides fade">
            <div class="numbertext">5 / 7</div>
            <img src="../../../style/img/fjord.jpg" style="width:100%">
            <div class="text"><fmt:message key="aboutUs.info6"/></div>
        </div>

        <div class="mySlides fade">
            <div class="numbertext">6 / 7</div>
            <img src="../../../style/img/starsky.jpg" style="width:100%">
            <div class="text"><fmt:message key="aboutUs.info7"/></div>
        </div>

        <div class="mySlides fade">
            <div class="numbertext">7 / 7</div>
            <img src="../../../style/img/northern_lights.jpg" style="width:100%">
            <div class="text"><fmt:message key="aboutUs.info8"/></div>
        </div>

    </div>
    <br>

    <div style="text-align:center">
        <span class="dot"></span>
        <span class="dot"></span>
        <span class="dot"></span>
        <span class="dot"></span>
        <span class="dot"></span>
        <span class="dot"></span>
        <span class="dot"></span>
    </div>


    <div class="w3-container w3-margin-top">
        <h3><fmt:message key="aboutUs.info9"/></h3>
        <h6><fmt:message key="aboutUs.info10"/></h6>
    </div>
    <div class="w3-row-padding w3-text-white w3-large">
        <div class="w3-half w3-margin-bottom">
            <div class="w3-display-container">
                <img src="../../../style/img/cinqueterre.jpg" alt="Cinque Terre" style="width:100%">
                <span class="w3-display-bottomleft w3-padding">Cinque Terre</span>
            </div>
        </div>
        <div class="w3-half">
            <div class="w3-row-padding" style="margin:0 -16px">
                <div class="w3-half w3-margin-bottom">
                    <div class="w3-display-container">
                        <img src="../../../style/img/newyork2.jpg" alt="New York" style="width:100%">
                        <span class="w3-display-bottomleft w3-padding">New York</span>
                    </div>
                </div>
                <div class="w3-half w3-margin-bottom">
                    <div class="w3-display-container">
                        <img src="../../../style/img/sanfran.jpg" alt="San Francisco" style="width:100%">
                        <span class="w3-display-bottomleft w3-padding">San Francisco</span>
                    </div>
                </div>
            </div>
            <div class="w3-row-padding" style="margin:0 -16px">
                <div class="w3-half w3-margin-bottom">
                    <div class="w3-display-container">
                        <img src="../../../style/img/pisa.jpg" alt="Pisa" style="width:100%">
                        <span class="w3-display-bottomleft w3-padding">Pisa</span>
                    </div>
                </div>
                <div class="w3-half w3-margin-bottom">
                    <div class="w3-display-container">
                        <img src="../../../style/img/paris.jpg" alt="Paris" style="width:100%">
                        <span class="w3-display-bottomleft w3-padding">Paris</span>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <!-- Explore Nature -->
    <div class="w3-container">
        <h3><fmt:message key="aboutUs.info11"/></h3>
        <p><fmt:message key="aboutUs.info12"/></p>
    </div>
    <div class="w3-row-padding">
        <div class="w3-half w3-margin-bottom">
            <img src="../../../style/img/ocean2.jpg" alt="Norway" style="width:100%">
            <div class="w3-container w3-white">
                <h3>West Coast, Norway</h3>
            </div>
        </div>
        <div class="w3-half w3-margin-bottom">
            <img src="../../../style/img/mountains2.jpg" alt="Austria" style="width:100%">
            <div class="w3-container w3-white">
                <h3>Mountains, Austria</h3>
            </div>
        </div>
    </div>

    <!-- Contact -->
    <div class="container">
        <div style="text-align:center">
            <h2><fmt:message key="aboutUs.info13"/></h2>
            <p><fmt:message key="aboutUs.info14"/></p>
        </div>
        <div class="row">
            <div class="column">
                <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d1282.143158883253!2d36.24661065818395!3d50.005988494854115!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x4127a0dca77e6d1d%3A0x282269afa6ccb9c6!2z0J7QsdGJ0LXQttC40YLQuNC1IOKEljUg0J3QotCjICLQpdCf0JgiLCDRg9C7LiDQn9GD0YjQutC40L3RgdC60LDRjywgNzkvNSwg0KXQsNGA0YzQutC-0LIsINCl0LDRgNGM0LrQvtCy0YHQutCw0Y8g0L7QsdC70LDRgdGC0YwsIDYxMDAw!5e0!3m2!1sru!2sua!4v1517674321936"
                        width="100%" height="500" frameborder="0" style="border:0" allowfullscreen></iframe>
            </div>
            <div class="column">
                <form action="controller" method="post" name="frm">
                    <label for="fname"><fmt:message key="aboutUs.info15"/></label>
                    <input type="text" id="fname" name="name" placeholder="<fmt:message key="aboutUs.info15"/>..">
                    <label for="email"><fmt:message key="aboutUs.info16"/></label>
                    <input type="text" id="email" name="email" placeholder="<fmt:message key="aboutUs.info16"/>..">
                    <label for="subject"><fmt:message key="aboutUs.info17"/></label>
                    <textarea id="subject" name="message" placeholder="<fmt:message key="aboutUs.info18"/>.."
                              style="height:170px"></textarea>
                    <input type="hidden" name="command" value="feedback">
                    <input class="w3-button w3-dark-grey" type="submit" onclick="return validateFeedback();"
                           value="<fmt:message key="recovery.button.send"/>">
                </form>
            </div>
        </div>
    </div>
</header>
<footer class="w3-container w3-center w3-opacity w3-margin-bottom">
    <p>Powered by <a target="_blank" class="w3-hover-text-green">Serhii Volodin</a></p>
</footer>
<script src="../../../style/js/about_us.js"></script>
</body>
</html>
