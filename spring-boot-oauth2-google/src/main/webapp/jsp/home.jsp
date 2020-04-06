<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h1>This is home</h1>
	<sec:authorize var="isAuthenticated" access="authenticated" ></sec:authorize>
	
	<c:if test="${not isAuthenticated}">
	<a href="login">Login with Google</a>
	</c:if>
	<c:if test="${isAuthenticated}">
	Welcome<sec:authentication property="userAuthentication"/>
	</c:if>
</body>
</html>