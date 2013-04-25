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
angular.module('hydra.services.query', [])
    .factory('queryService', ['$filter','$rootScope', '$compile',  function($filter, $rootScope, $compile){
        return {
            initTable:function(traceList, scope1){
//                $('#traceTable').dataTable({
//                    sDom: "<'row'<'mySpan'l><'mySpan'f>r>t<'row'<'mySpan'i><'mySpan'p>>",
//                    sInfo:"Showing _START_ to _END_ of _TOTAL_ entries",
//                    aaData:traceList,
//                    aoColumns: [
//                        { "mData": "serviceId" },
//                        { "mData": "timestamp" , "sClass": "center"},
//                        { "mData": "duration" ,"sClass": "center"},
//                        { "mData": "traceId" ,"sClass": "center"}
//                    ],
//                    sPaginationType: "bootstrap",
//                    oLanguage: {
//                        sLengthMenu: "每页展示 _MENU_ 条数据",
//                        sZeroRecords: "没有任何数据",
//                        sInfo: "从 _START_ 到 _END_ 总共 _TOTAL_ 条数据",
//                        sInfoEmpty: "从 0 到 0 总共 0 条数据",
//                        sSearch:"搜索：",
//                        oPaginate: {
//                            sPrevious: "上一页",
//                            sNext: "下一页"
//                        }
//                    },
//                    aLengthMenu: [[25, 50, 100, -1], [25, 50, 100, "全部"]],
//                    iDisplayLength: 25,
//                    fnRowCallback: function( nRow, aData, iDisplayIndex ) {
//                        $('td:eq(0)', nRow).html($('#traceTable').data['serviceName']);
//                        $('td:eq(1)', nRow).html($filter('date')(aData['timestamp'], "yyyy-MM-dd HH:mm:ss"));
//                        console.log($rootScope.$new().query);
//                        $('td:eq(3)', nRow).html('<div compile="query.sum"></div>')
////                        $('td:eq(3)', nRow).html('<button type="button" class="btn btn-info" >查看详细</button>');
//                    }
//                } );
                var element = $compile('<button ng-click="test1()"></button>')(scope1)
                $('body').prepend(element);
            },
            initDate : function(){
                $('#startTime').datetimepicker({
                    language:  'zh-CN',
                    weekStart: 1,
                    todayBtn:  1,
                    autoclose: 1,
                    todayHighlight: 1,
                    startView: 2,
                    forceParse: 0,
                    pickerReferer:'input'
                });
            },
            loadTableData : function(traceList){
                $('#traceTable').dataTable().fnClearTable();
                $('#traceTable').dataTable().fnAddData(traceList);
            },
            setTableServiceName : function(serviceName){
                $('#traceTable').data['serviceName'] = serviceName;
            }
        };
    }]);


//// declare a new module, and inject the $compileProvider
//angular.module('compile', [], function($compileProvider) {
//    // configure new 'compile' directive by passing a directive
//    // factory function. The factory function injects the '$compile'
//    $compileProvider.directive('compile', function($compile) {
//        // directive factory creates a link function
//        return function(scope, element, attrs) {
//            scope.$watch(
//                function(scope) {
//                    // watch the 'compile' expression for changes
//                    return scope.$eval(attrs.compile);
//                },
//                function(value) {
//                    // when the 'compile' expression changes
//                    // assign it into the current DOM
//                    element.html(value);
//
//                    // compile the new DOM and link it to the current
//                    // scope.
//                    // NOTE: we only compile .childNodes so that
//                    // we don't get into infinite loop compiling ourselves
//                    $compile(element.contents())(scope);
//                }
//            );
//        };
//    })
//});