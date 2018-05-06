<!DOCTYPE html>
<html>

<!-- Define Charset -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<!-- Responsive Meta Tag -->
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0">

<title>Email Template</title>

<!-- Responsive and Valid Styles -->

<style type="text/css">

body{
    width: 100%;
    background-color: #F1F2F7;
    margin:0;
    padding:0;
    -webkit-font-smoothing: antialiased;
    font-family: arial;
    font-size: 12px;
}

html{
  width: 100%;
}
.clearfix {
  clear:both;
}
.pull-right {
  float: right !important;
}
.pull-left {
  float: left !important;
}
.text-left {
  text-align: left;
}
.text-right {
  text-align: right;
}
.text-center {
  text-align: center;
}
.text-primary {
  color: #337ab7;
}
a.text-primary:hover,
a.text-primary:focus {
  color: #286090;
}
.text-success {
  color: #3c763d;
}
a.text-success:hover,
a.text-success:focus {
  color: #2b542c;
}
.text-info {
  color: #31708f;
}
a.text-info:hover,
a.text-info:focus {
  color: #245269;
}
.text-warning {
  color: #8a6d3b;
}
a.text-warning:hover,
a.text-warning:focus {
  color: #66512c;
}
.text-danger {
  color: #a94442;
}
a.text-danger:hover,
a.text-danger:focus {
  color: #843534;
}
.alert {
  padding: 15px;
  margin-bottom: 20px;
  border: 1px solid transparent;
}
.alert-success {
  color: #3c763d;
  background-color: #dff0d8;
  border-color: #d6e9c6;
}
.alert-success hr {
  border-top-color: #c9e2b3;
}
.alert-success .alert-link {
  color: #2b542c;
}
.alert-info {
  color: #31708f;
  background-color: #d9edf7;
  border-color: #bce8f1;
}
.alert-info hr {
  border-top-color: #a6e1ec;
}
.alert-info .alert-link {
  color: #245269;
}
.alert-warning {
  color: #8a6d3b;
  background-color: #fcf8e3;
  border-color: #faebcc;
}
.alert-warning hr {
  border-top-color: #f7e1b5;
}
.alert-warning .alert-link {
  color: #66512c;
}
.alert-danger {
  color: #a94442;
  background-color: #f2dede;
  border-color: #ebccd1;
}
.alert-danger hr {
  border-top-color: #e4b9c0;
}
.alert-danger .alert-link {
  color: #843534;
}
.container {
  padding-right: 15px;
  padding-left: 15px;
  margin-right: auto;
  margin-left: auto;
}

.email-header{
	background: #7087A3;
	color: #fff;
	padding: 10px 10px;
	height: 20px;
	margin: 10px 0 0 0;
}
.email-header-item{
	width: 50%;
	line-height:20px;
}
.email-content {
	padding: 10px 10px;
	background: #fff;
}
.email-footer {
	background: #7087A3;
    color: #fff;
    padding: 10px 10px;
    margin: 0 0 10px 0;
}
</style>

</head>

<body leftmargin="0" topmargin="0" marginheight="0" marginwidth="0">

<div class="container">
	<!-- email-header start-->
	<div class="email-header">
		<div class="email-header-item pull-left">
			<strong>${EMAIL_STORE_NAME}</strong>
		</div>
		<div class="email-header-item pull-left text-right">
			<strong>${EMAIL_PHONE_LABEL}:${EMAIL_PHONE}</strong>
		</div>
	</div><!--email-header end-->
</div>
<div class="container">
	<!-- email-content start -->
	<div class="email-content">
		<h3>${EMAIL_NEW_USER_TEXT}</h3>
		<p class="lead">
	    	${EMAIL_TEXT_NEW_USER_CREATED}<br/>
	    	${EMAIL_ADMIN_USERNAME_LABEL}: ${EMAIL_ADMIN_NAME}<br />
	    	${EMAIL_ADMIN_PASSWORD_LABEL}: ${EMAIL_ADMIN_PASSWORD}</br>
	    	${EMAIL_ADMIN_URL_LABEL}: ${EMAIL_ADMIN_URL}<br /><br />
		</p>
		
		<hr class="alert-success">		
		<div class="email-tip alert alert-info" style="font-size:12px!important">
			${EMAIL_DISCLAIMER}<br/>
			${EMAIL_SPAM_DISCLAIMER}

		</div>
	</div><!-- email-content end -->
</div>
<div class="container">
	<!-- email-footer start-->
	<div class="email-footer text-center">
		${LOGOPATH}
		<p>${EMAIL_FOOTER_COPYRIGHT}</p>
	</div><!--email-footer end-->
</div>

</body>
</html>