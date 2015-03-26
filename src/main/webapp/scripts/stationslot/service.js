'use strict';

bikeManApp.factory('StationSlot', ['$resource',
    function ($resource) {
        return $resource('api/stationslots/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);
