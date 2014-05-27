'use strict';

velocityApp
    .config(['$stateProvider', '$httpProvider', '$translateProvider',
        function ($stateProvider, $httpProvider, $translateProvider) {
            $stateProvider
                .state('stationslot', {
                    url: '/stationslot',
                    templateUrl: 'views/stationslots.html',
                    controller: 'StationSlotController',
                    resolve:{
                        resolvedStationSlot: ['StationSlot', function (StationSlot) {
                            return StationSlot.query();
                        }]
                    }
                })
        }]);
