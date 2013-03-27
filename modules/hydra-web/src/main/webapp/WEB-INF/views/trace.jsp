<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title></title>
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

        .viewDiv{
            border:1px solid red;
            display: inline-block;
            height:auto;
        }

        .node rect {
            cursor: pointer;
            fill: #fff;
            fill-opacity: .5;
            stroke: #3182bd;
            stroke-width: 1.5px;
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

    </style>
</head>
<body>
<div>
    <div id="treeDiv" class="viewDiv" style="width:25%;"></div>
    <div id="sequenceDiv" class="viewDiv" style="width:74%;"></div>
</div>
</body>
</html>