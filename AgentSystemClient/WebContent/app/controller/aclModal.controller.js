(function() {
	angular.module("agentApp").controller("aclModalController", aclModalController);
		
	aclModalController.$inject = ['$uibModalInstance', '$scope', '$http'];
	
	function aclModalController($uibModalInstance, $scope, $http) {
		
		$scope.name = "";
		
		
		$scope.cancel = function() {
			$uibModalInstance.dismiss('cancel');
		}
		
	
		$scope.sendMsg = function() {
			$http.post('rest/agents/messages/' + $scope.name)
			.success(function(d) {
				$scope.cancel();
			})
			.error(function(d) {
				console.log("Error loading agent types");
			});
		}
		
	}
	
})();