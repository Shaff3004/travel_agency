<%--
  Created by IntelliJ IDEA.
  User: glite
  Date: 04.01.2018
  Time: 13:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="WEB-INF/jspf/directive/taglib.jspf"%>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="resources" />
<html>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="style/css/w3.css">
<link rel="stylesheet" href="style/css/registration_page.css">
<head>
    <title>Registration</title>
</head>
<body>
<div align="center">

    <c:if test="${not empty sessionScope.message2}">
        <fieldset class="messageField"><b>${sessionScope.message2}</b></fieldset>
    </c:if>
    <form name="frm" class="w3-display-middle" action="controller" method="post">
        <fieldset class="mainField">
           <fmt:message key="registration.label.legend"/>:
            <br>
            <fieldset class="leftField">
                <legend align="center"><fmt:message key="login.label.login"/>:</legend>
                <input type="text" name="login"  value="<c:if test="${not empty sessionScope.login1}"/>${sessionScope.login}" required>
            </fieldset>
            <fieldset class="rightField">
                <legend align="center"><fmt:message key="login.label.password"/>:</legend>
                <input type="password" name="password" required><br>
            </fieldset>


            <fieldset class="rightField">
                <legend align="center"><fmt:message key="registration.label.confirm_password"/>:</legend>
                <input type="password" name="confirmPass" required><br>
            </fieldset>

            <fieldset class="leftField">
                <legend align="center"><fmt:message key="registration.label.full_name"/>:</legend>
                <input type="text" name="fullName" value="<c:if test="${not empty sessionScope.fullName}"/>${sessionScope.fullName}" required><br>
            </fieldset>

            <fieldset class="centerField">
                <legend align="center"><fmt:message key="registration.label.email"/></legend>
                <input type="email" name="email" value="<c:if test="${not empty sessionScope.email1}"/>${sessionScope.email}" required><br><br>
            </fieldset>

            <input type="hidden" name="command" value="registration">
            <a class="w3-button w3-dark-grey rad" href="http://localhost:8080"><fmt:message key="button.back"/></a>
            <input class="w3-button w3-dark-grey" type="submit" value="<fmt:message key="registration.button.register"/>" onclick="return validateForm()">
        </fieldset>
    </form>
</div>


<script src="style/js/registration.js" charset="UTF-8"></script>

</body>
</html>