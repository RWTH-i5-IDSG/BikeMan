'use strict';

bikeManApp.controller('AddressController', ['$scope', 'resolvedAddress', 'Address',
    function ($scope, resolvedAddress, Address) {

        $scope.addresss = resolvedAddress;

        $scope.create = function () {
            Address.save($scope.address,
                function () {
                    $scope.addresss = Address.query();
                    $('#saveAddressModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.address = Address.get({id: id});
            $('#saveAddressModal').modal('show');
        };

        $scope.delete = function (id) {
            Address.delete({id: id},
                function () {
                    $scope.addresss = Address.query();
                });
        };

        $scope.clear = function () {
            $scope.address = {id: null, sampleTextAttribute: null, sampleDateAttribute: null};
        };
    }]);
