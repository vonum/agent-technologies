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
			$scope.isRegistered = false;
		})
		.error(function(d) {
			console.log("Error loading bye");
		});
	}
	
	
	//check if the node is master
	$http.get('rest/node/master')
		.success(function(d) {
			console.log("IsMaster " + d);
			$scope.masterAddr = d;
			
		})
		.error(function(d) {
			console.log("Error loading bye");
		});
		
	// Are we registered
	$http.get('rest/node/registered')
		.success(function(d) {
			$scope.isRegistered = d;
			console.log($scope.isRegistered);
		})
		.error(function(d) {
			console.log("Error loading bye");
		});
		
	}
	
})();