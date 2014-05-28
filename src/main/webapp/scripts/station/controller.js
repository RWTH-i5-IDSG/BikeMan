'use strict';

velocityApp.controller('StationController', ['$scope', 'resolvedStation', 'Station', 'CreateEditStation', 'StationSlot',
    function ($scope, resolvedStation, Station, CreateEditStation, StationSlot) {

        $scope.stations = resolvedStation;

        console.log($scope.stations);

//        $scope.update = function () {
//            Station.update($scope.station,
//                function () {
//                    $scope.stations = Station.query();
//                    $('editStationModal').hideModal();
//                    $scope.clear;
//                });
//        }

//        $scope.showUpdate = function (id) {
//            $scope.station = CreateEditStation.get({id: id});
//            console.log($scope.station);
//            $('#saveStationModal').modal('show');
//        };

        $scope.requestSlots = function(id) {
            $scope.stationSlots = StationSlot.query();
        }

        $scope.delete = function (id) {
            Station.delete({id: id},
                function () {
                    $scope.stations = Station.query();
                });
        };

        // numberOfSlots: null,
        $scope.clear = function () {
//            $scope.station = {manufacturerId: null, address: null, name: null, locationLatitude: null, locationLongitude: null, note: null, state: 'OPERATIVE'}
            $scope.station = null;
        };
    }]);


velocityApp.controller('StationDetailController', ['$scope', 'resolvedStation', 'Station',
    function ($scope, resolvedStation, Station) {

        $scope.station = resolvedStation;


    }]);

velocityApp.controller('StationCreateController', ['$scope', 'Station',
    function ($scope, Station)  {

        $scope.station = null;

        $scope.create = function () {
            Station.save($scope.station,
                function () {
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.station = null;
        };

    }]);
