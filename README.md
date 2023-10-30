# Musaico
Musaico is a first, very, very crude prototype of a "higher-level
programming language" and a "co-operating system".

It is in the very early stages of prototyping and experimentation,
so don't expect anything to work.

If you're intrigued by any of the philosophical blathering below, I'd
love to hear from you, work together, bounce ideas, etc.

Cheers,

Johann
jtienhaara@yahoo.com

## Higher-level programming
Many of the most popular programming languages today are still based on C.

Types are divided between "primitives" and "structs", "classes",
sometimes "functions", and similar building-block constructs.
Some languages have generics, one even has monads.  Very few languages
delve into the other corners of the Lambda Cube, and those that do
are typically more useful for proving mathematical theorems than they
are for building commercial or open source applications.

Some languages point in a particular higher-level programming direction,
without really taking dramatic steps toward higher-level types.
Infrastructure management tools and container orchestrators, in particular,
don't focus on building up "primitives" (ints, floats, and so on)
into classes and structs.  Instead, they provide languages which
can be used to build systems out of large, complex atomic blocks,
each of which is an entire program.

Let us, for now, define higher-level programming as:

Building distributed systems from programs as the primitive types.

Higher-level programming might include writing code to:

- define how micro-services communicate with each other;
- define how entire distributed systems communicate with each other;
- define how to operate distributed systems
  (versioning, backup, security, observability, and so on; "non-functional requirements" to some).

## Types in Musaico
In its first, crude prototype implementation, at least, types in Musaico
will not be mathematically pure.  There will be no distinction
between a "kind" (a type of types) and a "type".

In Musaico, a "type" can be used to construct an "instance".

An "instance" might also be a "type" that can construct its own
"instances".

Types that are currently envisioned for this prototype of Musaico:

- hosts
  - vms
  - containers
  - clusters
  - clouds
- apps
  - guests to be run inside hosts
- streams
  - versioned schemas
  - layers (applications layer, kernel layer, system call layer)

Each of these types is briefly described below.

### Type "host"
A "host" type, such as a `host.vm.qemu`, or a `host.container.docker`,
can be used to construct distributed systems, containing
apps or other hosts as their guests.

Hosts can be nested, so that a `host.container.docker`
can be the guest of a `host.cluster.kind`, which,
in turn, is the guest of a `host.vm.qemu`.

```
anarkube = host.vm.qemu {  // Constructs a vm_instance called "anarkube".
  cpu = 1.0;  // Measured in vcpu, see notes below.
  memory = 4.0;  // Measured in gigabytes (GB / Gi).
  image = "https://cloud-images.ubuntu.com/jammy/current/jammy-server-cloudimg-amd64-disk-kvm.img";  // URL of a qcow2 disk image to load into the disk.
  volumes {
    metadata = 3 * host.vm.qemu.disk {  // Construct 3 empty disks.
      format = host.vm.volumes.formats.ext4;
      size = 1.0;  // Measured in gigabytes (GB / Gi).
    }
    data = 3 * host.vm.qemu.disk {  // Construct 3 empty disks.
      format = host.vm.volumes.formats.ext4;
      size = 2.0;  // Measured in gigabytes (GB / Gi).
    }
  }
  networks {
    // TODO...
  }
  guests {
    kind = host.cluster.kind {
      guests {
        nats = host.container.docker {  // NATS server
          cpu = 0.1;    // CPU request
          memory = 3.5; // Memory limit
          image = "nats:2.10.4-alpine3.18";
          volumes {
            data = host.container.docker.volumes.host {
            }
          }
        }
        audio = app.audio {  // Musaico audio test app
        }
      }
    }
  }
}
```

Once a host has been constructed as a host_definition, the host_definition
can be instantiated as a host_runtime.

```
anarkube_nodes[] = 3 * anarkube { // Constructs 3 running VMs of type anarkube.
}
```

Different types of hosts support different types of guests.
For example, a `host.container.docker` can have a Docker container image
as a guest, such as the NATS server in the example above.

An "app" can be the guest of a vm or a container.

A container can be the guest of a cluster or a vm.

A cluster can be the guest of a vm or a cloud.

(N.b. right now I'm not really interested in sorting out
all the esoteric details to allow bizarre combinations
[vms running in containers, apps running directly in clusters
without any middle layers, etc].)

### Type "app"
An "app" is some code or other that can be installed and built inside a
host_defintion, and executed inside a host_runtime.

In the example code above, the `app.audio` test app is the guest
of a `host.container.docker`.  It could also potentially be hosted
as a guest of a `host.vm.qemu`.  And so on.

