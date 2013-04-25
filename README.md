CloudMyTask
===========
CloudMyTask is a distributed service which provides clients with the possibility to process python scripts on multiple machines. The service is client oriented and the clients can be any applications which need to process python scripts. The system implements multiple instances which are in charge of processing the scripts as well as doing the load balancing part and it also has a central unit which keeps the topology of the system and a list with possible banned clients. The communication between entities is done through TCP, UDP or NIOTCP.

CloudMyTask can be very useful in case of a lack of processing power and it is also designed to be scalable.
