<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@tag pageEncoding="UTF-8" %>
<%@tag body-content="empty" trimDirectiveWhitespaces="true" %>
<c:set var="language"
       value="${not empty language ? language : pageContext.request.locale.language}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="resources"/>

<c:forEach var="tour" items="${requestScope.hotTours}">

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
                <input class="w3-button w3-dark-grey" type="submit" value="<fmt:message key="button.buy"/>">
            </form>
        </td>
    </tr>

</c:forEach>