'use strict';

velocityApp.factory('Transaction', ['$resource',
    function ($resource) {
        return $resource('app/rest/transactions/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);
