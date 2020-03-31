FROM bitnami/rabbitmq:3.8

# Enable plugins
RUN rabbitmq-plugins enable rabbitmq_management
RUN rabbitmq-plugins enable rabbitmq_stomp
RUN rabbitmq-plugins enable rabbitmq_web_stomp

ENV RABBITMQ_PID_FILE /var/lib/rabbitmq/mnesia/rabbitmq
ENV RABBITMQ_USER test
ENV RABBITMQ_PASSWORD test

ADD init_rabbitmq.sh /init_rabbitmq.sh

USER root
RUN sudo chmod +x /init_rabbitmq.sh
#USER jrigon
EXPOSE 15672
EXPOSE 61613

# Define default command
CMD ["/init_rabbitmq.sh"]