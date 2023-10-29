ARG VERSION=0.0.1
FROM musaico-core:${VERSION}

ENV DEBIAN_FRONTEND=noninteractive

USER root

RUN apt-get update --yes \
    && apt-get install --no-install-recommends --yes \
       jackd2 \
    && apt-get clean

USER musaico

RUN /musaico/bin/musaico_keygen.sh at_rest

CMD [""]
