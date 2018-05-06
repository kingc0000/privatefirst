<!DOCTYPE >
<html >
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/guanglian-tags.tld" prefix="sm" %>  
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, user-scalable=no,initial-scale=1" />
<meta name="renderer" content="webkit|ie-comp|ie-stand" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="description" content="kekeinfo信息科技有限公司，专注于企业服务软件开发和IT服务">
<meta name="author" content="kekeinfo" />
<meta name="keyword" content="kekeinfo，软件开发，IT服务" />
<link rel="shortcut icon" href='<c:url value="/resources/img/favicon.ico"/>' type="image/x-icon" />
<title>上海广联环境岩土工程股份有限公司</title>
<script>
var _context = '${pageContext.request.contextPath}';
</script>
<!-- Bootstrap core CSS -->
<link href='<c:url value="/resources/css/bootstrap.min.css" />'
	rel="stylesheet">
<link href='<c:url value="/resources/css/bootstrap-reset.css" />' rel="stylesheet" />
<!--external css-->
<link
	href='<c:url value="/resources/assets/font-awesome/css/font-awesome.css" />' rel="stylesheet" />
<!--toastr-->
<link href='<c:url value="/resources/assets/toastr-master/toastr.css"/>' rel="stylesheet" type="text/css" />

<!-- mask -->
<link href='<c:url value="/resources/css/mask.css"/>' rel="stylesheet" type="text/css" />


<!-- Custom styles for this template -->
<link href='<c:url value="/resources/css/style.css" />' rel="stylesheet" />
<link href='<c:url value="/resources/css/style-responsive.css" />' rel="stylesheet" />

<!-- HTML5 shim and Respond.js IE8 support of HTML5 tooltipss and media queries -->
<!--[if lt IE 9]>
    <script href='<c:url value="/resources/js/html5shiv.js" />'></script>
    <script href='<c:url value="/resources/js/respond.min.js" />'></script>
    <script type="text/javascript">	
		alert('您的IE浏览器版本太低，请升级到高版本，谢谢！');
		window.location.href='<c:url value="/water/nonsupport.html"/>';
	</script>
<![endif]-->
<script src='<c:url value="/resources/js/jquery.js" />'></script>
<script src='<c:url value="/resources/js/fastclick.js"/>'></script>
<style type="text/css">
.functiondiv {
	margin: 30px 0;
}
.functiondiv img {
	width: 100%;
	margin:10px 0;
}
.gray { 
    -webkit-filter: grayscale(100%);
    -moz-filter: grayscale(100%);
    -ms-filter: grayscale(100%);
    -o-filter: grayscale(100%);
    filter: grayscale(100%);
    filter: gray;
}
.deal-badge {
	top: 3px;
    right: 30%;
    position: absolute;
}
@media (max-width: 768px) {
	.functiondiv {
		text-align:center;
	}
	.functiondiv img {
		width: 100%;
		margin:10px 0;
	}
	.deal-badge {
	    right: 15%;
	}	
	.wrapper{
		margin-top: 20px;
		padding:20px;
	}
}
</style>
</head>

