'use strict';

velocityApp.controller('StationController', ['$scope', 'resolvedStation', 'Station', 'CreateEditStation',
    function ($scope, resolvedStation, Station, CreateEditStation) {

        $scope.stations = resolvedStation;

        $scope.create = function () {

//            $scope.geocoder = new google.maps.Geocoder();

            console.log($scope.station);
            Station.save($scope.station,
                function () {
                    $scope.stations = Station.query();
                    $('#saveStationModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function () {
            Station.update($scope.station,
                function () {
                    $scope.stations = Station.query();
                    $('editStationModal').hideModal();
                    $scope.clear;
                });
        }

        $scope.showUpdate = function (id) {
            $scope.station = CreateEditStation.get({id: id});
            console.log($scope.station);
            $('#saveStationModal').modal('show');
        };

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
