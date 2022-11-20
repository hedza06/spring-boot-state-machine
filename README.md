## Spring Boot State Machine Demo
Spring Statemachine is a framework for application developers to use state machine concepts with 
Spring applications.

State machines are powerful because behaviour is always guaranteed to be consistent, making it relatively easy to debug. 
This is because operational rules are written in stone when the machine is started. The idea is that your application may exist in a finite number of states and certain predefined triggers can take your application from one state to the next. Such triggers can be based on either events or timers.

It is much easier to define high level logic outside of your application and then rely on the state machine to 
manage state. You can interact with the state machine by sending an event, listening for changes or simply 
request a current state.

The State Machine can be used in Saga orchestration pattern which is useful for brownfield microservice application 
development architecture. In other words, this pattern works when we already have a set of microservices and would 
like to implement the Saga pattern in the application. We need to define the appropriate compensating transactions to 
proceed with this pattern.

Here are a few frameworks available to implement the orchestrator pattern:

* **Camunda:** a Java-based framework that supports Business Process Model and Notation (BPMN) standard for workflow 
and process automation.
* **Apache Camel:** provides the implementation for Saga Enterprise Integration Pattern (EIP)


### State Machine Terminology

1. States:
The specific state of state machine. Finite and predetermined values. Frequently defined in enumeration.

2. Events:
Something that happens to the system - may or may not change the state

3. Actions:
The response of State Machine to events. Can be changing variables, calling a method or changing to a different state

4. Transitions:
Type of action which changes state

5. Guards:
Boolean conditions

6. Extended State:
State Machine variables (in addition to state)


### Implemented process definition
![alt text](/src/main/resources/state.png)


### Testing the flows
Implemented flow can be tested using automated tests which are placed in folder **src/test**. Two types of tests are 
implemented: test class that tests the transitions between states in isolated mode 
(without interacting with database and other components) and test class which tests actions that react to state change.


### Contribution/Suggestions
If someone is interested for contribution or have some suggestions please contact me on e-mail `hedzaprog@gmail.com`.

### Author
Heril Muratović  
Software Engineer  
<br>
**Mobile/Viber/WhatsUp**: +38269657962  
**E-mail**: hedzaprog@gmail.com  
**Skype**: hedza06  
**Twitter**: heril.muratovic  
**LinkedIn**: https://www.linkedin.com/in/heril-muratovi%C4%87-021097132/  
**StackOverflow**: https://stackoverflow.com/users/4078505/heril-muratovic

