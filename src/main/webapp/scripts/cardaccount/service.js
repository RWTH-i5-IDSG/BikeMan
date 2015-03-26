'use strict';

bikeManApp.factory('CardAccount', ['$resource',
    function ($resource) {
        return $resource('api/cardaccounts/:cardId', {}, {
            'query': { method: 'GET', isArray: true},
            'enable': { method: 'POST', url: 'api/cardaccounts/:cardId/enable', params: {cardId: "@cardId"} },
            'disable': { method: 'POST', url: 'api/cardaccounts/:cardId/disable', params: {cardId: "@cardId"} }
        });
    }]);
