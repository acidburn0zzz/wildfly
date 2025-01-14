/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2016, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.test.xts.suspend.wsat;

import org.apache.commons.lang3.SystemUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.test.shared.TestSuiteEnvironment;
import org.jboss.as.test.shared.integration.ejb.security.PermissionUtils;
import org.jboss.as.test.xts.suspend.AbstractTestCase;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FilePermission;
import java.net.SocketPermission;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@RunWith(Arquillian.class)
public class AtomicTransactionSuspendTestCase extends AbstractTestCase {

    static final String serverHostPort = TestSuiteEnvironment.getServerAddress() + ":"
            + TestSuiteEnvironment.getHttpPort();

    @TargetsContainer(EXECUTOR_SERVICE_CONTAINER)
    @Deployment(name = EXECUTOR_SERVICE_ARCHIVE_NAME, testable = false)
    public static WebArchive getExecutorServiceArchive() {
        WebArchive war = getExecutorServiceArchiveBase().addClasses(AtomicTransactionExecutionService.class,
                AtomicTransactionRemoteService.class, TransactionParticipant.class);

        if (SystemUtils.JAVA_VENDOR.startsWith("IBM")) {
            war.addAsManifestResource(PermissionUtils.createPermissionsXmlAsset(
                    //This is not catastrophic if absent
                    ///.../testsuite/integration/xts/xcatalog
                    //$JAVA_HOME/jre/conf/jaxm.properties
                    //$JAVA_HOME/jre/lib/jaxws.properties
                    //$JAVA_HOME/jre/conf/jaxws.properties
                    new FilePermission(System.getProperties().getProperty("jbossas.ts.integ.dir") + File.separator + "xts" + File.separator
                            + "xcatalog", "read"),
                    new FilePermission(System.getenv().get("JAVA_HOME") + File.separator + "jre" + File.separator
                            + "conf" + File.separator + "jaxm.properties", "read"),
                    new FilePermission(System.getenv().get("JAVA_HOME") + File.separator + "jre" + File.separator
                            + "conf" + File.separator + "jaxws.properties", "read"),
                    new FilePermission(System.getenv().get("JAVA_HOME") + File.separator + "jre" + File.separator
                            + "lib" + File.separator + "jaxws.properties", "read"),
                    new RuntimePermission("accessClassInPackage.com.sun.org.apache.xerces.internal.jaxp"),
                    new SocketPermission(serverHostPort, "connect, resolve")
            ), "permissions.xml");
        } else {
            war.addAsManifestResource(
                    PermissionUtils.createPermissionsXmlAsset(new SocketPermission(serverHostPort, "connect, resolve")),
                    "permissions.xml");
        }

        return war;
    }

    @TargetsContainer(REMOTE_SERVICE_CONTAINER)
    @Deployment(name = REMOTE_SERVICE_ARCHIVE_NAME, testable = false)
    public static WebArchive getRemoteServiceArchive() {
        WebArchive war = getRemoteServiceArchiveBase().addClasses(AtomicTransactionRemoteService.class,
                TransactionParticipant.class);
        if (SystemUtils.JAVA_VENDOR.startsWith("IBM")) {
            war.addAsManifestResource(PermissionUtils.createPermissionsXmlAsset(
                    //This is not catastrophic if absent
                    ///.../testsuite/integration/xts/xcatalog
                    //$JAVA_HOME/jre/conf/jaxm.properties
                    //$JAVA_HOME/jre/lib/jaxws.properties
                    //$JAVA_HOME/jre/conf/jaxws.properties
                    new FilePermission(System.getProperties().getProperty("jbossas.ts.integ.dir") + File.separator + "xts" + File.separator
                            + "xcatalog", "read"),
                    new FilePermission(System.getenv().get("JAVA_HOME") + File.separator + "jre" + File.separator
                            + "conf" + File.separator + "jaxm.properties", "read"),
                    new FilePermission(System.getenv().get("JAVA_HOME") + File.separator + "jre" + File.separator
                            + "conf" + File.separator + "jaxws.properties", "read"),
                    new FilePermission(System.getenv().get("JAVA_HOME") + File.separator + "jre" + File.separator
                            + "lib" + File.separator + "jaxws.properties", "read"),
                    new RuntimePermission("accessClassInPackage.com.sun.org.apache.xerces.internal.jaxp"),
                    new SocketPermission(serverHostPort, "connect, resolve")
            ), "permissions.xml");
        } else {
            war.addAsManifestResource(
                    PermissionUtils.createPermissionsXmlAsset(new SocketPermission(serverHostPort, "connect, resolve")),
                    "permissions.xml");
        }
        return war;
    }

    protected void assertParticipantInvocations(List<String> invocations) {
        assertEquals(Arrays.asList("prepare", "commit"), invocations);
    }

}
