'use strict';

bikeManApp.factory('Majorcustomer', ['$resource',
    function ($resource) {
        return $resource('api/majorcustomers/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'},
            'update': { method: 'PUT'},
            'searchByLogin': { method: 'GET', url: 'api/majorcustomers/:login'}
        });
    }]);
