FROM bitnami/rabbitmq:3.8

# Enable plugins
RUN rabbitmq-plugins enable rabbitmq_management
RUN rabbitmq-plugins enable rabbitmq_stomp
RUN rabbitmq-plugins enable rabbitmq_web_stomp

ENV RABBITMQ_PID_FILE /var/lib/rabbitmq/mnesia/rabbitmq
ENV RABBITMQ_USER test
ENV RABBITMQ_PASSWORD test

ADD init_rabbitmq.sh /init_rabbitmq.sh

# TODO Da fixare e usare user normale
USER root
RUN chmod +x /init_rabbitmq.sh

# TODO Probabilmente non serve esporle anche qua avendolo gia fatto nello yml
#EXPOSE 5672
EXPOSE 15672
#EXPOSE 15674
#EXPOSE 25672
EXPOSE 61613



# Define default command
CMD ["/init_rabbitmq.sh"]