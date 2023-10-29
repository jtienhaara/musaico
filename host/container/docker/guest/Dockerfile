ARG MUSAICO_VERSION
FROM musaico-core:${MUSAICO_VERSION}

ENV DEBIAN_FRONTEND=noninteractive

USER musaico

RUN /musaico/bin/musaico_keygen.sh at_rest

USER root

COPY ./guest/ /musaico/app/
RUN chown -R musaico:musaico /musaico/app/ \
    && chmod -R ug+r,ug-w,o-rwx /musaico/app/

WORKDIR /musaico/app
RUN . /musaico/settings.env \
    && make install

USER musaico
RUN . /musaico/settings.env \
    && make build

CMD ["make", "run"]
