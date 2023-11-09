This directory contains third party software used to build
Musaico components and systems.

Please read the license documentation in each zip or tar.gz file.

Examples of third party software used to build Musaico:

- Test harnesses
- Code coverage analysis tools
- Static analysis tools

None of these tools need be distributed with the Musaico product
(source or binary) itself.  However the Musaico build scripts
rely heavily on them.

None of the third party tools have been modified in any way.
You should be able to download a newer version from the
maintainer's website, and install it in place of the "default"
version, without any adverse effects to the Musaico build.
Of course if the API of a given tool has changed substantially
then you will find problems...

Third party libraries required by Musaico product components are
mentioned and/or included in the individual project or module
directories.  For example, if kernel/devices/foo depends on
a SQL connection library or an XML parser library and so on, then
it will be mentioned in the documentation for the project
kernel/devices/foo (and possibly the library will also be
included there, depending on the license).
