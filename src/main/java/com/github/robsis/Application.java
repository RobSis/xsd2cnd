/**
 * xsd2cnd
 * Copyright (C) 2018
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.github.robsis;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Map;

import javax.jcr.NamespaceRegistry;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.jackrabbit.core.nodetype.xml.NodeTypeReader;
import org.apache.jackrabbit.spi.QNodeTypeDefinition;
import org.apache.jackrabbit.spi.commons.nodetype.compact.CompactNodeTypeDefWriter;

public class Application {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.exit(1);
        }
        File file = new File(args[0]);
        if (!file.exists()) {
            System.exit(1);
        }
        Repository repository = JcrUtils.getRepository();
        Session session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
        StringWriter writer = new StringWriter();

        try {
            NamespaceRegistry nsRegistry = session.getWorkspace().getNamespaceRegistry();
            NodeTypeReader xsdReader = new NodeTypeReader(new FileInputStream(file));
            QNodeTypeDefinition[] nodeTypes = xsdReader.getNodeTypeDefs();
            for (Map.Entry<Object, Object> e : xsdReader.getNamespaces().entrySet()) {
                if (!Arrays.asList(nsRegistry.getPrefixes()).contains(e.getKey())) {
                    nsRegistry.registerNamespace((String) e.getKey(), (String) e.getValue());
                }
            }
            CompactNodeTypeDefWriter cndWriter = new CompactNodeTypeDefWriter(writer, session, false);
            cndWriter.write(Arrays.asList(nodeTypes));

            System.out.print(writer.toString());
        } finally {
            session.logout();
        }
    }
}
