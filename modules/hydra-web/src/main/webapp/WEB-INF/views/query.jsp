<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <style type="text/css">
            /*query*/
        .queryDiv {
            float: left;
            height: auto;
            width: 350px;
            padding: 0 10 0 20;
        }

        .resultDiv {
            float: left;
            height: auto;
            width: 75%;
            margin: 15 10 0 20;
            border: solid 1px #ddd;
            -webkit-border-radius: 4px;
        }

        <!--
        bootstrap-table

        -->
        div.dataTables_length label {
            float: left;
            text-align: left;
        }

        div.dataTables_length select {
            width: 80px;
        }

        div.dataTables_filter label {
            float: right;
        }

        div.dataTables_filter input {
            height: 30px;
        }

        div.dataTables_info {
            padding-top: 8px;
        }

        div.dataTables_paginate {
            float: right;
            margin: 0;
        }

        table {
            margin: 1em 0;
            clear: both;
        }

        table.dataTable th:active {
            outline: none;
        }

        .sorting_1 {

        }

        .mySpan {
            width: 95%;
        }

        .dataTables_info {
            padding-left: 20px;
        }

        .dataTables_length {
            padding-left: 20px;
        }

        #traceTable tr.even:hover td {
            background-color: #DDFF75;
        }

        #traceTable tr.odd:hover td {
            background-color: #E6FF99;
        }

            /*trace*/
        rect.background {
            fill: white;
        }

        .axis {
            shape-rendering: crispEdges;
        }

        .axis path, .axis line {
            fill: none;
            stroke: #000;
        }

        .viewDiv {
            display: inline-block;
            height: auto;
        }

        .node rect {
            cursor: pointer;
            fill: #fff;
            fill-opacity: .5;
            stroke: #3182bd;
            stroke-width: 1px;
        }

        .node text {
            font: 10px sans-serif;
            pointer-events: none;
        }

        path.link {
            fill: none;
            stroke: #9ecae1;
            stroke-width: 1.5px;
        }

        .traceDiv {
            height: auto;
            width: 98%;
            margin: 20 20 20 20;
            border: solid 1px #ddd;
        }

        .myNavbar {
            height: 18px;
        }

    </style>
    <link href="/statics/lib/bootstrap/datetimepicker/css/datetimepicker.css" rel="stylesheet" media="screen">
    <link href="/statics/lib/DataTables-1.9.4/media/css/jquery.dataTables.css" rel="stylesheet" type="text/css"/>
    <link href="/statics/lib/qTip2-2.0.1/dist/jquery.qtip.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="/statics/lib/d3/d3.js"></script>
    <script src="/statics/lib/qTip2-2.0.1/dist/jquery.qtip.js" type="text/javascript"></script>
    <script type="text/javascript" src="/statics/lib/DataTables-1.9.4/media/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="/statics/lib/DataTables-1.9.4/media/js/plugin.js"></script>
    <script type="text/javascript"
            src="/statics/lib/bootstrap/datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript"
            src="/statics/lib/bootstrap/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
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
                        <button class="btn btn-success btn-large" type="submit" style="width: 200px;">查询</button>
                    </td>
                </tr>
                </tbody>
            </table>
        </form>
    </div>
    <div id="result" class="resultDiv">
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
        <div id="treeDiv" class="viewDiv" style="width:25%;"></div>
        <div id="sequenceDiv" class="viewDiv" style="width:74%;"></div>
    </div>
</div>
</body>
</html>