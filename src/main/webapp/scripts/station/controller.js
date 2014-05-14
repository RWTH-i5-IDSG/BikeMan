'use strict';

velocityApp.controller('StationController', ['$scope', 'resolvedStation', 'Station',
    function ($scope, resolvedStation, Station) {

        $scope.stations = resolvedStation;

        $scope.create = function () {

            $scope.geocoder = new google.maps.Geocoder();

            var address = $scope.station.address.streetAndHousenumber + ', ' + $scope.station.address.zip + ', ' + $scope.station.address.city + ', ' + $scope.station.address.country;

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
            $scope.station = {address: null, name: null, numberOfSlots: null, locationLatitude: null, locationLongitude: null}
        };
    }]);


velocityApp.controller('StationDetailController', ['$scope', 'resolvedStation', 'Station',
    function ($scope, resolvedStation, Station) {

        $scope.station = resolvedStation;


    }]);