<script type="text/javascript">
var apptype='';
var app = navigator.userAgent.indexOf("android");
if(app==-1){
	app = navigator.userAgent.indexOf("iOS");
	if(app!=-1){
		apptype='iOS';
	}
}else{
	apptype='android';
}
$(function() {
    FastClick.attach(document.body);
});
</script>
<body>
	<div id="loading">
	<div id="loading-center">
		<div id="loading-center-absolute">
			<div class="object" id="object_four"></div>
			<div class="object" id="object_three"></div>
			<div class="object" id="object_two"></div>
			<div class="object" id="object_one"></div>
		</div>
	</div>
 
	</div>
	<section id="container" class="full-width">
		<!--header start-->
		<header class="header white-bg">
			<a href='#' class="logo"><span class=" hidden-xs">上海</span><span>广联环境岩土</span><span class=" hidden-xs">工程股份有限公司</span></a>
			<!--logo end-->
			<div class="top-nav ">
				<ul class="nav pull-right top-menu">
					<!-- user login dropdown start-->
					<li class="dropdown "><a data-toggle="dropdown"
						class="dropdown-toggle needsclick" href="#"> 
						<c:choose>
							<c:when test="${not empty requestScope.headimg }">
								<img alt="person" src='<sm:contentImage imageName="${requestScope.headimg}" imageType="LOGO"/>' width="30" height="30">
							</c:when>
							<c:otherwise>
								<img alt="person" src='<c:url value="/resources/img/avatar1_small.jpg"/>' >
							</c:otherwise>
						</c:choose>
						 <span
							class="username"><sec:authentication property="principal.username" /></span> <b class="caret"></b>
					</a>
						<ul class="dropdown-menu extended logout">
							<li class="log-arrow-up"></li>
							<li><a href='<c:url value="/water/users/displayUser.html"/>'><i class=" fa fa-user"></i><s:message code="label.basic.info" text="Basic Information"/></a></li>
							<li><a href='<c:url value="/water/users/password.html"/>'><i class="fa fa-chain"></i><s:message code="label.change.password" text="Change Password"/></a></li>
							<li><a href='<c:url value="/water/users/head.html"/>'><i class="fa fa-list"></i><s:message code="label.set.avatar" text="Set Avatar"/></a></li>
							<li><a href='<c:url value="/water/amessage/list.html"/>' class="hidden-xs"><i class="fa fa-bell-o"></i>消息中心<c:if test="${sessionScope.user_un_read!=0}">
																	<span class="badge bg-important">${sessionScope.user_un_read }</span>
																</c:if></a></li>
							<li><a href="#" onclick="logonout()"><i class="fa fa-key"></i><s:message code="label.logout" text="Log Out"/></a></li>
						</ul></li>
					<!-- user login dropdown end -->
				</ul>
			</div>
		</header>
		<!--header end-->
		<!--main content start-->
		<section id="main-content">
			<section class="wrapper container-fluid">
				<div class="row functiondiv">
					<div class="col-xs-6 col-sm-2 col-sm-offset-0 col-xs-offset-0">
						<c:choose>
							<c:when test="${camera == '1' }">
								<a href='<c:url value="/water/camera.html"/>'><img src='<c:url value="/resources/img/camera.jpeg"/>'/></a>		
							</c:when>
							<c:otherwise>
								<img src='<c:url value="/resources/img/camera.jpeg"/>' class="gray hidden"/>
							</c:otherwise>
						</c:choose>
						
					</div>
					<div class="col-xs-6 col-sm-2 col-xs-offset-0 col-sm-offset-0">
						<c:choose>
							<c:when test="${water == '1' }">
								<c:choose>
									<c:when test="${sessionScope.app==null || sessionScope.app==''}">
										<a href='<c:url value="/water/csite/pcfrom/chome.html"/>'><img src='<c:url value="/resources/img/water.jpeg"/>'/>
											<c:if test="${sessionScope.user_report_deal!=0 }"><span class="badge bg-important deal-badge">${sessionScope.user_report_deal }</span></c:if>
										</a>
									</c:when>
									<c:otherwise>
										<a href='<c:url value="/water/csite/phonefrom/chome.html"/>'><img src='<c:url value="/resources/img/water.jpeg"/>'/>
											<c:if test="${sessionScope.user_report_deal!=0 }"><span class="badge bg-important deal-badge">${sessionScope.user_report_deal }</span></c:if>
										</a>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<img src='<c:url value="/resources/img/water.jpeg"/>' class="gray hidden"/>
							</c:otherwise>
						</c:choose>
						
					</div>
					<div class="col-xs-6 col-sm-2 col-sm-offset-0 col-xs-offset-0">
						<c:choose>
							<c:when test="${monitor == '1' }">
								<c:choose>
									<c:when test="${sessionScope.app==null || sessionScope.app==''}">
										<a href='<c:url value="/water/monitor.html"/>'><img src='<c:url value="/resources/img/monitor.jpg"/>'/></a>	
									</c:when>
									<c:otherwise>
										<a href='<c:url value="/water/amessage/app.html?type=monitor"/>'><img src='<c:url value="/resources/img/monitor.jpg"/>'/></a>	
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<img src='<c:url value="/resources/img/monitor.jpg"/>' class="gray"/>
							</c:otherwise>
						</c:choose>
						
					</div>
					<div class="col-xs-6 col-sm-2 col-sm-offset-0 col-xs-offset-0">
						<c:choose>
							<c:when test="${guard == '1' }">
								<c:choose>
									<c:when test="${sessionScope.app==null || sessionScope.app==''}">
										<a href='<c:url value="/water/guard.html"/>'><img src='<c:url value="/resources/img/guard.jpg"/>'/></a>
									</c:when>
									<c:otherwise>
										<a href='<c:url value="/water/amessage/app.html?type=guard"/>'><img src='<c:url value="/resources/img/guard.jpg"/>'/></a>	
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<img src='<c:url value="/resources/img/guard.jpg"/>' class="gray"/>
							</c:otherwise>
						</c:choose>
						
					</div>
					<div class="col-xs-6 col-sm-2 col-xs-offset-0 col-sm-offset-0">
						<c:choose>
							<c:when test="${system == '1' }">
								<a href='<c:url value="/water/system.html"/>'><img src='<c:url value="/resources/img/system.jpeg"/>'/></a>		
							</c:when>
							<c:otherwise>
								<img src='<c:url value="/resources/img/system.jpeg"/>' class="gray hidden"/>
							</c:otherwise>
						</c:choose>
						
					</div>
				</div>
			</section>
		</section>
		<!--main content end-->
	<div id="test" style="display: none"></div>
		<!--footer start-->
		<footer class="site-footer navbar-fixed-bottom">
			<div class="text-center" >
				<span style="font-size:14px;">2016 &copy; 上海<span>广联环境岩土</span>工程股份有限公司</span>
			</div>
		</footer>
		<!--footer end-->
	</section>
