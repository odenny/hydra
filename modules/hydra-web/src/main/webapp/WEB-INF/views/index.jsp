<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<html lang="en" ng-app="myApp">
<head>
    <title>Hydra</title>
</head>
<body>
<div style="width:100%;background-color: #7AC5CD;height: 50px;color: #ffffff;font-size:35px;">
    <div style="padding: 10 0 0 10">Hydra</div>
</div>
<div style="width: 100%;" ng-view></div>



<link rel="stylesheet" type="text/css" href="/statics/lib/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="/statics/css/app.css" />


<script type="text/javascript" src="/statics/lib/jquery-1.8.0.min.js"></script>
<script src="/statics/lib/angular/angular.js"></script>
<script src="/statics/lib/angular/angular-resource.js?"></script>
<script type="text/javascript" src="/statics/lib/bootstrap.min.js"></script>

<script src="/statics/js/app.js"></script>
<!-- repo -->
<script src="/statics/js/repository/trace-repo.js"></script>
<script src="/statics/js/repository/service-repo.js"></script>

<!-- service -->
<script src="/statics/js/service/sequence-service.js"></script>
<script src="/statics/js/service/tree-service.js"></script>
<script src="/statics/js/service/query-service.js"></script>
<!-- controller -->
<script src="/statics/js/controller/query-controller.js"></script>

<!-- filter -->
<script src="/statics/js/filters.js"></script>
<%--<script src="/statics/js/directives.js"></script>--%>
</body>
</html>