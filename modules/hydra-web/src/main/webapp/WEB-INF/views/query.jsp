<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>
        .viewDiv {
            border: 1px solid red;
            display: inline-block;
            height: auto;
        }
    </style>
</head>
<body>
<div id="query" class="viewDiv container" style="width: 300px;">
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
                <td>结束时间:</td>
                <td>
                    <div id="endTime" class="input-append date form_datetime"
                         data-date-format="yyyy-mm-dd hh:ii" data-link-field="dtp_input1" style="width: 150px;">
                        <input size="16" type="text" value="" style="height: 30px;width: 120px;" readonly>
                        <span class="add-on"><i class="icon-remove"></i></span>
                        <span class="add-on"><i class="icon-th"></i></span>
                    </div>
                    <input type="hidden" id="dtp_input2" value=""/><br/>
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
<div id="result" class="viewDiv" style=""></div>
</body>
</html>