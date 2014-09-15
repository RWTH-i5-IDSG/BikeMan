/**
 * Created by max on 04/06/14.
 */
'use strict';

angular.module('bikeManApp')
    .filter('usernameFilter', function() {
        return function(input) {
            if (null == input) return 'Account'
            else return input;
        }
    })
    .filter('operativeFilter', function() {
        return function(input) {
            return (input === 'OPERATIVE') ? '\u2713' : '\u2718';
        }
    })
    .filter('occupiedFilter', function() {
        return function(input) {
            return input ? '\u2713' : '\u2718';
        }
    })
    .filter('pedelecViewFilter', function () {
        return function(inputPedelec) {
            if (null == inputPedelec) return '-'
            else return inputPedelec.manufacturerId;
        }
    })
    .filter('capitalizeFirst', function() {
    return function(input, scope) {
        if (input!=null)
            input = input.toLowerCase();
        return input.substring(0,1).toUpperCase()+input.substring(1);
    }
});