<!-- Material button -->
    <button class="material-scrolltop" type="button"></button>
	<!-- js placed at the end of the document so the pages load faster -->
	<script src='<c:url value="/resources/js/bootstrap.min.js" />'></script>
	<script class="include"
		src='<c:url value="/resources/js/jquery.dcjqaccordion.2.7.js" />'></script>
	<script src='<c:url value="/resources/js/jquery.scrollTo.min.js" />'></script>
	<script src='<c:url value="/resources/js/jquery.nicescroll.js" />'></script>
	
	<script src='<c:url value="/resources/js/respond.min.js" />'></script>

	<!--custom switch-->
	<script src='<c:url value="/resources/js/bootstrap-switch.js"/>'></script>
	<!--custom checkbox & radio-->
	
	<!--custom tagsinput-->
	<script src='<c:url value="/resources/js/jquery.tagsinput.js" />'></script>

	<!--right slidebar-->
	<script src='<c:url value="/resources/js/slidebars.min.js" />'></script>
	<!--common script for all pages-->
	<script src='<c:url value="/resources/js/common-scripts.js" />'></script>
	<script src='<c:url value="/resources/js/form-component.js"/>'></script>
	<!--toastr-->
	<script src='<c:url value="/resources/assets/toastr-master/toastr.js"/>'></script>
	 <script src='<c:url value="/resources/js/link-hover.js"/>'></script>
	 <script src='<c:url value="/resources/js/jquery.cookie.js" />'></script>
</body>
<script type="text/javascript">
	$(document).ready(function() {
	    $('body').materialScrollTop({
	        revealElement: 'header',
	        revealPosition: 'bottom'
	    });
	    if(apptype=="android" || apptype=="iOS"){
	    	getjobs();
		}
	    
	});
	function getjobs(){
		//alert("开始设置工作安排提醒");
		$('#loading').show();
		$.post('<c:url value="/water/jobs.shtml"/>', function(response){
			if(response!=null && response!=""){
				if(apptype=="android"){
					var alarms='';
					var titles='';
					//ios发布之前的版本
					var deletea='';
					for(var i=0;i<response.length;i++){
						//ios发布之前的版本
						if(response[i].rstatus==0){
							alarms+=response[i].stime;
							titles+=response[i].title;
							//最后一个不要逗号
							if(i!=response.length-1){
								alarms+=",";
								titles+=",";
							}
						}else{
							deletea="delete";
						}
					}
					//ios发布之前的版本
					if(alarms!=''){
						scanUtils.setAlarm(alarms,titles);
					}else if(deletea!=''){
						//清空
						scanUtils.setAlarm("","");
					}
					$('#loading').hide();
				}else if(apptype=="iOS"){
					var list= new Array();
					for(var i=0;i<response.length;i++){
						if(response[i].rstatus==0){
							var ob=new Object();
							ob.fireDate=(response[i].stime);
							ob.content=response[i].title;
							list[list.length]=ob;
						}else{
							window.webkit.messageHandlers.deleteLocalNotfic.postMessage(response[i].stime);
						}	
					}
					if(list.length>0){
						window.webkit.messageHandlers.addLocalNotfic.postMessage(list);
					}else{
						$('#loading').hide();
					}
				}
				
			}else{
				$('#loading').hide();
			}
		}).error(function() {$('#loading').hide();
		});
	}
	 function addLocalNotficSuccess(str){
		 $.post('<c:url value="/water/rjobs.shtml"/>', function(response){
				$('#loading').hide();
			}).error(function() {$('#loading').hide();toastr.error('工作安排同步失败!!!');});
     };
     
     function addLocalNotficFailed(notificTime,num){
    	 $('#loading').hide();
    	 toastr.error('工作安排同步失败!!!333');
     };
</script>
</html>
