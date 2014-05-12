'use strict';

velocityApp.controller('StationController', ['$scope', 'resolvedStation', 'Station',
    function ($scope, resolvedStation, Station) {

        $scope.stations = resolvedStation;

        $scope.create = function () {

            $scope.geocoder = new google.maps.Geocoder();

            var address = $scope.station.address.streetAndHousenumber + ', ' + $scope.station.address.zip + ', ' + $scope.station.address.city + ', ' + $scope.station.address.country;

            // use Google geocoder to fetch LatLon geocoordinates for address
            $scope.geocoder.geocode({'address': address},
                function(results, status) {
                    if (status == google.maps.GeocoderStatus.OK) {
//                        console.log(results[0].geometry.location);

                        $scope.station.locationLatitude = results[0].geometry.location.k;
                        $scope.station.locationLongitude = results[0].geometry.location.A;

                        Station.save($scope.station,
                            function () {
                                $scope.stations = Station.query();
                                $('#saveStationModal').modal('hide');
                                $scope.clear();
                            });

                    } else {
                        console.log("Geocode for " + address + " was unsuccessful! ()" + status);
                    }
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
            $scope.station = {address: null, name: null, numberOfSlots: null}
        };
    }]);
