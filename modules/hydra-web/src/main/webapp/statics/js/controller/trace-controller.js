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
function TraceCtrl($scope, $location, Trace, sequenceService, treeService){

    var params = $location.search();
    var traceId = params['traceId'];
    $scope.serviceName = params['serviceName'];
    $scope.returnToQuery = function(){

    }

    var trace = Trace.get({traceId:traceId},function(t){
        sequenceService.getMyTrace(t);
        var spanMap = sequenceService.getSpanMap(t);

        sequenceService.createView(t);//生成时序图的svg
        sequenceService.createSpanAndDetail(t, spanMap);//生成时序图的具体细节

        treeService.createTree(t);//生成树的svg
        treeService.createTreeDetail(t);//生成树的具体结构
    });

    $scope.trace = trace;


//    trace = {
//        traceId: 'trace123',
//        serviceName: 'testService|getUser',
//        rootSpan: {
//            spanId: 'span0001',
//            spanName: 'getSpan1',
//            durationClient: 479,
//            durationServer: 442,
//            used:{
//                start:1363910400123,//clientSend
//                duration:442,//durationServer
//                viewIndex: 0,
//                type:'used'
//            },
//            wasted:{
//                start:1363910400565,//clientSend + durationServer
//                duration:37,//durationClient - durationServer
//                viewIndex: 0,
//                type:'wasted'
//            },
//            annotations:[
//                {cs: 1363910400123},//clientSend
//                {ss: 1363910400582},//serverSend
//                {sr: 1363910400140},//senverReceive
//                {cr: 1363910400602}//clientReceive
//            ],
//            children: [
//                {
//                    spanId: 'span0002',
//                    spanName: 'getSpanA',
//                    durationClient: 150,
//                    durationServer: 123,
//                    used:{
//                        start:1363910400150,//clientSend
//                        duration:123,//durationServer
//                        viewIndex: 1,
//                        type:'used'
//                    },
//                    wasted:{
//                        start:1363910400273,//clientSend + durationServer
//                        duration:27,//durationClient - durationServer
//                        viewIndex: 1,
//                        type:'wasted'
//                    },
//                    annotations:[
//                        {clientSend: 1363910400150},
//                        {serverSend: 1363910400278},
//                        {senverReceive: 1363910400155},
//                        {clientReceive: 1363910400300}
//                    ]
//                },
//                {
//                    spanId: 'span0003',
//                    spanName: 'getSpanB',
//                    durationClient: 260,
//                    durationServer: 225,
//                    used:{
//                        start:1363910400310,//clientSend
//                        duration:143,//durationServer
//                        viewIndex: 2,
//                        type:'used'
//                    },
//                    wasted:{
//                        start:1363910400453,//clientSend + durationServer
//                        duration:35,//durationClient - durationServer
//                        viewIndex: 2,
//                        type:'wasted'
//                    },
//                    annotations:[
//                        {clientSend: 1363910400310},
//                        {serverSend: 1363910400560},
//                        {senverReceive: 1363910400335},
//                        {clientReceive: 1363910400570}
//                    ]
//                }
//            ]
//        }
//    };

}