'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('hydra.services', ['ngResource'])
    .factory('serviceQuery', function ($resource) {
        return $resource('/rest/service/all', {}, {
            getAllService: {method: 'GET', isArray: true}
        });
    })
    .factory('Trace', function ($resource) {
        return $resource('/rest/trace/:traceId');
    })
    .factory('createView', function () {
        return function(trace){
            var margin = {top: 20, right: 20, bottom: 20, left: 120},
                width = 960 - margin.right - margin.left,
                height = 500 - margin.top - margin.bottom;

            var x = d3.scale.linear()
                .range([0, width]);

            var y = 20; // bar height

            var z = d3.scale.ordinal()
                .range(["steelblue", "#ccc"]); // bar color

            var duration = 750,
                delay = 25;

            var hierarchy = d3.layout.partition()
                .value(function (d) {
                    return d.size;
                });

            var xAxis = d3.svg.axis()
                .scale(x)
                .orient("top");

            var svg = d3.select("body").append("svg")
                .attr("width", width + margin.right + margin.left)
                .attr("height", height + margin.top + margin.bottom)
                .append("g")
                .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

            svg.append("rect")
                .attr("class", "background")
                .attr("width", width)
                .attr("height", height);

            svg.append("g")
                .attr("class", "x axis");

            svg.append("g")
                .attr("class", "y axis")
                .append("line")
                .attr("y1", "100%");
        };
    })


