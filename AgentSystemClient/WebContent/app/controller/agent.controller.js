(function() {
	angular.module("agentApp").controller("agentController", agentController);
		
	agentController.$inject = ["$http", "$scope", "$uibModal", "$interval"];
	
	function agentController($http, $scope, $uibModal, $interval) {

		
		
		$scope.startAgent = function(type, module) {
			
		   var modalInstance = $uibModal.open({
		          templateUrl: '../AgentSystemClient/app/views/agentModal.html',
		          controller: 'agentModalController',
		          resolve: {
		        	  getAgentType : function() {
		        		  return {"type" : type, "module" : module};
		        	  }
		   
		        	  
		          }
		   });
		   
		   modalInstance.result.finally(function() {
			   runningAgents(); //refresh running agents
		   });
		    
			
			
		}
		
		$scope.sendAclMessage = function() {
			
			  var modalInstance = $uibModal.open({
		          templateUrl: '../AgentSystemClient/app/views/aclModal.html',
		          controller: 'aclModalController',
		          resolve: {
		        	  getPerformatives : function() {
		        		  return $scope.performatives;
		        	  },
		        	  getRunningAgents : function() {
		        		  return $scope.runningAgents;
		        	  }
		          }
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
		
		//get list of performatives
		$http.get('rest/agents/messages')
		.success(function(d) {
			$scope.performatives = d;
		})
		.error(function(d) {
			console.log("Error loading performatives");
		});
		
		//load running agents
		function runningAgents() {
			$http.get('rest/agents/all')
			.success(function(d) {
				console.log(d);
				$scope.runningAgents = d;
			})
			.error(function(d) {
				console.log("Error loading running agents");
			});
		}
		runningAgents();
		
		
		$scope.clear = function(){
			//$scope.count = $scope.logMessages.length;
			$scope.logMessages = [];
		}
		
		//Update console output
		
		$scope.logMessages = [];
		$scope.count = 0;
		
		$scope.updateConsole = function() {
			
			$http.get('rest/logger/messages/' + $scope.count)
			.success(function(d) {
				
				$scope.count += d.length;
				
				//for each new msg push
				angular.forEach(d, function(v, k) {
					$scope.logMessages.push(v);
				});
				
			})
			.error(function(d) {
				console.log("Error loading log messages");
			});
		}

		//update console every  1 second
		$interval(function() {
			$scope.updateConsole();
		}, 1000);
	}
	
})();