'use strict';

bikeManApp.factory('Customer', ['$resource',
    function ($resource) {
        return $resource('api/customers/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'},
            'update': { method: 'PUT' },
            'searchByName':{ method: 'GET', url: 'api/customers/name/:name', isArray: true},
            'searchByLogin':{ method: 'GET', url: 'api/customers/login/:login'},
            'activate': { method: 'PUT', url: 'api/customers/:id/activate', params: {id: "@id"} },
            'deactivate': {method: 'PUT', url: 'api/customers/:id/deactivate', params: {id: "@id"} }
        });
    }]);
