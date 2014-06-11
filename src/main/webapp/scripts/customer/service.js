'use strict';

velocityApp.factory('Customer', ['$resource',
    function ($resource) {
        return $resource('app/rest/customers/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'},
            'update': { method: 'PUT' },
            'searchByName':{ method: 'GET', url: 'app/rest/customers/name/:name', isArray: true},
            'searchByLogin':{ method: 'GET', url: 'app/rest/customers/login/:login'}
        });
    }]);
