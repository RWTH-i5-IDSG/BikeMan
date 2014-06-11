'use strict';

velocityApp.controller('FleetmanagerController', ['$scope', 'resolvedCustomer', 'Customer',
    function ($scope, resolvedCustomer, Fleetmanager) {

        $scope.fleetmanagers = resolvedFleetmanagers;

        $scope.create = function () {
            Fleetmanager.save($scope.fleetmanager,
                function () {
                    $scope.fleetmanagers = Fleetmanager.query();
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

    }]);
