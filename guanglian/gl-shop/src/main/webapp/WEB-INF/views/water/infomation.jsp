<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/guanglian-tags.tld" prefix="sm" %> 
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<div class="container-fluid body-500">
    <div class="container">
      <section style="padding:70px 0px;text-align:center;background:#eee;">
          <img  src='<c:url value="/resources/img/${image}"/>'>
          <h4>${information}
          <a class="btn btn-danger" onClick="javascript :history.back(-1);"><s:message code="label.generic.back" text="Back "/></a></h4>
      </section>
    </div>
</div>
