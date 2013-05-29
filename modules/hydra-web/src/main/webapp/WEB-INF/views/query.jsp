<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <script language="javascript" type="text/javascript">
        var ctp = "<%=request.getContextPath() %>";
    </script>
    <link href="<%=request.getContextPath() %>/statics/lib/bootstrap/datetimepicker/css/datetimepicker.css" rel="stylesheet" media="screen">
    <link href="<%=request.getContextPath() %>/statics/lib/DataTables-1.9.4/media/css/jquery.dataTables.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath() %>/statics/lib/jquery.qtip.min.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<%=request.getContextPath() %>/statics/lib/d3.v3.min.js"></script>
    <script src="<%=request.getContextPath() %>/statics/lib/jquery.qtip.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%=request.getContextPath() %>/statics/lib/DataTables-1.9.4/media/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath() %>/statics/lib/DataTables-1.9.4/media/js/plugin.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath() %>/statics/lib/bootstrap/datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath() %>/statics/lib/bootstrap/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
</head>
<body>
<div ng-show="showType == 'query'">
    <div>
        <ul class="nav nav-pills myNavbar">
            <li>
                <a href="">首页</a>
            </li>
            <li class="active">
                <a href="">查询</a>
            </li>
        </ul>
    </div>
    <div id="query" class="queryDiv">
        <form class="form-horizontal" ng-submit="query.submitQuery()" id="myForm" name="myForm">
            <table class="table table-striped table-bordered" style="width: 100%;">
                <thead>
                <tr>
                    <th colspan="2" style="text-align: center;">查询跟踪</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td style="width: 120px;text-align: center;">所属应用:</td>
                    <td>
                        <select style="width: 100%;" ng-model="query.selectApp"
                                ng-options="app.name for app in query.appList" ng-change="query.appChange()">
                            <option value="">选择一个应用</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: center;">服务名:</td>
                    <td>
                        <input id="serviceName" type="text" data-provide="typeahead" style="height: 30px;" required/>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: center;">开始时间:</td>
                    <td>
                        <div id="startTime" class="input-append date form_datetime"
                             data-date-format="yyyy-mm-dd hh:ii" data-link-field="realTime" style="width: 150px;">
                            <input name="start" size="16" type="text" value="" style="height: 30px;width: 100%;"
                                   readonly>
                            <span class="add-on"><i class="icon-remove"></i></span>
                            <span class="add-on"><i class="icon-th"></i></span>
                        </div>
                        <input type="hidden" id="realTime" value="" ng-model="query.startTime"/><br/>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: center;">查询总数:</td>
                    <td>
                        <select ng-model="query.sum" style="width: 100%;">
                            <option>500</option>
                            <option>1000</option>
                            <option>2000</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th colspan="2" style="text-align: center;">可选条件（以下筛选条件只能选择一种）</th>
                </tr>
                <tr>
                    <td style="text-align: center;">调用时长(ms):</td>
                    <td>
                        <input id="durationMin" type="number" min="0" max="5000" style="width: 90px;height: 30px;"
                               ng-model="query.durationMin" ng-change="query.durationChange()"/> --- <input
                            id="durationMax" type="number" min="{{query.durationMin}}" max="5000"
                            style="width: 90px;height: 30px;" ng-model="query.durationMax"
                            ng-change="query.durationChange()"/>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: center;">异常状况:</td>
                    <td>
                        <button id="ex" type="button" class="btn btn-warning" data-toggle="button"
                                ng-click="query.exBtn.click()">{{query.exBtn.name()}}
                        </button>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align: center;">
                        <div class="alert alert-error" id="alertDiv" ng-show="query.invalid">
                            {{query.validateMsg}}
                        </div>
                        <button ng-class="query.queryBtn.myClass" type="submit" style="width: 200px;">{{query.queryBtn.name}}</button>
                    </td>
                </tr>
                </tbody>
            </table>
        </form>
    </div>
    <div id="result" class="resultDiv" ng-style="env.queryDivStyle">
        <table cellpadding="0" cellspacing="0" border="0" class="bordered-table table-striped" id="traceTable"
               ng-show="tableType == 'duration'">
            <thead>
            <tr>
                <th>服务名</th>
                <th>调用时间</th>
                <th>调用时长(ms)</th>
                <th>操作</th>
            </tr>
            </thead>
        </table>
        <table cellpadding="0" cellspacing="0" border="0" class="bordered-table table-striped" id="traceExTable"
               ng-show="tableType == 'ex'">
            <thead>
            <tr>
                <th>服务名</th>
                <th>跟踪id</th>
                <th>调用时间</th>
                <th>异常信息</th>
                <th>操作</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
<div ng-show="showType == 'trace'">
    <div>
        <ul class="nav nav-pills myNavbar">
            <li>
                <a href="" ng-click="returnToQuery()">首页</a>
            </li>
            <li>
                <a href="" ng-click="returnToQuery()">查询</a>
            </li>
            <li class="active"><a href="">{{serviceName}}</a></li>
        </ul>
    </div>
    <div class="traceDiv">
        <div ng-show="!trace.available" class="alert alert-block">当前跟踪数据未收集全，无法展示.</div>
        <div id="treeDiv" class="viewDiv" style="width:25%;" ng-show="trace.available"></div>
        <div id="sequenceDiv" class="viewDiv" ng-show="trace.available" ng-style="env.sequenceDivStyle"></div>
    </div>
</div>
</body>
</html>