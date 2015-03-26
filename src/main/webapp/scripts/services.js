'use strict';

/* Services */

bikeManApp.factory('Account', ['$resource',
    function ($resource) {
        return $resource('api/account', {}, {});
    }]);

bikeManApp.factory('Password', ['$resource',
    function ($resource) {
        return $resource('api/account/change_password', {}, {});
    }]);

bikeManApp.factory('Sessions', ['$resource',
    function ($resource) {
        return $resource('api/account/sessions/:series', {}, {
            'get': {method: 'GET', isArray: true}
        });
    }]);

bikeManApp.factory('MetricsService', ['$resource',
    function ($resource) {
        return $resource('metrics/metrics', {}, {
            'get': {method: 'GET'}
        });
    }]);

bikeManApp.factory('ThreadDumpService', ['$http',
    function ($http) {
        return {
            dump: function () {
                var promise = $http.get('dump').then(function (response) {
                    return response.data;
                });
                return promise;
            }
        };
    }]);

bikeManApp.factory('HealthCheckService', ['$rootScope', '$http',
    function ($rootScope, $http) {
        return {
            check: function () {
                var promise = $http.get('health').then(function (response) {
                    return response.data;
                });
                return promise;
            }
        };
    }]);

bikeManApp.factory('LogsService', ['$resource',
    function ($resource) {
        return $resource('api/logs', {}, {
            'findAll': {method: 'GET', isArray: true},
            'changeLevel': {method: 'PUT'}
        });
    }]);

bikeManApp.factory('AuditsService', ['$http',
    function ($http) {
        return {
            findAll: function () {
                var promise = $http.get('api/audits/all').then(function (response) {
                    return response.data;
                });
                return promise;
            },
            findByDates: function (fromDate, toDate) {
                var promise = $http.get('api/audits/byDates', {
                    params: {
                        fromDate: fromDate,
                        toDate: toDate
                    }
                }).then(function (response) {
                    return response.data;
                });
                return promise;
            }
        }
    }]);

bikeManApp.factory('Session', [
    function () {
        this.create = function (login, firstName, lastName, email, userRoles) {
            this.login = login;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.userRoles = userRoles;
        };
        this.invalidate = function () {
            this.login = null;
            this.firstName = null;
            this.lastName = null;
            this.email = null;
            this.userRoles = null;
        };
        return this;
    }]);

bikeManApp.constant('USER_ROLES', {
    all: '*',
    admin: 'ROLE_ADMIN',
    manager: 'ROLE_MANAGER',
    customer: 'ROLE_CUSTOMER',
    major_customer: 'ROLE_MAJOR_CUSTOMER'
});

