<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<html lang="en" ng-app="myApp">
<head>
    <title>Hydra</title>
</head>
<script language="JavaScript" type="text/javascript">
    var ctp = "<%=request.getContextPath() %>";
</script>
<body>
<div style="width:100%;background-color: #7AC5CD;height: 50px;color: #ffffff;font-size:35px;">
    <div style="padding: 10 0 0 10">Hydra</div>
</div>
<div style="width: 100%;" ng-view></div>



<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/statics/lib/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/statics/css/app.css" />


<script type="text/javascript" src="<%=request.getContextPath() %>/statics/lib/jquery-1.8.0.min.js"></script>
<script src="<%=request.getContextPath() %>/statics/lib/angular/angular.js"></script>
<script src="<%=request.getContextPath() %>/statics/lib/angular/angular-resource.js?"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/lib/bootstrap.min.js"></script>

<script src="<%=request.getContextPath() %>/statics/js/app.js"></script>
<!-- repo -->
<script src="<%=request.getContextPath() %>/statics/js/repository/trace-repo.js"></script>
<script src="<%=request.getContextPath() %>/statics/js/repository/service-repo.js"></script>

<!-- service -->
<script src="<%=request.getContextPath() %>/statics/js/service/sequence-service.js"></script>
<script src="<%=request.getContextPath() %>/statics/js/service/tree-service.js"></script>
<script src="<%=request.getContextPath() %>/statics/js/service/query-service.js"></script>
<!-- controller -->
<script src="<%=request.getContextPath() %>/statics/js/controller/query-controller.js"></script>

<!-- filter -->
<script src="<%=request.getContextPath() %>/statics/js/filters.js"></script>
<%--<script src="/statics/js/directives.js"></script>--%>
</body>
</html>