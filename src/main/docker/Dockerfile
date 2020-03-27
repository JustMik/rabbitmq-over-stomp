FROM bitnami/rabbitmq:3.8

RUN rabbitmq-plugins enable --offline rabbitmq_stomp
RUN rabbitmq-plugins enable --offline rabbitmq_web_stomp
#RUN rabbitmqctl add_user test test
#RUN rabbitmqctl set_user_tags test administrator
#RUN rabbitmqctl set_permissions -p / test ".*" ".*" ".*"

EXPOSE 61613
EXPOSE 15674
