<%--
  Created by IntelliJ IDEA.
  User: glite
  Date: 18.01.2018
  Time: 22:14
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@include file="/WEB-INF/jspf/head.jspf" %>

<html>
<head>
    <title>Settings</title>


    <link rel="stylesheet" type="text/css" href="../../../style/css/w3.css">
    <link rel="stylesheet" type="text/css" href="../../../style/css/main_page.css">
    <link rel="stylesheet" type="text/css" href="../../../style/css/personal_settings.css">

    <script type="text/javascript" src="../../../style/js/jquery-latest.js"></script>
    <script type="text/javascript" src="../../../style/js/jquery.tablesorter.js"></script>
</head>

<body>


<!-- Navigation Bar -->
<div class="w3-bar w3-white w3-border-bottom w3-xlarge">
    <a href="controller?command=go_home" class="w3-bar-item w3-button w3-text-red w3-hover-red"><b><fmt:message
            key="personalSettings.home"/></b></a>
    <a href="controller?command=about_us" class="w3-bar-item w3-button w3-text-red w3-hover-red"><b><fmt:message key="navBar.about"/></b></a>


    <form action="controller" method="get">
        <input type="hidden" name="command" value="logout">
        <input class="w3-bar-item w3-button w3-right w3-hover-red w3-text-grey" type="submit" name="logout"
               value="<fmt:message key="navBar.logout"/>" onclick="clearStorage()">
    </form>
</div>

