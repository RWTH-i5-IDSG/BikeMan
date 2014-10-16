'use strict';

bikeManApp.factory('Majorcustomer', ['$resource',
    function ($resource) {
        return $resource('app/rest/majorcustomer/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);
