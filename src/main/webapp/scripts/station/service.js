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
            'update': { method: 'PUT' }
        });
    }]);
