<%@ tag import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ tag import="net.tanesha.recaptcha.ReCaptcha" %>
<%@tag pageEncoding="UTF-8" %>
<%@tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%
    ReCaptcha c = ReCaptchaFactory.newReCaptcha("6LdNJ0QUAAAAAD-9IQDPVY_sjuHTkohY5vaWdol0", "6LdNJ0QUAAAAANY2gjaMDsbC-5ibXnRljKvzbgTG", false);
    out.print(c.createRecaptchaHtml(null, null));
%>