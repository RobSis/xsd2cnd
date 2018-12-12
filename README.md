xsd2cnd
=======

The xsd2cnd tool is a converter for turning XML schema definitions to
equivalent JCR node types using the compact node type definition format.

This was written as a replacement for https://github.com/ceefour/xsd2cnd
which is obsolete and non-trivial to compile (it's based on Maven 1.0 & JR 1.0)

Usage:
`$ java -jar xsd2cnd.jar custom-nodetypes-file.xml`
