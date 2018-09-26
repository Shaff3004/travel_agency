<%--
  Created by IntelliJ IDEA.
  User: glite
  Date: 01.02.2018
  Time: 15:55
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@include file="/WEB-INF/jspf/head.jspf" %>
<html>
<head>
    <title>Statistic</title>
    <link rel="stylesheet" type="text/css" href="../../../style/css/w3.css">
    <link rel="stylesheet" type="text/css" href="../../../style/css/personal_settings.css">
</head>
<body>
<div class="w3-bar w3-white w3-border-bottom w3-xlarge">
    <a class="w3-bar-item w3-button w3-text-red w3-hover-red" href="controller?command=go_home"><fmt:message
            key="button.back"/></a>
    <form action="controller" method="post">
        <input type="hidden" name="command" value="logout">
        <input class="w3-bar-item w3-button w3-right w3-hover-red w3-text-grey" type="submit"
               name="<fmt:message key="navBar.logout"/>"
               value="<fmt:message key="navBar.logout"/>" onclick="clearStorage()">
    </form>
</div>
<header>
    <div class="myDiv">
        <div class="w3-bar w3-black" id="block">
            <button class="w3-bar-item w3-button tablink" onclick="openLink(event, 'By_users');rememberButton(0);">
                <fmt:message key="statistics.users.info"/>
            </button>
            <button class="w3-bar-item w3-button tablink"
                    onclick="openLink(event, 'By_date');rememberButton(1);submit();"><fmt:message
                    key="statistics.date.info"/>
            </button>
            <button class="w3-bar-item w3-button tablink" onclick="openLink(event, 'News');rememberButton(2);">
                <fmt:message key="statistics.tab.newsletter"/>
            </button>

            <%--<button class="w3-bar-item w3-button tablink" onclick="openLink(event, 'Choice');rememberButton(3);">
                Выбор пользователей
            </button>--%>
        </div>

        <!-- Tabs -->
        <div id="By_users" class="w3-container w3-white w3-padding-16 myLink">
            <h3><fmt:message key="statistics.users.info"/></h3>
            <hr>
            <details class="spoiler w3-row-padding">
                <summary><fmt:message key="statistics.users.legend1"/></summary>
                <c:set var="total" value="${0}"/>
                <br>
                <table class="table">
                    <thead>
                    <%--<tabH:tableHeading values="№,Пользователь,ID,Кол-во заказов, Сумма"/>--%>
                    <tabH:tableHeading values="heading.Number,heading.User,heading.ID,heading.Count,heading.Amount"
                                       lang="${language}"/>
                    </thead>

                    <tbody>
                    <c:forEach var="token" items="${requestScope.order_statistics}" varStatus="loop">
                        <tr>
                            <td>${loop.count}</td>
                            <td>${token.customer}</td>
                            <td>${token.customerId}</td>
                            <td>${token.orderCount}</td>
                            <td>${token.sum}</td>
                        </tr>
                        <c:set var="total" value="${total + token.sum}"/>
                    </c:forEach>
                    </tbody>
                    <fmt:message key="statistics.profit"/>: <c:out value="${total}"/>
                </table>

            </details>
            <br>
            <details class="spoiler w3-row-padding">
                <summary><fmt:message key="statistics.users.legend2"/></summary>
                <c:set var="total" value="${0}"/>
                <br>
                <table class="table">
                    <thead>
                    <tabH:tableHeading values="heading.Number,heading.User,heading.ID,heading.Count,heading.Amount"
                                       lang="${language}"/>
                    </thead>
                    <tbody>
                    <c:forEach var="token" items="${requestScope.account_statistics}" varStatus="loop">
                        <tr>
                            <td>${loop.count}</td>
                            <td>${token.customer}</td>
                            <td>${token.customerId}</td>
                            <td>${token.accountsCount}</td>
                            <td>${token.sum}</td>
                            <c:set var="total" value="${total + token.sum}"/>
                        </tr>
                    </c:forEach>
                    </tbody>
                    <fmt:message key="statistics.profit"/>: <c:out value="${total}"/>
                </table>
            </details>
            <br>
        </div>

        <div id="By_date" class="w3-container w3-white w3-padding-16 myLink">
            <h3><fmt:message key="statistics.date.info"/></h3>
            <hr>
            <details class="spoiler w3-row-padding">
                <summary><fmt:message key="statistics.users.legend1"/></summary>
                <c:set var="total" value="${0}"/>
                <br>
                <table class="table">
                    <thead>
                    <tabH:tableHeading values="heading.Number,heading.User,heading.Count,heading.Amount"
                                       lang="${language}"/>
                    </thead>
                    <tbody>
                    <c:forEach var="token" items="${requestScope.order_date_statistics}" varStatus="loop">
                        <tr>
                            <td>${loop.count}</td>
                            <td>${token.date}</td>
                            <td>${token.countOrder}</td>
                            <td>${token.totalPrice}</td>
                        </tr>
                        <c:set var="total" value="${total + token.totalPrice}"/>
                    </c:forEach>
                    </tbody>
                    <fmt:message key="statistics.profit"/>: <c:out value="${total}"/>
                </table>


            </details>
            <br>
            <details class="spoiler w3-row-padding">
                <summary><fmt:message key="statistics.users.legend2"/></summary>
                <c:set var="total" value="${0}"/>
                <br>
                <table class="table">
                    <thead>
                    <tabH:tableHeading values="heading.Number,heading.User,heading.Count,heading.Amount"
                                       lang="${language}"/>
                    </thead>
                    <tbody>
                    <c:forEach var="token" items="${requestScope.account_date_statistics}" varStatus="loop">
                        <tr>
                            <th>${loop.count}</th>
                            <th>${token.date}</th>
                            <th>${token.count}</th>
                            <th>${token.totalPrice}</th>
                        </tr>
                        <c:set var="total" value="${total + token.totalPrice}"/>
                    </c:forEach>
                    </tbody>
                    <fmt:message key="statistics.profit"/>: <c:out value="${total}"/>
                </table>

            </details>

        </div>

        <div id="News" class="w3-container w3-white w3-padding-16 myLink">
            <h3><fmt:message key="statistics.newsletter"/></h3>
            <div
            <c:if test="${not empty sessionScope.resultMessage}">
                <b>${sessionScope.resultMessage}</b>
            </c:if>

        </div>
        <hr>
        <div>
            <fieldset class="message">
                <legend><fmt:message key="statistics.newsletter.info"/></legend>
                <form name="form" action="controller" method="post" class="myForm">
                    <input type="hidden" name="command" value="send_email">
                    <input id="email" type="email" name="email"
                           placeholder="<fmt:message key="statistics.newsletter.placeholder1"/>">
                    <input type="checkbox" name="newsletter" onclick="blockEmail()"><fmt:message
                        key="statistics.newsletter"/>
                    <br><br>
                    <textarea class="textarea" name="message"
                              placeholder="<fmt:message key="statistics.newsletter.placeholder2"/>"></textarea>

                    <br>
                    <input class="w3-button w3-dark-grey" type="submit"
                           value="<fmt:message key="recovery.button.send"/>" onclick="return validateMessage()">
                </form>
            </fieldset>
        </div>


        <%--<div id="Choice" class="w3-container w3-white w3-padding-16 myLink">
            <h3>Choise of the people</h3>
            <hr>
            <table class="table">
                <thead>
                <tr>
                    <tabH:tableHeading
                            values="heading.Country,heading.City,heading.Type,heading.Persons,heading.Hotel,heading.sum"
                            lang="${language}"/>
                </tr>
                </thead>

                <tbody>
                <c:forEach var="tour" items="${requestScope.allTours}">
                    <tr>
                        <td>${tour.country}</td>
                        <td>${tour.city}</td>
                        <td>${tour.type}</td>
                        <td>${tour.persons}</td>
                        <td>${tour.hotel}</td>
                        <td>${tour.sum}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>--%>
    </div>
</header>
</body>
<script src="../../../style/js/main_page.js"></script>
</html>
