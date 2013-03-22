<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

    </style>
</head>
<body>
<div>
    {{trace.id}}
    {{trace.name}}

    <svg width="960" height="500">
        <g transform="translate(120,20)">
            <rect class="background" width="820" height="460"></rect>
            <!-- x轴 -->
            <g class="x axis">
                <g style="opacity: 1;">
                    <line class="tick" y2="-6" x2="0" style="color:red;"></line>
                    <text y="-9" dy="0em" text-anchor="middle" x="0">0</text>
                </g>
                <g style="opacity: 1;" transform="translate(82,0)">
                    <line class="tick" y2="-6" x2="0"></line>
                    <text y="-9" dy="0em" text-anchor="middle" x="0">50,000</text>
                </g>
                <g style="opacity: 1;" transform="translate(164,0)">
                    <line class="tick" y2="-6" x2="0"></line>
                    <text y="-9" dy="0em" text-anchor="middle" x="0">100,000</text>
                </g>
                <g style="opacity: 1;" transform="translate(246,0)">
                    <line class="tick" y2="-6" x2="0"></line>
                    <text y="-9" dy="0em" text-anchor="middle" x="0">150,000</text>
                </g>
                <g style="opacity: 1;" transform="translate(328,0)">
                    <line class="tick" y2="-6" x2="0"></line>
                    <text y="-9" dy="0em" text-anchor="middle" x="0">200,000</text>
                </g>
                <g style="opacity: 1;" transform="translate(410,0)">
                    <line class="tick" y2="-6" x2="0"></line>
                    <text y="-9" dy="0em" text-anchor="middle" x="0">250,000</text>
                </g>
                <g style="opacity: 1;" transform="translate(492,0)">
                    <line class="tick" y2="-6" x2="0"></line>
                    <text y="-9" dy="0em" text-anchor="middle" x="0">300,000</text>
                </g>
                <g style="opacity: 1;" transform="translate(574,0)">
                    <line class="tick" y2="-6" x2="0"></line>
                    <text y="-9" dy="0em" text-anchor="middle" x="0">350,000</text>
                </g>
                <g style="opacity: 1;" transform="translate(656,0)">
                    <line class="tick" y2="-6" x2="0"></line>
                    <text y="-9" dy="0em" text-anchor="middle" x="0">400,000</text>
                </g>
                <g style="opacity: 1;" transform="translate(737.9999999999999,0)">
                    <line class="tick" y2="-6" x2="0"></line>
                    <text y="-9" dy="0em" text-anchor="middle" x="0">450,000</text>
                </g>
                <g style="opacity: 1;" transform="translate(820,0)">
                    <line class="tick" y2="-6" x2="0"></line>
                    <text y="-9" dy="0em" text-anchor="middle" x="0">500,000</text>
                </g>
                <path class="domain" d="M0,-6V0H820V-6"></path>
            </g>
            <g class="enter" transform="translate(0,5)">
                <g style="cursor: pointer; opacity: 1;" transform="translate(0,0)">
                    <rect width="709.51156" height="20" style="fill: #4682b4;"></rect>
                </g>
                <g style="cursor: pointer; opacity: 1;" transform="translate(0,24)">
                    <rect width="270.85748" height="20" style="fill: #4682b4;patting-left:50px;"></rect>
                </g>
                <g style="cursor: pointer; opacity: 1;" transform="translate(0,48)">
                    <rect width="164.03936000000002" height="20" style="fill: #4682b4;"></rect>
                </g>
                <g style="cursor: pointer; opacity: 1;" transform="translate(0,72)">
                    <rect width="147.14244" height="20" style="fill: #4682b4;"></rect>
                </g>
                <g style="cursor: pointer; opacity: 1;" transform="translate(0,96)">
                    <rect width="79.89424" height="20" style="fill: #4682b4;"></rect>
                </g>
                <g style="cursor: pointer; opacity: 1;" transform="translate(0,120)">
                    <rect width="51.32215999999999" height="20" style="fill: #4682b4;"></rect>
                </g>
                <g style="cursor: pointer; opacity: 1;" transform="translate(0,144)">
                    <rect width="49.66576" height="20" style="fill: #4682b4;"></rect>
                </g>
                <g style="cursor: pointer; opacity: 1;" transform="translate(0,168)">
                    <rect width="49.09176" height="20" style="fill: #4682b4;"></rect>
                </g>
                <g style="cursor: pointer; opacity: 1;" transform="translate(0,192)">
                    <rect width="39.776559999999996" height="20" style="fill: #4682b4;"></rect>
                </g>
                <g style="cursor: pointer; opacity: 1;" transform="translate(0,216)">
                    <rect width="6.75024" height="20" style="fill: #4682b4;"></rect>
                </g>
            </g>
            <g class="y axis">
                <line y1="100%"></line>
            </g>
        </g>
    </svg>
</div>
</body>
</html>