<%--
  Created by IntelliJ IDEA.
  User: glite
  Date: 25.01.2018
  Time: 17:18
  To change this template use File | Settings | File Templates.
--%>

<html>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@include file="/WEB-INF/jspf/head.jspf" %>
<body onload="getSnackbar()">

<%@include file="/WEB-INF/jspf/navigationBar.jspf" %>
<header>
    <c:if test="${not empty requestScope.newsletter}">
        <div id="snackbar">
            <img src="../../../style/img/close.png" class="exit" onclick="this.parentElement.style.display='none'">
            <br>
            <fmt:message key="client.subscribe.title"/>
            <form action="controller" method="post">
                <input type="hidden" name="command" value="subscribe">
                <input type="submit" class="w3-button w3-dark-grey" value="<fmt:message key="client.button.subscribe"/>">
            </form>
        </div>
    </c:if>

    <div class="myDiv">
        <div class="w3-bar w3-black" id="block">
            <button class="w3-bar-item w3-button tablink" onclick="openLink(event, 'Tour');rememberButton(0);">
                <fmt:message key="tab.client.FindTour"/>
            </button>
            <button class="w3-bar-item w3-button tablink"
                    onclick="openLink(event, 'All_tours');rememberButton(1);submit();">
                <fmt:message key="tab.client.AllTours"/>
            </button>
            <button class="w3-bar-item w3-button tablink" onclick="openLink(event, 'Hot');rememberButton(2);">
                <fmt:message key="tab.client.HotHours"/>
            </button>
        </div>

        <!-- Tabs -->
        <div id="Tour" class="w3-container w3-white w3-padding-16 myLink">
            <form name="frm" class="myForm" action="controller" method="post">

                <h3><fmt:message key="tab.FindTour.heading"/></h3>
                <div class="w3-row-padding">
                    <div class="w3-half">
                        <label><fmt:message key="tab.FindTour.tourType"/> </label>
                        <label>
                            <select class="w3-input w3-border" name="type">
                                <option class="default-option" disabled selected value><fmt:message
                                        key="tab.FindTour.defaultValue"/></option>
                                <c:forEach var="type" items="${requestScope.tourTypes}">
                                    <option>${type}</option>
                                </c:forEach>
                            </select>
                        </label>
                    </div>
                    <div class="w3-half">
                        <label><fmt:message key="tab.FindTour.peoples"/></label>
                        <label>
                            <select class="w3-input w3-border" name="peoples">
                                <option class="default-option" disabled selected value><fmt:message
                                        key="tab.FindTour.defaultValue"/></option>
                                <c:forEach var="person" items="${requestScope.personsCount}">
                                    <option>${person}</option>
                                </c:forEach>
                            </select>
                        </label>
                    </div>
                    <div class="w3-half">
                        <label><fmt:message key="tab.FindTour.hotelType"/></label>
                        <label>
                            <select class="w3-input w3-border" name="hotel">
                                <option class="default-option" disabled selected value><fmt:message
                                        key="tab.FindTour.defaultValue"/></option>
                                <c:forEach var="hotel" items="${requestScope.hotelTypes}">
                                    <option>${hotel}</option>
                                </c:forEach>
                            </select>
                        </label>
                    </div>
                    <div class="w3-half">
                        <label><fmt:message key="tab.FindTour.price"/></label>
                        <input class="w3-input w3-border" type="text"
                               placeholder="<fmt:message key="tab.FindTour.placeholder"/>" name="price">
                    </div>
                </div>

                <input type="hidden" name="command" value="find_tour">

                <p><input class="w3-button w3-dark-grey" type="submit" value="<fmt:message key="tab.FindTour.button"/> "
                          onclick="return validateSearchForm()"></p>
            </form>
            <c:if test="${not empty sessionScope.message}"><b>${sessionScope.message}</b></c:if>
            <table class="table">

                <c:if test="${not empty sessionScope.resultTour}">
                    <thead>
                    <%--<tabH:tableHeading values="Country,City,Type,Persons,Hotel,Date,Price,Action"/>--%>
                    <tabH:tableHeading values="heading.Country,heading.City,heading.Type,heading.Persons,heading.Hotel,heading.Date,heading.Price,heading.sum,heading.Action"
                                       lang="${language}"/>
                    </thead>
                </c:if>

                <tbody>
                <c:forEach var="tour" items="${sessionScope.resultTour}">

                    <tr <c:if test="${tour.hot == 1}">class="hot"</c:if>>
                        <td>${tour.country}</td>
                        <td>${tour.city}</td>
                        <td>${tour.type}</td>
                        <td>${tour.persons}</td>
                        <td>${tour.hotel}</td>
                        <td>${tour.date}</td>
                        <td>${tour.price - tour.price*requestScope.userDiscount}</td>
                        <td>${tour.sum}</td>

                        <td>
                            <form action="controller" method="post">
                                <input type="hidden" name="tourID" value="${tour.id}">
                                <input type="hidden" name="command" value="buy">
                                <input class="w3-button w3-dark-grey" type="submit"
                                       value="<fmt:message key="button.buy"/>">
                            </form>
                        </td>

                    </tr>

                </c:forEach>
                </tbody>
            </table>
        </div>

        <div id="All_tours" class="w3-container w3-white w3-padding-16 myLink">
            <h3><fmt:message key="tab.AllTours.heading"/></h3>
            <table class="table">
                <thead>
                <%--<tabH:tableHeading values="Country,City,Type,Persons,Hotel,Date,Price, Action"/>--%>
                <tabH:tableHeading values="heading.Country,heading.City,heading.Type,heading.Persons,heading.Hotel,heading.Date,heading.Price,heading.sum,heading.Action"
                                   lang="${language}"/>
                                   <%--lang="${sessionScope.language}--%>
                </thead>
                <tbody>
                <action:printAllTours/>
                </tbody>

            </table>
        </div>

        <div id="Hot" class="w3-container w3-white w3-padding-16 myLink">
            <h3><fmt:message key="tab.HotTours.heading1"/></h3>
            <p><fmt:message key="tab.HotTours.heading2"/></p>

            <table class="table">

                <thead>
                <%--<tabH:tableHeading values="Country,City,Type,Persons,Hotel,Date,Price, Action"/>--%>
                <tabH:tableHeading values="heading.Country,heading.City,heading.Type,heading.Persons,heading.Hotel,heading.Date,heading.Price,heading.sum,heading.Action"
                                    lang="${language}"/>

                </thead>

                <tbody>
                <action:printHotTours/>
                </tbody>

            </table>
        </div>
    </div>
</header>

</body>
<script src="../../../style/js/crs.min.js" charset="UTF-8"></script>
<script src="../../../style/js/main_page.js"></script>
</html>
