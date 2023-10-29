ARG MUSAICO_VERSION=0.0.1
ARG MUSAICO_APP_ID=no-app-name
FROM musaico-core:${MUSAICO_VERSION}

ENV DEBIAN_FRONTEND=noninteractive

USER root

COPY ./guest/ /musaico/app/
RUN chown -R musaico:musaico /musaico/app/ \
    && chmod -R ug+r,ug-w,o-rwx /musaico/app/

WORKDIR /musaico/app
RUN make install

USER musaico
RUN make build

RUN /musaico/bin/musaico_keygen.sh at_rest

ENV MUSAICO_USER=musaico

CMD ["make", "run"]
