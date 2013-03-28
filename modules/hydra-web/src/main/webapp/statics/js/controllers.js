'use strict';

/* Controllers */

//用来操作多条件查询和grid展现的Ctrl
function QueryCtrl($scope, serviceQuery) {
    $scope.serviceArray = serviceQuery.getAllService();
//    $scope.queryResult = serviceQuery.getAllTrace();
}
//QueryCtrl.$inject = [$scope, queryService];

function TraceCtrl($scope, Trace, getMyTrace, createView, createSpanAndDetail, createTree, createTreeDetail){
    //跟踪的js-model

    var trace = Trace.get({traceId:12},function(t){
        getMyTrace(t);
        createView(t);//生成时序图的svg
        createSpanAndDetail(t);//生成时序图的具体细节

        createTree(t);//生成树的svg
        createTreeDetail(t);//生成树的具体结构

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


function MyCtrl2() {
}
MyCtrl2.$inject = [];
