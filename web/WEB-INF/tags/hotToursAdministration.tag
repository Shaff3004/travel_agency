<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag pageEncoding="UTF-8" %>
<%@tag body-content="empty" trimDirectiveWhitespaces="true"%>
<c:set var="language"
       value="${not empty language ? language : pageContext.request.locale.language}"
       scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="resources" />

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
                       type="submit" value="<fmt:message key="moder.tours.button.hot"/>">
            </form>

            <form action="controller" method="post">
                <input type="hidden" name="tourID" value="${tour.id}">
                <input type="hidden" name="command" value="set_normal">
                <input class="w3-button w3-dark-grey <c:if test="${tour.hot == 0}">invisible</c:if>"
                       type="submit" value="<fmt:message key="moder.tours.button.normal"/>">
            </form>
        </td>
    </tr>
</c:forEach>