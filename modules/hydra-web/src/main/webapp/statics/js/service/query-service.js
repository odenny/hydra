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
    .factory('queryService', function(){
        return {
            initTable:function(traceList){
//                $.extend( $.fn.dataTableExt.oStdClasses, {
//                    "sSortAsc": "header headerSortDown",
//                    "sSortDesc": "header headerSortUp",
//                    "sSortable": "header"
//                } );

                $('#traceTable').dataTable({
                    sDom: "<'row'<'mySpan'l><'mySpan'f>r>t<'row'<'mySpan'i><'mySpan'p>>",
                    sInfo:"Showing _START_ to _END_ of _TOTAL_ entries",
                    aaData:traceList,
                    aoColumns: [
                        { "mData": "serviceId" },
                        { "mData": "timestamp" },
                        { "mData": "duration" },
                        { "mData": "traceId" }
                    ],
                    sPaginationType: "bootstrap",
                    oLanguage: {
                        sLengthMenu: "每页展示 _MENU_ 条数据",
                        sZeroRecords: "没有任何数据",
                        sInfo: "从 _START_ 到 _END_ 总共 _TOTAL_ 条数据",
                        sInfoEmpty: "从 0 到 0 总共 0 条数据",
                        sSearch:"搜索："
//                        sPrevious:"上一页",
//                        sNext:"下一页"
                    },
                    aLengthMenu: [[10, 25, 50, -1], [10, 25, 50, "全部"]]
                } );
            }
        };
    });
