'use strict';

bikeManApp.factory('StationErrors', ['$resource',
    function ($resource) {
        return $resource('api/stations/errors', {}, {
            'query': { method: 'GET', isArray: true}
        });
    }]);

bikeManApp.factory('PedelecErrors', ['$resource',
    function ($resource) {
        return $resource('api/pedelecs/errors', {}, {
            'query': { method: 'GET', isArray: true}
        });
    }]);
