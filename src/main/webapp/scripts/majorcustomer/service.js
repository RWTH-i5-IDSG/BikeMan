'use strict';

bikeManApp.factory('Majorcustomer', ['$resource',
    function ($resource) {
        return $resource('app/rest/majorcustomers/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'},
            'update': { method: 'PUT'},
            'searchByLogin': { method: 'GET', url: 'app/rest/majorcustomers/:login'}
        });
    }]);
