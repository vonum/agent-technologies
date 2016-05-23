(function() {
	angular.module("agentApp").controller("agentController", agentController);
		
	agentController.$inject = ["$http", "$scope", "$uibModal"];
	
	function agentController($http, $scope, $uibModal) {

		
		$scope.startAgent = function(type, module) {
			
		   var modalInstance = $uibModal.open({
		          templateUrl: '../AgentSystemClient/app/views/agentModal.html',
		          controller: 'agentModalController',
		          resolve: {
		        	  getAgentType : function() {
		        		  return {"type" : type, "module" : module};
		        	  },
		        	  
		          }
		   });
		   
		   modalInstance.result.finally(function() {
			   runningAgents(); //refresh running agents
		   });
		    
			
			
		}
		
		//kill the fucker he has aids
		$scope.stopAgent = function(aids)
		{
			console.log(aids);
			
			$http.get('rest/agents/stop/' + aids.name)
			.success(function() {
				
				
				for(var i = 0; i < $scope.runningAgents.length; ++i) {
					if($scope.runningAgents[i].aids.name === aids.name) {
						break;
					}
				};
				
				delete $scope.runningAgents[aids.name];
				
			})
			.error(function() {
				console.log("Error while stoping agent");
			});
		}
		
		//get agent types
		$http.get('rest/agents/classes')
		.success(function(d) {
			console.log(d);
			$scope.agentTypes = d;
		})
		.error(function(d) {
			console.log("Error loading agent types");
		});
		
		//load running agents
		function runningAgents() {
			$http.get('rest/agents/running')
			.success(function(d) {
				console.log(d);
				$scope.runningAgents = d;
			})
			.error(function(d) {
				console.log("Error loading running agents");
			});
		}
		
		runningAgents();

		
	}
	
})();