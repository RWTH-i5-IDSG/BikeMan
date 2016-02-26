'use strict';

bikeManApp.factory('Pedelec', ['$resource',
    function ($resource) {
        return $resource('api/pedelecs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'},
            'update': { method: 'PUT'},
            'getConfig': { method: 'GET', url: 'api/pedelecs/:id/config', params: {"id": "@id"}},
            'updateConfig': { method: 'POST', params: {"manufacturerId": "@id"}}
        });
    }]);
