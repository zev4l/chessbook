<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%session.invalidate();%>
<%response.sendRedirect(request.getContextPath() + "/index.jsp");%>