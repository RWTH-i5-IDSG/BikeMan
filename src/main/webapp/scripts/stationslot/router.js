'use strict';

velocityApp
    .config(['$routeProvider', '$httpProvider', '$translateProvider',
        function ($routeProvider, $httpProvider, $translateProvider) {
            $routeProvider
                .when('/stationslot', {
                    templateUrl: 'views/stationslots.html',
                    controller: 'StationSlotController',
                    resolve:{
                        resolvedStationSlot: ['StationSlot', function (StationSlot) {
                            return StationSlot.query();
                        }]
                    }
                })
        }]);
