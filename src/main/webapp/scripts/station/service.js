'use strict';

bikeManApp.factory('Station', ['$resource',
    function ($resource) {
        return $resource('api/stations/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'},
            'update': { method: 'PUT' }
        });
    }]);

bikeManApp.factory('CreateEditStation', ['$resource',
    function ($resource) {
        return $resource('api/stations/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'},
            'create': { method: 'POST' },
            'update': { method: 'PUT' },
            'getConfig': { method: 'GET', url: 'api/stations/:id/config', params: {"id": "@id"}},
            'updateConfig': { method: 'POST', params: {"id": "@id"}},
            'rebootStation': { method: 'POST', url: "api/stations/:id/reboot", params: {"id": "@id"}},
            'slotState': { method: 'POST', url: "api/stations/:id/slotState", params: {"id": "@id"}}
        });
    }]);

