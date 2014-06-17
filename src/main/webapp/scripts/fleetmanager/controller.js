'use strict';

velocityApp.controller('FleetmanagerController', ['$scope', 'resolvedFleetmanagers', 'Fleetmanager',
    function ($scope, resolvedFleetmanagers, Fleetmanager) {

        $scope.fleetmanagers = resolvedFleetmanagers;

        $scope.create = function () {
            Fleetmanager.save($scope.fleetmanager,
                function () {
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.fleetmanager= Fleetmanager.get({id: id});
        };

        $scope.delete = function (id) {
            Fleetmanager.delete({id: id},
                function () {
                    $scope.fleetmanagers = Fleetmanager.query();
                });
        };

        $scope.clear = function () {
            $scope.fleetmanager = null;
        };

        $scope.deleteModal = function (fleetmanager) {
            $scope.managerToDelete = fleetmanager;
        };

    }]);

velocityApp.controller('FleetmanagerCreateController', ['$scope', 'Fleetmanager', '$timeout', '$state',
    function ($scope, Fleetmanager, $timeout, $state)  {

        $scope.fleetmanager = null;
        $scope.createSuccess = false;

        $scope.create = function () {
            Fleetmanager.save($scope.fleetmanager,
                function () {

                    $state.go('fleetmanagers');

//                    $scope.createSuccess = true;
//                    $timeout(function () {
//                        $scope.createSuccess = false;
//                        $scope.clear();
//                    }, 3000);
                });
        };

        $scope.clear = function () {
            $scope.pedelec = null;
        };

    }]);
