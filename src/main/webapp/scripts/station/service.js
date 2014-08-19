'use strict';

velocityApp.factory('Station', ['$resource',
    function ($resource) {
        return $resource('app/rest/stations/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'},
            'update': { method: 'PUT' }
        });
    }]);

velocityApp.factory('CreateEditStation', ['$resource',
    function ($resource) {
        return $resource('app/rest/stations/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'},
            'create': { method: 'POST' },
            'update': { method: 'PUT' },
            'getConfig': { method: 'GET', url: 'app/rest/stations/:id/config', params: {"id": "@id"}},
            'updateConfig': { method: 'POST', params: {"id": "@id"}},
            'rebootStation': { method: 'POST', url: "app/rest/stations/:id/reboot", params: {"id": "@id"}}
        });
    }]);

