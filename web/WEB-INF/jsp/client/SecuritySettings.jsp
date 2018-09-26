<%--
  Created by IntelliJ IDEA.
  User: glite
  Date: 29.01.2018
  Time: 18:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<html>
<head>
    <title>Settings</title>
    <link rel="stylesheet" type="text/css" href="../../../style/css/w3.css">
    <link rel="stylesheet" type="text/css" href="../../../style/css/personal_settings.css">
</head>
<body>
<div class="w3-bar w3-white w3-border-bottom w3-xlarge">
    <a class="w3-bar-item w3-button w3-text-red w3-hover-red" href="controller?command=personal_settings"><fmt:message key="button.back"/></a>
    <form action="controller" method="post">
        <input type="hidden" name="command" value="logout">
        <input class="w3-bar-item w3-button w3-right w3-hover-red w3-text-grey" type="submit"
               name="<fmt:message key="navBar.logout"/>"
               value="<fmt:message key="navBar.logout"/>" onclick="clearStorage()">
    </form>
</div>
<header>

    <%--<form name="frm" class="w3-display-middle" action="controller" method="post">--%>
    <div class="w3-col m7 pic securityDiv">
        <div class="w3-container w3-card w3-white w3-round w3-margin"><br>
            <h2><fmt:message key="securitySettings.header"/></h2>
            <div><c:if test="${not empty sessionScope.action}">
                <b>${sessionScope.action}<b>
            </c:if>
            </div>
            <h6><fmt:message key="securitySettings.globalInfo"/>${sessionScope.user.email}</h6>

            <fieldset style="padding: 5px;">
                <legend><fmt:message key="securitySettings.label.email"/></legend>
                <form name="frm" class="securityForm w3-half" action="controller" method="post">
                    <input type="hidden" name="command" value="change_email">
                    <input type="email" name="email" placeholder="<fmt:message key="securitySettings.placeholder1"/>">
                    <input type="hidden" name="userID" value="${sessionScope.user.id}"><br><br>
                    <input type="submit" class="w3-button w3-dark-grey button-right" value="<fmt:message key="admin.tours.button.change"/>" onclick="return validateEmail()">
                </form>
            </fieldset>
            <fieldset style="padding: 5px">
                <legend><fmt:message key="securitySettings.label.password"/></legend>
                <form name="frm2" class="securityForm w3-half" action="controller" method="post">
                    <input type="hidden" name="command" value="change_password">
                    <input class="securityInp" type="password" name="oldPass" placeholder="<fmt:message key="securitySettings.placeholder2"/>">
                    <input class="securityInp" type="password" name="newPass" placeholder="<fmt:message key="securitySettings.placeholder3"/>">
                    <input class="securityInp" type="password" name="confirmPass" placeholder="<fmt:message key="securitySettings.placeholder4"/>">
                    <input type="hidden" name="userID" value="${sessionScope.user.id}"><br><br>
                    <input class="w3-button w3-dark-grey button-right securityInp" type="submit" value="<fmt:message key="admin.tours.button.change"/>" onclick="return validatePassword()">
                </form>
            </fieldset>
            <br>
        </div>
    </div>

    <%--</form>--%>


</header>

<script src="../../../style/js/security_settings.js"></script>
</body>
</html>
