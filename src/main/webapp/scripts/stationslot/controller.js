'use strict';

bikeManApp.controller('StationSlotController', ['$scope', 'resolvedStationSlot', 'StationSlot',
    function ($scope, resolvedStationSlot, StationSlot) {

        $scope.stationslots = resolvedStationSlot;

        $scope.create = function () {
            StationSlot.save($scope.stationslot,
                function () {
                    $scope.stationslots = StationSlot.query();
                    $('#saveStationSlotModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.stationslot = StationSlot.get({id: id});
            $('#saveStationSlotModal').modal('show');
        };

        $scope.delete = function (id) {
            StationSlot.delete({id: id},
                function () {
                    $scope.stationslots = StationSlot.query();
                });
        };

        $scope.clear = function () {
            $scope.stationslot = {id: null, sampleTextAttribute: null, sampleDateAttribute: null};
        };
    }]);
