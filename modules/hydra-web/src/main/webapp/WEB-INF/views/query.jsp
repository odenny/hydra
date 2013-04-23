<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style type="text/css">
        .queryDiv {
            float: left;
            height: auto;
            width: 300px;
            padding: 40 0 0 20;
        }
        .resultDiv{
            float: left;
            height: auto;
            width:75%;
            padding: 40 0 0 20;
        }
        <!-- bootstrap-table -->
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
        .sorting_1{

        }
        .mySpan{
            width: 95%;
        }
        .dataTables_info{
            padding-left: 20px;
        }
        .dataTables_length{
            padding-left: 20px;
        }


        #traceTable tr.even:hover td {
            background-color: #DDFF75;
        }


        #traceTable tr.odd:hover td{
            background-color: #E6FF99;
        }

    </style>
    <link href="/statics/lib/bootstrap/datetimepicker/css/datetimepicker.css" rel="stylesheet" media="screen">
    <link href="/statics/lib/DataTables-1.9.4/media/css/jquery.dataTables.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="/statics/lib/DataTables-1.9.4/media/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="/statics/lib/DataTables-1.9.4/media/js/plugin.js"></script>
    <script type="text/javascript" src="/statics/lib/bootstrap/datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="/statics/lib/bootstrap/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
</head>
<body ng-controller="QueryCtrl">
<div id="query" class="queryDiv">
    <form class="form-horizontal">
        <table class="table table-striped" style="width: 100%;">
            <thead>
            <tr>
                <th colspan="2"></th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>服务名：</td>
                <td>
                    <input id="serviceName" type="text" data-provide="typeahead" style="width: 175px;height: 30px;"/>
                </td>
            </tr>
            <tr>
                <td>开始时间:</td>
                <td>
                    <div id="startTime" class="input-append date form_datetime"
                         data-date-format="yyyy-mm-dd hh:ii" data-link-field="dtp_input1" style="width: 150px;">
                        <input size="16" type="text" value="" style="height: 30px;width: 120px;" readonly>
                        <span class="add-on"><i class="icon-remove"></i></span>
                        <span class="add-on"><i class="icon-th"></i></span>
                    </div>
                    <input type="hidden" id="dtp_input1" value=""/><br/>
                </td>
            </tr>
            <tr>
                <td colspan="2" style="text-align: center;">
                    <button class="btn btn-success btn-large" style="width: 200px;">查询</button>
                </td>
            </tr>
            </tbody>
        </table>
    </form>
</div>
<div id="result" class="resultDiv">
    <table cellpadding="0" cellspacing="0" border="0" class="bordered-table table-striped" id="traceTable">
        <thead>
        <tr>
            <th>服务名</th>
            <th>调用时间</th>
            <th>调用时长</th>
            <th>操作</th>
        </tr>
        </thead>
    </table>
</div>
</body>
</html>