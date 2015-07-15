'use strict';

bikeManApp
    .config(['$stateProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($stateProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $stateProvider
                .state('monitor', {
                    url: '/monitor',
                    templateUrl: 'views/monitor.html',
                    controller: 'MonitorController',
                    resolve: {
                        resolvedSessionStatus: ['Monitor', function (Monitor) {
                            return Monitor.get();
                        }],
                        resolvedStoreList: ['Monitor', function (Monitor) {
                            return Monitor.storeList();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
                .state('monitorDetail', {
                    url: '/monitor/:storeName',
                    templateUrl: 'views/monitorDetail.html',
                    controller: 'MonitorDetailController',
                    resolve: {
                        resolvedStore: ['Monitor', '$stateParams', function (Monitor, $stateParams) {
                            return Monitor.storeDetail({storeName:$stateParams.storeName});
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        }]);
