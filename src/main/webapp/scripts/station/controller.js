'use strict';

velocityApp.controller('StationController', ['$scope', 'resolvedStation', 'Station',
    function ($scope, resolvedStation, Station) {

        $scope.stations = resolvedStation;

        $scope.create = function () {
            Station.save($scope.station,
                function () {
                    $scope.stations = Station.query();
                    $('#saveStationModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.station = Station.get({id: id});
            $('#saveStationModal').modal('show');
        };

        $scope.delete = function (id) {
            Station.delete({id: id},
                function () {
                    $scope.stations = Station.query();
                });
        };

        $scope.clear = function () {
            $scope.station = {id: null, sampleTextAttribute: null, sampleDateAttribute: null};
        };
    }]);
