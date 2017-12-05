/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 * 
 */
package org.apache.directory.api.ldap.model.schema.normalizers;


import org.apache.directory.api.ldap.model.entry.Value;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.schema.Normalizer;
import org.apache.directory.api.ldap.model.schema.SchemaManager;


/**
 * Caches previously normalized values.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@SuppressWarnings("serial")
public class CachingNormalizer extends Normalizer
{
    /** Cache maximum size default */
    public static final int CACHE_MAX = 250;

    /** The underlying decorated Normalizer */
    protected final Normalizer normalizer;


    // ------------------------------------------------------------------------
    // C O N S T R U C T O R S
    // ------------------------------------------------------------------------

    /**
     * Creates a CachingNormalizer that decorates another normalizer using a
     * default cache size.  This Normalizer delegates
     * 
     * @param normalizer the underlying Normalizer being decorated
     */
    public CachingNormalizer( Normalizer normalizer )
    {
        this( normalizer, CACHE_MAX );
    }


    /**
     * Creates a CachingNormalizer that decorates another normalizer using a
     * specified cache size.
     * 
     * @param normalizer the underlying Normalizer being decorated
     * @param cacheSz the maximum size of the name cache
     */
    public CachingNormalizer( Normalizer normalizer, int cacheSz )
    {
        super( normalizer.getOid() );
        this.normalizer = normalizer;
    }


    /**
     * Overrides default behavior by returning the OID of the wrapped
     * Normalizer.
     */
    @Override
    public String getOid()
    {
        return normalizer.getOid();
    }


    /**
     * Overrides default behavior by setting the OID of the wrapped Normalizer.
     * 
     * @param oid the object identifier to set
     */
    @Override
    public void setOid( String oid )
    {
        super.setOid( oid );
        normalizer.setOid( oid );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Value<?> normalize( Value<?> value ) throws LdapException
    {
        if ( value == null )
        {
            return null;
        }

        return normalizer.normalize( value );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String normalize( String value ) throws LdapException
    {
        if ( value == null )
        {
            return null;
        }

        return normalizer.normalize( value );
    }


    /**
     * Sets the SchemaManager
     * 
     * @param schemaManager The SchemaManager
     */
    @Override
    public void setSchemaManager( SchemaManager schemaManager )
    {
        normalizer.setSchemaManager( schemaManager );
    }
}
