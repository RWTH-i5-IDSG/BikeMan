'use strict';

bikeManApp.factory('Address', ['$resource',
    function ($resource) {
        return $resource('api/addresss/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);
