'use strict';

bikeManApp.factory('Pedelec', ['$resource',
    function ($resource) {
        return $resource('app/rest/pedelecs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'},
            'update': { method: 'PUT'},
            'getConfig': { method: 'GET', url: 'app/rest/pedelecs/:id/config', params: {"id": "@id"}},
            'updateConfig': { method: 'POST', params: {"manufacturerId": "@id"}}
        });
    }]);
