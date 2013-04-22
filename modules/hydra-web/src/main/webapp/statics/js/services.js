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
    .factory('TraceQuery', function ($resource) {
        return $resource('/rest/trace/list/:serviceId');
    })
    .factory('createView', function () {
        return function (trace) {
            var view = {};
            var margin = {top: 20, right: 40, bottom: 20, left: 5};
            view.width = $('#sequenceDiv').width() - margin.right - margin.left;
            view.height = 600 - margin.top - margin.bottom;

            view.x = d3.scale.linear()
                .range([0, view.width]);

            view.y = 20; // bar height

            view.duration = 750;
            view.delay = 25;
            view.color = {};
            view.color.used = '#1AC8AF';
            view.color.wasted = '#C3ECF2';


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


            view.svg.append("svg:text")
                .attr("dy", 5)
                .attr("dx", view.width + 2)
                .text("ms");
            trace.view = view;
        };
    })
    .factory('createSpanAndDetail', function (createSpanTip) {
        return function (trace, spanMap) {

            var rootSpan = trace.rootSpan;
            var view = trace.view;
            if (!rootSpan.children) return;

            view.x.domain([0, rootSpan.durationClient]).nice();

            var enter = bar(rootSpan, spanMap)
                .attr("transform", stack(0))
                .style("opacity", 1)
                .style('fill', '#F0FFF0');


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
                    return view.color[time.type];
                });

            //生成每一个span
            function bar(rootSpan) {
                var spans = [rootSpan.used, rootSpan.wasted];

                var maxIndex = 0;
                function pushChildren(span) {
                    if (span.children) {
                        for (var i in span.children) {
                            if (span.children[i].used.viewIndex > maxIndex){
                                maxIndex = span.children[i].used.viewIndex;
                            }
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
                    .attr('span', function (time) {
                        return time.spanId;
                    })
                    .attr('viewindex', function (time) {
                        return time.viewIndex;
                    })
                    .attr('timetype', function(time){
                        return time.type;
                    });


                bar.append("rect")
                    .attr("width", function (time) {
                        return view.x(time.duration);
                    })
                    .attr("height", view.y)
                    .on('mouseover', function(){
                        d3.select(this).attr('stroke', '#F3B1A5').attr('stroke-width', '3px');
                    })
                    .on('mouseout', function(){
                        d3.select(this).attr('stroke-width', '0px');
                    });

                view.svg.append("g")
                    .attr("class", "y axis")
                    .attr('id', 'yaxis')
                    .append("line")
                    .attr("y1", (maxIndex+1) * view.y * 1.2);

                createSpanTip(spanMap, view);//tip

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
    .factory('createTree', function () {
        return function (trace) {
            var view = {};
            var margin = {top: 20, right: 20, bottom: 20, left: 5};
            $('#treeDiv').append('<div style="margin-top:' + margin.top + '"></div>')
            view.w = $('#treeDiv').width();
            view.h = 600 - margin.top;
            view.i = 0;
            view.barHeight = 20 * 1.2;
            view.barWidth = view.w * 0.5;
            view.duration = 400;
            view.root;

            view.tree = d3.layout.tree().size([view.h, 100]);

            view.diagonal = d3.svg.diagonal()
                .projection(function (d) {
                    return [d.y, d.x];
                });

            view.vis = d3.select("#treeDiv div").append("svg:svg")
                .attr("width", view.w)
                .attr("height", view.h)
                .append("svg:g")
                .attr("transform", "translate(20,15)");
            trace.treeView = view
        };
    })
    .factory('createTreeDetail', function (foldChildrenSequence, unfoldChildrenSequence) {
        return function (trace) {
            var view = trace.treeView;
            var root = trace.rootSpan;
            root.x0 = 0;
            root.y0 = 0;

            update(root);


            function update(source) {

                // Compute the flattened node list. TODO use d3.layout.hierarchy.
                var nodes = view.tree.nodes(root);

                // Compute the "layout".
                nodes.forEach(function (n, i) {
                    n.x = i * view.barHeight;
                    n.y = n.y * 0.5;
                });

                // Update the nodes…
                var node = view.vis.selectAll("g.node")
                    .data(nodes, function (d) {
                        return d.id || (d.id = ++view.i);
                    });

                var nodeEnter = node.enter().append("svg:g")
                    .attr("class", "node")
                    .attr("transform", function (d) {
                        return "translate(" + source.y0 + "," + source.x0 + ")";
                    })
                    .style("opacity", 1e-6);

                // Enter any new nodes at the parent's previous position.
                nodeEnter.append("svg:rect")
                    .attr("y", -view.barHeight / 2)
                    .attr("height", view.barHeight)
                    .attr("width", view.barWidth)
                    .style("fill", color)
                    .on("click", click);

                nodeEnter.append("svg:text")
                    .attr("dy", 3.5)
                    .attr("dx", 5.5)
                    .text(function (d) {
                        return d.spanName;
                    });

                // Transition nodes to their new position.
                nodeEnter.transition()
                    .duration(view.duration)
                    .attr("transform", function (d) {
                        return "translate(" + d.y + "," + d.x + ")";
                    })
                    .style("opacity", 1);

                node.transition()
                    .duration(view.duration)
                    .attr("transform", function (d) {
                        return "translate(" + d.y + "," + d.x + ")";
                    })
                    .style("opacity", 1)
                    .select("rect")
                    .style("fill", color);

                // Transition exiting nodes to the parent's new position.
                node.exit().transition()
                    .duration(view.duration)
                    .attr("transform", function (d) {
                        return "translate(" + source.y + "," + source.x + ")";
                    })
                    .style("opacity", 1e-6)
                    .remove();

                // Update the links…
                var link = view.vis.selectAll("path.link")
                    .data(view.tree.links(nodes), function (d) {
                        return d.target.id;
                    });

                // Enter any new links at the parent's previous position.
                link.enter().insert("svg:path", "g")
                    .attr("class", "link")
                    .attr("d", function (d) {
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
                    .attr("d", function (d) {
                        var o = {x: source.x, y: source.y};
                        return view.diagonal({source: o, target: o});
                    })
                    .remove();

                // Stash the old positions for transition.
                nodes.forEach(function (d) {
                    d.x0 = d.x;
                    d.y0 = d.y;
                });
            }

            // Toggle children on click.
            function click(d) {
                if (d.children) {//如果有子span,收起
                    if (d.children) {
                        foldChildrenSequence(d, trace.view);
                    }
                    d._children = d.children;
                    d.children = null;
                } else {//如果有子span，展开
                    d.children = d._children;
                    d._children = null;
                    if (d.children) {
                        unfoldChildrenSequence(d, trace.view);
                    }
                }
                update(d);
            }

            function color(d) {
                return d._children ? "#23C9B5" : d.children ? "#A4EFE8" : "#EBCCB0";
            }
        }
    })
    .factory('getMyTrace', function () {
        return function (trace) {
            var span = trace.rootSpan;
            var spanIndex = {index: 0}
            getMySpan(span, spanIndex);


            function getMySpan(span, spanIndex) {
                var anMap = {};
                for (var i in span.annotations) {
                    if (span.annotations[i]['value']=='cs') {
                        anMap['cs'] = span.annotations[i]['timestamp'];
                        continue;
                    }
                    if (span.annotations[i]['value']=='ss') {
                        anMap['ss'] = span.annotations[i]['timestamp'];
                        continue;
                    }
                    if (span.annotations[i]['value']=='sr') {
                        anMap['sr'] = span.annotations[i]['timestamp'];
                        continue;
                    }
                    if (span.annotations[i]['value']=='cr') {
                        anMap['cr'] = span.annotations[i]['timestamp'];
                        continue;
                    }
                }
                span.used = {
                    spanId: span.id,
                    start: anMap['cs'],
                    duration: span.durationServer,
                    viewIndex: spanIndex.index,
                    type: 'used'
                }
                span.wasted = {
                    spanId: span.id,
                    start: parseInt(anMap['cs']) + parseInt(span.durationServer),
                    duration: parseInt(span.durationClient) - parseInt(span.durationServer),
                    viewIndex: spanIndex.index,
                    type: 'wasted'
                }
                spanIndex.index++;

                for (var i in span.children) {
                    getMySpan(span.children[i], spanIndex);
                }
            }
        }
    })
    .factory('foldChildrenSequence', function () {
        return function (span, view) {
            hideChildrenSpan(span);
            updateTheOthers(span);

            function hideChildrenSpan(span) {
                for (var i in span.children) {
                    d3.selectAll('g[span="' + span.children[i].id + '"]')
                        .style("opacity", 0)
                    hideChildrenSpan(span.children[i]);
                }
            }


            function updateTheOthers(span) {
                var index = span.used.viewIndex;
                d3.selectAll('#sequenceDiv g').each(function () {
                    var g = d3.select(this);
                    if (g.attr('viewindex') > index) {
                        var transform = g.attr('transform');
                        var y = transform.substring(transform.indexOf(',') + 1, transform.length - 1);
                        transform = transform.substring(0, transform.indexOf(',') + 1) + (parseInt(y) - view.y * 1.2) + ')';

                        g.transition()
                            .duration(view.duration)
                            .attr("transform", transform);
                    }
                });
            }
        }
    })
    .factory('unfoldChildrenSequence', function () {
        return function (span, view) {
            updateTheOthers(span);
            showChildrenSpan(span);

            function showChildrenSpan(span) {
                for (var i in span.children) {
                    d3.selectAll('g[span="' + span.children[i].id + '"]').style("opacity", 1)
                    showChildrenSpan(span.children[i]);
                }
            }

            function updateTheOthers(span) {
                var index = span.used.viewIndex;
                d3.selectAll('#sequenceDiv g').each(function () {
                    var g = d3.select(this);
                    if (g.attr('viewindex') > index) {
                        var transform = g.attr('transform');
                        var y = transform.substring(transform.indexOf(',') + 1, transform.length - 1);
                        transform = transform.substring(0, transform.indexOf(',') + 1) + (parseInt(y) + view.y * 1.2) + ')';

                        g.transition()
                            .duration(view.duration)
                            .attr("transform", transform);
                    }
                });
            }
        };
    })
    .factory('createSpanTip', function(){
        return function(spanMap, view){
            $('#sequenceDiv g[span]').each(function(){
                var spanId = $(this).attr('span');
                var spanModel = spanMap[spanId];

                var isUsed = $(this).attr('timetype')=='used'?true:false;
                $(this).qtip({
                    style:{
                        classes:'alert alert-block',
                        width:150
                    },
                    position:{
                        viewport: $(window)
                    },
                    hide:{
                        delay:200,
                        fixed:true
                    },
                    content:function(){
                        var html = '<div><table class="table table-condensed" style="width:150;font-family:Tahoma;">';

                        html += '<tr><td>服务名:</td><td>'+spanModel.spanName+'</td></tr>';
                        if (isUsed){
                            html += '<tr><td style="text-align:center;"><span class="label label-success">调用时长</span></td>';
                        }else {
                            html += '<tr><td style="text-align:center;"><span class="label label-info">网络消耗</span></td>';
                        }
                        html += '<td>'+(isUsed?spanModel.used.duration:spanModel.wasted.duration)+'ms</td></tr>';
                        html += '</table></div>';
                        return html;
                    }()
                });
            })
        };
    })
    .factory('getSpanMap', function(){
        return function(trace){
            var spanMap = {};
            spanMap[trace.rootSpan.id] = trace.rootSpan;
            setAllSpanIn(trace.rootSpan);

            function setAllSpanIn(span){
                for(var i in span.children){
                    spanMap[span.children[i].id] = span.children[i];
                    setAllSpanIn(span.children[i]);
                }
            }
            return spanMap;
        }
    })
