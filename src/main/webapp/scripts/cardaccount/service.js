'use strict';

bikeManApp.factory('CardAccount', ['$resource',
    function ($resource) {
        return $resource('app/rest/cardaccounts/:cardId', {}, {
            'query': { method: 'GET', isArray: true},
            'enable': { method: 'POST', url: 'app/rest/cardaccounts/:cardId/enable', params: {cardId: "@cardId"} },
            'disable': { method: 'POST', url: 'app/rest/cardaccounts/:cardId/disable', params: {cardId: "@cardId"} }
        });
    }]);
