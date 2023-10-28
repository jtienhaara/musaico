# Musaico0
Musaico0 is a first, very, very crude prototype of Musaico, a higher-level
programming language.

It is in the very early stages of prototyping and experimentation,
so don't expect anything to work.

If you're intrigued by any of the philosophical blathering below, I'd
love to hear from you, maybe work with you.

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
don't start at the "primitives" level of types (ints, floats, and so on).
Instead, they provide languages which build systems out of
atomic blocks, each of which is an entire program.

Let us, for now, define higher-level programming as:

Building distributed systems using programs as "primitive" types.

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

- function
- vm
- container
- stream

Each of these types is briefly described below.

### Type "function"
!!!

### Type "vm"
A "vm" type can be used to construct "vm_instance" types.

```
anarkube = vm.qemu {  // Constructs a vm_instance called "anarkube".
  cpu = 1.0;  // Measured in vcpu, see notes below.
  memory = 4.0;  // Measured in gigabytes (GB / Gi).
  boot = volumes.os;  // Boot from the first disk in the volumes list.
  volumes = vm.volumes {
    os = vm.qemu.disk {  // Construct a disk.
      source = !!!;  // URL of a qcow2 disk image to load into the disk.
    }
    metadata[] = 3 * vm.qemu.disk {  // Construct 3 empty disks.
      format = vm.volumes.formats.ext4;
      size = 1.0;  // Measured in gigabytes (GB / Gi).
    }
    data[] = 3 * vm.qemu.disk {  // Construct 3 empty disks.
      format = vm.volumes.formats.ext4;
      size = 2.0;  // Measured in gigabytes (GB / Gi).
    }
  }
  networks = vm.networks {
    !!!;
  }
  init = vm.cloud_init {
    !!!;
  }
}
```

A "vm_instance" type can be used to construct running virtual machine
instances.

```
anarkube_nodes[] = 3 * anarkube { // Constructs 3 running VMs of type anarkube.
  !!!;
}
```

Each running virtual machine instance can be started or stopped,
or commands can be executed inside it (via ssh):

```
anarkube_nodes[0].stop()
anarkube_nodes[0].start()
anarkube_nodes[1].restart()
result = anarkube_nodes[2].execute {
  !!!;
}
```

The result of executing commands inside a running virtual machine
can be examined:

```
print("Results: stdout    = " result.stdout)
print("Results: stderr    = " result.stderr)
print("Results: exit_code = " result.exit_code)
```

### Type "container"
!!!

### Type "stream"
A stream declares a mode of communication between Musaico instances
such as containers.

Streaming in Musaico is built around NATS Jetstream and a custom
schema registry, designed to make it easy and (some day) safe
to upgrade message schemas without borking your whole distributed system.

!!!

## Installing Musaico
For now, Musaico is download-and-build-yourself.  Git clone this repository,
as long as you have the required pre-requisites (described below).

### Pre-requisites
For now, Musaico only runs on Debian Linux.  There is no
compatibility matrix for specific versions of Debian.  It has generally
been prototyped on Debian 11 and 12.

- A Debian Linux operating system.  (11 or 12 at the time of writing.)
- Make installed.
  `sudo apt-get install -y make`
- Docker installed and executable by the user running Musaico.
  `sudo apt-get install -y docker.io`
  `sudo chmod a+s /usr/bin/docker`
- Qemu installed and executable by the user running Musaico.
  `sudo apt-get install -y qemu!!!`
  `!!!`

## Building Musaico
From the toplevel Musaico directory, run:

```
make
```

This should build all of the necessary containers to run Musaico
(unless everything is borken, in which case don't complain I didn't warn you).

## Using Musaico
!!!
