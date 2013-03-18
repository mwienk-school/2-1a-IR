<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>${title}</title>

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/bootstrap-responsive.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet">
</head>
<body class="${pageClass} page">
	<div id="wrap">
		<!-- Navbar ================================================== -->
		<div class="navbar navbar-inverse navbar-fixed-top">
			<div class="navbar-inner">
				<div class="container">
					<button class="btn btn-navbar" data-target=".nav-collapse"
						data-toggle="collapse" type="button">
						<span class="icon-bar"></span> <span class="icon-bar"></span> <span
							class="icon-bar"></span>
					</button>
					<div class="nav-collapse collapse">
						<ul class="nav">
							<li class=""><a class="brand" href="${pageContext.request.contextPath}/?extreme=${(param.extreme == 'on') ? 'on' : 'off'}">Verhalenbank</a></li>
							<li class=""><a href="${pageContext.request.contextPath}/search?extreme=${(param.extreme == 'on') ? 'on' : 'off'}">Faceted search</a></li>
							<li class=""><a href="${pageContext.request.contextPath}/advanced?extreme=${(param.extreme == 'on') ? 'on' : 'off'}">Advanced search</a></li>
							<li class=""><a href="${pageContext.request.contextPath}/indexer?extreme=${(param.extreme == 'on') ? 'on' : 'off'}">Build index</a></li>
						</ul>
						<div>
							<button id="toggle-extreme" class="btn btn-danger btn-small" type="button" style="float: right">Extreme content</button>
						</div>
					</div>
				</div>
			</div>
		</div>
<!-- 		<header id="overview"> -->
<!-- 			<div class="header-wrapper"> -->
<!-- 				<div class="container"> -->
<!-- 					<h1>Volksverhalenbank</h1> -->
<!-- 					<p class="lead">Er was eens een zoekmachine</p> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 		</header> -->
		<div class="container">
			<div class="row">
				<div class="span2"></div>
				<div class="span12 main">
				<c:if test="${not empty error}">
					<p class="text-error">${error}</p>
				</c:if>