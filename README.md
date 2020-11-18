# Math-Client-Server
Project for CS4390 Computer Networks demonstrating client-server network architecture with math.

## **How to compile**  
### On windows:  
./mvnw.cmd clean package

### On linux:  
./mvnw clean package

### To run server:  
java -jar target/server-runnable.jar

### To run client:  
java -jar target/client-runnable.jar

### User input mode  
allow the client to send their own math requests three times  
### Automatic mode  
will generate three math requests to be sent to the server

**Group Members:**  
Natasha Trayers nnt180002
Arjun Khurana axk172230
Pallavi Vayalali  pxv170130

**Protocal Design:**
The protocal design for this project is a message formation for sending and recieving math calculations, message format for joining and terminating connection and format for keeping logs of client activities at server side

Below is the pseudo out program is based on for both the server and the client:
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

## **How to compile**  
### On windows:  
./mvnw.cmd clean package

### On linux:  
./mvnw clean package

### To run server:  
java -jar target/server-runnable.jar

### To run client:  
java -jar target/client-runnable.jar

### User input mode  
allow the client to send their own math requests three times  
### Automatic mode  
will generate three math requests to be sent to the server

Parameters For Code Execution:

**Challenges Faced:**

There were some difficulties that were faced during this project. For example, it was definitely an adjustment to work virtually for this project. This was primarily because a lot of this project had aspect that couldn't be coding separately, as one integral part of the code would be dependent on another part and coding them separately could lead to more bug fixes. Additionally, there were times when the code would only work on one partners computer and lead to run time errors on another computer due to package download errors. Additionally, another challenge for the group was finding time as a group to meet every week with all our different schedules with could lead to time constraints on the project schedule. But overall, many of the challenges that we faced as a team we found solutions to that worked best for us to complete the project. 

**What we have learned during this project:**

During the course of this project we had a great opportunity to implement various aspects we have learned during lecture. For example, we got a better understanding of the client and server relationship and how a server can best understand what requests to complete first and how the process is efficiently completed.
