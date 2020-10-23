# Math-Client-Server
Project for CS4390 Computer Networks demonstrating client-server network architecture with math.

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



