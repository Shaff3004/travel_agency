<%--
  Created by IntelliJ IDEA.
  User: glite
  Date: 28.01.2018
  Time: 16:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="WEB-INF/jspf/directive/taglib.jspf" %>
<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="resources"/>
<html>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="style/css/w3.css">
<link rel="stylesheet" href="style/css/login.css">
<head>
    <title>Password recovery</title>
</head>
<body>
<div align="center">
    <c:if test="${not empty sessionScope.recoverMessage}">
        <fieldset class="messageField"><b>${sessionScope.recoverMessage}</b></fieldset>
    </c:if>

    <fieldset class="mainField">
        <form name="frm" action="controller" method="post">
            <fmt:message key="recovery.label.email"/>:
            <br>
            <fieldset>
                <input type="hidden" name="command" value="generate_security_key">
                <input type="text" name="email" required value="${sessionScope.email}"><br><br>
                <input class="w3-button w3-dark-grey" type="submit" value="<fmt:message key="recovery.button.send"/>"
                       onclick="return validateEmail()">
            </fieldset>
        </form>


        <hr>
        <form name="frm2" action="controller" method="post">
            <fmt:message key="recovery.label.key"/>:
            <fieldset>
                <input type="hidden" name="command" value="check_key">
                <input type="text" name="key" required><br><br>
                <a href="http://localhost:8080" class="w3-button w3-dark-grey back"><fmt:message key="button.back"/></a>
                <input class="w3-button w3-dark-grey" type="submit" value="<fmt:message key="login.button.submit"/>"
                       onclick="return validateKey()">
            </fieldset>
        </form>


    </fieldset>


</div>
<script src="style/js/password_recovery.js"></script>
</body>
</html>
