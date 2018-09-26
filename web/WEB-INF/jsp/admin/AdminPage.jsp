<%--
  Created by IntelliJ IDEA.
  User: glite
  Date: 25.01.2018
  Time: 16:54
  To change this template use File | Settings | File Templates.
--%>
<html>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@include file="/WEB-INF/jspf/head.jspf" %>

<body class="w3-light-grey">
<%@include file="/WEB-INF/jspf/navigationBar.jspf" %>
<header>

    <div class="myDiv">
        <div class="w3-bar w3-black" id="block_admin">
            <button class="w3-bar-item w3-button tablink" name="tab"
                    onclick="openLink(event, 'Orders_admin'); rememberButton(0);"><fmt:message key="moder.orders"/>
            </button>
            <button class="w3-bar-item w3-button tablink" name="tab"
                    onclick="openLink(event, 'Tours_admin');rememberButton(1);"><fmt:message key="moder.tours"/>
            </button>
            <button class="w3-bar-item w3-button tablink" name="tab"
                    onclick="openLink(event, 'Discounts_admin');rememberButton(2);"><fmt:message key="moder.discounts"/>
            </button>
            <button class="w3-bar-item w3-button tablink" name="tab"
                    onclick="openLink(event, 'Users_admin');rememberButton(3);"><fmt:message key="admin.users"/>
            </button>
            <button class="w3-bar-item w3-button tablink" name="tab"
                    onclick="openLink(event, 'Replenishments_admin');rememberButton(4);"><fmt:message
                    key="tab.Replenishments"/>
            </button>
        </div>

        <!-- Tabs -->
        <div id="Orders_admin" class="w3-container w3-white w3-padding-16 myLink">

            <h1><fmt:message key="moder.orders.info"/></h1>
            <c:choose>
                <c:when test="${not empty requestScope.orders}">
                    <table class="table">
                        <thead>
                        <%--<tabH:tableHeading
                                values="ID,user ID,Customer,tour ID,Country,City,Price,Status,Action"/>--%>
                        <tabH:tableHeading
                                values="heading.ID,heading.userID,heading.Customer,heading.tourID,heading.Country,heading.City,heading.Price,heading.Status,heading.Action"
                                lang="${language}"/>
                        </thead>

                        <tbody>
                        <c:forEach var="orders" items="${requestScope.orders}">
                            <tr <c:choose>
                                <c:when test="${orders.status == 'registered'}">class="registered"</c:when>
                                <c:when test="${orders.status == 'paid'}">class="paid"</c:when>
                                <c:when test="${orders.status == 'canceled'}">class="canceled"</c:when>
                            </c:choose>>
                                <td>${orders.id}</td>
                                <td>${orders.customer.id}</td>
                                <td>${orders.customer.fullName}</td>
                                <td>${orders.tour.id}</td>
                                <td>${orders.tour.country}</td>
                                <td>${orders.tour.city}</td>
                                <td>${orders.tour.price}</td>
                                <td>${orders.status}</td>
                                <td>
                                    <form action="controller" method="post">
                                        <input type="hidden" name="orderID" value="${orders.id}">
                                        <input type="hidden" name="customerID" value="${orders.customer.id}">
                                        <input type="hidden" name="command" value="confirm_order">
                                        <input class="w3-button w3-dark-grey <c:if test="${orders.status == 'paid' || orders.status == 'canceled'}">invisible</c:if>"
                                               type="submit" value="<fmt:message key="moder.orders.button.confirm"/>"
                                               onclick="notifyMessage()">
                                    </form>
                                    <form action="controller" method="post">
                                        <input type="hidden" name="orderID" value="${orders.id}">
                                        <input type="hidden" name="customerID" value="${orders.customer.id}">
                                        <input type="hidden" name="price" value="${orders.tour.price}">
                                        <input type="hidden" name="command" value="cancel_order">
                                        <input class="w3-button w3-dark-grey <c:if test="${orders.status == 'paid' || orders.status == 'canceled'}">invisible</c:if>"
                                               type="submit" value="<fmt:message key="moder.orders.button.cancel"/>"
                                               onclick="notifyMessage()">
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>

                    </table>
                </c:when>
                <c:otherwise><h3><fmt:message key="moder.orders.noOrder"/></h3></c:otherwise>
            </c:choose>
        </div>

        <div id="Tours_admin" class="w3-container w3-white w3-padding-16 myLink">
            <h1><fmt:message key="moder.tours.info"/></h1>
            <details class="spoiler w3-row-padding" onclick="rememberFieldSet(1);">
                <summary><fmt:message key="admin.tours.hot"/></summary>
                <table class="table">
                    <thead>
                    <%--<tabH:tableHeading values="ID,Country,City,Type,Persons,Hotel,Date,Price,Action"/>--%>
                    <tabH:tableHeading values="heading.ID,heading.Country,heading.City,heading.Type,heading.Persons,heading.Hotel,heading.Date,heading.Price,heading.Action"
                                       lang="${language}"/>
                    </thead>
                    <tbody>
                    <c:forEach var="tour" items="${requestScope.allTours}">
                        <tr <c:if test="${tour.hot == 1}">class="hot"</c:if>>
                            <td>${tour.id}</td>
                            <td>${tour.country}</td>
                            <td>${tour.city}</td>
                            <td>${tour.type}</td>
                            <td>${tour.persons}</td>
                            <td>${tour.hotel}</td>
                            <td>${tour.date}</td>
                            <td>${tour.price}</td>
                            <td>
                                <form action="controller" method="post">
                                    <input type="hidden" name="tourID" value="${tour.id}">
                                    <input type="hidden" name="command" value="set_hot">
                                    <input class="w3-button w3-dark-grey <c:if test="${tour.hot == 1}">invisible</c:if>"
                                           type="submit" value="<fmt:message key="moder.tours.button.hot"/>"
                                           onclick="notifyMessage()">
                                </form>

                                <form action="controller" method="post">
                                    <input type="hidden" name="tourID" value="${tour.id}">
                                    <input type="hidden" name="command" value="set_normal">
                                    <input class="w3-button w3-dark-grey <c:if test="${tour.hot == 0}">invisible</c:if>"
                                           type="submit" value="<fmt:message key="moder.tours.button.normal"/>"
                                           onclick="notifyMessage()">
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </details>
            <br>

            <details class="spoiler" onclick="rememberFieldSet(2);">
                <summary><fmt:message key="admin.tours.add"/></summary>
                <br>
                <form name="frm2" action="controller" method="post" class="myForm">
                    <div class="w3-row-padding form-left-column">
                        <div class="w3-half2">
                            <label><fmt:message key="admin.tours.add.country"/>
                                <select class="w3-input w3-border crs-country" data-region-id="two"
                                        name="country">
                                </select>
                            </label>
                        </div>
                        <div class="w3-half2">
                            <label><fmt:message key="admin.tours.add.type"/>
                                <select class="w3-input w3-border" name="type">
                                    <option class="default-option" disabled selected value><fmt:message
                                            key="admin.tours.add.default"/>
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
                                    <option class="default-option" disabled selected value><fmt:message
                                            key="admin.tours.add.default"/>
                                    </option>
                                    <c:forEach var="hotel" items="${requestScope.hotelTypes}">
                                        <option>${hotel}</option>
                                    </c:forEach>
                                </select>
                            </label>
                        </div>
                    </div>
                    <div class="form-right-column">
                        <div class="w3-half2">
                            <label><fmt:message key="admin.tours.add.city"/>
                                <select id="two" class="w3-input w3-border" name="city">

                                </select>
                            </label>
                        </div>
                        <div class="w3-half2">
                            <label><fmt:message key="admin.tours.add.persons"/>
                                <select class="w3-input w3-border" name="persons">
                                    <option class="default-option" disabled selected value><fmt:message
                                            key="admin.tours.add.default"/>
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
                                <input class="w3-input w3-border" type="date" placeholder="When" name="date">
                            </label>
                        </div>
                        <div class="w3-half2">
                            <label><fmt:message key="admin.tours.add.price"/>
                                <input class="w3-input w3-border" type="text"
                                       placeholder="<fmt:message key="admin.tours.price.placeholder"/>"
                                       name="price">
                            </label>
                        </div>
                        <input type="hidden" name="command" value="add_tour">
                        <br>
                        <input class="w3-button w3-dark-grey button-right" type="submit"
                               value="<fmt:message key="admin.tours.button.add"/>"
                               onclick="return validateAddTourForm();notifyMessage()">
                    </div>
                </form>
            </details>
            <br>
            <details class="spoiler" onclick="rememberFieldSet(3);">
                <summary><fmt:message key="admin.tours.change.info"/></summary>

                <table class="table">
                    <thead>
                    <%--<tabH:tableHeading values="ID,Country,City,Type,Persons,Hotel,Date,Price,Action"/>--%>
                    <tabH:tableHeading values="heading.ID,heading.Country,heading.City,heading.Type,heading.Persons,heading.Hotel,heading.Date,heading.Price,heading.Action"
                                       lang="${language}"/>
                    </thead>
                    <tbody>
                    <c:forEach var="tour" items="${requestScope.allTours}">
                        <tr>
                            <td>${tour.id}</td>
                            <td>${tour.country}</td>
                            <td>${tour.city}</td>
                            <td>${tour.type}</td>
                            <td>${tour.persons}</td>
                            <td>${tour.hotel}</td>
                            <td>${tour.date}</td>
                            <td>${tour.price}</td>
                            <td>
                                <a href="controller?command=prepare_change_page&tourID=${tour.id}"
                                   class="w3-button w3-dark-grey"><b><fmt:message key="admin.tours.button.change"/></b></a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

            </details>
            <br>
            <details class="spoiler" onclick="rememberFieldSet(4);">
                <summary><fmt:message key="admin.tours.delete.info"/></summary>
                <table class="table">
                    <thead>
                    <%--<tabH:tableHeading values="ID,Country,City,Type,Persons,Hotel,Date,Price,Action"/>--%>
                    <tabH:tableHeading values="heading.ID,heading.Country,heading.City,heading.Type,heading.Persons,heading.Hotel,heading.Date,heading.Price,heading.Action"
                                       lang="${language}"/>
                    </thead>
                    <tbody>
                    <c:forEach var="tour" items="${requestScope.allTours}">
                        <tr>
                            <td>${tour.id}</td>
                            <td>${tour.country}</td>
                            <td>${tour.city}</td>
                            <td>${tour.type}</td>
                            <td>${tour.persons}</td>
                            <td>${tour.hotel}</td>
                            <td>${tour.date}</td>
                            <td>${tour.price}</td>
                            <td>
                                <form action="controller" method="post">
                                    <input type="hidden" name="tourID" value="${tour.id}">
                                    <input type="hidden" name="command" value="delete_tour">
                                    <input class="w3-button w3-dark-grey"
                                           type="submit" value="<fmt:message key="admin.tours.button.delete"/>"
                                           onclick="confirmAction();notifyMessage()">
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </details>
        </div>

        <div id="Discounts_admin" class="w3-container w3-white w3-padding-16 myLink">
            <h1><fmt:message key="moder.discounts.info"/></h1>
            <h5>
                <fmt:message key="admin.discounts.current"/>: ${requestScope.discountStep *100}%.<br>
                <fmt:message key="admin.discounts.max"/>: ${requestScope.maxDiscount * 100}%.<br>

            </h5>
            <div>
                <fieldset class="left_d">
                    <legend><fmt:message key="moder.discounts.label.max"/></legend>
                    <form action="controller" method="post">
                        <input class="slider" type="range" name="maxDiscountInput" id="discountInputId"
                               value="1" min="1"
                               max="15" oninput="discountOutputId.value = discountInputId.value">
                        <output name="discountOutput" id="discountOutputId">1</output>
                        %
                        <input type="hidden" name="command" value="set_max_discount">
                        <br>
                        <input class="w3-button w3-dark-grey" type="submit"
                               value="<fmt:message key="moder.discounts.button.set"/>" onclick="notifyMessage()">
                    </form>
                </fieldset>

                <fieldset class="right_d">
                    <legend><fmt:message key="moder.discounts.label.step"/></legend>
                    <form action="controller" method="post">
                        <input class="slider" type="range" name="discountStep" min="1" max="5" step="1,2,3,5"
                               id="discountStepId" oninput="discountStepIdOutput.value = discountStepId.value">
                        <output name="discountStepOutput" id="discountStepIdOutput">1</output>
                        %
                        <input type="hidden" name="command" value="set_discount_step">
                        <br>
                        <input class="w3-button w3-dark-grey" type="submit"
                               value="<fmt:message key="moder.discounts.button.set"/>" onclick="notifyMessage()">
                    </form>
                </fieldset>
            </div>
        </div>

        <div id="Users_admin" class="w3-container w3-white w3-padding-16 myLink">
            <h1><fmt:message key="admin.users.info"/></h1>
            <table class="table" id="userTable">
                <thead>
                <%--<tabH:tableHeading values="ID,Role,Login,Full name, Status,Action"/>--%>
                <tabH:tableHeading
                        values="heading.ID,heading.Role,heading.Login,heading.FullName,heading.Status,heading.Action"
                        lang="${language}"/>
                </thead>
                <tbody>
                <c:forEach var="user" items="${requestScope.users}">
                    <tr <c:if test="${user.roleId == 2}">class="moder"</c:if>>
                        <td>${user.id}</td>
                        <td><c:if test="${user.roleId == 3}">user</c:if>
                            <c:if test="${user.roleId == 2}">moder</c:if></td>
                        <td>${user.login}</td>
                        <td>${user.fullName}</td>
                        <td><c:if test="${user.status == true}">active</c:if>
                            <c:if test="${user.status == false}">blocked</c:if></td>
                        <td>
                            <form action="controller" method="post">
                                <input type="hidden" name="userID" value="${user.id}">
                                <input type="hidden" name="command" value="block_user">
                                <input class="w3-button w3-dark-grey <c:if test="${user.status == false}">invisible</c:if>"
                                       type="submit" value="<fmt:message key="admin.users.button.block"/>"
                                       onclick="notifyMessage()">
                            </form>
                            <form action="controller" method="post">
                                <input type="hidden" name="userID" value="${user.id}">
                                <input type="hidden" name="command" value="unblock_user">
                                <input class="w3-button w3-dark-grey <c:if test="${user.status == true}">invisible</c:if>"
                                       type="submit" value="<fmt:message key="admin.users.button.unblock"/>"
                                       onclick="notifyMessage()">
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <div id="Replenishments_admin" class="w3-container w3-white w3-padding-16 myLink">
            <h1><fmt:message key="tab.Replenishments.info"/></h1>
            <table class="table">
                <thead>
                <%--<tabH:tableHeading values="ID,user ID,Full name,Date,Price,Status,Action"/>--%>
                <tabH:tableHeading
                        values="heading.ID,heading.userID,heading.FullName,heading.Date,heading.Price,heading.Status,heading.Action"
                        lang="${language}"/>
                </thead>
                <tbody>
                <c:forEach var="account" items="${requestScope.allAccounts}">
                    <tr <c:choose>
                        <c:when test="${account.status == 'accepted'}">class="paid"</c:when>
                        <c:when test="${account.status == 'registered'}">class="registered"</c:when>
                        <c:when test="${account.status == 'canceled'}">class="canceled"</c:when>
                    </c:choose>>
                        <td>${account.id}</td>
                        <td>${account.userId}</td>
                        <td>${account.userName}</td>
                        <td>${account.date}</td>
                        <td>${account.price}</td>
                        <td>${account.status}</td>
                        <td>
                            <c:choose>
                                <c:when test="${account.status == 'registered'}">
                                    <form action="controller" method="post">
                                        <input type="hidden" name="command" value="confirm_replenishment">
                                        <input type="hidden" name="userID" value="${account.userId}">
                                        <input type="hidden" name="accountID" value="${account.id}">
                                        <input type="hidden" name="money" value="${account.price}">
                                        <input class="w3-button w3-dark-grey" type="submit"
                                               value="<fmt:message key="moder.orders.button.confirm"/>"
                                               onclick="notifyMessage()">
                                    </form>
                                    <form action="controller" method="post">
                                        <input type="hidden" name="command" value="cancel_replenishment">
                                        <input type="hidden" name="userID" value="${account.userId}">
                                        <input type="hidden" name="accountID" value="${account.id}">
                                        <input class="w3-button w3-dark-grey" type="submit"
                                               value="<fmt:message key="moder.orders.button.cancel"/>"
                                               onclick="notifyMessage()">
                                    </form>
                                </c:when>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

        </div>
    </div>
</header>
</body>
<script src="../../../style/js/crs.min.js" charset="UTF-8"></script>
<script src="../../../style/js/main_page.js"></script>


</html>
