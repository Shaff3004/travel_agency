<%--
  Created by IntelliJ IDEA.
  User: glite
  Date: 25.01.2018
  Time: 17:15
  To change this template use File | Settings | File Templates.
--%>

<html>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@include file="/WEB-INF/jspf/head.jspf" %>
<body>
<%@include file="/WEB-INF/jspf/navigationBar.jspf" %>
<header>
    <div class="myDiv">
        <div class="w3-bar w3-black" id="block_moder">
            <button class="w3-bar-item w3-button tablink"
                    onclick="openLink(event, 'Orders_moder');rememberButton(0);"><fmt:message key="moder.orders"/>
            </button>
            <button class="w3-bar-item w3-button tablink"
                    onclick="openLink(event, 'Tours_moder');rememberButton(1);"><fmt:message key="moder.tours"/>
            </button>
            <button class="w3-bar-item w3-button tablink"
                    onclick="openLink(event, 'Discounts_moder');rememberButton(2);"><fmt:message key="moder.discounts"/>
            </button>
        </div>

        <!-- Tabs -->
        <div id="Orders_moder" class="w3-container w3-white w3-padding-16 myLink">

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

        <div id="Tours_moder" class="w3-container w3-white w3-padding-16 myLink">
            <h1><fmt:message key="moder.tours.info"/></h1>
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
        </div>

        <div id="Discounts_moder" class="w3-container w3-white w3-padding-16 myLink">
            <h1><fmt:message key="moder.discounts.info"/></h1>
            <h5>
                <fmt:message key="admin.discounts.current"/>: ${requestScope.discountStep *100}%.<br>
                <fmt:message key="admin.discounts.max"/>: ${requestScope.maxDiscount * 100}%.<br>

            </h5>
            <div>
                <fieldset class="left_d">
                    <legend><fmt:message key="moder.discounts.label.max"/></legend>

                    <form action="controller" method="post">
                        <input class="slider" type="range" name="maxDiscountInput" id="discountInputId2"
                               value="1" min="1"
                               max="15" oninput="discountOutputId2.value = discountInputId2.value">
                        <output name="discountOutput" id="discountOutputId2">1</output>
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
                               id="discountStepId2"
                               oninput="discountStepIdOutput2.value = discountStepId2.value">
                        <output name="discountStepOutput" id="discountStepIdOutput2">1</output>
                        %
                        <input type="hidden" name="command" value="set_discount_step">
                        <br>
                        <input class="w3-button w3-dark-grey" type="submit"
                               value="<fmt:message key="moder.discounts.button.set"/>" onclick="notifyMessage()">
                    </form>
                </fieldset>
            </div>
        </div>
    </div>
</header>
<script src="../../../style/js/crs.min.js" charset="UTF-8"></script>
<script src="../../../style/js/main_page.js"></script>
</body>
</html>
