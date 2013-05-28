'use strict';


// Declare app level module which depends on filters, and services
angular.module('myApp',
        [
            'hydra.services.sequence','hydra.services.tree',
            'hydra.repository.trace','hydra.repository.service','hydra.services.query',
            'hydra.filters'
        ]).
    config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/query', {templateUrl:  ctp + '/index/query.html', controller: QueryCtrl});
    $routeProvider.otherwise({redirectTo: '/query'});
    }]);
