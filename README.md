# Math-Client-Server
Project for CS4390 Computer Networks demonstrating client-server network architecture with math.

How to run:
mvn compile
mvn package
java -jar target/server-runnable.jar
java -jar target/client-runnable.jar

Group Members:
Natasha Trayers nnt180002
Arjun Khurana axk172230
Pallavi Vayalali  pxv170130

Protocal Design:
The protocal design for this project is a message formation for sending and recieving math calculations, message format for joining and terminating connection and format for keeping logs of client activities at server side

server:

//reqs: 
	keep track of users (who, when, time)
	wait for request
	log details on connect
	accept get request w math string
	return result
	respond in order
	accept request for close, log close

init(port)
listen(port)

clients = []

while true:
	if request():
		switch(request.status):
			case CONNECT:
				clients.add({
					request.name,
					time(now)
				})
				send(ack)
			
			case MATH:
				result = evaluate(request.math)
				send(result)
			
			case DONE:
				send(ack)

			case ERROR:
				handle(request.error)

exit()

client:

//reqs:
	attaches with name, wait for ack
	sends 3 math requests w random intervals
	sends close request, terminates

init(name)
attach(name)
for i = 1 to 3:
	math = generate_math()
	wait(random)

close()
exit()

class message:

//reqs:
	name of sender
	port of dest
	math string
	status

The programming environment that was used was called Visual Studio Code. In addition, we created a Github to allow for changes 
to be made in the program and and an easier method to transfer code between the group members.

Compiling and Execution of Code:
To compile and execute the code, the first step requires the user to run Server.java. 
terminate the batch
run server.java

Parameters For Code Execution:

Challenges Faced:
There were some difficulties that were faced during this project. For example, it was definitely an adjustement 

