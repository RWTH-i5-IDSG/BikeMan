'use strict';

velocityApp.factory('Pedelec', ['$resource',
    function ($resource) {
        return $resource('app/rest/pedelecs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'},
            'update': { method: 'PUT'}
        });
    }]);
