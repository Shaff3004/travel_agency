<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: glite
  Date: 17.01.2018
  Time: 01:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<html>
<head>
    <title>Change</title>
    <link rel="stylesheet" type="text/css" href="../../../style/css/w3.css">
    <link rel="stylesheet" type="text/css" href="../../../style/css/change.css"/>
</head>
<body>
<div class="w3-bar w3-white w3-border-bottom w3-xlarge">
    <form action="controller" method="post">
        <input type="hidden" name="command" value="logout">
        <input class="w3-bar-item w3-button w3-right w3-hover-red w3-text-grey" type="submit" name="<fmt:message key="navBar.logout"/>"
               value="<fmt:message key="navBar.logout"/>" onclick="clearStorage()">
    </form>
</div>
<header>


    <form name="frm" class="w3-display-middle" action="controller" method="post">
        <fieldset>
            <h1><fmt:message key="admin.tours.button.change"/></h1>
            <br>

            <table class="table">
                <%--<tr>
                    <th>ID</th>
                    <th>Country</th>
                    <th>City</th>
                    <th>Type</th>
                    <th>Persons</th>
                    <th>Hotel</th>
                    <th>Date</th>
                    <th>Price</th>

                </tr>--%>
                    <tabH:tableHeading values="heading.ID,heading.Country,heading.City,heading.Type,heading.Persons,heading.Hotel,heading.Date,heading.Price"
                                       lang="${language}"/>
                <tr>
                    <td>${requestScope.currentTour.id}</td>
                    <td>${requestScope.currentTour.country}</td>
                    <td>${requestScope.currentTour.city}</td>
                    <td>${requestScope.currentTour.type}</td>
                    <td>${requestScope.currentTour.persons}</td>
                    <td>${requestScope.currentTour.hotel}</td>
                    <td>${requestScope.currentTour.date}</td>
                    <td>${requestScope.currentTour.price}</td>
                </tr>
            </table>

            <div class="w3-row-padding form-left-column">

                <div class="w3-half2">
                    <label><fmt:message key="admin.tours.add.type"/>
                        <select class="w3-input w3-border" name="type">
                            <option class="default-option" disabled selected value><fmt:message key="admin.tours.add.default"/>
                            </option>
                            <c:forEach var="type" items="${requestScope.tourTypes}">
                                <option>${type}</option>
                            </c:forEach>
                        </select>
                    </label>
                </div>
                <div class="w3-half2">
                    <label><fmt:message key="admin.tours.add.hotel"/>
                        <select class="w3-input w3-border" name="hotel">
                            <option class="default-option" disabled selected value><fmt:message key="admin.tours.add.default"/>
                            </option>
                            <c:forEach var="hotel" items="${requestScope.hotelTypes}">
                                <option>${hotel}</option>
                            </c:forEach>
                        </select>
                    </label>
                </div>

                <div class="w3-half2">
                    <label><fmt:message key="admin.tours.add.price"/>
                        <input class="w3-input w3-border" type="text" placeholder="<fmt:message key="admin.tours.price.placeholder"/>"
                               name="price">
                    </label>
                </div>

            </div>
            <div class="form-right-column myInput">


                <div class="w3-half2">
                    <label><fmt:message key="admin.tours.add.persons"/>
                        <select class="w3-input w3-border" name="persons">
                            <option class="default-option" disabled selected value><fmt:message key="admin.tours.add.default"/>
                            </option>
                            <option>1</option>
                            <option>2</option>
                            <option>3</option>
                            <option>4</option>
                            <option>5</option>
                        </select>
                    </label>
                </div>

                <div class="w3-half2">
                    <label><fmt:message key="admin.tours.add.date"/>
                        <input class="w3-input w3-border" type="date" placeholder="When"
                               name="date">
                    </label>
                </div>


            </div>

            <input type="hidden" name="command" value="change_tour">
            <input type="hidden" name="tourID" value="${requestScope.currentTour.id}">
            <input class="w3-button w3-dark-grey button-right" type="submit" value="<fmt:message key="admin.tours.button.change"/>" onclick="return validateForm()">
            <%--<button class="w3-button w3-dark-grey w3-right w3-hover-red button-right" type="button" href="controller?command=go_home">Back</button>--%>
            <a class="w3-button w3-dark-grey w3-right w3-hover-red button-right" href="controller?command=go_home"><fmt:message key="button.back"/></a>

        </fieldset>

    </form>


</header>

<script src="../../../style/js/change_page.js"></script>
</body>
</html>