//bikeManApp.factory('AuthenticationSharedService', ['$rootScope', '$http', 'authService', 'Session', 'Account', 'Base64Service', 'AccessToken', '$q',
//    function ($rootScope, $http, authService, Session, Account, Base64Service, AccessToken, $q) {
//        return {
//            login: function (param) {
//                var data = "username=" + param.username + "&password=" + param.password + "&grant_type=password&scope=read%20write&client_secret=mySecretOAuthSecret&client_id=bikeManApp";
//                $http.post('oauth/token', data, {
//                    headers: {
//                        "Content-Type": "application/x-www-form-urlencoded",
//                        "Accept": "application/json",
//                        "Authorization": "Basic " + Base64Service.encode("bikeManApp" + ':' + "mySecretOAuthSecret")
//                    },
//                    ignoreAuthModule: 'ignoreAuthModule'
//                }).success(function (data, status, headers, config) {
//                    httpHeaders.common['Authorization'] = 'Bearer ' + data.access_token;
//                    AccessToken.set(data);
//
//                    Account.get(function(accountData) {
//                        Session.create(accountData.login, accountData.firstName, accountData.lastName, accountData.email, accountData.roles);
//                        $rootScope.account = Session;
//
//                        authService.loginConfirmed(data, function(req) {
//                            req.headers.Authorization = 'Bearer ' + data.access_token;
//                            return req;
//                        });
//                    });
//                }).error(function (data, status, headers, config) {
//                    $rootScope.authenticationError = true;
//                    Session.invalidate();
//                });
//            },
//            valid: function (authorizedRoles) {
//                httpHeaders.common['Authorization'] = 'Bearer ' + AccessToken.get();
//
//                var deferred = $q.defer();
//
//                $http.get('protected/transparent.gif', {
//                    ignoreAuthModule: 'ignoreAuthModule'
//                }).success(function (data, status, headers, config) {
//                    if (!Session.login || AccessToken.get() != undefined) {
//                        if (AccessToken.get() == undefined || AccessToken.expired()) {
//                            $rootScope.authenticated = false;
//                            return;
//                        }
//                        Account.get(function(data) {
//                            Session.create(data.login, data.firstName, data.lastName, data.email, data.roles);
//                            $rootScope.account = Session;
//
//                            if (!$rootScope.isAuthorized(authorizedRoles)) {
//                                event.preventDefault();
//                                // user is not allowed
//                                $rootScope.$broadcast("event:auth-notAuthorized");
//                                deferred.resolve(false);
//                            }
//
//                            $rootScope.authenticated = true;
//                            deferred.resolve(true);
//                        });
//                    }
//                    $rootScope.authenticated = !!Session.login;
//                }).error(function (data, status, headers, config) {
//                    $rootScope.authenticated = false;
//                    deferred.resolve(false);
//                });
//                return deferred.promise;
//            },
//            isAuthorized: function (authorizedRoles) {
//                if (!angular.isArray(authorizedRoles)) {
//                    if (authorizedRoles == '*') {
//                        return true;
//                    }
//
//                    authorizedRoles = [authorizedRoles];
//                }
//
//                var isAuthorized = false;
//                angular.forEach(authorizedRoles, function(authorizedRole) {
//                    var authorized = (!!Session.login &&
//                        Session.userRoles.indexOf(authorizedRole) !== -1);
//
//                    if (authorized || authorizedRole == '*') {
//                        isAuthorized = true;
//                    }
//                });
//
//                return isAuthorized;
//            },
//            logout: function () {
//                $rootScope.authenticationError = false;
//                $rootScope.authenticated = false;
//                $rootScope.account = null;
//                AccessToken.remove();
//
//                $http.get('app/logout');
//                Session.invalidate();
//                httpHeaders.common['Authorization'] = null;
//                authService.loginCancelled();
//            },
//            refresh: function () {
//                var data = "refresh_token=" + Token.get('refresh_token') + "&grant_type=refresh_token&client_secret=mySecretOAuthSecret&client_id=carcloudapp";
//                $http.post('oauth/token', data, {
//                    headers: {
//                        "Content-Type": "application/x-www-form-urlencoded",
//                        "Accept": "application/json",
//                        "Authorization": "Basic " + Base64Service.encode("carcloudapp" + ':' + "mySecretOAuthSecret")
//                    },
//                    ignoreAuthModule: 'ignoreAuthModule'
//                }).success(function (data, status, headers, config) {
//                    if (data.access_token) httpHeaders.common['Authorization'] = 'Bearer ' + data.access_token;
//                    Token.set(data);
//
//                    Account.get(function (data) {
//                        Session.create(data.login, data.firstName, data.lastName, data.email, data.roles);
//                        $rootScope.account = Session;
//                        authService.loginConfirmed(data, function(config) {
//                            console.log("setting new header");
//                            config.headers['Authorization'] = 'Bearer ' + Token.get('access_token');
//                            return config;
//                        });
//                    });
//                }).error(function (data, status, headers, config) {
//                    $rootScope.authenticationError = true;
//                    Session.invalidate();
//                });
//            }
//        };
//    }]);