<header>
    <div class="w3-col m3 pic">
        <!-- Profile -->
        <div class="w3-card w3-round pic">
            <div class="w3-container profile">
                <h4 class="w3-center">${sessionScope.user.fullName}</h4>

                <div style="box-sizing: border-box; height: auto;width: 100%;max-width: 160px;margin: 0 auto"><img
                        style="max-width: 160px; border-radius:15%" alt="Avatar"
                <c:choose>
                        <c:when test="${empty sessionScope.avatar}">src="../../../style/img/default_avatar.png"</c:when>
                        <c:otherwise>src="${sessionScope.avatar}"</c:otherwise>
                </c:choose>
                ></div>
                <details align="center">
                    <summary><fmt:message key="personalSettings.button.avatar"/></summary>
                    <form action="avatar" method="post" enctype="multipart/form-data">
                        <input type="file" name="avatar" id="file" required accept="image/*">
                        <input class="w3-button w3-dark-grey" type="submit" value="<fmt:message key="admin.tours.button.change"/>">
                    </form>
                </details>

                <hr>
                <p><fmt:message key="personalSettings.role"/>:
                    <c:if test="${sessionScope.user.roleId == 3}">client</c:if>
                </p>
                <p><fmt:message key="personalSettings.status"/>:
                    <c:if test="${sessionScope.user.status == true}">active</c:if>
                <p><fmt:message key="personalSettings.balance"/>:
                    ${requestScope.balance}
                <p>
                <hr>
                <a class="secureSettings" href="controller?command=security_settings"><fmt:message
                        key="personalSettings.button.security"/></a><br><br>


                <button class="w3-button w3-dark-grey recharge" type="button" id="show"><fmt:message
                        key="personalSettings.button.balance"/></button>
                <br>
                <br>

                <div>
                    <dialog class="w3-col m7 dial">
                        <img src="../../../style/img/close.png" class="exit" id="close">
                        <form class="f5" name="frm" action="controller" type="post">
                            <input type="hidden" name="command" value="replenishment">
                            <input class="input card" type="text" name="credit_number"
                                   placeholder="<fmt:message key="personalSettings.creditNumber"/>">
                            <br>
                            <br>
                            <input class="input card" type="text" name="money"
                                   placeholder="<fmt:message key="tab.FindTour.placeholder"/>">
                            <br>
                            <br>
                            <input class="w3-button w3-dark-grey rih" type="submit"
                                   value="<fmt:message key="login.button.submit"/>" onclick="return validateCard()">
                        </form>
                    </dialog>
                </div>
            </div>
        </div>
        <br>
        <!-- Alert Box -->
        <c:if test="${sessionScope.user.discount > 0}">
            <div class="w3-container profile">
                <img src="../../../style/img/close.png" class="exit" onclick="this.parentElement.style.display='none'">
                <p><strong><fmt:message key="personalSettings.alert"/></strong></p>
                <p><fmt:message key="personalSettings.alert_info1"/> ${sessionScope.user.discount * 100}% <fmt:message
                        key="personalSettings.alert_info2"/></p>
            </div>
        </c:if>
    </div>

    <!-- Middle Column -->
    <div class="w3-col m7 pic">
        <div class="w3-bar w3-black" id="block">
            <button class="w3-bar-item w3-button tablink" onclick="openLink(event, 'Orders');rememberButton(0);">
                <fmt:message key="personalSettings.tab.AllOrders"/>
            </button>
            <button class="w3-bar-item w3-button tablink"
                    onclick="openLink(event, 'Replenishments');rememberButton(1);submit();"><fmt:message key="personalSettings.tab.Payments"/>
            </button>
            <button class="w3-bar-item w3-button tablink" onclick="openLink(event, 'Ready');rememberButton(2);"><i class="fa fa-plane w3-margin-right"></i>
                <fmt:message key="personalSettings.tab.Ready"/>
            </button>
        </div>

        <!-- Tabs -->
        <div id="Orders" class="w3-container w3-white w3-padding-16 myLink">
            <h2><fmt:message key="personalSettings.orders"/></h2>
            <hr class="w3-clear">
            <c:choose>
                <c:when test="${not empty requestScope.orders}">
                    <table class="table">
                        <thead>
                        <%--<tabH:tableHeading values="ID,Country,City,Persons,Departure,Price,Status"/>--%>
                        <tabH:tableHeading values="heading.ID,heading.Country,heading.City,heading.Persons,heading.Departure,heading.Price,heading.Status"
                                           lang="${language}"/>
                        </thead>
                        <tbody>
                        <c:forEach var="order" items="${requestScope.orders}">
                            <tr <c:choose>
                                <c:when test="${order.status == 'registered'}">class="registered"</c:when>
                                <c:when test="${order.status == 'paid'}">class="paid"</c:when>
                                <c:when test="${order.status == 'canceled'}">class="canceled"</c:when>
                            </c:choose>>

                                <td>${order.id}</td>
                                <td>${order.tour.country}</td>
                                <td>${order.tour.city}</td>
                                <td>${order.tour.persons}</td>
                                <td>${order.tour.date}</td>
                                <td>${order.tour.price}</td>
                                <td>${order.status}</td>

                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise><h3><fmt:message key="personalSettings.noOrders"/>.</h3></c:otherwise>
            </c:choose>
        </div>

        <div id="Replenishments" class="w3-container w3-white w3-padding-16 myLink">
            <h2><fmt:message key="personalSettings.payments"/></h2>
            <hr class="w3-clear">
            <c:choose>
                <c:when test="${not empty requestScope.accounts}">
                    <table class="table">
                        <thead>
                        <%--<tabH:tableHeading values="ID,Customer,Date,Price,Status"/>--%>
                        <tabH:tableHeading values="heading.ID,heading.Customer,heading.Date,heading.Price,heading.Status"
                                           lang="${language}"/>
                        </thead>
                        <tbody>
                        <c:forEach var="account" items="${requestScope.accounts}">
                            <tr <c:choose>
                                <c:when test="${account.status == 'registered'}">class="registered"</c:when>
                                <c:when test="${account.status == 'accepted'}">class="paid"</c:when>
                                <c:when test="${account.status == 'canceled'}">class="canceled"</c:when>
                            </c:choose>>

                                <td>${account.id}</td>
                                <td>${account.userName}</td>
                                <td>${account.date}</td>
                                <td>${account.price}</td>
                                <td>${account.status}</td>

                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise><h3><fmt:message key="personalSettings.noPayments"/></h3></c:otherwise>
            </c:choose>

        </div>

        <div id="Ready" class="w3-container w3-white w3-padding-16 myLink">
            <h3><fmt:message key="personalSettings.ready"/></h3>
            <hr class="w3-clear">
            <c:choose>
                <c:when test="${not empty requestScope.ready}">
                    <table class="table">
                        <thead>
                        <%--<tabH:tableHeading values="ID,Country,City,Persons,Departure,Price,Status,Action"/>--%>
                        <tabH:tableHeading values="heading.ID,heading.Country,heading.City,heading.Persons,heading.Departure,heading.Price,heading.Status,heading.Action"
                                           lang="${language}"/>
                        </thead>
                        <tbody>
                        <c:forEach var="ready" items="${requestScope.ready}">
                            <tr class="paid">

                                <td>${ready.id}</td>
                                <td>${ready.tour.country}</td>
                                <td>${ready.tour.city}</td>
                                <td>${ready.tour.persons}</td>
                                <td>${ready.tour.date}</td>
                                <td>${ready.tour.price}</td>
                                <td>${ready.status}</td>
                                <td>
                                    <form action="controller" method="post">
                                        <input type="hidden" name="command" value="send_ticket">
                                        <input type="hidden" name="orderID" value="${ready.id}">
                                        <input type="hidden" name="userID" value="${ready.customer.id}">
                                        <input type="submit" value="<fmt:message key="recovery.button.send"/>"
                                               class="w3-button w3-dark-grey">
                                    </form>

                                    <form action="controller" method="post">
                                        <input type="hidden" name="command" value="download_ticket">
                                        <input type="hidden" name="orderID" value="${ready.id}">
                                        <input type="hidden" name="userID" value="${ready.customer.id}">
                                        <input type="submit"
                                               value="<fmt:message key="personalSettings.button.download"/>"
                                               class="w3-button w3-dark-grey">
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise><h3><fmt:message key="personalSettings.noReady"/></h3></c:otherwise>
            </c:choose>
        </div>
    </div>


</header>

</body>
<script src="../../../style/js/personal_settings.js"></script>
<script src="../../../style/js/main_page.js"></script>

</html>
