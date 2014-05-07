'use strict';

velocityApp.factory('StationSlot', ['$resource',
    function ($resource) {
        return $resource('app/rest/stationslots/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);