Apps and streams are the functional building blocks of a Musaico
co-operating system.

For now, each app "xyz" is a directory under `./app/xyz`.  It must contain
a Makefile with a number of rules (as seen in, for example, the
not-very-useful "audio" app: `./app/audio/Makefile`):

- make install
  Installs operating system components in the host (apt-get install, etc).
  Executed when the host is constructed, before runtime.
  Before the app is installed, an AGE public/private key pair is generated
  for at-rest communications.
  Always executed as the root user.
- make build
  Compiles any code required in the host (gcc -c xyz.c -o xyz, etc).
  Can use any programming language.
  Executed when the host is constructed, before runtime.
  Always executed as user "musaico".
- make run
  Executes the app.
  Executed at runtime.
  Before the app is run, an AGE public/private key pair is generated
  for in-flight communications.
  The app can use the at-rest public/private key pair from its host type
  to persist data (for example, using SOPS, which comes pre-installed
  in every host).  It can also use the in-flight key pair for
  exchanging point-to-point messages with other applications or the kernel.
  Always executed as user "musaico".
- make test
  Like make run, but for integration testing, rather than production
  or systems integration testing.

### Type "stream"
A stream declares a mode of communication between Musaico instances
such as containers.

Streaming in Musaico is built around NATS Jetstream and a kernel
schema registry, designed to make it easy and (some day) safe
to upgrade message schemas without borking your whole distributed system.

This includes:

- Versioned data structures / schemas
- A kernel schema registry
- Declarative enrichment of streams (such as calculating the FFT
  frequency spectrum of audio data)

Whenever a new or upgraded schema is declared to the kernel schema
registry, all publishers and subscribers of that schema are notified
via system call interface stream, and provided with a new parser
and a new formatter for the schema.

(Securing the system call stream to mitigate the risk of malevolent
code will be an exercise left to the reader for now.)

At this point, only sketches exist for the stream stuff.  Code samples
will appear as experimentation and prototyping continues.

## Co-operating system
A co-operating system is designed to help software developers
(especially those of us who either work at small companies or have
our own big project ambitions) tame the mess of proprietary interfaces
that are used, today, to build, test, deploy and operate distributed
systems.

Today's distributed system is far more complicated and difficult
to reason about than the operating systems of the 1950s and 1960s,
even though they were written in assembly language on punch cards.

A co-operating system is designed to provide a software developer
with reasonably straightforward, and consistent, access to processing,
storage, and inter-process communication.

## Installing Musaico
For now, Musaico is download-and-build-yourself.  Git clone this repository,
as long as you have the required pre-requisites (described below).

### Pre-requisites
For now, Musaico only runs on Debian Linux.  There is no
compatibility matrix for specific versions of Debian.  It has generally
been prototyped on Debian 11 and 12.

- A Debian Linux operating system.  (11 or 12 at the time of writing.)
- Make must be installed.
  `sudo apt-get install -y make`
- Docker installed and executable by the user running Musaico.
  You can either do:
  `sudo apt-get install -y docker.io`
  `sudo chmod a+s /usr/bin/docker`
  Or, from the root directory, you can run "sudo make install"
  to install everything.
- Qemu isn't enabled yet, but it will eventually be required.


## Building Musaico
From the toplevel Musaico directory, run:

```
make
```

This should build all of the necessary containers to run Musaico
(unless everything is borken, in which case don't complain I didn't warn you).

## Using Musaico
Right now there's very little to see.

You can run a test of the Musaico language parser by running:

```
make test_language
```

Along the way, this also builds the compiler, which only parse `.musaico`
files at the moment (it does not compile anything).

You can then play around with the parser (and report bugs!):

```
./runtime/app/musaico/work/musaico /path/to/my.musaico
```

There is also a test Musaico file you can experiment with the parser:

```
./runtime/app/musaico/work/musaico ./language/test.musaico
```

In the absence of a full-fledged compiler, you can create apps,
then manually run them inside host.container.docker.
For example, `make test_audio_app` runs through the steps of
installing, building and test-running an (not very useful) app:

```
. ./runtime/root/settings.env
cd host/container/docker/guest
export MUSAICO_APP_ID=audio
export MUSAICO_APP_DIR=$$MUSAICO_ROOT_DIR/app/audio
make install build test
cd ../../../..
```

# Contact
Johann Tienhaara
jtienhaara@yahoo.com

# License
Musaico is free software; you can redistribute it and/or modify it under the terms of the GNU Affero General Public License ("AGPL") as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.

https://www.gnu.org/licenses/agpl-3.0.en.html
