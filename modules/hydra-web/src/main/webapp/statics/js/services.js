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
                        return view.x(time.duration);
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
    .factory('createTree', function(){
        return function(trace){
            var view = {};
            var margin = {top: 20, right: 20, bottom: 20, left: 5};
            $('#treeDiv').append('<div style="margin-top:'+margin.top+'"></div>')
            view.w = $('#treeDiv').width();
            view.h = 600 - margin.top;
            view.i = 0;
            view.barHeight = 20*1.1;
            view.barWidth = view.w * .8;
            view.duration = 400;
            view.root;

            view.tree = d3.layout.tree().size([view.h, 100]);

            view.diagonal = d3.svg.diagonal()
                .projection(function(d) { return [d.y, d.x]; });

            view.vis = d3.select("#treeDiv div").append("svg:svg")
                .attr("width", view.w)
                .attr("height", view.h)
                .append("svg:g")
                .attr("transform", "translate(20,15)");
            trace.treeView = view
        };
    })
    .factory('createTreeDetail', function(){
        return function(trace){
            var view = trace.treeView;
            var root = trace.rootSpan;
            root.x0 = 0;
            root.y0 = 0;

            update(root);


            function update(source) {

                // Compute the flattened node list. TODO use d3.layout.hierarchy.
                var nodes = view.tree.nodes(root);

                // Compute the "layout".
                nodes.forEach(function(n, i) {
                    n.x = i * view.barHeight;
                });

                // Update the nodes…
                var node = view.vis.selectAll("g.node")
                    .data(nodes, function(d) { return d.id || (d.id = ++view.i); });

                var nodeEnter = node.enter().append("svg:g")
                    .attr("class", "node")
                    .attr("transform", function(d) { return "translate(" + source.y0 + "," + source.x0 + ")"; })
                    .style("opacity", 1e-6);

                // Enter any new nodes at the parent's previous position.
                nodeEnter.append("svg:rect")
                    .attr("y", -view.barHeight / 2)
                    .attr("height", view.barHeight)
                    .attr("width", function(d){
//                    var num = 0;
//                    while(d.parent){
//                        num ++;
//                        d = d.parent;
//                    }
//                    return 100 - num * 25;
                        return 200;
                    })
                    .style("fill", color)
                    .on("click", click);

                nodeEnter.append("svg:text")
                    .attr("dy", 3.5)
                    .attr("dx", 5.5)
                    .text(function(d) { return d.spanName; });

                // Transition nodes to their new position.
                nodeEnter.transition()
                    .duration(view.duration)
                    .attr("transform", function(d) { return "translate(" + d.y + "," + d.x + ")"; })
                    .style("opacity", 1);

                node.transition()
                    .duration(view.duration)
                    .attr("transform", function(d) { return "translate(" + d.y + "," + d.x + ")"; })
                    .style("opacity", 1)
                    .select("rect")
                    .style("fill", color);

                // Transition exiting nodes to the parent's new position.
                node.exit().transition()
                    .duration(view.duration)
                    .attr("transform", function(d) { return "translate(" + source.y + "," + source.x + ")"; })
                    .style("opacity", 1e-6)
                    .remove();

                // Update the links…
                var link = view.vis.selectAll("path.link")
                    .data(view.tree.links(nodes), function(d) { return d.target.id; });

                // Enter any new links at the parent's previous position.
                link.enter().insert("svg:path", "g")
                    .attr("class", "link")
                    .attr("d", function(d) {
                        var o = {x: source.x0, y: source.y0};
                        return view.diagonal({source: o, target: o});
                    })
                    .transition()
                    .duration(view.duration)
                    .attr("d", view.diagonal);

                // Transition links to their new position.
                link.transition()
                    .duration(view.duration)
                    .attr("d", view.diagonal);

                // Transition exiting nodes to the parent's new position.
                link.exit().transition()
                    .duration(view.duration)
                    .attr("d", function(d) {
                        var o = {x: source.x, y: source.y};
                        return view.diagonal({source: o, target: o});
                    })
                    .remove();

                // Stash the old positions for transition.
                nodes.forEach(function(d) {
                    d.x0 = d.x;
                    d.y0 = d.y;
                });
            }

            // Toggle children on click.
            function click(d) {
                if (d.children) {
                    d._children = d.children;
                    d.children = null;
                } else {
                    d.children = d._children;
                    d._children = null;
                }
                update(d);
            }

            function color(d) {
                return d._children ? "#3182bd" : d.children ? "#c6dbef" : "#fd8d3c";
            }
        }
    })
