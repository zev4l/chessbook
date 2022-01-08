<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty sessionScope.user}">
    <jsp:forward page="login.jsp"/>
</c:if>

<jsp:forward page="profile.jsp"/>