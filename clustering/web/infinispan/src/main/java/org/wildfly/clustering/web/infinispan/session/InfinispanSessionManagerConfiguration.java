/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014, Red Hat, Inc., and individual contributors
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
package org.wildfly.clustering.web.infinispan.session;

import java.util.Map;

import org.infinispan.Cache;
import org.wildfly.clustering.Registrar;
import org.wildfly.clustering.ee.Batcher;
import org.wildfly.clustering.ee.Key;
import org.wildfly.clustering.ee.Recordable;
import org.wildfly.clustering.ee.Scheduler;
import org.wildfly.clustering.ee.cache.CacheProperties;
import org.wildfly.clustering.ee.cache.tx.TransactionBatch;
import org.wildfly.clustering.web.IdentifierFactory;
import org.wildfly.clustering.web.session.ImmutableSessionMetaData;
import org.wildfly.clustering.web.session.SessionExpirationListener;
import org.wildfly.clustering.web.session.SessionExpirationMetaData;
import org.wildfly.clustering.web.session.SessionManager;

/**
 * Configuration for an {@link InfinispanSessionManager}.
 * @param <SC> the ServletContext specification type
 * @param <LC> the local context type
 * @author Paul Ferraro
 */
public interface InfinispanSessionManagerConfiguration<SC, LC> {
    SC getServletContext();
    SessionExpirationListener getExpirationListener();
    Cache<Key<String>, ?> getCache();
    CacheProperties getProperties();
    IdentifierFactory<String> getIdentifierFactory();
    Batcher<TransactionBatch> getBatcher();
    Scheduler<String, SessionExpirationMetaData> getExpirationScheduler();
    Recordable<ImmutableSessionMetaData> getInactiveSessionRecorder();
    Registrar<SessionExpirationListener> getExpirationRegistar();
    Runnable getStartTask();
    Registrar<Map.Entry<SC, SessionManager<LC, TransactionBatch>>> getContextRegistrar();
}
