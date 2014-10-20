'use strict';

bikeManApp.factory('CardAccount', ['$resource',
    function ($resource) {
        return $resource('app/rest/cardaccount/:cardId', {}, {
            'query': { method: 'GET', isArray: true},
            'enable': { method: 'POST', url: 'app/rest/cardaccount/:cardId/enable', params: {cardId: "@cardId"} },
            'disable': { method: 'POST', url: 'app/rest/cardaccount/:cardId/disable', params: {cardId: "@cardId"} }
        });
    }]);
