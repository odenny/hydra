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
angular.module('hydra.services.sequence', [])
    .factory('sequenceService', function(){
        return {
            getMyTrace :function (trace, myScope) {
                var span = trace.rootSpan;
                var spanIndex = {index: 0}

                getMySpan(span, spanIndex);
                trace.spanLength = spanIndex.index + 1;


                function getMySpan(span, spanIndex, myParent, index) {

                    var ip;
                    var anMap = {};
                    //我自己的anMap
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
                            ip = span.annotations[i]['host']['ip'];
                            continue;
                        }
                        if (span.annotations[i]['value']=='cr') {
                            anMap['cr'] = span.annotations[i]['timestamp'];
                            continue;
                        }
                    }

                    //以下进行在页面上校准时间的处理
                    if (myParent){//如果有父span
                        if (index == 0){//如果是第一个子span
                            //按照父span的时间处理
                            anMap['cs'] = myParent.anMap['sr'];
                        }else {
                            //按照自己前一个兄弟span的时间处理
                            var myBrother = myParent.children[index - 1];
                            anMap['cs'] = myBrother['cr'];
                        }
                    }
                    anMap['cr'] = parseInt(anMap['cs']) + parseInt(span.durationClient)
                    //sr约等于网络消耗时长的一半
                    anMap['sr'] = parseInt(anMap['cs']) + (parseInt(span.durationClient) - parseInt(span.durationServer)) / 2;



                    span.used = {
                        spanId: span.id,
                        start: parseInt(anMap['cs']),
                        duration: parseInt(span.durationServer),
                        viewIndex: spanIndex.index,
                        ip: ip,
                        type: 'used',
                        hasEx : span.exception?true:false
                    }
                    span.wasted = {
                        spanId: span.id,
                        start: parseInt(anMap['cs']) + parseInt(span.durationServer),
                        duration: function(){
                            if (parseInt(span.durationServer) < parseInt(span.durationClient)){
                                //正常情况下考虑到网络消耗，客户端时长要长于服务端时长
                                return parseInt(span.durationClient) - parseInt(span.durationServer);
                            }else {
                                //如果出现服务端时长大于客户端时长的情况，有可能是因为服务端调用超时了
                                //既然超时了，就不再考虑网络消耗
                                return 0;
                            }
                        }(),
                        viewIndex: spanIndex.index,
                        type: 'wasted'
                    }
                    span.anMap = anMap;
                    span.serviceName = myScope.serviceMap[span.serviceId];
                    spanIndex.index++;

                    //处理x轴的最大值
                    if (!trace.minTimestamp){
                        trace.minTimestamp = span.used.start;
                    }else {
                        if (span.used.start < trace.minTimestamp){
                            trace.minTimestamp = span.used.start;
                        }
                    }
                    var eachMaxTime = parseInt(span.used.start) + parseInt(span.used.duration) + parseInt(span.wasted.duration);
                    if (!trace.maxTimestamp){
                        trace.maxTimestamp = eachMaxTime;
                    }else {
                        if (eachMaxTime > trace.maxTimestamp){
                            trace.maxTimestamp = eachMaxTime;
                        }
                    }

                    //迭代
                    for (var i in span.children) {
                        getMySpan(span.children[i], spanIndex, span, i);
                    }
                }

            },
            getSpanMap:function(trace){
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
            },
            createView:function (trace) {
                var view = {};
                var margin = {top: 20, right: 40, bottom:0, left: 5};
                view.width = $('#sequenceDiv').width() - margin.right - margin.left;
                view.y = 20; // bar height

                view.height = trace.spanLength * view.y * 1.2 - margin.top - margin.bottom;

                view.x = d3.scale.linear()
                    .range([0, view.width]);

                view.duration = 750;
                view.delay = 25;
                view.color = {};
                view.color.used = '#1AC8AF';
                view.color.wasted = '#C3ECF2';
                view.color.ex = '#FD6773';


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
            },
            createSpanAndDetail:function (trace, spanMap, myScope) {

                var rootSpan = trace.rootSpan;
                var view = trace.view;
//                if (!rootSpan.children) return;
                var mainDuration = parseInt(trace['maxTimestamp']) - parseInt(trace['minTimestamp']);
                view.x.domain([0, mainDuration]).nice();

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
                        return view.color[time.hasEx?'ex':time.type];
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
                        .attr("y1", function(){
                            myScope.env.sequenceDivStyle.height = ((maxIndex+1) * view.y * 1.2 + 50) > 500?((maxIndex+1) * view.y * 1.2 + 50):500;
                            return myScope.env.sequenceDivStyle.height;
                        });

                    createSpanTip(spanMap);//tip

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

                function createSpanTip(spanMap){
                    $('#sequenceDiv g[span]').each(function(){
                        var spanId = $(this).attr('span');
                        var spanModel = spanMap[spanId];

                        var isUsed = $(this).attr('timetype')=='used' || $(this).attr('timetype')=='ex'?true:false;
                        var isEx = spanModel.exception;
                        $(this).qtip({
                            style:{
                                classes:'alert alert-success',
                                width:450,
                                height:'auto'
                            },
                            position:{
                                viewport: $('#sequenceDiv')
                            },
                            hide:{
                                delay:200,
                                fixed:true
                            },
                            content:function(){
                                var html = '<div><table class="table table-condensed" style="font-family:Tahoma;">';

                                html += '<tr><td>服务名:</td><td style="font-size: small;word-break:break-all">'+spanModel.serviceName+'</td></tr>';
                                html += '<tr><td>方法名:</td><td>'+spanModel.spanName+'</td></tr>';

                                if (isUsed){
                                    html += '<tr><td style="text-align:center;">ip</td>';
                                    html += '<td>'+ spanModel.used.ip+'</td></tr>';
                                    html += '<tr><td style="text-align:center;"><span class="label label-success">开始时间</span></td><td>'+spanModel.used.start+'(long)</td></tr>'
                                    html += '<tr><td style="text-align:center;"><span class="label label-success">调用时长</span></td>';
                                }else {
                                    html += '<tr><td style="text-align:center;"><span class="label label-info">网络消耗</span></td>';
                                }
                                html += '<td>'+(isUsed?spanModel.used.duration:spanModel.wasted.duration)+'ms</td></tr>';
                                if (isUsed && isEx){
                                    html += '<tr><td style="text-align:center;"><span class="label label-warning">异常情况</span></td>';
                                    html += '<td style="word-break:break-all" title="'+spanModel.exception.value+'">';
                                    html += spanModel.exception.value.length>400?(spanModel.exception.value.substring(0, 400)+'...'):spanModel.exception.value+'</td></tr>';
                                }
                                html += '</table></div>';
                                return html;
                            }()
                        });
                    })
                }

            }


        }
    });
