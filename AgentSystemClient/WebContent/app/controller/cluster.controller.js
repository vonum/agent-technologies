(function() {
	angular.module("agentApp").controller("clusterController", clusterController);
		
	clusterController.$inject = ['$scope', '$http'];
	
	function clusterController($scope, $http) {
		
		$http.get('rest/node/nodes')
		.success(function(d) {
			console.log(d);
			$scope.nodes = d;
		})
		.error(function(d) {
			console.log("Error loading running nodes");
		});
		
		
	$scope.register = function() {
		$http.get('rest/node/hello')
		.success(function(d) {
			alert("izi");
		})
		.error(function(d) {
			console.log("Error loading register node");
		});
	}
	
	$scope.unregister = function() {
		$http.get('rest/node/bye')
		.success(function(d) {
			alert("izi");
		})
		.error(function(d) {
			console.log("Error loading bye");
		});
	}
		
	}
	
})();