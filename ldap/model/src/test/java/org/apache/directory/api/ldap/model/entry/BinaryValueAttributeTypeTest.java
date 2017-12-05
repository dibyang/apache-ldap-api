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
package org.apache.directory.api.ldap.model.entry;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidAttributeValueException;
import org.apache.directory.api.ldap.model.schema.AttributeType;
import org.apache.directory.api.ldap.model.schema.LdapSyntax;
import org.apache.directory.api.ldap.model.schema.MutableAttributeType;
import org.apache.directory.api.ldap.model.schema.MutableMatchingRule;
import org.apache.directory.api.ldap.model.schema.Normalizer;
import org.apache.directory.api.ldap.model.schema.comparators.ByteArrayComparator;
import org.apache.directory.api.ldap.model.schema.syntaxCheckers.OctetStringSyntaxChecker;
import org.apache.directory.api.util.Strings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mycila.junit.concurrent.Concurrency;
import com.mycila.junit.concurrent.ConcurrentJunitRunner;


/**
 * Tests that the BinaryValue class works properly as expected.
 *
 * Some notes while conducting tests:
 *
 * <ul>
 *   <li>comparing values with different types - how does this behave</li>
 *   <li>exposing access to at from value or to a comparator?</li>
 * </ul>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@RunWith(ConcurrentJunitRunner.class)
@Concurrency()
public class BinaryValueAttributeTypeTest
{
    private LdapSyntax s;
    private MutableAttributeType at;
    private MutableMatchingRule mr;

    private static final byte[] BYTES1 = new byte[]
        { 0x01, 0x02, 0x03, 0x04 };
    private static final byte[] BYTES2 = new byte[]
        { ( byte ) 0x81, ( byte ) 0x82, ( byte ) 0x83, ( byte ) 0x84 };


    /**
     * Initialize an AttributeType and the associated MatchingRule
     * and Syntax
     */
    @Before
    public void initAT()
    {
        s = EntryUtils.syntaxFactory( "1.1.1.1", false );
        s.setSyntaxChecker( OctetStringSyntaxChecker.INSTANCE );
        mr = EntryUtils.matchingRuleFactory( "1.1.2.1" );
        mr.setSyntax( s );

        mr.setLdapComparator( new ByteArrayComparator( "1.1.1" ) );
        mr.setNormalizer( new Normalizer( "1.1.1" )
        {
            public static final long serialVersionUID = 1L;


            public Value<?> normalize( Value<?> value ) throws LdapException
            {
                if ( !value.isHumanReadable() )
                {
                    byte[] val = value.getBytes();
                    // each byte will be changed to be > 0, and spaces will be trimmed
                    byte[] newVal = new byte[val.length];
                    int i = 0;

                    for ( byte b : val )
                    {
                        newVal[i++] = ( byte ) ( b & 0x007F );
                    }

                    return new BinaryValue( Strings.trim( newVal ) );
                }

                throw new IllegalStateException( "expected byte[] to normalize" );
            }


            public String normalize( String value ) throws LdapException
            {
                throw new IllegalStateException( "expected byte[] to normalize" );
            }
        } );

        at = new MutableAttributeType( "1.1.3.1" );
        at.setEquality( mr );
        at.setOrdering( mr );
        at.setSubstring( mr );
        at.setSyntax( s );
    }


    /**
     * Serialize a BinaryValue
     */
    private ByteArrayOutputStream serializeValue( BinaryValue value ) throws IOException
    {
        ObjectOutputStream oOut = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try
        {
            oOut = new ObjectOutputStream( out );
            value.writeExternal( oOut );
        }
        catch ( IOException ioe )
        {
            throw ioe;
        }
        finally
        {
            try
            {
                if ( oOut != null )
                {
                    oOut.flush();
                    oOut.close();
                }
            }
            catch ( IOException ioe )
            {
                throw ioe;
            }
        }

        return out;
    }


    /**
     * Deserialize a BinaryValue
     */
    private BinaryValue deserializeValue( ByteArrayOutputStream out, AttributeType at ) throws IOException,
        ClassNotFoundException
    {
        ObjectInputStream oIn = null;
        ByteArrayInputStream in = new ByteArrayInputStream( out.toByteArray() );

        try
        {
            oIn = new ObjectInputStream( in );

            BinaryValue value = new BinaryValue( ( AttributeType ) null );
            value.readExternal( oIn );

            return value;
        }
        catch ( IOException ioe )
        {
            throw ioe;
        }
        finally
        {
            try
            {
                if ( oIn != null )
                {
                    oIn.close();
                }
            }
            catch ( IOException ioe )
            {
                throw ioe;
            }
        }
    }


    /**
     * Test the constructor with bad AttributeType
     */
    @Test
    public void testBadConstructor()
    {
        // create a AT without no syntax
        MutableAttributeType attribute = new MutableAttributeType( "1.1.3.1" );

        try
        {
            new BinaryValue( attribute );
            fail();
        }
        catch ( IllegalArgumentException ae )
        {
            // Expected...
        }
    }


    /**
     * Test the constructor with a null value
     */
    @Test
    public void testServerBinaryValueNullValue() throws LdapInvalidAttributeValueException
    {
        AttributeType attribute = EntryUtils.getBytesAttributeType();

        BinaryValue value = new BinaryValue( attribute, null );

        assertNull( value.getReference() );
        assertTrue( value.isNull() );
    }


    /**
     * Test the constructor with an empty value
     */
    @Test
    public void testServerBinaryValueEmptyValue() throws LdapInvalidAttributeValueException
    {
        AttributeType attribute = EntryUtils.getBytesAttributeType();

        BinaryValue value = new BinaryValue( attribute, Strings.EMPTY_BYTES );

        assertTrue( Arrays.equals( Strings.EMPTY_BYTES, value.getReference() ) );
        assertFalse( value.isNull() );
    }


    /**
     * Test the constructor with a value
     */
    @Test
    public void testServerBinaryValueNoValue()
    {
        AttributeType attribute = EntryUtils.getBytesAttributeType();
        byte[] val = new byte[]
            { 0x01 };
        BinaryValue bv = new BinaryValue( attribute );

        bv = new BinaryValue( val );
        assertTrue( Arrays.equals( val, bv.getReference() ) );
        assertFalse( bv.isNull() );
        assertTrue( Arrays.equals( val, bv.getValue() ) );
    }


    /**
     * Test the constructor with a value
     */
    @Test
    public void testServerBinaryValue() throws LdapInvalidAttributeValueException
    {
        AttributeType attribute = EntryUtils.getBytesAttributeType();
        byte[] val = new byte[]
            { 0x01 };
        BinaryValue value = new BinaryValue( attribute, val );

        assertTrue( Arrays.equals( val, value.getReference() ) );
        assertFalse( value.isNull() );
        assertTrue( Arrays.equals( val, value.getValue() ) );
    }


    /**
     * Test the clone method
     */
    @Test
    public void testClone() throws LdapException
    {
        AttributeType at1 = EntryUtils.getBytesAttributeType();
        BinaryValue bv = new BinaryValue( at1, null );
        BinaryValue bv1 = bv.clone();

        assertEquals( bv, bv1 );

        bv = new BinaryValue( Strings.EMPTY_BYTES );

        assertNotSame( bv, bv1 );
        assertTrue( Arrays.equals( Strings.EMPTY_BYTES, bv.getBytes() ) );

        bv = new BinaryValue( BYTES2 );
        bv1 = bv.clone();

        assertEquals( bv, bv1 );

        bv.apply( at );

        // Even if we didn't normalized sbv2, it should be equal to sbv,
        // as if they have the same AT, and the same value, they are equal.
        assertEquals( bv, bv1 );
    }


    /**
     * Test the equals method
     */
    @Test
    public void testEquals() throws LdapInvalidAttributeValueException
    {
        AttributeType at1 = EntryUtils.getBytesAttributeType();

        BinaryValue value1 = new BinaryValue( at1, new byte[]
            { 0x01, ( byte ) 0x02 } );
        BinaryValue value2 = new BinaryValue( at1, new byte[]
            { 0x01, ( byte ) 0x02 } );
        BinaryValue value3 = new BinaryValue( at1, new byte[]
            { 0x01, ( byte ) 0x82 } );
        BinaryValue value4 = new BinaryValue( at1, new byte[]
            { 0x01 } );
        BinaryValue value5 = new BinaryValue( at1, null );
        BinaryValue value6 = new BinaryValue( at, new byte[]
            { 0x01, 0x02 } );
        StringValue value7 = new StringValue( EntryUtils.getIA5StringAttributeType(),
            "test" );

        assertTrue( value1.equals( value1 ) );
        assertTrue( value1.equals( value2 ) );
        assertTrue( value1.equals( value3 ) );
        assertFalse( value1.equals( value4 ) );
        assertFalse( value1.equals( value5 ) );
        assertFalse( value1.equals( "test" ) );
        assertFalse( value1.equals( null ) );

        assertFalse( value1.equals( value6 ) );
        assertFalse( value1.equals( value7 ) );
    }


    /**
     * Test the getNormValue method
     */
    @Test
    public void testGetNormalizedValue() throws LdapInvalidAttributeValueException
    {
        AttributeType attribute = EntryUtils.getBytesAttributeType();

        BinaryValue value = new BinaryValue( attribute, null );
        assertNull( value.getNormValue() );

        value = new BinaryValue( attribute, Strings.EMPTY_BYTES );
        assertTrue( Arrays.equals( Strings.EMPTY_BYTES, value.getNormValue() ) );

        value = new BinaryValue( attribute, BYTES2 );
        assertTrue( Arrays.equals( BYTES1, value.getNormValue() ) );
    }


    /**
     * Test the getNormValue method
     */
    @Test
    public void testGetNormalizedValueCopy() throws LdapInvalidAttributeValueException
    {
        AttributeType attribute = EntryUtils.getBytesAttributeType();

        BinaryValue value = new BinaryValue( attribute, null );
        assertNull( value.getNormValue() );

        value = new BinaryValue( attribute, Strings.EMPTY_BYTES );
        assertTrue( Arrays.equals( Strings.EMPTY_BYTES, value.getNormValue() ) );

        value = new BinaryValue( attribute, BYTES2 );
        assertTrue( Arrays.equals( BYTES1, value.getNormValue() ) );
    }


    /**
     * Test the getNormValue method
     */
    @Test
    public void testGetNormalizedValueReference() throws LdapInvalidAttributeValueException
    {
        AttributeType attribute = EntryUtils.getBytesAttributeType();

        BinaryValue value = new BinaryValue( attribute, null );
        assertNull( value.getNormReference() );

        value = new BinaryValue( attribute, Strings.EMPTY_BYTES );
        assertTrue( Arrays.equals( Strings.EMPTY_BYTES, value.getNormReference() ) );

        value = new BinaryValue( attribute, BYTES2 );
        assertTrue( Arrays.equals( BYTES1, value.getNormReference() ) );
    }


    /**
     * Test the getAttributeType method
     */
    @Test
    public void testgetAttributeType()
    {
        AttributeType attribute = EntryUtils.getBytesAttributeType();
        BinaryValue sbv = new BinaryValue( attribute );

        assertEquals( attribute, sbv.getAttributeType() );
    }


    /**
     * Test the isValid method
     * 
     * The SyntaxChecker does not accept values longer than 5 chars.
     */
    @Test
    public void testIsValid() throws LdapInvalidAttributeValueException
    {
        AttributeType attribute = EntryUtils.getBytesAttributeType();

        new BinaryValue( attribute, null );
        new BinaryValue( attribute, Strings.EMPTY_BYTES );
        new BinaryValue( attribute, new byte[]
            { 0x01, 0x02 } );

        try
        {
            new BinaryValue( attribute, new byte[]
                { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 } );
            fail();
        }
        catch ( LdapInvalidAttributeValueException liave )
        {
            assertTrue( true );
        }
    }


    /**
     * Tests to make sure the hashCode method is working properly.
     * @throws Exception on errors
     */
    @Test
    public void testHashCode() throws LdapInvalidAttributeValueException
    {
        AttributeType attribute = EntryUtils.getBytesAttributeType();
        BinaryValue v0 = new BinaryValue( attribute, new byte[]
            { 0x01, 0x02 } );
        BinaryValue v1 = new BinaryValue( attribute, new byte[]
            { ( byte ) 0x81, ( byte ) 0x82 } );
        BinaryValue v2 = new BinaryValue( attribute, new byte[]
            { 0x01, 0x02 } );
        assertEquals( v0.hashCode(), v1.hashCode() );
        assertEquals( v1.hashCode(), v2.hashCode() );
        assertEquals( v0.hashCode(), v2.hashCode() );
        assertEquals( v0, v1 );
        assertEquals( v0, v2 );
        assertEquals( v1, v2 );

        BinaryValue v3 = new BinaryValue( attribute, new byte[]
            { 0x01, 0x03 } );
        assertFalse( v3.equals( v0 ) );
        assertFalse( v3.equals( v1 ) );
        assertFalse( v3.equals( v2 ) );
    }


    /**
     * Test the instanceOf method
     */
    @Test
    public void testInstanceOf() throws LdapException
    {
        AttributeType attribute = EntryUtils.getBytesAttributeType();
        BinaryValue sbv = new BinaryValue( attribute );

        assertTrue( sbv.isInstanceOf( attribute ) );

        attribute = EntryUtils.getIA5StringAttributeType();

        assertFalse( sbv.isInstanceOf( attribute ) );
    }


    /**
     * Test the normalize method
     */
    @Test
    public void testNormalize() throws LdapException
    {
        AttributeType attribute = EntryUtils.getBytesAttributeType();
        BinaryValue bv = new BinaryValue( attribute );

        bv.apply( at );
        assertEquals( null, bv.getNormValue() );

        bv = new BinaryValue( attribute, Strings.EMPTY_BYTES );
        bv.apply( at );
        assertTrue( Arrays.equals( Strings.EMPTY_BYTES, bv.getNormValue() ) );

        bv = new BinaryValue( attribute, BYTES2 );
        bv.apply( at );
        assertTrue( Arrays.equals( BYTES1, bv.getNormValue() ) );
    }


    /**
     * Test the compareTo method
     */
    @Test
    public void testCompareTo() throws LdapInvalidAttributeValueException
    {
        AttributeType at1 = EntryUtils.getBytesAttributeType();
        BinaryValue v0 = new BinaryValue( at1, BYTES1 );
        BinaryValue v1 = new BinaryValue( at1, BYTES2 );

        assertEquals( 0, v0.compareTo( v1 ) );
        assertEquals( 0, v1.compareTo( v0 ) );

        BinaryValue v2 = new BinaryValue( at1, null );

        assertEquals( 1, v0.compareTo( v2 ) );
        assertEquals( -1, v2.compareTo( v0 ) );
    }


    /**
     * Test serialization of a BinaryValue which has a normalized value
     */
    @Test
    public void testNormalizedBinaryValueSerialization() throws LdapException, IOException, ClassNotFoundException
    {
        byte[] v1 = Strings.getBytesUtf8( "  Test   Test  " );
        byte[] v1Norm = Strings.getBytesUtf8( "Test   Test" );

        // First check with a value which will be normalized
        BinaryValue sbv = new BinaryValue( at, v1 );

        sbv.apply( at );
        byte[] normalized = sbv.getNormReference();

        assertTrue( Arrays.equals( v1Norm, normalized ) );
        assertTrue( Arrays.equals( v1, sbv.getReference() ) );

        BinaryValue sbvSer = deserializeValue( serializeValue( sbv ), at );

        assertEquals( sbv, sbvSer );
    }


    /**
     * Test serialization of a BinaryValue which normalized value is the same
     * than the value
     */
    @Test
    public void testNormalizedBinarySameValueSerialization() throws LdapException, IOException, ClassNotFoundException
    {
        byte[] v1 = Strings.getBytesUtf8( "Test   Test" );

        // First check with a value which will be normalized
        BinaryValue sbv = new BinaryValue( at, v1 );

        BinaryValue sbvSer = deserializeValue( serializeValue( sbv ), at );

        assertEquals( sbv, sbvSer );
    }


    /**
     * Test serialization of a BinaryValue which does not have a normalized value
     */
    @Test
    public void testNoNormalizedBinaryValueSerialization() throws LdapException, IOException, ClassNotFoundException
    {
        byte[] v1 = Strings.getBytesUtf8( "test" );
        byte[] v1Norm = Strings.getBytesUtf8( "test" );

        // First check with a value which will be normalized
        BinaryValue sbv = new BinaryValue( at, v1 );

        sbv.apply( at );
        byte[] normalized = sbv.getNormReference();

        assertTrue( Arrays.equals( v1Norm, normalized ) );
        assertTrue( Arrays.equals( v1, sbv.getBytes() ) );

        BinaryValue sbvSer = deserializeValue( serializeValue( sbv ), at );

        assertEquals( sbv, sbvSer );
    }


    /**
     * Test serialization of a null BinaryValue
     */
    @Test
    public void testNullBinaryValueSerialization() throws LdapException, IOException, ClassNotFoundException
    {
        // First check with a value which will be normalized
        BinaryValue sbv = new BinaryValue( at );

        sbv.apply( at );
        byte[] normalized = sbv.getNormReference();

        assertEquals( null, normalized );
        assertEquals( null, sbv.getValue() );

        BinaryValue sbvSer = deserializeValue( serializeValue( sbv ), at );

        assertEquals( sbv, sbvSer );
    }


    /**
     * Test serialization of an empty BinaryValue
     */
    @Test
    public void testEmptyBinaryValueSerialization() throws LdapException, IOException, ClassNotFoundException
    {
        // First check with a value which will be normalized
        BinaryValue sbv = new BinaryValue( at, Strings.EMPTY_BYTES );

        sbv.apply( at );
        byte[] normalized = sbv.getNormReference();

        assertTrue( Arrays.equals( Strings.EMPTY_BYTES, normalized ) );
        assertTrue( Arrays.equals( Strings.EMPTY_BYTES, sbv.getBytes() ) );

        BinaryValue sbvSer = deserializeValue( serializeValue( sbv ), at );

        assertEquals( sbv, sbvSer );
    }


    /**
     * Test serialization of a BinaryValue which is the same than the value
     */
    @Test
    public void testSameNormalizedBinaryValueSerialization() throws LdapException, IOException, ClassNotFoundException
    {
        byte[] v1 = Strings.getBytesUtf8( "test" );
        byte[] v1Norm = Strings.getBytesUtf8( "test" );

        // First check with a value which will be normalized
        BinaryValue sbv = new BinaryValue( at, v1 );

        sbv.apply( at );
        byte[] normalized = sbv.getNormReference();

        assertTrue( Arrays.equals( v1Norm, normalized ) );
        assertTrue( Arrays.equals( v1, sbv.getBytes() ) );

        BinaryValue sbvSer = deserializeValue( serializeValue( sbv ), at );

        assertEquals( sbv, sbvSer );
    }
}