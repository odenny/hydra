'use strict';

/* Filters */

angular.module('hydra.filters', []).
  filter('interpolate', ['version', function(version) {
    return function(text) {
      return String(text).replace(/\%VERSION\%/mg, version);
    }
  }])
    .filter('dateToLong', function() {
        return function(input) {
            var date = new Date(input);
            return date.getTime();
        };
    });
