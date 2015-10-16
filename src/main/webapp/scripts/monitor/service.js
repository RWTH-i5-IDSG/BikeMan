bikeManApp.factory('Monitor', ['$resource',
    function ($resource) {
        return $resource('api/monitor/session-status', {}, {
            'get' : { method : 'GET', isArray: true},
            'storeList' : { url: 'api/monitor/store-list', method: 'GET', isArray: true},
            'storeDetail': { url: 'api/monitor/store/:storeName', method: 'GET', isArray: false},

            'kill': { method: 'PUT', url: 'api/monitor/session/:systemId/:sessionId', params: {"systemId": "@systemId", "sessionId": "@sessionId"}}
        });
    }]);
