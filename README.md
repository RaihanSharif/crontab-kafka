# In this branch
- Crontab parser that can parse a crontab entry with a schedule and a command.
- Docker compose with 6 services. 1 Zookeper, 1 Kafka broker, 1 Producer, 2 Consumers, 1 Kafdrap to inspect messages.
- Producer sets up a topic with two partitions, parses crontab file, and publishes the schedule and command to the topic.
- Consumers in the same group that consume from the topic. Each gets one partition.
- Consumers execute supplied command, assumes single line command such as `echo "hello kafka"`.
- Makefile to quickly spin up or the containers, and do basic logging.
