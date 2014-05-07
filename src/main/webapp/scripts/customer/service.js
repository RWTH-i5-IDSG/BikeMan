'use strict';

velocityApp.factory('Customer', ['$resource',
    function ($resource) {
        return $resource('app/rest/customers/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);
