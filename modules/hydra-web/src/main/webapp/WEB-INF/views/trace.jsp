<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <style type="text/css">

        text {
            font: 10px sans-serif;
        }

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

        .viewDiv{
            border:1px solid red;
            display: inline-table;
            height: 600;
        }

    </style>
</head>
<body>
<div>
    <div id="treeDiv" class="viewDiv" style="width:25%;"></div>
    <div id="sequenceDiv" class="viewDiv" style="width:74%;"></div>
</div>
</body>
</html>