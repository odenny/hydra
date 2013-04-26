<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title></title>
    <link href="/statics/lib/qTip2-2.0.1/dist/jquery.qtip.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="/statics/lib/d3/d3.js"></script>
    <script src="/statics/lib/qTip2-2.0.1/dist/jquery.qtip.js" type="text/javascript"></script>
    <style type="text/css">

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

        .traceDiv{
            height: auto;
            width:98%;
            margin: 20 20 20 20;
            border:solid 1px #ddd;
        }

    </style>
</head>
<body>
<div id="navbar">
    <ul class="breadcrumb" style="height: 15px;">
        <li>
            <a href="" ng-click="returnToQuery()">查询</a> <span class="divider">/</span>
        </li>
        <li class="active" style="width: auto;">
            <a href="#">{{serviceName}}</a> <span class="divider">/</span>
        </li>
    </ul>
</div>
<div class="traceDiv">
    <div id="treeDiv" class="viewDiv" style="width:25%;"></div>
    <div id="sequenceDiv" class="viewDiv" style="width:74%;"></div>
</div>
</body>
</html>