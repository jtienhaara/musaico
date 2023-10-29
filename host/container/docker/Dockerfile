#
# @version Last updated 2023-10-28
# Image from:
#
#     https://hub.docker.com/_/debian
#
FROM debian:12.2-slim

USER root

ENV DEBIAN_FRONTEND=noninteractive

#
# Packages for Musaico core, with [footnote]s.
#
# Packages that aren't available for download through the operating system's
# package management system are installed later in this file, with their own
# documentation and [footnote]s.
#
#     age [required] [encryption]
#         Simple public/private keys.  Useful standalone and with SOPS.
#         Each Musaico container image type generates its own AGE
#         private/public key pair.  When a container instance stores
#         and retrieves persistent data at runtime, it encrypts using
#         its image type's public key, and decrypts using its image type's
#         private key.
#         Each Musaico container instance also generates its own AGE
#         private/public key pair.  When communicating point-to-point
#         with other Musaico containers at runtime, the two containers
#         first exchange public keys.  The sender then uses the recipient's
#         public key to encrypt each message it sends.  The recipient
#         uses its private key to decrypt each message.  If a response
#         is warranted (depending on the type of communication),
#         then the recipient uses the sender's public key to encrypt
#         the response, and the same procedure is used by the sender
#         to decrypt the response.
#     bind9-dnsutils [troubleshooting] [network] [security]
#         Includes dig, for troubleshooting out DNS problems from within
#         a running Musaico container.
#     ca-certificates [required] [network]
#         Required for downloading and compiling packages from GitHub,
#         such as SOPS and nats.c.
#     cmake [required] [dynamic] [security]
#         Newfangled cmake.  Required for building certain packages,
#         such as nats.c.
#     curl [troubleshooting] [network] [security]
#         HTTP client for troubleshooting web problems from within
#         a running Musaico container.
#     emacs [troubleshooting] [security]
#         Editor that has macros, for dealing with complex operational
#         issues inside a running container.  Yes, you can still use
#         vim along with your astrolabe, if you like ancient technology.
#         (Emacs is no more ancient than quill and ink; it is what
#         elegant people use, while the ignorant use gel pens and crayons.)
#     gcc [required] [dynamic] [security]
#         C compiler.  Required for building things on the fly.
#     git [required] [dynamic] [network] [security]
#         Version control.  Required for tracking changes.
#     make [required] [dynamic] [security]
#         Traditional make.  Required for building things from Makefiles.
#     tcpdump [troubleshooting] [network] [security]
#         Capture network packets in .pcap files, for troubleshooting
#         a running container.  Copy the .pcap file back to your machine,
#         load it into WireShark, and spend hours of fun trying to figure out
#         why your network is borked.
#     traceroute [troubleshooting] [network] [security]
#         Through black magic, figures out the IP hops to a destination.
#         Useful for troubleshooting inside a running container.
#     vim [troubleshooting] [security]
#         When troubleshooting, you can use a horrid editor with modes
#         to try to fix operational issues.  2023 and cavemen still
#         use this rock-tipped spear for picking berries.
#     wget [troubleshooting] [network] [security]
#         Like curl, but makes it slightly easier to just download
#         files from the interweb over HTTP.
#
# [Footnote]s:
#     [dynamic]
#         Used to dynamically build pieces of the Musaico ecosystem
#         at runtime, such as types and instances.
#     [encryption]
#         Used to encrypt and decrypt data at rest and in flight
#         throughout the Musaico ecosystem.
#     [network]
#         Used at runtime for network communications, with other
#         Musaico containers, with third party services, and so on.
#         Some networking packages are required, others are installed
#         for troubleshooting.
#     [required]
#         Musaico cannot run without this package.
#     [security]
#         Poses a potential security risk, such as enabling
#         intruders or innocents access to tools or data
#         that can be misused and break the distributed system.
#         If a security package is not also required, then it
#         can be uninstalled by derived containers, to limit
#         what intruders or innocents can deliberately/accidentally do
#         once they're inside a running Musaico core container.
#         Note that this mitigation is imperfect: all of the files
#         for the subsequently-uninstalled package remain in an overlay
#         file system layer, accessible to the intruder / innocent.
#     [troubleshooting]
#         Used to troubleshoot issues at runtime.
#
RUN apt-get update --yes \
    && apt-get install --no-install-recommends --yes \
       age \
       bind9-dnsutils \
       ca-certificates \
       cmake \
       curl \
       emacs \
       gcc \
       git \
       make \
       tcpdump \
       traceroute \
       vim \
       wget \
    && apt-get clean

#
# Package:
#
#     sops [required] [encryption]
#
# (See [footnote]s above in this file.)
#
ENV SOPS_VERSION=3.8.0
RUN if test `uname -m` = 'aarch64' \
    -o `uname -m` = 'arm64'; \
    then \
        SOPS_ARCH="arm64"; \
    else \
        SOPS_ARCH="amd64"; \
    fi; \
    wget https://github.com/getsops/sops/releases/download/v${SOPS_VERSION}/sops_${SOPS_VERSION}_${SOPS_ARCH}.deb \
        --output-document /tmp/sops_${SOPS_VERSION}.${SOPS_ARCH}.deb; \
    cp /tmp/sops_${SOPS_VERSION}.${SOPS_ARCH}.deb /tmp/sops_${SOPS_VERSION}.deb
RUN dpkg -i /tmp/sops_${SOPS_VERSION}.deb

#
# User musaico
#
# Install a non-root user, to mitigate intrusions and mistakes
# at runtime.
#
# 32 digit random number for musaico user's password.
#
RUN mkdir /home/musaico \
    && useradd \
           --home-dir /home/musaico \
           --password `od --read-bytes 32 --format u --address-radix none /dev/urandom | tr --delete ' \n'` \
           musaico \
    && chown -R musaico:musaico /home/musaico

#
# Musaico working directory
#
# Persistent data used by Musaico processes is stored in /musaico/...
#
#   bin
#     Scripts and commands that are used inside the running container,
#     including entrypoint.sh, which initializes the running Musaico
#     environment.
#   keys
#     Where at_rest, in_flight, and possibly other AGE key pairs
#     will be stored.  To be sourced at runtime by entrypoint.sh.
#
RUN mkdir /musaico /musaico/bin /musaico/keys \
    && chown -R musaico:musaico /musaico \
    && chmod ug+rwx,o-rwx /musaico /musaico/bin /musaico/keys

#
# Install Musaico commands:
#
COPY entrypoint.sh /musaico/bin/
COPY musaico_keygen.sh /musaico/bin/
RUN chown -R musaico:musaico /musaico/bin/* \
    && chmod -R ug+x /musaico/bin/*

#
# Prevent any other user from accessing the /musaico/keys directory.
#
RUN chmod -R u+rwx,go-rwx /musaico/keys/

USER musaico
WORKDIR /musaico

ARG MUSAICO_VERSION
RUN echo "export MUSAICO_ROOT_DIR=/musaico" \
    >> /musaico/settings.env \
    && echo "export MUSAICO_RUNTIME=/musaico/app" \
    >> /musaico/settings.env \
    && echo "export MUSAICO_USER=musaico" \
    >> /musaico/settings.env \
    && echo "export MUSAICO_VERSION=$MUSAICO_VERSION" \
    >> /musaico/settings.env

#
# Start everything by running entrypoint.sh, which sets up encryption
# and so on before executing whatever commands are desired by the
# derived container image.
#
CMD [""]
ENTRYPOINT ["/musaico/bin/entrypoint.sh"]
