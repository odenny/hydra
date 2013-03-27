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
        return function (trace) {
            var view = {};
            var margin = {top: 20, right: 20, bottom: 20, left: 5};
            view.width = $('#sequenceDiv').width() - margin.right - margin.left;
            view.height = 600 - margin.top - margin.bottom;

            view.x = d3.scale.linear()
                .range([0, view.width]);

            view.y = 20; // bar height

            view.duration = 750;
            view.delay = 25;

            view.hierarchy = d3.layout.partition()
                .value(function (d) {
                    return d.size;
                });

            view.xAxis = d3.svg.axis()
                .scale(view.x)
                .orient("top");

            view.svg = d3.select("#sequenceDiv").append("svg")
                .attr("width", view.width + margin.right + margin.left)
                .attr("height", view.height + margin.top + margin.bottom)
                .append("g")
                .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

            view.svg.append("rect")
                .attr("class", "background")
                .attr("width", view.width)
                .attr("height", view.height);

            view.svg.append("g")
                .attr("class", "x axis");

            view.svg.append("g")
                .attr("class", "y axis")
                .append("line")
                .attr("y1", "100%");
            trace.view = view;
        };
    })
    .factory('createSpanAndDetail', function () {
        return function (trace) {

            var rootSpan = trace.rootSpan;
            var view = trace.view;
            if (!rootSpan.children) return;

            view.x.domain([0, rootSpan.durationClient]).nice();

            var enter = bar(rootSpan)
                .attr("transform", stack(0))
                .style("opacity", 1)
                .style('fill', '#F0FFF0');

            enter.select("text").style("fill-opacity", 1e-6);


            // Update the x-axis.
            view.svg.selectAll(".x.axis").transition().duration(view.duration).call(view.xAxis);

            // Transition entering bars to their new position.
            var enterTransition = enter.transition()
                .duration(view.duration)
                .delay(function (d, i) {
                    return i * view.delay;
                })
                .attr("transform", function (time) {
                    return 'translate(' + view.x(time.start - rootSpan.used.start) + ',' + view.y * time.viewIndex * 1.2 + ')';
                })
                .style('fill', function (time) {
                    if (time.type == 'used') {//used
                        return '#00FF7F';
                    } else {//wasted
                        return 'steelblue';
                    }
                });

            //生成每一个span
            function bar(rootSpan) {
                var spans = [rootSpan.used, rootSpan.wasted];

                function pushChildren(span) {
                    if (span.children) {
                        for (var i in span.children) {
                            spans.push(span.children[i].used);
                            spans.push(span.children[i].wasted);
                            pushChildren(span.children[i]);
                        }
                    }
                }

                pushChildren(rootSpan);


                var bar = view.svg.insert("g", ".y.axis")
                    .attr("class", "enter")
                    .attr("transform", "translate(0,5)")
                    .selectAll("g")
                    .data(spans)
                    .enter().append("g")
                    .style("cursor", function (d) {
                        return "pointer";
                    })


                bar.append("text")
                    .attr("x", -6)
                    .attr("y", view.y / 2)
                    .attr("dy", ".35em")
                    .attr("text-anchor", "end")
                    .text(function (d) {
                        return d.spanName;
                    });

                bar.append("rect")
                    .attr("width", function (time) {
                        var v = view.x(time.duration);
                        console.log(v)
                        return v;
                    })
                    .attr("height", view.y);

                return bar;
            }

            //span展开前的位置
            function stack(i) {
                var x0 = 0;
                return function (d) {
                    var tx = "translate(" + x0 + "," + view.y * i * 1.2 + ")";
                    x0 += view.x(d.duration);
                    return tx;
                };
            }

        }
    })