bikeManApp.factory('AuthenticationSharedService', ['$rootScope', '$http', 'authService', 'Session', 'Account', '$q',
    function ($rootScope, $http, authService, Session, Account, $q) {
        return {
            login: function (param) {
                var data = "j_username=" + param.username + "&j_password=" + param.password + "&_spring_security_remember_me=" + param.rememberMe + "&submit=Login";
                $http.post('api/authentication', data, {
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    },
                    ignoreAuthModule: 'ignoreAuthModule'
                }).success(function (data, status, headers, config) {
                    Account.get(function (data) {
                        Session.create(data.login, data.firstName, data.lastName, data.email, data.roles);
                        $rootScope.account = Session;
                        authService.loginConfirmed(data);
                    });
                }).error(function (data, status, headers, config) {
                    $rootScope.authenticationError = true;
                    Session.invalidate();
                });
            },
            valid: function (authorizedRoles) {

                var deferred = $q.defer();

                $http.get('protected/transparent.gif', {
                    ignoreAuthModule: 'ignoreAuthModule'
                }).success(function (data, status, headers, config) {
                    if (!Session.login) {
                        Account.get(function (data) {
                            Session.create(data.login, data.firstName, data.lastName, data.email, data.roles);
                            $rootScope.account = Session;

                            if (!$rootScope.isAuthorized(authorizedRoles)) {
                                event.preventDefault();
                                // user is not allowed
                                $rootScope.$broadcast("event:auth-notAuthorized");
                                deferred.resolve(false);
                            }
                            else {
                                $rootScope.$broadcast("event:auth-Authorized");
                                $rootScope.authenticated = true;
                                deferred.resolve(true);
                            }
                        });
                    }
                    $rootScope.authenticated = !!Session.login;
                }).error(function (data, status, headers, config) {
                    $rootScope.authenticated = false;
                    deferred.resolve(false);
                });
                return deferred.promise;
            },
            isAuthorized: function (authorizedRoles) {
                if (!angular.isArray(authorizedRoles)) {
                    if (authorizedRoles == '*') {
                        return true;
                    }

                    authorizedRoles = [authorizedRoles];
                }

                var isAuthorized = false;
                angular.forEach(authorizedRoles, function (authorizedRole) {
                    var authorized = (!!Session.login &&
                    Session.userRoles.indexOf(authorizedRole) !== -1);

                    if (authorized || authorizedRole == '*') {
                        isAuthorized = true;
                    }
                });

                return isAuthorized;
            },
            logout: function () {
                $rootScope.authenticationError = false;
                $rootScope.authenticated = false;
                $rootScope.account = null;

                $http.get('api/logout');
                Session.invalidate();
                authService.loginCancelled();
            }
        };
    }]);

bikeManApp.factory('responseRejector', ['$q', '$rootScope', function ($q, $rootScope) {
    var responseRejector = {
        'response': function (response) {
            var errorInterval = 3000;
            var successInterval = 1500;

            $rootScope.$broadcast('remove-error-message');

            if (response.config.method.toUpperCase() != 'GET') {
                var alertType = 'alert-success';
                $rootScope.$broadcast('new-success-message', {msg: "Successful"});
            }

            return response;
        }
    };
    return responseRejector;
}]);

bikeManApp.factory('responseRecoverer', ['$q', '$rootScope', function ($q, $rootScope) {
    var responseRecoverer = {
        'responseError': function (rejection) {
            // do something on error
            var alertType = 'alert-danger';
            switch (rejection.status) {
                // remove this because 401 is used for checking user auth
                case 400:

                    $rootScope.$broadcast('new-error-message', {
                        msg: rejection.data.message,
                        errors: rejection.data.fieldErrors
                    });
                    break;
                case 401:
                    // do nothing for now
//                                showMessage('Wrong usename or password', 'errorMessage', errorInterval, alertType);
                    break;
                case 403:
                    $rootScope.$broadcast('new-error-message', {msg: 'You don\'t have the right to do this'});
                    break;
                case 404:
                    $rootScope.$broadcast('new-error-message', {msg: 'You don\'t have the right to do this'});
                    break;
                case 500:
                    $rootScope.$broadcast('new-error-message', {
                        msg: rejection.data.message,
                        error: rejection.data.fieldErrors
                    });
                    break;
                default:
                    console.log(errorResponse);
                    showMessage(errorResponse.data.message, 'errorMessage', errorInterval, alertType);
                    $rootScope.$broadcast('new-error-message', {msg: 'You don\'t have the right to do this'});
                    break;
            }
            return $q.reject(rejection);
        }
    };
    return responseRecoverer;
}]);
