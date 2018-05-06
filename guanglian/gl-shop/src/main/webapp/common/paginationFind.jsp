<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ page session="false"%>


<ul class="pagination pull-right">
	<c:choose>
		<c:when test="${requestScope.paginationData.startPages eq 1}">
			<!-- li ><a href="javascript:void(0);"class="disabled">|&lt;</a></li>
 									<li ><a href="javascript:void(0);"class="disabled">&lt;&lt;</a></li-->
		</c:when>
		<c:otherwise>
			<li><a href="javascript:void(0);" onclick="doAction('1')"><span
					class="glyphicon glyphicon-step-backward padding-height"
					aria-hidden="true"></span></a></li>
			<li><a href="javascript:void(0);"
				onclick="doAction('${requestScope.paginationData.startPages-1}')"><span
					class="glyphicon glyphicon-backward padding-height"
					aria-hidden="true"></span></a></li>
		</c:otherwise>
	</c:choose>
	<c:forEach begin="${requestScope.paginationData.startPages}"
		end="${requestScope.paginationData.showPages}"
		varStatus="paginationDataStatus">
		<li
			class="${requestScope.paginationData.currentPage eq (paginationDataStatus.index) ? 'active' : ''}">
			<c:choose>
				<c:when
					test="${requestScope.paginationData.currentPage eq paginationDataStatus.index}">
					<a href="javascript:void(0);" class="disabled">${paginationDataStatus.index}</a>
				</c:when>
				<c:otherwise>
					<a href="javascript:void(0);"
						onclick="doAction('${paginationDataStatus.index}')">${paginationDataStatus.index}</a>
				</c:otherwise>
			</c:choose>
		</li>
	</c:forEach>
	<c:choose>
		<c:when
			test="${requestScope.paginationData.startPages+4 ge requestScope.paginationData.totalPages}">
			<!-- li ><a href="javascript:void(0);" class="disabled">&gt;&gt;</a></li>
 									<li ><a href="javascript:void(0);" class="disabled">&gt;|</a></li-->
		</c:when>
		<c:otherwise>
			<li><a href="javascript:void(0);"onclick="doAction('${requestScope.paginationData.startPages+5}')"><span
					class="glyphicon glyphicon-forward padding-height" aria-hidden="true"></a></li>
			<li><a href="javascript:void(0);"
				onclick="doAction('${requestScope.paginationData.totalPages}')"><span class="glyphicon glyphicon-step-forward padding-height" aria-hidden="true"></a></li>
		</c:otherwise>
	</c:choose>
</ul>
