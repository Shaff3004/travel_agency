<%--
  Created by IntelliJ IDEA.
  User: glite
  Date: 04.01.2018
  Time: 14:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Error</title>
    <link rel="stylesheet" href="../../style/css/error_page.css">
    <link rel="stylesheet" href="../../style/css/w3.css">
    <link rel="stylesheet" type="text/css" href="../../../style/css/main_page.css"/>
</head>
<body>
<header>
    <div align="center">
        <div class="myDiv">
            <fieldset class="mainField">
                ${requestScope.errorMessage}

                <c:if test="${requestScope.errorMessage == 'Low balance'}">
                    <a href="controller?command=personal_settings"><b>You can recharge your balance right now</b></a>
                </c:if>
                <br><br>
                <button class="w3-bar-item w3-button w3-text-red w3-hover-red" type="button"
                        onclick="window.history.back()">Back
                </button>
            </fieldset>
        </div>

    </div>
</header>
</body>
</html>
