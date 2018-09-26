
<%--
  Created by IntelliJ IDEA.
  User: glite
  Date: 03.01.2018
  Time: 13:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="WEB-INF/jspf/directive/taglib.jspf" %>
<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="resources"/>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags"%>

<html>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="style/css/w3.css">
<link rel="stylesheet" href="style/css/login.css">
<head>
    <title>Login</title>
</head>
<body>
<div align="center">
    <c:if test="${not empty sessionScope.message}">
        <fieldset class="messageField"><b>${sessionScope.message}</b></fieldset>
    </c:if>

    <form name="frm" class="w3-display-middle" action="controller" method="post">
        <input type="hidden" name="command" value="login">
        <fieldset class="mainField">
            <%--<legend align="left">--%><fmt:message key="login.label.legend"/>:<%--</legend>--%>
            <br>
            <fieldset>
                <legend align="center"><fmt:message key="login.label.login"/>:</legend>
                <input type="text" name="login" value="<c:if test="${not empty sessionScope.loginP}"/>${sessionScope.loginP}" required>
            </fieldset>

            <fieldset>
                <legend align="center"><fmt:message key="login.label.password"/>:</legend>
                <input type="password" name="password" id="password" required><br>
            </fieldset>
                <%--<action:captcha/>--%>


            <input type="checkbox" onclick="hidePassword()"><fmt:message key="login.checkbox.show"/><br>
            <a href="controller?command=password_recovery_page"><fmt:message key="login.label.forgot"/></a>
            <br>
            <br>
            <input class="w3-button w3-dark-grey" type="submit" value="<fmt:message key="login.button.submit"/>"
                   onclick="return validateForm();">

            <form action="controller" method="get">
                <input type="hidden" name="command" value="registration_page">
                <%--<input class="w3-button w3-dark-grey" type="button"  value="<fmt:message key="login.button.registration"/>">--%>
                <a class="w3-button w3-dark-grey rad" href="controller?command=registration_page"><fmt:message
                        key="login.button.registration"/></a>
            </form>

        </fieldset>

    </form>

</div>

<script src="style/js/login.js"></script>
</body>
</html>