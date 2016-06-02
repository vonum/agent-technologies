(function() {
	angular.module("agentApp").controller("promoController", promoController);
		
	promoController.$inject = ['$scope', '$http', '$location'];
	
	function promoController($scope, $http, $location) {
		
		$scope.rest = function() {
			$location.path('/agent/rest');
		}
		
		$scope.websocket = function() {
			$location.path('/agent/websocket');
		}
	}
	
})();