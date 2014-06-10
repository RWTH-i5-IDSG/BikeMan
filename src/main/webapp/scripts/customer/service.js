'use strict';

velocityApp.factory('Customer', ['$resource',
    function ($resource) {
        return $resource('app/rest/customers/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'},
            'searchByEmail':{ method: 'GET', url: 'app/rest/customers/email/:email'},
            'searchByLogin':{ method: 'GET', url: 'app/rest/customers/login/:login'},
            'searchByName':{ method: 'GET', isArray: true, url: 'app/rest/customers/name/:name'}
        });
    }]);
