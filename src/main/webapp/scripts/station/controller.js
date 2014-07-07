'use strict';

velocityApp.controller('StationController', ['$scope', 'resolvedStation', 'Station', 'CreateEditStation', 'StationSlot',
    function ($scope, resolvedStation, Station, CreateEditStation, StationSlot) {

        $scope.stations = resolvedStation;

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

        $scope.getStationDetails = function(station) {

            Station.get({id: station.stationId}, function(requestedStation) {
                station.slots = requestedStation.slots;
            });
        }

        // numberOfSlots: null,
        $scope.clear = function () {
//            $scope.station = {manufacturerId: null, address: null, name: null, locationLatitude: null, locationLongitude: null, note: null, state: 'OPERATIVE'}
            $scope.station = null;
        };
    }]);


velocityApp.controller('StationDetailController', ['$scope', 'resolvedStation', 'Station', 'CreateEditStation',
    function ($scope, resolvedStation, Station, CreateEditStation) {

        $scope.station = resolvedStation;

        $scope.isEditing = false;

        $scope.toggleEdit = function () {
            $scope.station = Station.get({id: $scope.station.stationId});
            $scope.isEditing = !$scope.isEditing;
        }

        $scope.saveStation = function () {

            $scope.saveStationDTO = {
                "stationId": $scope.station.stationId,
                "manufacturerId": $scope.station.manufacturerId,
                "name": $scope.station.name,
                "address": $scope.station.address,
                "locationLatitude": $scope.station.locationLatitude,
                "locationLongitude": $scope.station.locationLongitude,
                "note": $scope.station.note,
                "state": $scope.station.state
            }

            CreateEditStation.update($scope.saveStationDTO, function () {
                $scope.isEditing = false;
            });
        }


    }]);

velocityApp.controller('StationCreateController', ['$scope', 'CreateEditStation', '$timeout',
    function ($scope, CreateEditStation, $timeout)  {

        $scope.station = null;

        $scope.createSuccess = false;

        // create station
        $scope.create = function () {
            // stations have state OPERATIVE by default
            $scope.station.state = "OPERATIVE";
            CreateEditStation.save($scope.station,
                function () {
                    $scope.createSuccess = true;
                    $scope.clear();
                    $timeout(function () {
                        $scope.createSuccess = false;
                    }, 1000);
                });
        };

        $scope.clear = function () {
            $scope.station = null;
        };

    }]);
