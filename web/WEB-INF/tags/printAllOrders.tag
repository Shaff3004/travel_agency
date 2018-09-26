<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@tag pageEncoding="UTF-8" %>
<%@tag body-content="empty" trimDirectiveWhitespaces="true" %>
<c:set var="language"
       value="${not empty language ? language : pageContext.request.locale.language}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="resources"/>

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
                       type="submit" value="<fmt:message key="moder.orders.button.confirm"/>" onclick="notifyMessage()">
            </form>
            <form action="controller" method="post">
                <input type="hidden" name="orderID" value="${orders.id}">
                <input type="hidden" name="customerID" value="${orders.customer.id}">
                <input type="hidden" name="price" value="${orders.tour.price}">
                <input type="hidden" name="command" value="cancel_order">
                <input class="w3-button w3-dark-grey <c:if test="${orders.status == 'paid' || orders.status == 'canceled'}">invisible</c:if>"
                       type="submit" value="<fmt:message key="moder.orders.button.cancel"/>" onclick="notifyMessage()">
            </form>
        </td>
    </tr>
</c:forEach>
</tbody>