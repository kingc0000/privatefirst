<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page pageEncoding="UTF-8"%>
<div class="col-lg-12 col-sm-12">
	<section class="adv-table " id="no-more-tables">
		<table class="display table table-bordered table-striped"
			id="approvelist">
			<thead>
				<tr>
					<th>序号</th>
					<th>送审时间</th>
					<th>审批节点</th>
					<th>审批人员</th>
					<th>处理人</th>
					<th>处理时间</th>
					<th>处理意见</th>
					<th>处理结果</th>
				</tr>
			</thead>
			<tbody id="approvebody">
			</tbody>
		</table>
	</section>
</div>
<script>
function opinionlist(aData) {
	//处理审批意见
	var tbody = $("#approvebody");
	tbody.html("");
	var opinions = eval(aData["opinions"]);
	$.each(opinions, function(i, item){
		var tr = $("<tr/>");
		if(i/2==0) {
			tr.addClass("odd");
		} else {
			tr.addClass("even");
		}
		tr.append("<td data-title='序号'>"+(i+1)+"</td>");
		tr.append("<td data-title='送审时间'>"+item["dateCreated"]+"</td>");
		var statusMsg = "";
		if(item["auditType"]=="1") statusMsg="审核";
		else if(item["auditType"]=="2") statusMsg="审定";
		else if(item["auditType"]=="5") statusMsg="结束";
		tr.append("<td data-title='审批环节'>"+statusMsg+"</td>");
		tr.append("<td data-title='审批人员'>"+item["users"]+"</td>");
		tr.append("<td data-title='处理人'>"+item["dealer"]+"</td>");
		tr.append("<td data-title='处理时间'>"+item["dateModified"]+"</td>");
		tr.append("<td data-title='处理意见'>"+item["note"]+"</td>");
		var result = "";
		if(item["result"]=="1") result="<span style='color:#78CD51'>通过</span>";
		else if(item["result"]=="0") result="<span style='color:#FF6C60'>不通过</span>";
		else result="待处理";
		tr.append("<td data-title='处理结果'>"+result+"</td>"); 
		tbody.append(tr);
	});
}
</script>
