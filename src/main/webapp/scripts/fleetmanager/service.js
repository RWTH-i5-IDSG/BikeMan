'use strict';

bikeManApp.factory('Fleetmanager', ['$resource',
    function ($resource) {
        return $resource('app/rest/managers/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);
