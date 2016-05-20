(function() {
	angular.module("agentApp").controller("agentController", agentController);
		
	agentController.$inject = ["$http", "$scope"];
	
	function agentController($http, $scope) {

		
		$scope.startAgent = function(type, module) {
			
			var agentData = {"type" : {"name" : type, "module" : module}, "name" : "mulan"};
//			
//			bootbox.prompt("What is your name?", function(result) {                
//				  if (result === null) {                                             
//				    Example.show("Prompt dismissed");                              
//				  } else {
//				    Example.show("Hi <b>"+result+"</b>");                          
//				  }
//				});
			
			//zbog jednostavnosti za sad pozivamo ovu metodu
			$http.put('rest/agents/running', agentData)
			.success(function() {
				runningAgents(); //refresh running agents
			})
			.error(function() {
				console.log("Error adding agent");
			});
		}
		
		//kill the fucker he has aids
		$scope.stopAgent = function(aids)
		{
			console.log(aids);
			
			$http.get('rest/agents/stop', aids.name )
			.success(function() {
				
				
				for(var i = 0; i < $scope.runningAgents.length; ++i) {
					if($scope.runningAgents[i].aids.name === aids.name) {
						break;
					}
				};
				
				$scope.runningAgents.splice(i, 1);
				
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