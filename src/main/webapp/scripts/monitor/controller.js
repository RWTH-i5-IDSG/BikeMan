bikeManApp.controller('MonitorController', ['$scope', 'Monitor', 'resolvedSessionStatus', 'resolvedStoreList',
    function ($scope, Monitor, resolvedSessionStatus, resolvedStoreList) {

        $scope.sessionStatus = resolvedSessionStatus;
        $scope.storeList = resolvedStoreList;

        $scope.refresh = function () {
            $scope.sessionStatus = Monitor.get();
            $scope.storeList = Monitor.storeList();
        }

        $scope.kill = function(systemId, sessionId) {
            Monitor.kill({systemId: systemId, sessionId: sessionId}, function() {
                $scope.sessionStatus = Monitor.get();
            });
        }


    }]);

bikeManApp.controller('MonitorDetailController', ['$scope', 'Monitor', 'resolvedStore',
    function ($scope, Monitor, resolvedStore) {

        $scope.targetStore = resolvedStore;

        $scope.refresh = function () {
            $scope.store = Monitor.storeDetail({id: $scope.store.linkName})
        }

    }]);
