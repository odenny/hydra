<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<html lang="en" ng-app="myApp">
<head>
    <title>Hydra</title>
</head>
<body>
<div style="width:100%;background-color: #7AC5CD;height: 50px;color: #ffffff;font-size:20px;">
    Hydra
</div>
<div style="width: 100%;" ng-view></div>



<link href="/statics/lib/qTip2-2.0.1/dist/jquery.qtip.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" href="http://img.bdp.jd.com/bootstrap/2.3.1/css/bootstrap.min.css" />
<link href="/statics/lib/bootstrap/datetimepicker/css/datetimepicker.css" rel="stylesheet" media="screen">


<script type="text/javascript" src="http://img.bdp.jd.com/d3/v3/d3.v3.min.js"></script>

<script type="text/javascript" src="http://img.bdp.jd.com/jquery/jquery-1.8.0.min.js"></script>
<script src="/statics/lib/qTip2-2.0.1/dist/jquery.qtip.js" type="text/javascript"></script>
<script src="/statics/lib/angular/angular.js"></script>
<script src="/statics/lib/angular/angular-resource.js?"></script>
<script type="text/javascript" src="http://img.bdp.jd.com/bootstrap/2.3.1/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/statics/lib/bootstrap/datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="/statics/lib/bootstrap/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js
"></script>



<script src="/statics/js/app.js"></script>
<!-- repo -->
<script src="/statics/js/repository/trace-repo.js"></script>
<script src="/statics/js/repository/service-repo.js"></script>

<!-- service -->
<script src="/statics/js/service/sequence-service.js"></script>
<script src="/statics/js/service/tree-service.js"></script>
<%--<script src="/statics/js/services.js?_version=${staticVersion}"></script>--%>
<script src="/statics/js/controllers.js?_version=${staticVersion}"></script>

<%--<script src="/statics/js/filters.js"></script>--%>
<%--<script src="/statics/js/directives.js"></script>--%>
</body>
</html>