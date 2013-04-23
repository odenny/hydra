<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <link href="/statics/lib/qTip2-2.0.1/dist/jquery.qtip.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="http://img.bdp.jd.com/d3/v3/d3.v3.min.js"></script>
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

    </style>
</head>
<body>
<div>
    <div id="treeDiv" class="viewDiv" style="width:25%;"></div>
    <div id="sequenceDiv" class="viewDiv" style="width:74%;"></div>
</body>
</html>