/*
 * Copyright jd
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

'use strict';
angular.module('hydra.services.tree', [])
    .factory('treeService', function () {
        return {
            createTree: function (trace) {
                var view = {};
                var margin = {top: 20, right: 20, bottom: 0, left: 5};
                view.w = $('#treeDiv').width();
                view.i = 0;
                view.barHeight = 20 * 1.2;
                view.barWidth = view.w * 0.5;
                view.duration = 400;
                view.h = trace.spanLength * view.barHeight;

                view.tree = d3.layout.tree().size([view.h, 100]);

                view.diagonal = d3.svg.diagonal()
                    .projection(function (d) {
                        return [d.y, d.x];
                    });

                view.vis = d3.select("#treeDiv").append("svg:svg")
                    .attr("width", view.w)
                    .attr("height", view.h)
                    .append("svg:g")
                    .attr("transform", "translate(20,35)");
                trace.treeView = view
            },
            createTreeDetail: function (trace, myScope) {
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
                            var length = (d.serviceName + '|' + d.spanName).length;
                            var lengthMax = function(){
                                var width = myScope.env.windowWidth;
                                if (width == 1920) {
                                    return 55;
                                } else if (width < 1920 && width >= 1366) {
                                    return 30;
                                } else {
                                    return 25;
                                }
                            }();
                            if (length > lengthMax){
                                return '...' + (d.serviceName + '|' + d.spanName).substring(length - 30, length);
                            }else {
                                return d.serviceName + '|' + d.spanName;
                            }
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

                function foldChildrenSequence(span, view) {
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

                function unfoldChildrenSequence(span, view) {
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
                }

            }
        };
    });

