<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en" ng-app="myApp">
<head>
    <title>Hydra</title>
</head>
<body>
<div ng-view></div>

<script src="/statics/lib/angular/angular.js"></script>
<script src="/statics/lib/angular/angular-resource.js"></script>
<script src="/statics/js/app.js?_version=${staticVersion}"></script>
<script src="/statics/js/services.js"></script>
<script src="/statics/js/controllers.js"></script>
<script src="/statics/js/filters.js"></script>
<script src="/statics/js/directives.js"></script>
</body>
</html>